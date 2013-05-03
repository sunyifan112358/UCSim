package ucsim.node.mac.csmamac;

import java.util.Random;
import ucsim.node.Node;
import ucsim.node.mac.simplemac.SimpleMAC;
import ucsim.packet.Packet;
import ucsim.world.World;

public class CSMAMAC extends SimpleMAC {
	protected double backoff;
	protected double defaultDIFS = 1;
	protected double difs = defaultDIFS;

	private void updateBackoff(){
	    Random rnd = new Random();
		this.backoff = rnd.nextInt(10) + 0;
	}

	public CSMAMAC(){
		updateBackoff();
	}

	public void tick(World w, Node n){
		while (receiveQueue.size ()>0) {
			Packet p = (Packet)receiveQueue.get(0);
			n.app.receivePacket(p, n, w);
			receiveQueue.remove(0);
		}

		if(n.getId()==0){
			@SuppressWarnings("unused")
            String s = String.format("In state: %d, difs: %f, backoff: %f", getState(), difs, backoff);
			//	      println(s);
		}



		if(getState() == 0){
			n.energyModule.listenConsumeEnergy(w.scheduler.getTimeAdvance());
			if (this.sendQueue.size()>0) {
			    updateBackoff();
			    this.setState(3);
			}else {
				this.setState(0);
			}
		}else if(getState() == 1){
			if(this.isSending() || this.getReceivingCount()>0){
				this.setState(1);
				difs = defaultDIFS;
				updateBackoff();
			}else{
				n.energyModule.listenConsumeEnergy(w.scheduler.getTimeAdvance());
				difs -= w.scheduler.getTimeAdvance();
				if(difs<0){
				    updateBackoff();
                    difs = defaultDIFS;
					if(this.sendQueue.size()>0){
    					Packet p = (Packet)this.sendQueue.get(0);
    					this.send(p, w, n);
    					this.sendQueue.remove(0);
    					this.setState(2);
					}else{
						this.setState(0);
					}
				}
			}
		}else if(getState()==2){
		    if(this.isSending()){
		        this.setState(2);
			}else{
				updateBackoff();
				difs = defaultDIFS;
				this.setState(3);
			}
		}else if(getState()==3){
			if(this.isSending() || this.getReceivingCount()>0){
				this.setState(3);
				difs = defaultDIFS;
			}else{
				backoff -= w.scheduler.getTimeAdvance();
				if(backoff<0){
					this.setState(1);
					difs = defaultDIFS;
				}
			}
		}

	}
}
