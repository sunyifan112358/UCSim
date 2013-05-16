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
package ucsim.core.block.pin;

import ucsim.core.block.Block;
import ucsim.core.block.Connector;

/**
 * class for output pins
 * @author yifan
 * 
 */
public class OutputPin extends Pin {
    
    private Connector connector = null;
   
    /**
     * Constructor for output pin
     * @param owner owner block 
     * @param name  name of this pin
     */
    public OutputPin(Block owner, String name) {
        super(owner, name);
    }
    
    /**
     * connect it with an input pin
     * @param inputPin
     */
    public void connect(InputPin inputPin){
        this.connector = (new Connector(this, inputPin));
    }
    
    /**
     * output data to connector
     * @param data
     */
    public void output(Object data){
        if(this.connector!=null){
            this.connector.trasmit(data);
        }
    }
    
    
    

}
