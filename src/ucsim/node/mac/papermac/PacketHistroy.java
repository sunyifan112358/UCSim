package ucsim.node.mac.papermac;

public class PacketHistroy {
	
	private int 		length;
	private boolean 	isCorrect = true;
	
	public PacketHistroy(int length, boolean isCorrect){
		this.length = length;
		this.isCorrect = isCorrect;
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isCorrect() {
		return isCorrect;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	

}
