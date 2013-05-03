package ucsim.node.mac.papermac;

import java.util.ArrayList;


public class LinkProfiler {
	
	public 	double 						estimatedBER = 0.1;
	public  int 						fromId;
	public 	int   						destinationId;
	private ArrayList<PacketHistroy> 	packetHistroyList 
								= new ArrayList<PacketHistroy>();
	
	private double runningFactor = 0.1;
	
	public LinkProfiler(int fromId, int destinationId){
		this.fromId = fromId;
		this.destinationId = destinationId;
	}
	
	public void addPacketHistroy(int length, boolean isCorrect){
		this.packetHistroyList.add(new PacketHistroy(length, isCorrect));
		this.estimateBER();
	}
	
	public void estimateBER(){
//		int step = 500000;
		double step = 1e-6;
		double bestBER = 0;
		System.out.printf("Step size: %f\n", step);
		double tempBER = this.estimatedBER;
		double lastLikelyhood = this.getLikelyhood(tempBER);
		//search upwards
		while(true){
			tempBER += step;
			double tempLikelyhood = this.getLikelyhood(tempBER);
			if(tempLikelyhood<lastLikelyhood || tempBER>=0.5){
				bestBER = tempBER-step;
				tempBER -= step;
				break;
			}else{
				lastLikelyhood = tempLikelyhood;
				bestBER = tempBER;
			}
		}
		while(true){
			tempBER -= step;
			double tempLikelyhood = this.getLikelyhood(tempBER);
			if(tempLikelyhood<lastLikelyhood || tempBER<=0){
				bestBER = tempBER+step;
				break;
			}else{
				lastLikelyhood = tempLikelyhood;
				bestBER = tempBER;
			}
		}
		
		if(this.packetHistroyList.size()<2){
			this.estimatedBER = bestBER;
		}else{
			this.estimatedBER = (1-this.runningFactor)*this.estimatedBER + this.runningFactor*bestBER;
		}
		System.out.printf("From %d, To %d, best BER: %f, Estimated BER: %f\n",
							this.fromId, 
							this.destinationId, 
							bestBER,
							this.estimatedBER);
	}
	
	private double getLikelyhood(double BER){
		double likely = 1;
		for(int j=0; j<this.packetHistroyList.size(); j++){
			PacketHistroy ph = this.packetHistroyList.get(j);
			if(ph.isCorrect()){
				likely *= Math.pow((1-BER), (ph.getLength()));
			}else{
				likely *= 1 - Math.pow((1-BER), (ph.getLength()));
			}
		}
		return likely;
	}
	
	private void optimizePacketSize(){
		
	}

	
	public String toLogString(){
		StringBuilder logString = new StringBuilder();
		logString.append("{");
		logString.append(String.format("\"From\":%d,", this.fromId));
		logString.append(String.format("\"To\":%d,", this.destinationId));
		logString.append(String.format("\"eBER\":%f", this.estimatedBER));
		logString.append("}\n");
		return logString.toString();
	}

}
