package ucsim.node.mac;

import java.util.ArrayList;

import ucsim.node.Node;
import ucsim.node.mac.simplemac.SimpleMAC;
import ucsim.node.mac.csmamac.CSMAMAC;
import ucsim.node.mac.csmacamac.CSMACAMAC;
import ucsim.node.mac.papermac.PaperMAC;
import ucsim.packet.Packet;
import ucsim.world.World;

public abstract class MAC {
	private   int 					      state 			= 0;
    public    ArrayList<Packet> 		  receiveQueue   	= new ArrayList<Packet>();
	public    ArrayList<Packet> 		  sendQueue 		= new ArrayList<Packet>();
	protected boolean 				      isSending 		= false;
	protected int 					      receivingCount 	= 0;
    protected int connectingWith = -1;
    
    public static final int SIMPLEMAC 	= 1;
    public static final int CSMAMAC 	= 2;
    public static final int CSMACAMAC   = 3;
    public static final int PAPERMAC    = 4; 
    
    public static MAC createMAC(int type){
    	MAC mac = null;
    	switch(type){
    	case MAC.SIMPLEMAC:
    		return new SimpleMAC();
    	case MAC.CSMAMAC:
    		return new CSMAMAC();
    	case MAC.CSMACAMAC:
    		return new CSMACAMAC();
    	case MAC.PAPERMAC:
    		return new PaperMAC();
    	}
		return mac;
    }

	public void tick(World world, Node n){}
	public void send(Packet p, World w, Node n){}
	public void sendEnd(){}
	public void receive(Packet p){}
	public void receiveEnd(Packet p, World w, Node n){}
    
	public void getFromApp(Packet p){
		this.sendQueue.add(p);
	}
	
	
	public boolean isSending() {
        return isSending;
    }
    public void setSending(boolean isSending) {
        this.isSending = isSending;
    }
    public int getReceivingCount() {
        return receivingCount;
    }
    public void setReceivingCount(int receivingCount) {
        this.receivingCount = receivingCount;
    }
	public int getState() {
		return state;
	}
	public void setState(int state) {
//		if(this.state!=3 && state == 3 ){
//			Exception e = new Exception();
//			e.printStackTrace();
//		}
		this.state = state;
//		System.out.printf("set state %d\n", state);
		
	}
	protected void sendACK(World w, Node n) {
		
	}
    
	
    
}
