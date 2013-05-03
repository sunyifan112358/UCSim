package ucsim.node.application.poissontraffic;

import java.util.Random;
import ucsim.node.Node;
import ucsim.node.application.Application;
import ucsim.packet.Packet;
import ucsim.world.World;

public class PoissonTraffic extends Application {
    private double poisLambda = 0.00001;  //packet per second
    private int frameLength = 1500*8; //bit per second
    
    public PoissonTraffic(double poisLambda, int frameLength){
        this.poisLambda = poisLambda;
        this.frameLength = frameLength;
    }
    
    public void tick(World w, Node n){
        generatePacket(w, n);
    }
    
    public void generatePacket(World w, Node node) {
      if(w.scheduler.getNowTime()>w.scheduler.getSendStopTime()){return;}
      
      double r = new Random().nextDouble();

      if (r < poisLambda*w.scheduler.getTimeAdvance()) {
          
        frameLength = new Random().nextInt(frameLength)+2000;
        
        int d;
        do {
          Random rnd = new Random();
          d = rnd.nextInt(w.nodes.size());
        }while (d==node.getId());
        
        node.mac.getFromApp(new Packet("Data", node.getId(), d, frameLength, w));
        
//        System.out.printf(
//                "random number: %f, node[%d].mac.sendQueue.size()=%d, macState: %d, isSending:%B, receiving:%d\n", 
//                r,
//                node.getId(),
//                node.mac.sendQueue.size(),
//                node.mac.getState(),
//                node.mac.isSending(),
//                node.mac.getReceivingCount()
//                );

      }
    }
    
    public void receivePacket(Packet p, Node n, World w){
//    	w.dataLogger.logPacket("Packet received by app", p, w);
    }
    
    

}
