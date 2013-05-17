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

import ucsim.core.block.Block;
import ucsim.core.block.pin.InputPin;
import ucsim.core.block.pin.OutputPin;
import ucsim.core.packet.Packet;


/**
 * Base class for different kinds of protocols
 * 
 * @author yifan
 */
abstract public class Protocol extends Block{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Constructor of Protocol
     */
    public Protocol(){
        OutputPin toLower = new OutputPin(this, "toLower");
        OutputPin toHigher  = new OutputPin(this, "toHigher");
        InputPin fromLower  = new InputPin(this, "fromLower");
        InputPin fromHigher = new InputPin(this, "fromHigher");
        this.registerInputPin(fromLower);
        this.registerInputPin(fromHigher);
        this.registerOutputPin(toLower);
        this.registerOutputPin(toHigher);
    }
    
    
    /**
     * process input data, generate output data
     */
    public abstract void process();
    
    /**
     * grab a packet from the "fromHigher" pin
     * @return packet or null
     */
    protected Packet getPacketFromHigherLayer(){
    	Packet p = null;
    	InputPin pin = this.getInputPin("fromHigher");
    	p = (Packet) pin.getData();
    	return p;
    }
    /**
     * grab a packet from the "fromLower" pin
     * @return packet or null
     */
    protected Packet getPacketFromLowerLayer(){
    	Packet p = null;
    	InputPin pin = this.getInputPin("fromLower");
    	p = (Packet) pin.getData();
    	return p;
    }
    
    /**
     * process the packet queue from higher layer
     */
    protected void processPacketFromHigherLayer(){
    	while(true){
    		Packet p = this.getPacketFromHigherLayer();
    		if(p==null){
    			return;
    		}else{
    			this.processOnePacketFromHigherLayer(p);
    		}
    	}
    }
    
    

	/**
     * process the packet queue from lower layer
     */
    protected void processPacketFromLowerLayer(){
    	while(true){
    		Packet p = this.getPacketFromLowerLayer();
    		if(p==null){
    			return;
    		}else{
    			this.processOnePacketFromLowerLayer(p);
    		}
    	}
    }


	/**
	 * Process a single packet from the lower layer
	 * @param p packet to be processed
	 */
	protected abstract void processOnePacketFromLowerLayer(Packet p);
	
	/**
	 * Process a single packet from the higher layer
	 * @param p packet to be processed
	 */
	protected abstract void processOnePacketFromHigherLayer(Packet p);


}
