package ucsim.core.block.pin;

import ucsim.core.block.Block;

/**
 * class for input pins
 * 
 * @author yifan
 *
 */
public class InputPin extends Pin {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

    /**
     * Constructor for InputPin
     * @param owner owner block
     * @param name name of this pin
     */
    public InputPin(Block owner, String name) {
        super(owner, name);
    }
    
    /**
     * input some data
     * @param data
     */
    public void input(Object data){
        this.buffer.add(data);
    }
    
    

}
