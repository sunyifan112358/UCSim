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
package ucsim;

import ucsim.core.world.World;


/**
 * program entrance of UCSim
 * @author yifan
 *
 */
public class UCSim {

    /**
     * Program entrance of UCSim
     * @param args arguments, not required
     */
    public static void main(String[] args) {
        World.setSize(1500, 1500, 1500);
        
    }

}
