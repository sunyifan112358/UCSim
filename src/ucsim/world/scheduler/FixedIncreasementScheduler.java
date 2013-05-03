package ucsim.world.scheduler;

public class FixedIncreasementScheduler extends Scheduler {

    public FixedIncreasementScheduler(double sendStopTime, double totalTime, double timeAdvance) {
        super(sendStopTime, totalTime);
        this.timeAdvance = timeAdvance;
    }
    
    public void update(){
        if(isRunning){
            this.advance();
        }
    }
    

}
