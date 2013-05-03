package ucsim.datalogger.ucsimevent;

import ucsim.packet.Packet;

public class UCSimPacketEvent extends UCSimEvent {
	
	
	protected Packet packet;

	@SuppressWarnings("unused")
	private final String identifer = "P";

	public UCSimPacketEvent(double nowTime, String whatHappened) {
		super(nowTime, whatHappened);
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

}
