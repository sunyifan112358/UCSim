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
package ucsim.visualization.spacedomaingraph;

import processing.core.PApplet;

/**
 * Space domain graph, a processing window;
 * @author yifan
 *
 */
public class SpaceDomainGraph extends PApplet {

    /**
     * default serial ID
     */
    private static final long serialVersionUID = 1L;
    
    public void setup(){
        size(800, 600, P3D);
        smooth(2);
        frameRate(30);
        noCursor();
    }
    
    public void draw(){
        
    }

}
