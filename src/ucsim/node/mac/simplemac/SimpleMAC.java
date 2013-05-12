package ucsim.node.mac.simplemac;

import java.util.Random;

import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.node.Node;
import ucsim.node.mac.MAC;
import ucsim.packet.Packet;
import ucsim.world.World;

public class SimpleMAC extends MAC {
	
	protected double carrierSensingThreshold = 1e-10;
	
    public SimpleMAC() {
    }
    
    protected boolean isChannelBusy(){
        if(this.isSending || this.receivingCount>0){
            return true;
        }else{
            return false;
        }
    }

    public void tick(World w, Node n) {
      //receive
      while (receiveQueue.size()>0) {
        Packet p = (Packet)receiveQueue.get(0);
        n.app.receivePacket(p, n, w);
        receiveQueue.remove(0);
      }
      //send
//      print("Node: ");print(n.id);print("mac.sendQueue.size: ");print(n.mac.sendQueue.size());print(", MAC state: ");print(this.state);println();
      switch(getState()) {
      case 0:
        n.energyModule.listenConsumeEnergy(w.scheduler.getTimeAdvance());
        if (this.sendQueue.size()>0) {
          setState(1);
        }
        else {
          setState(0);
        }
        break;
      case 1:
        
        if (this.receivingCount==0 && !this.isSending) {
          Packet p = (Packet)this.sendQueue.get(0);
          this.send(p, w, n);
          if (this.sendQueue.size()>0) {
            setState(1);
          }
          else {
            setState(0);
          }
        }else{
          n.energyModule.listenConsumeEnergy(w.scheduler.getTimeAdvance());
        }
        break;
      default:
        setState(0);
        break;
      }
    }

    public void receive(Packet p) {
    	if(p.getPower()>this.carrierSensingThreshold){
    		receivingCount++;
    	}
    	
    }

    public void receiveEnd(Packet p, World w, Node n) {
        p.setReceiveTime(w.scheduler.getNowTime());  
    	if(p.getPower()>this.carrierSensingThreshold){
    		receivingCount--;
    	}
    	this.decode(p);
    	this.receiveQueue.add(p);
    	n.energyModule.receiveConsumeEnergy(p.getReceiveTime()-p.getSendTime());
    }
    
    @Override
    public void send(Packet p, World w, Node n) {
      this.isSending = true;
      try {
          p.send(w);
      } catch (CloneNotSupportedException e) {
          e.printStackTrace();
      }
      n.energyModule.transmitConsumeEnergy(p.getLength(), World.dataRate);
//      UCSimEvent event = UCSimEvent.createUCSimEvent(
//                  w.scheduler.getNowTime(), 
//                  "Energy Update", 
//                  String.format("{\"energy\":%f}", n.energyModule.getEnergyConsumption())
//              );
//      n.dataLogger.logEvent(event);
      
    }
    
    public void decode(Packet p){
        double SINR = p.getPower()/(p.getInterference()+p.getNoise());
        double BER = 0.5*(1 - erf(SINR)); 
        double PER = 1-Math.pow(1-BER, p.getLength());
        double r = new Random().nextDouble();
        System.out.printf("Theoretical BER: %f, PER: %f, r: %f\n", BER, PER, r);
        
        if(r<PER){
            p.setHasError(true);
        }else{
            p.setHasError(false);
        }
    }

    public void sendEnd() {
      this.isSending = false;
    }
    
    private double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

        // use Horner's method
        double ans = 1 - t * Math.exp( -z*z   -  1.26551223 +
                                      t * ( 1.00002368 +
                                      t * ( 0.37409196 + 
                                      t * ( 0.09678418 + 
                                      t * (-0.18628806 + 
                                      t * ( 0.27886807 + 
                                      t * (-1.13520398 + 
                                      t * ( 1.48851587 + 
                                      t * (-0.82215223 + 
                                      t * ( 0.17087277))))))))));
        if (z >= 0) return  ans;
        else        return -ans;
      }
}
