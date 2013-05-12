package ucsim.node.mac.papermac;

import java.util.ArrayList;

import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.node.Node;
import ucsim.node.energymodel.EnergyModule;
import ucsim.node.mac.csmacamac.CSMACAMAC;
import ucsim.packet.ControlPacket;
import ucsim.packet.Packet;
import ucsim.world.World;

public class PaperMAC extends CSMACAMAC {
	
	public    ArrayList<Packet> 		  downQueue 		= new ArrayList<Packet>();
	public    ArrayList<LinkProfiler>     linkProfiles      = new ArrayList<LinkProfiler>();
	public    int                         dataPacketLength  = 0; 
	
	public    static int                  headerSize        = 40;
	
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
		if(!downQueue.isEmpty() && sendQueue.isEmpty()){
			Packet p = downQueue.get(0);
			int size = bestPacketSize(p.getDestination());
			int lengthLeft = p.getLength();
			while(true){
			    if(size>lengthLeft){
			        Packet frag = (Packet) p.clone();
			        frag.setLength(lengthLeft+PaperMAC.headerSize);
			        this.sendQueue.add(frag);
			        break;
			    }else{
			        Packet frag = (Packet) p.clone();
			        frag.setLength(size+PaperMAC.headerSize);
			        lengthLeft -= size;
			        this.sendQueue.add(frag);
			    }
			    
			}
			
			this.downQueue.remove(0);
		}
		
		super.tick(w, n);
		
	}
	
	private int bestPacketSize(int destination){
	    int size = 8;
	    int tempSize = size;
	    int maxSize = 1500*8;
	    double maxEff = -1;
	    
	    LinkProfiler lp = this.linkProfiles.get(destination);
	    lp.estimateBER();
	    if(lp.isPacketHistroyEmpty()){
	        return 20000*8;
	    }else{
    	    double BER = lp.estimatedBER;
    	    for(tempSize = size; tempSize<maxSize; tempSize+=8){
    	        double eff = this.efficiency(tempSize, BER);
    	        if(eff>maxEff){
    	            maxEff = eff;
    	            size = tempSize;
    	        }
    	    }
	    }
	    System.out.printf("Optimal Size: %d\n", size);
	    return size;
	}
	
	private double efficiency(int tempSize, double BER){
	    double eff;
	    double Er = Packet.startPower * ControlPacket.RTSSize / World.dataRate;
	    double Ec = Packet.startPower * ControlPacket.CTSSize / World.dataRate;
	    double Ed = EnergyModule.packetPreambleTime * Packet.startPower;
	    double Eb = 1 / World.dataRate * Packet.startPower;
	    double Ea = Packet.startPower * ControlPacket.ACKSize / World.dataRate;
	    
	    double expectedTrans = this.expectedTrans(tempSize, BER);
	    
	    double E = Er + Ec + expectedTrans*(Ed + Eb*(tempSize+PaperMAC.headerSize)) + Ea; 
	    
	    eff = tempSize / E;
	    
	    return eff;
	}
	
	private double expectedTrans(int payloadSize, double BER){
	    double expectedTrans;
	    double PER = 1 - Math.pow(1-BER, payloadSize+PaperMAC.headerSize);
	    expectedTrans = 1/(1-PER);
	    return expectedTrans;
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
			
			lp.estimateBER();
			
			n.dataLogger.logEvent(
					UCSimEvent.createUCSimEvent(
								w.scheduler.getNowTime(), 
								"Estimated BER Updated", 
								lp.toLogString()
							)
						);
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
			
			lp.estimateBER();
			
			n.dataLogger.logEvent(
					UCSimEvent.createUCSimEvent(
								w.scheduler.getNowTime(), 
								"Estimated BER Updated", 
								lp.toLogString()
							)
						);
		}	
		super.receiveNAK(p, w, n);
	}
	
}
