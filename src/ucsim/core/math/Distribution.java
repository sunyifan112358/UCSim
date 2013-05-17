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
package ucsim.core.math;

import java.util.Random;

/**
 * random variable generator 
 * @author Yifan
 *
 */
public class Distribution extends Random {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param lambda
	 * @return random variable follows poisson distribution
	 */
	public int nextPoisson(double lambda) {
		double elambda = Math.exp(-1*lambda);
		double product = 1;
		int count =  0;
		int result=0;
		while (product >= elambda) {
			product *= nextDouble();
			result = count;
			count++; // keep result one behind
			}
		return result;
	}


}
