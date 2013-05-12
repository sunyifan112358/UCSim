package ucsim.datalogger.ucsimevent;

import ucsim.packet.Packet;

public class UCSimEvent {
	protected double time;
	protected String content;
	protected String whatHappened;
	
	private final String identifer = "G";
	
	public static UCSimEvent createUCSimEvent(double nowTime, String whatHappened, String content){
		UCSimEvent e = new UCSimEvent(nowTime, whatHappened);
		e.content = content;
		return e;
	}
	
	public static UCSimEvent createUCSimEvent(double nowTime, String whatHappened, Packet p){
		UCSimPacketEvent e = new UCSimPacketEvent(nowTime, whatHappened);
		e.setPacket(p);
		e.content = e.packet.toLogString();
		return e;
	}
	
	public UCSimEvent(double nowTime, String whatHappened){
		this.time = nowTime;
		this.whatHappened = whatHappened;
	}
	
	@Override
	public String toString(){
		return String.format(
								"{" +
								"\"time\":%03.8f, " +
								"\"identifer\":\"%s\", " +
								"\"whatHappened\":\"%s\"," +
								"\"content\":%s" +
								"}", 
								this.time, 
								this.identifer, 
								this.whatHappened,
								this.content
							);
	}
	

}
