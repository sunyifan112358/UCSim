package ucsim.world.scheduler;

import processing.core.*;
import ucsim.graph.showable.Showable;
import ucsim.world.World;

public class Scheduler implements Showable{
    
    protected double totalTime;
    protected double sendStopTime;
    protected double nowTime;
    protected boolean isRunning = true;

    protected double speed = 1;

    protected double lastSystemTime=-1;
    protected double nowSystemTime=0;
    
    protected double timeAdvance = 0;
    
    public static final int REALTIME = 1;
    public static final int FIXEDINCREASEMENT = 2;
    
    public static Scheduler createScheduler(int type, double sendStopTime, double totalTime, double parameter){
        
        switch(type){
        case Scheduler.FIXEDINCREASEMENT:
            return new FixedIncreasementScheduler(sendStopTime, totalTime, parameter);
        case Scheduler.REALTIME:
            return new RealtimeScheduler(sendStopTime, totalTime, parameter);
        }
        return null;
        
    }

    public Scheduler(double sendStopTime, double totalTime) {
        this.setSendStopTime(sendStopTime);
        this.setTotalTime(totalTime);
    }

    public void update() {
        if (isRunning) {
        	
            nowSystemTime = System.nanoTime();
            if (lastSystemTime<0) {
                lastSystemTime = nowSystemTime;
                return;
            }
            else {
                setTimeAdvance((nowSystemTime-lastSystemTime)/1e9*speed);

                this.advance();
                
                lastSystemTime = nowSystemTime;
            }
        }
        else {
            setTimeAdvance(0);
            lastSystemTime = System.nanoTime();
        }
    }
    
    protected void advance(){
        
        setNowTime(getNowTime() + getTimeAdvance());
        if (getNowTime()>getTotalTime()) {
            setNowTime(getTotalTime());
            isRunning = false;
        }
        
//        System.out.printf("Time Advancement: %f, tickPS: %f\n", getTimeAdvance(), 1/getTimeAdvance());
    }

    public void show(PApplet pApplet, World w) {
        pApplet.camera(250, 250, 1000, 250, 250, 250, 0, 1, 0);
        pApplet.pushMatrix();
	        pApplet.translate(250, 500, 500);
	        pApplet.stroke(0, 100);
	        pApplet.strokeWeight(1);
	        pApplet.noFill();
	        pApplet.box(500, 20, 20);
        pApplet.popMatrix();
        
        pApplet.pushMatrix();
	        pApplet.noStroke();
	        pApplet.fill(255, 0, 0, 100);
	        pApplet.translate((float) ((getNowTime()/getTotalTime())*250), 500, 500);
	        pApplet.box((float)(getNowTime()/getTotalTime()*500), 20, 20);
        pApplet.popMatrix();
        
        pApplet.pushMatrix();
        	pApplet.translate(510, 500, 510);
	        String s = this.nowTimeToString();
	        pApplet.textSize(11);
	        pApplet.fill(0);
	        pApplet.textAlign(PConstants.LEFT);
	        pApplet.text(s, 0, 0, 0);
        pApplet.popMatrix();
        

    }

    public double getNowTime() {
        return nowTime;
    }

    public void setNowTime(double nowTime) {
        this.nowTime = nowTime;
    }

    public double getTimeAdvance() {
        return timeAdvance;
    }

    public void setTimeAdvance(double timeAdvance) {
        this.timeAdvance = timeAdvance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getSendStopTime() {
        return sendStopTime;
    }

    public void setSendStopTime(double sendStopTime) {
        this.sendStopTime = sendStopTime;
    }
    
    public String nowTimeToString(){
        int hour = (int) (nowTime/3600);
        int minute = (int) ((nowTime%3600)/60);
        int second = (int) (nowTime%60);
        int millis = (int) ((nowTime%1)*1000);
        String s = String.format("%02d:%02d:%02d:%03d", hour, minute, second, millis);
        return s;
    }



}
