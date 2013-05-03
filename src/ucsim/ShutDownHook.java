package ucsim;

import ucsim.world.World;

public class ShutDownHook extends Thread {
	public World w;
	
	public ShutDownHook(World w){
		this.w = w;
	}
	
	@Override
	public void run(){
		w.finish();
	}
}
