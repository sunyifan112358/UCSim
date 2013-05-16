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
package ucsim.core.node.protocol;

import ucsim.core.block.pin.OutputPin;
import ucsim.core.packet.Packet;

/**
 * @author Yifan
 *
 */
public class SimpleProtocol extends Protocol {

	/**
	 * Default serial Id
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see ucsim.core.node.protocol.Protocol#process()
	 */
	@Override
	public void process() {
		this.processPacketFromHigherLayer();
		this.processPacketFromLowerLayer();
	}
	
	@Override
	protected void processOnePacketFromHigherLayer(Packet p) {
		OutputPin op = this.getOutputPin("toLower");
		op.output(p);
	}
	
	@Override
	protected void processOnePacketFromLowerLayer(Packet p) {
		OutputPin op = this.getOutputPin("toHigher");
		op.output(p);
	}

}
