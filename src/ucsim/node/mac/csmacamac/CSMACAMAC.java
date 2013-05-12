package ucsim.node.mac.csmacamac;

import java.util.Random;

import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.node.Node;
import ucsim.node.mac.papermac.PaperMAC;
import ucsim.node.mac.simplemac.SimpleMAC;
import ucsim.packet.ControlPacket;
import ucsim.packet.Packet;
import ucsim.world.World;

public class CSMACAMAC extends SimpleMAC {
    
    protected double 	defaultDIFT    = 3;
    protected double 	defaultSIFT    = 1;
    protected double 	maxBackoff     = 20;
    protected int    	maxRTSRetry    = 5;
    protected double 	maxRTSTimeout  = 30;
    protected int    	maxDataRetry   = 5;
    protected double 	maxACKTimeout  = 30;
    protected double 	maxDataTimeout = 50;


    
    
    
    protected double    ift                 = this.defaultDIFT;
    protected double    backoff             = 0;
    protected int       RTSRetry            = 0;
    protected double    RTSTimeout          = this.maxRTSTimeout;
    protected double    ACKTimeout          = this.maxACKTimeout;
    protected int       DataRetry           = 0;
    protected boolean   RTSOccupied         = false;
    protected int       RTSFrom             = -1;
    protected int       RTSTo               = -1;
    protected double    DataTimeout         = this.maxDataTimeout;
    protected double 	NAVTimer            = 0;
    protected int       DataFrom            = -1;
    
    
	protected boolean   RTSSent             = false;
    protected boolean   CTSSent             = false;
    protected boolean   DataSent            = false;
    protected boolean   ACKSent             = false;
    protected boolean   NAKSent             = false;
    protected boolean   isACK               = true;
    protected int       connectingWith      = -1;
    
    
    
    private void updateBackoff(){
        Random rnd = new Random();
        this.backoff = rnd.nextDouble()*this.maxBackoff + 0;
    }
    
    
    
    public void tick(World w, Node n){
    	this.setNAVTimer(this.getNAVTimer()-w.scheduler.getTimeAdvance());
        switch(this.getState()){
        case 0: //IDLE
            if(!this.sendQueue.isEmpty() && this.NAVTimer<=0){
                this.abortConnection();
            	if(!this.isConnecting()){
                    this.setState(2);
                    this.updateBackoff();
                    this.ift = this.defaultDIFT;
                }
                
            }
            break;
        case 1: //DIFT before RTS
            this.carrierSensing(2, 3, w);
            break;
        case 2: //backoff for state 1
            this.backoff(1, w, true);
            break;
        case 3: //send RTS
            if(!this.RTSSent){
                this.sendRTS(w, n);
                this.RTSSent = true;
            }else{
                if(this.isSending){
                    this.setState(3);
                }else{
                    this.setState(4);
                    this.RTSTimeout = this.maxRTSTimeout;
                    this.RTSSent = false;
                }
            }
            break;
        case 4:  //Wait CTS
            this.RTSTimeout -= w.scheduler.getTimeAdvance();
            if(this.RTSTimeout<0){
                this.RTSRetry++;
                if(this.RTSRetry>this.maxRTSRetry){
                    this.setState(5);
                }else{
                    this.setState(0);
                    this.ift = this.defaultDIFT;
                }
            }
            break;
        case 5: 
            this.discardOnePacket();
            this.setState(0);
            break;
        case 6: //SIFT before data
            this.carrierSensing(7, 8, w);
            break;
        case 7://backoff for state 6
            this.backoff(6, w);
            break;
        case 8: //sending data
            if(!this.DataSent){
                this.sendData(w, n);
                this.DataSent = true;
            }else{
                if(this.isSending){
                }else{
                    this.setState(9); 
                    this.ACKTimeout = this.maxACKTimeout;
                    this.DataSent = false;
                }
            }
            break;
        case 9: //wait for ACK
            this.ACKTimeout -= w.scheduler.getTimeAdvance();
            if(this.ACKTimeout<0){
                this.DataRetry ++;
                if(this.DataRetry>this.maxDataRetry){
                    this.setState(5);
                }else{
                    this.setState(6);
                    this.ift = this.defaultSIFT;
                }
            }
            break;
        case 10: //NAK
            this.DataRetry++;
            if(this.DataRetry>this.maxDataRetry){
                this.setState(5);
            }else{
                this.setState(6);
                this.ift = this.defaultSIFT;
            }
            break;
        case 11: //ACK
            setState(5);
            break;
        case 12: //SIFT before CTS
            this.carrierSensing(13, 14, w);
            break;
        case 13: //backoff for state 13
            this.backoff(13, w);
            break;
        case 14: //sending CTS
            if(!this.CTSSent){
                this.sendCTS(w, n);
                this.CTSSent = true;
            }else{
                if(this.isSending){
                }else{
                    this.setState(15);
                    this.CTSSent = false;
                }
            }
            break;
        case 15: //waitData
            this.DataTimeout -= w.scheduler.getTimeAdvance();
            if(DataTimeout<0){
                this.setState(0);
                this.DataTimeout = this.maxDataTimeout;
                this.RTSOccupied = false;
                this.abortConnection();
            }
            break;
        case 16: //sift before ACK
            this.carrierSensing(17, 18, w);
            break;
        case 17:
            this.backoff(16, w);
            break;
        case 18: //sendACK
            if(!this.ACKSent){
                this.sendACK(w, n);
                this.ACKSent = true;
            }else{
                if(this.isSending){
                }else{
                    this.setState(0);
                    this.ACKSent = false;
                }
            }
            break;
        default:
            break;
        }
    }

    private void carrierSensing(int backoffState, int nextState, World w){
        if(this.isChannelBusy()){ //channel busy
            this.setState(backoffState); //to bakcoff
            this.updateBackoff();
        }else{
        	
            this.ift -= w.scheduler.getTimeAdvance();
            if(this.ift<0){
                this.setState(nextState);
                if(this.ift>0){
            		Exception e = new Exception();
            		try{
            			throw e;
            		}catch (Exception es){
            			es.printStackTrace();
            		}
            	}
            }
        }
    }
    
    private void backoff(int fromState, World w){
        this.backoff(fromState, w, false);
    }
    private void backoff(int fromState, World w, Boolean isDIFT){
        if(isChannelBusy()){
        }else{
            this.backoff -= w.scheduler.getTimeAdvance();
            if(this.backoff<0){
                this.setState(fromState);
                if(isDIFT){
                	this.ift = this.defaultDIFT;
                }else{
                	this.ift = this.defaultSIFT;
                }
            }
        }
    }

    private void discardOnePacket() {
        if(!this.sendQueue.isEmpty()){
            this.sendQueue.remove(0);
        }  
    }



    private void sendRTS(World w, Node n) {
		if(this.ift>0){
			Exception e = new Exception();
			try{
				throw e;
			}catch (Exception es){
				es.printStackTrace();
			}
		}
		if(this.getState()!=3){
			Exception e = new Exception("State not 3");
			try{
				throw e;
			}catch (Exception es){
				es.printStackTrace();
			}
		}
		
		Packet dataPacket = this.sendQueue.get(0);
	    this.RTSTo = dataPacket.getDestination();
	    ControlPacket p = (ControlPacket) Packet.createPacket(Packet.CONTROL_PACKET, "RTS", n.getId(), dataPacket.getDestination(), 160, w);
	    this.send(p, w, n);
	}



	protected void sendCTS(World w, Node n) {
	    ControlPacket p = (ControlPacket) Packet.createPacket(Packet.CONTROL_PACKET, "CTS", n.getId(), this.RTSFrom, 80, w);
	    this.send(p, w, n);
	}



	protected void sendData(World w, Node n) {
	    Packet p = this.sendQueue.get(0);
	    this.send(p,w,n);
	}



	protected void sendACK(World w, Node n) {
	    String control = "";
	    if(this.isACK){
	        this.abortConnection();
	        control = "ACK";
	    }else{
	        this.DataRetry++;
	        if(this.DataRetry>this.maxDataRetry){
	            this.abortConnection();
	        }
	        control = "NAK";
	    }
	    ControlPacket p = (ControlPacket) Packet.createPacket(Packet.CONTROL_PACKET, control, n.getId(), this.DataFrom, 80, w);
	    this.send(p, w, n);
	    
	}



	protected void receiveRTS(Packet p, World w, Node n) {
        
        if(p.getDestination() == n.getId()){
        	if(this.NAVTimer>0){
        		//NAVing
        		
        	}else{
	            if(!this.isConnecting()){
	                //establish a new connection
	                this.establishConnection(p.getSource());
	                this.RTSFrom = p.getSource();
	                this.setState(12);
	                this.ift = this.defaultSIFT;
	
	            }else if(this.connectingWith == p.getSource()){
	                //connection have established, 
                    //but the connecting node send another RTS
	                this.setState(12); 
	                this.DataRetry = 1;
	                this.ift = this.defaultSIFT;
	            }
        	}
        }else{
        	this.setNAVTimer(3*defaultSIFT); 
        }
        
        
    }
    
    protected void receiveCTS(Packet p, World w, Node n){
        if(p.getDestination() == n.getId()){
            if(this.getState() == 4 || this.getState()==3){
                this.setState(6);
                this.ift = this.defaultSIFT;
                this.establishConnection(p.getSource());                
                this.RTSTo = -1;
            }
        }else{
        	this.setNAVTimer(2*defaultSIFT);
        }
    }
    
    protected void receiveData(Packet p, World w, Node n) {
	    if(p.getDestination() == n.getId()){
	    	this.DataFrom = p.getSource();
	        if(p.isHasError()){
	            this.isACK = false;
	        }else{
	            this.isACK = true;
	            
	            double energy = 0;
	            for(int i=0; i<w.nodes.size(); i++){
	                Node node = w.nodes.get(i);
	                energy += node.energyModule.getEnergyConsumption();
	            }
	            UCSimEvent event = UCSimEvent.createUCSimEvent(
	                    w.scheduler.getNowTime(), 
	                    "Receive data", 
	                    String.format("{\"data\":%d, \"energy\":%f}", 
	                                p.getLength()-PaperMAC.headerSize,
	                                energy
	                            )
	                );
//	            n.dataLogger.logEvent(event);
	            
	            n.app.receivePacket(p, n, w);
	        }
	        
	        this.setState(16);
	        this.ift = this.defaultSIFT;
	        
	        
	    }
	}



	protected void receiveNAK(Packet p, World w, Node n){
        if(p.getDestination() == n.getId()){
            if(this.getState() ==9 || this.getState()==8){
                setState(10);
                
            }
        }
        
    }
    
    protected void receiveACK(Packet p, World w, Node n){
        if(p.getDestination() == n.getId()){
            if(this.getState() ==9 || this.getState()==8){
                setState(11);
                this.abortConnection();
            }
        }
        
    }
    
    public void receiveEnd(Packet p, World w, Node n) {
        super.receiveEnd(p, w, n);
        if(!p.isHasError()){
            if(p.getData().contains("RTS")){
                this.receiveRTS(p, w, n);
            }else if(p.getData().contains("CTS")){
                this.receiveCTS(p, w, n);
            }else if(p.getData().contains("ACK")){
                this.receiveACK(p, w, n);
            }else if(p.getData().contains("NAK")){
                this.receiveNAK(p, w, n);
            }
        }
        
        if(p.getData().contains("Data")){
            this.receiveData(p, w, n);
        }
        
        n.energyModule.receiveConsumeEnergy(p.getReceiveTime()-p.getSendTime());
//        UCSimEvent event = UCSimEvent.createUCSimEvent(
//                w.scheduler.getNowTime(), 
//                "Energy Update", 
//                String.format("{\"energy\":%f}", n.energyModule.getEnergyConsumption())
//            );
//        n.dataLogger.logEvent(event);
    }


    public int getConnectingWith() {
        return connectingWith;
    }
    public void setConnectingWith(int connectingWith) {
        this.connectingWith = connectingWith;
    }
    public double getNAVTimer() {
		return NAVTimer;
	}
	public void setNAVTimer(double nAVTimer) {
		if(nAVTimer<0){
			NAVTimer = 0;
			return;
		}
		NAVTimer = nAVTimer;
	}
	
	private void establishConnection(int with){
	    this.connectingWith = with;
	}
	
	protected void abortConnection(){
	    this.connectingWith = -1;
	}
	
	private boolean isConnecting(){
	    return (connectingWith>=0);
	}
	
	





}
