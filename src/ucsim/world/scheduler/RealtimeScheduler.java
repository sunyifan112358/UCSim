package ucsim.world.scheduler;

public class RealtimeScheduler extends Scheduler {

    public RealtimeScheduler(double sendStopTime, double totalTime, double speed) {
        super(sendStopTime, totalTime);
        this.speed = speed;
    }

}
