package ucsim.packet;

import java.awt.Color;
import processing.core.*;
import ucsim.coordinate.Coordinate;
import ucsim.graph.showable.Showable;
import ucsim.node.Node;
import ucsim.world.World;

public class Packet implements Showable, Cloneable{
    

    protected   int         length;
    protected   int         source;
    protected   int         destination;
    protected   int         to;

    public static double    startPower          = 3.4;
    protected   double      power               = Packet.startPower;

    protected   double      noise               = 1e-3;
    protected   double      interference        = 0;
    
    protected   String      data                = "Data";


    protected   boolean     headArrived         = false;
    protected   boolean     tailArrived         = false;
    protected   boolean     headLeaved          = true;
    protected   boolean     tailLeaved          = false;

    protected   boolean     collided            = false;
    protected   boolean     hasError            = false;

    protected   double      createTime;
    protected   double      sendTime;
    protected   double      tailLeaveTime;
    protected   double      headArriveTime;
    protected   double      arriveTime;
    protected   double      receiveTime;

    protected   double      headPos             = 0;
    protected   double      tailPos             = 0;
    
    public static final int DATA_PACKET         = 1;
    public static final int CONTROL_PACKET      = 2;
    
    protected int type = Packet.DATA_PACKET;
    
    public static Packet createPacket(int kind, String data, int source, int destination, int length, World w){
        switch(kind){
            case Packet.DATA_PACKET:
                return new Packet(data, source, destination, length, w);
            case Packet.CONTROL_PACKET:
                return new ControlPacket(data, source, destination, length, w);
            default:
                break;  
        }
        return null;
    }
    
    public void send(World w) throws CloneNotSupportedException{
        
        for (int i=0; i<w.nodes.size();i++) {
            Node node = (Node)w.nodes.get(i);
            if (node.getId()!=this.source) {
              Packet outPacket = (Packet)this.clone();
              outPacket.to = node.getId();
              outPacket.setSendTime(w.scheduler.getNowTime());
              outPacket.noise = w.propagationModel.noise();
              w.packets.add(outPacket);
              
//              w.dataLogger.logPacket("Packet sent", outPacket, w);
            }
          }
    }

    public Packet(String data, int source, int destination, int length, World w) {
        this.setSource(source);
        this.setDestination(destination);
        this.setLength(length);
        if(w!=null){
            this.setCreateTime(w.scheduler.getNowTime());
        }
        this.setData(data);
    }


    public void propagate(Node fromNode, Node toNode, World w) {
        double distanceAdvance = w.scheduler.getTimeAdvance()*w.propagationModel.getPropagationSpeed();
        double distanceCoverage = this.getLength()/World.dataRate*w.propagationModel.getPropagationSpeed();

        double d = distance(fromNode, toNode);

        double scaledAdvance = distanceAdvance/d;
        double scaledCoverage = distanceCoverage/d;

        this.headPos += scaledAdvance;
        this.tailPos = this.headPos - scaledCoverage;

        if (!isHeadArrived()) {
            this.setPower(Packet.startPower/w.propagationModel.attenuation(headPos*d));
        }


        if (!isHeadArrived()) {
            if (headPos>1) {
                this.setHeadArrived(true);
                toNode.mac.receive(this);
                this.headArriveTime = w.scheduler.getNowTime();
//                w.dataLogger.logPacket("Packet head arrived", this, w);
            }
        }

        if (!isTailArrived()) {
            if (tailPos>1) {
//                System.out.printf("Power: %f, Noise: %f, Inter %f\n ", this.power, this.noise, this.interference );
                this.setTailArrived(true);
                this.arriveTime = w.scheduler.getNowTime();
                toNode.mac.receiveEnd(this, w, (Node)w.nodes.get(this.getTo()));
//                w.dataLogger.logPacket("Packet tail arrived", this, w);
            }
        }

        if (!isTailLeaved()) {
            if (tailPos>0) {
                this.setTailLeaved(true);
                fromNode.mac.sendEnd();
                this.tailLeaveTime = w.scheduler.getNowTime();
//                w.dataLogger.logPacket("Packet tail leaved", this, w);
            }
        }

        if (isHeadArrived() && !isTailArrived()) {
            //during receiving
            if (toNode.mac.getReceivingCount()>1) {
                this.collided = true;

                double inter = 0;
                for (int i=0; i<w.packets.size(); i++) {
                    Packet pa = (Packet)w.packets.get(i);
                    if (pa.getTo() == toNode.getId() && pa.isHeadArrived() && !pa.isTailArrived()) {
                        inter += pa.getPower();
                  }
              }
              if (inter>this.getInterference()) {
                  this.setInterference(inter);
              }
            }
            if(toNode.mac.isSending()){
                this.collided = true;
                this.setInterference(1e10);
            }
        }
      
      
      
    }

    private double distance(Node n1, Node n2) {
      return n1.getPosition().distance(n2.getPosition());
    }
    
    public void show(PApplet pApplet, World w){
        Color c = this.findColor();
        this.show(pApplet, w, c);
    }

    public void show(PApplet pApplet, World w, Color color) {
        Node fromNode = w.nodes.get(this.source);
        Node toNode   = w.nodes.get(this.to);
        
        //    print("Packet: ");println();
        double displayHead = this.headPos;
        double displayTail = this.tailPos;
        if (displayHead<0)displayHead=0;
        if (displayHead>1)displayHead=1;
        if (displayTail<0)displayTail=0;
        if (displayTail>1)displayTail=1;
        
        

        Coordinate headPos = fromNode.getPosition().interpolate(displayHead, toNode.getPosition());
        Coordinate tailPos = fromNode.getPosition().interpolate(displayTail, toNode.getPosition());


        pApplet.noFill();
        
        pApplet.stroke(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
       
        double wid = (10*Math.log(this.getPower())/Math.log(10)+30)/3.0;
        if(wid<0) wid=0;
        pApplet.strokeWeight((float) wid);
        
        pApplet.line(
                        (float)headPos.getScreenX(w.getScale()),
                        (float)headPos.getScreenY(w.getScale()), 
                        (float)headPos.getScreenZ(w.getScale()), 
                        (float)tailPos.getScreenX(w.getScale()), 
                        (float)tailPos.getScreenY(w.getScale()), 
                        (float)tailPos.getScreenZ(w.getScale())
                    );
        //display text
//        if(this.headPos>0 && this.headPos<1){
//            pApplet.fill(0);
//            pApplet.textSize(25);
//            pApplet.textAlign(PConstants.CENTER);
//            pApplet.text(
//                            data+String.format("%d", this.length), 
//                            (float)headPos.getScreenX(w.getScale()), 
//                            (float)headPos.getScreenY(w.getScale())-20,
//                            (float)headPos.getScreenZ(w.getScale())
//                        );
//        }
        
    }
    
    public String toLogString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("{");
    	
    	sb.append(String.format("\"source\":%d, "			, this.source			));
    	sb.append(String.format("\"destination\":%d, "		, this.destination		));
    	sb.append(String.format("\"to\":%d, "				, this.to				));
    	sb.append(String.format("\"data\":\"%s\", "				, this.data				));
    	sb.append(String.format("\"power\":%f, "			, this.power			));
    	sb.append(String.format("\"noise\":%f, "			, this.noise			));
    	sb.append(String.format("\"interference\":%f, "		, this.interference		));
    	sb.append(String.format("\"headLeaved\":%b, "		, this.headLeaved		));
    	sb.append(String.format("\"tailLeaved\":%b, "		, this.tailLeaved		));
    	sb.append(String.format("\"headArrived\":%b, "		, this.headArrived  	));
    	sb.append(String.format("\"tailArrived\":%b, "		, this.tailArrived		));
    	sb.append(String.format("\"collided\":%b, "			, this.collided			));
    	sb.append(String.format("\"hasError\":%b, "			, this.hasError			));
    	sb.append(String.format("\"createTime\":%f, "		, this.createTime		));
    	sb.append(String.format("\"sendTime\":%f, "			, this.sendTime			));
    	sb.append(String.format("\"tailLeavetime\":%f, "	, this.tailLeaveTime	));
    	sb.append(String.format("\"headArriveTime\":%f, "	, this.headArriveTime	));
    	sb.append(String.format("\"arriveTime\":%f, "		, this.arriveTime		));
    	sb.append(String.format("\"receiveTime\":%f, "		, this.receiveTime		));
    	sb.append(String.format("\"headPos\":%f, "			, this.headPos			));
    	sb.append(String.format("\"tailPos\":%f "			, this.tailPos			));
    	
    	sb.append("}");
		return sb.toString(); 
    }
    
    public Color findColor(){
        Color color = new Color(24, 58, 196);
        if(this.destination!=this.to){
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
        }else{
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
        }
        if(this.collided){
            color = color.darker();
        }
        return color;
    }
    
    public Packet clone(){
        Packet p = null;
        try {
            p =  (Packet) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return p;
    }
    

	

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getInterference() {
        return interference;
    }

    public void setInterference(double interference) {
        this.interference = interference;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(double receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public double getSendTime() {
        return sendTime;
    }

    public void setSendTime(double sendTime) {
        this.sendTime = sendTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

	public boolean isHeadLeaved() {
		return headLeaved;
	}

	public void setHeadLeaved(boolean headLeaved) {
		this.headLeaved = headLeaved;
	}

	public boolean isTailLeaved() {
		return tailLeaved;
	}

	public void setTailLeaved(boolean tailLeaved) {
		this.tailLeaved = tailLeaved;
	}

	public boolean isHeadArrived() {
		return headArrived;
	}

	public void setHeadArrived(boolean headArrived) {
		this.headArrived = headArrived;
	}

	public boolean isTailArrived() {
		return tailArrived;
	}

	public void setTailArrived(boolean tailArrived) {
		this.tailArrived = tailArrived;
	}
      


}
