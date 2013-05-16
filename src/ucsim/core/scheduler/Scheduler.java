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
 * 
 * 
 * @author Yifan
 *
 */
public class Scheduler {
	
	protected double totalTime;
	protected double nowTime;
	protected double timeAdvance;
	protected boolean isRunning;
	
	/**
	 * Instance of the scheduler
	 */
	private static Scheduler scheduler = null;
	
	/**
	 * real-time scheduler
	 */
	public static final int REALTIME_SCHEDULER = 1;
	/**
	 * Event driven scheduler
	 */
	public static final int EVENTDRIVEN_SCHEDULER = 2;
	
	
	/**
	 * default constructor for scheduler
	 */
	protected Scheduler(){
		
	}
	
	/**
	 * Define the scheduler type
	 * @param type
	 */
	public static void defineScheduler(int type){
		switch(type){
		case Scheduler.REALTIME_SCHEDULER:
			break;
		case Scheduler.EVENTDRIVEN_SCHEDULER:
			break;
		default:
			break;	
		}
	}
	
	/**
	 * get the instance of Scheduler
	 * @return Instance of Scheduler
	 */
	public static Scheduler getInstance(){
		if(Scheduler.scheduler==null){
			try{
				Exception e = new Exception("Scheduler not defined");
				throw e;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}else{
			return Scheduler.scheduler;
		}
	}
	
	/**
	 * timer update,going forward
	 */
	public static void update(){
		Scheduler s = Scheduler.getInstance();
		s.advance();
	}

	/**
	 * time advance
	 */
	public void advance() {}
	
	/**
	 * 
	 */
	public static void togglePause(){
		Scheduler s = Scheduler.getInstance();
		if(s.isRunning){
			s.isRunning = false;
		}else{
			s.isRunning = true;
		}
	}

}
