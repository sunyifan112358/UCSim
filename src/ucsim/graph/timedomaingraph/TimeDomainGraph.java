package ucsim.graph.timedomaingraph;

import processing.core.*;
import ucsim.world.World;
import java.awt.event.*;

@SuppressWarnings("serial")
public class TimeDomainGraph extends PApplet {
    private World world;
    public PGraphics pg;
    private ScrollButton sb;
    private float scale = 1;
    public double lastDrawTime = 0;
	public double nowDrawTime = 0;;
    public TimeDomainGraph(World w){
        this.world = w;
        sb = new ScrollButton();
    }
    public void setup(){
        size(800, 600);
        background(255);
        this.pg = createGraphics((int) (Math.min(20000, world.scheduler.getTotalTime()*10)), 600);
        addMouseWheelListener(new MouseWheelListener() { 
            public void mouseWheelMoved(MouseWheelEvent mwe) { 
              mouseWheel(mwe.getWheelRotation());
            }
        });

			
    }
    public void draw(){
    	this.nowDrawTime  = world.scheduler.getNowTime();
    	pg.beginDraw();
        world.showInTimeDomain(this, world);
        pg.endDraw();
        background(255);
        PImage img = pg.get(); 
        
        image(pg,(this.width-scale*img.width)*sb.getPosition(),0);
        float currentX = (float) ((this.width-scale*img.width)*sb.getPosition() + world.scheduler.getNowTime()/world.scheduler.getTotalTime()*pg.width);
        stroke(255, 32, 45, 200);
        line(currentX, 0, currentX, this.height);
        
        sb.show(this, world);
        this.lastDrawTime = this.nowDrawTime;
        
        
    }
    
    public void mouseWheel(int wheelRotation) {
		this.sb.setPosition((float) (this.sb.getPosition()+wheelRotation*0.001));
		
	}
    
    public void mousePressed(){
    	sb.pressed(this);
    }
    
    public void mouseDragged(){
    	sb.dragged(this);
    }
    public void mouseReleased(){
    	sb.released(this);
    }
}
