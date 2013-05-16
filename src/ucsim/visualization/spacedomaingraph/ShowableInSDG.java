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
 * 
 * @author Yifan
 *
 */
public interface ShowableInSDG {
	/**
	 * show in space domain graph
	 * @param g graph for 
	 */
	public void showInSDG(PApplet g);
}
