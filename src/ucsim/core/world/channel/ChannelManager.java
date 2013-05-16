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

import java.util.ArrayList;

/**
 * 
 * Channel Manager: Wrapped channels
 * @author yifan
 *
 */
public class ChannelManager{

    private static ChannelManager channelManager= new ChannelManager();
    
    private ArrayList<Channel> channels = new ArrayList<Channel>();
    
    /**
     * Constructor
     */
    private ChannelManager() {
        
    }

    
    /**
     * get how many channels are being used
     * @return Channel.channels.size()
     */
    public static int numberOfChannels(){
        return ChannelManager.channelManager.channels.size();
    }
    
    /**
     * get instance of channelManager
     * @return Channel or null
     */
    public static ChannelManager getInstance(){
        return ChannelManager.channelManager;
    }
    
    /**
     * get instance of Channel
     * @param i index of requested Channel
     * @return Channel or null
     */
    public static Channel getChannel(int i){
        if(i>ChannelManager.numberOfChannels()-1){
            try {
                throw new Exception(String.valueOf(i)+" is out of Bound");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }else{
            return ChannelManager.channelManager.channels.get(i);
        }
    }
    
    /**
     * Define a new Channel
     * @return index of inserted channel
     */
    public static int registerChannel(){
        Channel c = new Channel();
        //TODO: parameter setting goes here
        ChannelManager.channelManager.channels.add(c);
        return ChannelManager.channelManager.channels.size()-1;
    }
    

}
