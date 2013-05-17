package ucsim.core.block.pin;

import java.io.Serializable;
import java.util.ArrayList;

import ucsim.core.block.Block;

/**
 * Base class for InputPin and OutputPin
 * 
 * @author yifan
 * 
 */
abstract public class Pin implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Block owner = null;
	/**
	 * name of the pin, used as the key of hashmap in block
	 */
    private String name = "";
    /**
     * FIFO buffer, for both output and input use
     */
    protected ArrayList<Object> buffer = new ArrayList<Object>();
    
    /**
     * Constructor for Pin
     * @param owner the block that this pin belongs to
     * @param name name of the pin, key in hashmap
     */
    public Pin(Block owner, String name){
        this.owner = owner;
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * get data from input buffer. 
     * if input buffer is empty, return null
     * @return T
     */
    public Object getData(){
        if(this.buffer.isEmpty()){
            return null;
        }else{
            return this.buffer.get(0);
        }
    }
    
    /**
     * pop an item from buffer
     */
    public void popBuffer(){
        if(this.buffer.isEmpty()){
            System.out.println("Input buffer is empty, cannot pop");
        }else{
            this.buffer.remove(0);
        }
    }
    
    /**
     * judge whether or not buffer is empty
     * @return if buffer is empty
     */
    public boolean hasNext(){
        return !this.buffer.isEmpty();
    }
    
    /**
     * get the buffer
     * @return buffer
     */
    public ArrayList<Object> getBuffer(){
    	return this.buffer;
    }
    
    /**
     * Clear buffer
     */
    public void clearBuffer(){
    	this.buffer.clear();
    }
    
}
