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


/**
 * Base class for different kinds of protocols
 * 
 * @author yifan
 */
abstract public class Protocol extends Block{
    
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


}
