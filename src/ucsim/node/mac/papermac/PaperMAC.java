package ucsim.node.mac.papermac;

import java.util.ArrayList;

import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.node.Node;
import ucsim.node.mac.csmacamac.CSMACAMAC;
import ucsim.packet.ControlPacket;
import ucsim.packet.Packet;
import ucsim.world.World;

public class PaperMAC extends CSMACAMAC {
	
	public    ArrayList<Packet> 		  downQueue 		= new ArrayList<Packet>();
	public    ArrayList<LinkProfiler>     linkProfiles      = new ArrayList<LinkProfiler>();
	public    int                         dataPacketLength  = 0; 
	
	public PaperMAC(){
		
	}
	
	public void createProfilerList(World w, Node n){
		for(int i=0; i<w.nodes.size(); i++){
			Node toNode = w.nodes.get(i);
			this.linkProfiles.add(new LinkProfiler(n.getId(), toNode.getId()));
		}
	}
	
	@Override
	public void getFromApp(Packet p){
		this.downQueue.add(p);
	}
	
	@Override
	public void tick(World w, Node n){
		if(!downQueue.isEmpty()){
			Packet p = downQueue.get(0);
			this.sendQueue.add(p);
			this.downQueue.remove(0);
		}
		super.tick(w, n);
		
	}
	
	@Override
	protected void sendACK(World w, Node n){
		String control = "";
	    if(this.isACK){
	        this.abortConnection();
	        control = String.format("ACK%d", this.dataPacketLength);
	    }else{
	        this.DataRetry++;
	        if(this.DataRetry>this.maxDataRetry){
	            this.abortConnection();
	        }
	        control = String.format("NAK%d", this.dataPacketLength);
	    }
	    ControlPacket p = (ControlPacket) Packet.createPacket(Packet.CONTROL_PACKET, control, n.getId(), this.DataFrom, 80, w);
	    this.send(p, w, n);
	}
	
	@Override
	 protected void receiveData(Packet p, World w, Node n) {
		 this.dataPacketLength = p.getLength();   
		 super.receiveData(p, w, n);
	}
	
	@Override
	protected void receiveACK(Packet p, World w, Node n){
		if(p.getDestination() == n.getId()){
			String 	s_len 		= p.getData().replace("ACK", "");
			int 	length 		= Integer.parseInt(s_len);
			
			LinkProfiler lp = this.linkProfiles.get(p.getSource());
			lp.addPacketHistroy(length, true);
//			n.dataLogger.logEvent(
//					UCSimEvent.createUCSimEvent(
//								w.scheduler.getNowTime(), 
//								"Estimated BER Updated", 
//								lp.toLogString()
//							)
//						);
		}
		super.receiveACK(p, w, n);
		
	}
	
	@Override
	protected void receiveNAK(Packet p, World w, Node n){
		if(p.getDestination() == n.getId()){
			String 	s_len 		= p.getData().replace("NAK", "");
			int 	length 		= Integer.parseInt(s_len);
			
			LinkProfiler lp = this.linkProfiles.get(p.getSource());
			lp.addPacketHistroy(length, false);
//			n.dataLogger.logEvent(
//					UCSimEvent.createUCSimEvent(
//								w.scheduler.getNowTime(), 
//								"Estimated BER Updated", 
//								lp.toLogString()
//							)
//						);
		}	
		super.receiveNAK(p, w, n);
	}
	
}
