package ucsim.core.block.pin;

import ucsim.core.block.Block;

/**
 * Base class for InputPin and OutputPin
 * 
 * @author yifan
 * 
 */
abstract public class Pin {
    protected Block owner = null;
    private String name = "";
    
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
    
}
