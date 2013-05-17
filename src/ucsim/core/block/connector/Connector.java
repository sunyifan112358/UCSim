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
package ucsim.core.block.connector;

import java.util.ArrayList;

import ucsim.core.block.pin.InputPin;
import ucsim.core.block.pin.OutputPin;




/**
 * Connection between an output pin and an input pin
 * 
 * @author yifan
 *
 */
public class Connector{
    private OutputPin output = null;
    private InputPin  input  = null;
    
    
    /**
     * @param outputPin
     * @param inputPin
     */
    public Connector(OutputPin outputPin, InputPin inputPin) {
        this.setOutput(outputPin);
        this.input = inputPin;
    }

    /**
     * deliver all the data from output pin to input pin
     */
    public void trasmit(){
    	ArrayList<Object> buf = this.output.getBuffer();
    	for(int i=0; i<buf.size(); i++){
    		Object o = buf.get(i);
    		this.input.input(o);
    	}
    }

    /**
     * @return the output
     */
    public OutputPin getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(OutputPin output) {
        this.output = output;
    }
    
    /**
     * clear the buffer of output pin
     */
    public void clear(){
    	this.output.clearBuffer();
    }
    
    /**
     * Connect output pin and input pin
     * @param op
     * @param ip
     */
    public static void Connect(OutputPin op, InputPin ip){
    	Connector c = new Connector(op, ip);
    	ConnectorManager.addConnector(c);
    }
    
}
