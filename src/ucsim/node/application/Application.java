package ucsim.node.application;

import ucsim.node.Node;
import ucsim.packet.Packet;
import ucsim.world.World;


public abstract class Application {
	public Application(){}
	public void tick(World w, Node n){}
	public void receivePacket(Packet p, Node n, World w){}
}
