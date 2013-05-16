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
package ucsim.core.simulation;

import ucsim.core.scheduler.Scheduler;
import ucsim.core.world.World;
import ucsim.visualization.PFrame;
import ucsim.visualization.spacedomaingraph.SpaceDomainGraph;

/**
 * Simulation - singleton 
 * @author yifan
 *
 */
public class Simulation {
	
	
	
	private static Simulation simulation = new Simulation();
	/**
	 * private constructor
	 */
	private Simulation(){
		
	}
	
	/**
	 * get instance of Simulation
	 * @return instance of simulation
	 */
	public static Simulation getInstance(){
		return Simulation.simulation;
	}
	
     /**
     * Simulation start;
     */
    public static void start(){
        SpaceDomainGraph sdg = new SpaceDomainGraph();
        @SuppressWarnings("unused")
		PFrame sdg_frame = new PFrame(sdg);
        while(true){
        	Scheduler.update();
        	World.getInstance().process();
        }
    }   
    
}
