package ucsim.node;

import processing.core.*;
import ucsim.coordinate.Coordinate;
import ucsim.datalogger.DataLogger;
import ucsim.graph.showable.Showable;
import ucsim.node.application.Application;
import ucsim.node.application.poissontraffic.PoissonTraffic;
import ucsim.node.energymodel.EnergyModule;
import ucsim.node.mac.MAC;
import ucsim.world.World;

public class Node implements Showable{
	protected int 			    id;
	private   Coordinate 		position;
	protected double 		    size = 15;
	protected double 			sendingTime = 0;


	public    MAC 			        mac 				= MAC.createMAC(MAC.PAPERMAC);
	public    Application 	        app 				= new PoissonTraffic(100, 1500*8);
	public    EnergyModule 	        energyModule 		= new EnergyModule();
	public    DataLogger            dataLogger          = null;

	public Node(int id, double x, double y, double z, World w, double rate){
	    this.id = id;
		this.setPosition(new Coordinate(x,y,z));
		this.dataLogger = new DataLogger(String.format("Node%d.log", this.id), true);
		this.app = new PoissonTraffic(rate, 1500*8);
	}

	public void tick(World w){
		app.tick(w, this);
		mac.tick(w, this);
		if(mac.isSending()){
			sendingTime+=w.scheduler.getTimeAdvance();
		}
	}

	public void show(PApplet pApplet, World w) {
		pApplet.noStroke();
		pApplet.pushMatrix();
		pApplet.translate(
		            (float)getPosition().getScreenX(w.getScale()),
		            (float)getPosition().getScreenY(w.getScale()), 
		            (float)getPosition().getScreenZ(w.getScale())
		         );
		if(mac.getReceivingCount()==0 && !mac.isSending()){
		    pApplet.fill(200);
		}else if((mac.getReceivingCount()==0&&mac.isSending())||(mac.getReceivingCount()==1&&!mac.isSending())){
		    pApplet.fill(21,248,48);
		}else{
		    pApplet.fill(212, 42, 15);
		}
		pApplet.textSize(20);
		pApplet.sphere((float) size);
		pApplet.textAlign(PConstants.CENTER);
		pApplet.fill(0);
        pApplet.text(String.format("%d", this.mac.getState()), 0,-20,0);
        
		pApplet.popMatrix();
		
	}
	
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
	

}
