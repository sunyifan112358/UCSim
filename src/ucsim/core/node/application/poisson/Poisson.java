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
package ucsim.core.node.application.poisson;

import ucsim.core.math.Distribution;
import ucsim.core.node.protocol.Protocol;
import ucsim.core.packet.Packet;
import ucsim.core.scheduler.Scheduler;

/**
 * @author Yifan
 *
 */
public class Poisson extends Protocol {

	/**
	 * default serial id
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * how many packet expected to generate per second
	 */
	private double packetPerSecond = 1.0;
	private final int    maxLength = 1500;
	private final int    minLength = 50;

	/**
	 * 
	 */
	public Poisson(){
		
	}
	/* (non-Javadoc)
	 * @see ucsim.core.block.Block#process()
	 */
	@Override
	public void process() {
		this.processPacketFromLowerLayer();
		double lambda = packetPerSecond * Scheduler.getTimeAdvance();
		Distribution d = new Distribution();
		int numPacket = d.nextPoisson(lambda);
		
		for(int i=0; i<numPacket; i++){
			Packet p = new Packet();
			int length = d.nextInt(this.maxLength)+this.minLength;
			p.setData( Packet.randomString(length) );
		}
		
	}
	
	/* (non-Javadoc)
	 * @see ucsim.core.node.protocol.Protocol#processOnePacketFromLowerLayer(ucsim.core.packet.Packet)
	 */
	@Override
	protected void processOnePacketFromLowerLayer(Packet p) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see ucsim.core.node.protocol.Protocol#processOnePacketFromHigherLayer(ucsim.core.packet.Packet)
	 */
	@Override
	protected void processOnePacketFromHigherLayer(Packet p) {
		// Not applicable
	}

}
