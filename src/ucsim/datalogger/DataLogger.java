package ucsim.datalogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.packet.Packet;
import ucsim.world.World;




public class DataLogger {
	protected String 			fileName;
	private   BufferedWriter 	file;
	
	protected boolean 			enabled		= false;
	
	protected ArrayList<UCSimEvent> eventList = new ArrayList<UCSimEvent>();
	
	public DataLogger(String fileName, Boolean enabled){
		this.enabled = enabled;
		this.fileName = fileName;
		FileWriter fstream = null;
		
		try {
			fstream = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.file = new BufferedWriter(fstream);
		
		try {
			this.file.write("{\"Events\":[");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void logEvent(UCSimEvent e){
		if(this.enabled){
			this.eventList.add(e);
//				file.write(e.toString());
//				file.flush();
		}
	}
	
	public void logPacket(String whatHappened, Packet p, World w){
		UCSimEvent e = UCSimEvent.createUCSimEvent(
				w.scheduler.getNowTime(), 
				"Packet Received", 
				p
			);
		w.dataLogger.logEvent(e);
	}
	
	public void close(){
		try {
		    for(int i=0; i<this.eventList.size(); i++){
		        UCSimEvent e = this.eventList.get(i);
		        this.file.write(e.toString());
		        if(i!=this.eventList.size()-1){
		            this.file.write(",\n");
		        }
		    }
			this.file.write("]}");
			this.file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
