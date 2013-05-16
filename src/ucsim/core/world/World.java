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

import ucsim.core.coordinate.Coordinate;
import ucsim.core.node.Node;

/**
 * World, the stage where the nodes packets in.
 * Singleton pattern
 * @author yifan
 *
 */
public class World {
    
	Coordinate bounday = null;
	
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Channel> channels = new ArrayList<Channel>();
    
	
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
	
	
}
