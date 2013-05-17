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
package ucsim.core.block.connector;

import java.util.ArrayList;

/**
 * A container contains all the connections
 * @author Yifan
 *
 */
public class ConnectorManager {
	
	/**
	 * Instance of Connector Manager
	 */
	private static ConnectorManager connectorManager = new ConnectorManager();
	
	/**
	 * arraylist that contains all the connectors
	 */
	private ArrayList<Connector> connectors = new ArrayList<Connector>();
	
	private ConnectorManager(){}
	
	/**
	 * process, transmit all data 
	 */
	public void process(){
		for(int i=0; i<connectors.size(); i++){
			Connector c = connectors.get(i);
			c.trasmit();
		}
		for(int i=0; i<connectors.size(); i++){
			Connector c = connectors.get(i);
			c.clear();
		}
	}
	
	/**
	 * get instance
	 * @return instance
	 */
	public static ConnectorManager getInstance(){
		return ConnectorManager.connectorManager;
	}
	
	/**
	 * @param c
	 */
	public static void addConnector(Connector c){
		if(ConnectorManager.getInstance().connectors.indexOf(c)<0){
			ConnectorManager.getInstance().connectors.add(c);
		}
	}
	
}
