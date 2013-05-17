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
package ucsim.core.scheduler;

/**
 * @author Yifan
 *
 */
public class RealtimeScheduler extends Scheduler {
	
	private double lastSystemTime;
	private double nowSystemTime;
	
	private double speed = 1;
	
	
	
	/**
	 * Constructor of realtimeScheduler
	 */
	public RealtimeScheduler(){
		
	}
	
	@Override
	public void advance(){
		if (isRunning) {
        	
            nowSystemTime = System.nanoTime();
            if (lastSystemTime<0) {
                lastSystemTime = nowSystemTime;
                return;
            }
            else {
                this.timeAdvance = (nowSystemTime-lastSystemTime)/1e9*speed;
                this.nowTime += this.timeAdvance;
                if(this.nowTime>this.totalTime){
                	this.nowTime = this.totalTime;
                	this.isRunning = false;
                }
                lastSystemTime = nowSystemTime;
            }
        }
        else {
            this.timeAdvance = 0;
            lastSystemTime = System.nanoTime();
        }
	}
	
}
