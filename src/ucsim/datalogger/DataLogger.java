package ucsim.datalogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import ucsim.datalogger.ucsimevent.UCSimEvent;
import ucsim.packet.Packet;
import ucsim.world.World;




public class DataLogger {
	protected String 			fileName;
	private   BufferedWriter 	file;
	
	protected boolean 			enabled		= false;
	
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
			try {
				file.write(e.toString());
				file.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
			this.file.write("\b]}");
			this.file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
