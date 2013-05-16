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
package ucsim.core.node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import ucsim.core.block.Block;
import ucsim.core.block.pin.InputPin;
import ucsim.core.block.pin.OutputPin;
import ucsim.core.node.protocol.Protocol;
import ucsim.core.world.channel.Channel;

/**
 * Network node extends block
 * @author yifan
 *
 */
public class Node extends Block implements Serializable{
    
    /**
     * default serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * arraylist contains all node
     */
    protected ArrayList<Protocol> protocolStack = new ArrayList<Protocol>();
 
    /**
     * constructor for Node
     */
    public Node(){
        OutputPin toChannel = new OutputPin(this, "toChannel");
        InputPin  fromChannel = new InputPin(this, "fromChannel");
        this.registerInputPin(fromChannel);
        this.registerOutputPin(toChannel);
    }
    
    /**
     * Clone node from prototype node
     * @param n prototype node
     * @return cloned node  
     */
    public static Node cloneNode(Node n){
        Node node = null;
        
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();   
            ObjectOutputStream out = new ObjectOutputStream(bos);   
            out.writeObject(n);   
            out.flush();   
            out.close();
            
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());    
            ObjectInputStream in = new ObjectInputStream(bis);   
            node = (Node) in.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return node;
        
    }

    /* (non-Javadoc)
     * @see ucsim.core.block.Block#process()
     */
    @Override
    public void process() {
        for(int i=0; i<protocolStack.size(); i++){
            Protocol protocol = this.protocolStack.get(i);
            protocol.process();
        }
    }
    
    
    /**
     * push protocol into protocol stack
     * @param p         protocol to be added
     * @param pBeneath  protocol beneath p 
     * @param pAbove    protocol above   p
     */
    public void addProtocol(
                                Protocol p, 
                                Protocol pBeneath, 
                                Protocol pAbove
                            ){
        this.protocolStack.add(p);
        if(pBeneath!=null){
            Block.connectPins(
                                p.getOutputPin("toLower"), 
                                pBeneath.getInputPin("fromHigher")
                             );
            Block.connectPins(
                                pBeneath.getOutputPin("toHigher"), 
                                p.getInputPin("fromLower")
                             );
        }
        if(pAbove!=null){
            Block.connectPins(
                                p.getOutputPin("toHigher"), 
                                pAbove.getInputPin("fromLower")
                             );  
            Block.connectPins(
                                pAbove.getOutputPin("toLower"), 
                                p.getInputPin("fromHigher")
                             );
        }
    }
       
    
    
    /**
     * Link lowest protocol with nodes output pins
     * @param p
     */
    public void assignPins(Protocol p){
        this.registerOutputPin(p.getOutputPin("toLower"));
    }
    
    /**
     * @param c
     */
    public void registerNode(Channel c){
       c.registerNode(this);
    }
   

}
