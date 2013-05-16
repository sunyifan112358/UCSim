package ucsim.core.block.pin;

import java.util.ArrayList;

import ucsim.core.block.Block;

/**
 * class for input pins
 * 
 * @author yifan
 *
 */
public class InputPin extends Pin {
    /**
     * Input FIFO Buffer, arrived data is stored before processed
     */
    private ArrayList<Object> InputBuffer = new ArrayList<Object>();

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
        this.InputBuffer.add(data);
        
    }
    
    /**
     * get data from input buffer. 
     * if input buffer is empty, return null
     * @return T
     */
    public Object getData(){
        if(this.InputBuffer.isEmpty()){
            return null;
        }else{
            return this.InputBuffer.get(0);
        }
    }
    
    /**
     * pop an item from buffer
     */
    public void popBuffer(){
        if(this.InputBuffer.isEmpty()){
            System.out.println("Input buffer is empty, cannot pop");
        }else{
            this.InputBuffer.remove(0);
        }
    }
    
    /**
     * judge whether or not buffer is empty
     * @return if buffer is empty
     */
    public boolean hasNext(){
        return !this.InputBuffer.isEmpty();
    }

}
