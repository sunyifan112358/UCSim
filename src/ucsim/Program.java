package ucsim;

import ucsim.graph.PFrame;
import ucsim.graph.spacedomaingraph.SpaceDomainGraph;
import ucsim.graph.timedomaingraph.TimeDomainGraph;
import ucsim.world.World;

public class Program {

    /**
     * @param args
     */
    public static void main(String[] args) {
        World world = new World(5000, 5000, 5000);
        SpaceDomainGraph sdg = new SpaceDomainGraph(world);
        TimeDomainGraph tdg = new TimeDomainGraph(world);
        
        ShutDownHook sdh = new ShutDownHook(world);
        Runtime.getRuntime().addShutdownHook(sdh);

        
        boolean visualize = true;
        PFrame tdg_frame = null;
        PFrame sdg_frame = null;
        
        if(visualize){
            tdg_frame = new PFrame(tdg);
            sdg_frame = new PFrame(sdg);
        }
        
        Thread t = new Thread(world);
        t.start();
        while(true){
            if(tdg_frame!=null || sdg_frame!=null){
            	if(!tdg_frame.isVisible() || !sdg_frame.isVisible()){
            		System.exit(0);
            	}
            }else{
//                System.out.printf("Current time %s\r", world.scheduler.nowTimeToString());
            }
        	try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        

    }

}

