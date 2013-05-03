package ucsim.graph.spacedomaingraph;

import processing.core.*;
import ucsim.world.World;


@SuppressWarnings("serial")
public class SpaceDomainGraph extends PApplet {
    private World world;
    
    public SpaceDomainGraph(World world){
        this.world = world;
    }
	
	public void setup(){
	    size(800, 600, P3D);
	    smooth(2);
	    frameRate(30);
	    noCursor();
	    
//	    Thread t = new Thread(world);
//	    t.start();
	}
	
	public void draw(){
	    beginCamera();
	    background(255);
	    noStroke();
	    lights();
	  //  directionalLight(0, 255, 0, 0, 1, 0);
	    world.show(this, null);
	    camera(mouseX, mouseY, 1000, 250, 250, 250, 0, 1, 0);
	    endCamera();
	}

}
