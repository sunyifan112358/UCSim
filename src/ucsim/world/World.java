package ucsim.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import processing.core.*;
import ucsim.coordinate.Coordinate;
import ucsim.datalogger.DataLogger;
import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.graph.showable.Showable;
import ucsim.graph.showable.ShowableInTimeDomain;
import ucsim.graph.timedomaingraph.TimeDomainGraph;
import ucsim.node.Node;
import ucsim.node.mac.MAC;
import ucsim.node.mac.csmacamac.CSMACAMAC;
import ucsim.node.mac.csmamac.CSMAMAC;
import ucsim.node.mac.papermac.PaperMAC;
import ucsim.packet.Packet;
import ucsim.world.scheduler.*;
import ucsim.world.propagationmodel.PropagationModel;

@SuppressWarnings("unused")
public class World implements Runnable, Showable, ShowableInTimeDomain {

	protected Coordinate 				worldMax 	          = new Coordinate(0,0,0);
	public    ArrayList<Node> 		    nodes 		          = new ArrayList<Node>();
	public    ArrayList<Packet> 		packets 	          = new ArrayList<Packet>();
	private   double 					scale		          = 1;

	public    Scheduler                 scheduler             = Scheduler.createScheduler(Scheduler.REALTIME, 2000, 2000, 1);
//	public    Scheduler                 scheduler             = Scheduler.createScheduler(Scheduler.FIXEDINCREASEMENT, 20000, 20000, 1e-3);
	public    DataLogger 				dataLogger            = new DataLogger("World.log", true);
	public    PropagationModel 		    propagationModel      = new PropagationModel();

	
	public    static final double 		dataRate              =  800;
	
    public World(double x, double y, double z) {
        this.worldMax = new Coordinate(x, y, z);
        this.setScale(500 / Math.max(Math.max(x, y), z));
        Random rnd = new Random();

//        for (int i = 0; i < 5; i++) {
//            double xTemp = rnd.nextDouble() * x;
//            double yTemp = rnd.nextDouble() * y;
//            double zTemp = rnd.nextDouble() * z;
//            this.nodes.add(new Node(i, xTemp, yTemp, zTemp, this));
//        }
//         this.nodes.add(new Node(0, 0, 0, 0));
//         this.nodes.add(new Node(1, 0, 0, z));
//         this.nodes.add(new Node(2, 0, y, 0));
//         this.nodes.add(new Node(3, 0, y, z));
//         this.nodes.add(new Node(4, x, 0, 0));
//         this.nodes.add(new Node(5, x, 0, z));
//         this.nodes.add(new Node(6, x, y, 0));
//         this.nodes.add(new Node(7, x, y, z));
        this.nodes.add(new Node(0, 0, y/2, z/2, this, 100));
        this.nodes.add(new Node(1, x, y/2, z/2, this, 0));
        for (int i=0; i<nodes.size(); i++){
        	Node n = nodes.get(i);
        	if(n.mac instanceof PaperMAC){
        		((PaperMAC)n.mac).createProfilerList(this, n);
        	}
        }
    }

	@Override
	public void run() {
		while(true){
			scheduler.update();
			for(int i=0; i<nodes.size(); i++){
				Node node = (Node)nodes.get(i);
				node.tick(this);
			}
			for (int i=0; i<packets.size(); i++) {
				Packet p = (Packet)packets.get(i);
				Node fromNode = (Node)nodes.get(p.getSource());
				Node toNode = (Node)nodes.get(p.getTo());

				p.propagate(fromNode, toNode, this);
			}
			if (scheduler.getNowTime()>=scheduler.getTotalTime()) {
				System.exit(0);
			}
		}

	}
	
	public void show(PApplet pApplet, World w) {
		pApplet.noFill();
		pApplet.stroke(0, 50);
		pApplet.strokeWeight(1);
		pApplet.pushMatrix();
		pApplet.translate(
        		            (float)(this.worldMax.getScreenX(getScale())/2.0), 
        		            (float)(this.worldMax.getScreenY(getScale())/2.0), 
        		            (float)(this.worldMax.getScreenZ(getScale())/2.0)
		                 );
		pApplet.box(
		                (float)this.worldMax.getScreenX(getScale()), 
		                (float)this.worldMax.getScreenY(getScale()), 
		                (float)this.worldMax.getScreenZ(getScale())
		           );
		pApplet.popMatrix();

		
		scheduler.show(pApplet, this);
		
		pApplet.camera(pApplet.mouseX, pApplet.mouseY, 1000, 250, 250, 250, 0, 1, 0);

		for (int i=0; i<nodes.size(); i++) {
			Node node = (Node)nodes.get(i);
			node.show(pApplet, this);
		}

		for (int i=0; i<nodes.size(); i++) {
			Node sender = (Node)nodes.get(i);
			for (int j=0; j<nodes.size(); j++) {
				Node receiver = (Node)nodes.get(j);
				pApplet.noFill();
				pApplet.stroke(100, 50);
				pApplet.strokeWeight(1);
				
				if(sender.mac instanceof CSMACAMAC){
    				if(((CSMACAMAC) sender.mac).getConnectingWith() == receiver.getId()){
    				    pApplet.stroke(12, 230, 76, 200);
    				}
				}
				
				pApplet.line(  
        				        (float)sender.getPosition().getScreenX(getScale()), 
        				        (float)sender.getPosition().getScreenY(getScale()), 
        				        (float)sender.getPosition().getScreenZ(getScale()), 
        				        (float)((sender.getPosition().getScreenX(getScale())+receiver.getPosition().getScreenX(getScale()))*0.5), 
        				        (float)((sender.getPosition().getScreenY(getScale())+receiver.getPosition().getScreenY(getScale()))*0.5), 
        				        (float)((sender.getPosition().getScreenZ(getScale())+receiver.getPosition().getScreenZ(getScale()))*0.5) 
				            );
			}
		}
		
		
//		System.out.println(packets.size());
		for (int i=0; i<packets.size(); i++) {
			Packet p = (Packet)packets.get(i);
			
			p.show(pApplet, this);
		}


	}

	@Override
    public void showInTimeDomain(PApplet pApplet, World w) {
		TimeDomainGraph tdg = (TimeDomainGraph)pApplet;
	    tdg.pg.stroke(200);
	    for (int i=0; i<nodes.size(); i++) {
            Node node = (Node)nodes.get(i);
            float h =(i+1)*(tdg.pg.height)/((float)nodes.size()+1); 
            tdg.pg.line(0, h, tdg.pg.width, h);
        }
	    
	    for(int i=0; i<packets.size(); i++){
	    	Packet packet = (Packet)packets.get(i);
	    	float x = (float) (w.scheduler.getNowTime()/w.scheduler.getTotalTime()*tdg.pg.width);
	    	
	    	if(packet.isHeadLeaved() && !packet.isTailLeaved()){
	    		Color c = packet.findColor();
	    		tdg.pg.fill(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	    		tdg.pg.noStroke();
	    		float y = (packet.getSource()+1)*(tdg.pg.height)/((float)nodes.size()+1);
	    		float width = (float) ((tdg.nowDrawTime-tdg.lastDrawTime)/w.scheduler.getTotalTime()*tdg.pg.width);
//	    		float width = 1;
	    		tdg.pg.rect(x-width, y-tdg.pg.height/(nodes.size()+2)/2, width, tdg.pg.height/(nodes.size()+2)/2);
	    	}
	    	if(packet.isHeadArrived() && !packet.isTailArrived()){
	    		Color c = packet.findColor();
	    		tdg.pg.fill(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	    		tdg.pg.noStroke();
	    		float y = (packet.getTo()+1)*(tdg.pg.height)/((float)nodes.size()+1);
	    		float width = (float) ((tdg.nowDrawTime-tdg.lastDrawTime)/w.scheduler.getTotalTime()*tdg.pg.width);
	    		float height = (float)(Math.max(0,Math.log10(packet.getPower()/(packet.getNoise()+packet.getInterference()))));
	    		tdg.pg.rect(x-width, y, width, height*tdg.pg.height/(nodes.size()+2)/2);
	    	}
	    	
	    }
    }
	
	
	public void finish() {
		float e = 0;
		for (int i=0; i<nodes.size(); i++) {
			Node n = (Node)nodes.get(i);
			e += n.energyModule.getEnergyConsumption();
			n.dataLogger.close();
		}
		UCSimEvent event = UCSimEvent.createUCSimEvent(
                    this.scheduler.getNowTime(), 
                    "Total Evergy", 
                    String.format("\"Energy\":%f", e)
                );
		this.dataLogger.logEvent(event);
		this.dataLogger.close();
	}




    public double getScale() {
        return scale;
    }




    public void setScale(double scale) {
        this.scale = scale;
    }




    


}
