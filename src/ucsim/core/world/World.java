/*
  Part of the UCSim project

  Copyright (c) 2013-13 Yifan Sun

  This library is free software; you can redistribute it and/or
  modify it under the terms of version 2.01 of the GNU Lesser General
  Public License as published by the Free Software Foundation.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library
*/
package ucsim.core.world;

import java.nio.channels.Channel;
import java.util.ArrayList;

import processing.core.PApplet;

import ucsim.core.block.Block;
import ucsim.core.coordinate.Coordinate;
import ucsim.core.node.Node;
import ucsim.visualization.spacedomaingraph.ShowableInSDG;

/**
 * World, the stage where the nodes packets in.
 * Singleton pattern
 * The largest block that every sub-block lies in
 * @author yifan
 *
 */
public class World extends Block implements ShowableInSDG{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Coordinate bounday = null;
	
    protected ArrayList<Node> nodes = new ArrayList<Node>();
    protected ArrayList<Channel> channels = new ArrayList<Channel>();
    
	
    /**
     * instance of world
     */
    private static World world = new World();
    
    /**
     * get the instance of world
     * @return instance of the world
     */
    public static World getInstance(){
        return World.world;
    }

	/**
	 * set the size of the world
	 * @param x max x
	 * @param y max y
	 * @param z max z
	 */
	public static void setSize(double x, double y, double z) {
		World.world.bounday = new Coordinate(x, y, z);
	}
	
	/**
	 * process 
	 */
	public void process(){
		
	}
	
	/**
	 * set the camera goes with mouse
	 * @param g
	 */
	public static void setCamera(PApplet g){
		World w = World.getInstance();
		g.camera(
				(float)((g.mouseX*1.0/g.width)*w.bounday.getX()),
				(float)((g.mouseY*1.0/g.height)*w.bounday.getY()),
				(float)(2.0*w.bounday.getZ()),
				(float)(w.bounday.getX()/2.0), 
				(float)(w.bounday.getY()/2.0), 
				(float)(w.bounday.getZ()/2.0),
				(float)0.0, (float)1.0, (float)0.0
			);
	}

	/* (non-Javadoc)
	 * @see ucsim.visualization.spacedomaingraph.ShowableInSDG#showInSDG(processing.core.PApplet)
	 */
	@Override
	public void showInSDG(PApplet g) {
		World.setCamera(g);
		g.noFill();
		g.stroke(0, 50);
		g.pushMatrix();
			g.translate(
						(float)(this.bounday.getX()/2.0),
						(float)(this.bounday.getY()/2.0),
						(float)(this.bounday.getZ()/2.0)
					);
			g.box(
						(float)(this.bounday.getX()),
						(float)(this.bounday.getY()),
						(float)(this.bounday.getZ())
				  );
		g.popMatrix();
		for(int i=0; i<nodes.size(); i++){
			Node n = nodes.get(i);
			n.showInSDG(g);
		}
		
	}
	
	/**
	 * add node to world
	 * @param n node to be added
	 */
	public static void addNode(Node n){
		World w = World.getInstance();
		w.nodes.add(n);
	}

}
