/**
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
package ucsim.core.world.channel;

import ucsim.core.node.Node;
import ucsim.core.block.Block;
import ucsim.core.block.pin.InputPin;
import ucsim.core.block.pin.OutputPin;

/**
 * Channel class;
 * <p>
 *  channel is a box with 1 input pin and many output pins;
 * </p>
 * 
 * @author yifan
 *
 */
public class Channel extends Block{
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor
     */
    public Channel() {
        this.registerInputPin(new InputPin(this, "intoChannel"));
    }

    /* (non-Javadoc)
     * @see ucsim.block.Block#process()
     */
    @Override
    public void process() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * register node (actually interface) to channel
     * @param n node to be registered
     */
    public void registerNode(
                                 Node n      
                             ){
        OutputPin op = new OutputPin(this, String.format("toNode%d", this.outputPins.size()));
        op.connect(n.getInputPin("intoChannel"));
        this.registerOutputPin(op);
        n.getOutputPin("toChannel").connect(this.getInputPin("intoChannel"));       
    }
    

}
