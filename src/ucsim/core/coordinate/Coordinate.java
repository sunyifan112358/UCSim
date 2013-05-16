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
package ucsim.core.coordinate;

import java.io.Serializable;

/**
 * A point in 3-dimension
 * @author yifan
 *
 */
public class Coordinate implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double x;
    private double y; 
    private double z;
    
    /**
     * Constructor of coordinate
     * @param x x position
     * @param y y position
     * @param z z position
     */
    public Coordinate(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

}
