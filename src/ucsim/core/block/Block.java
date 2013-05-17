package ucsim.core.block;

import java.io.Serializable;
import java.util.HashMap;

import ucsim.core.block.pin.InputPin;
import ucsim.core.block.pin.OutputPin;


/**
 * The data processing unit - BLOCK
 * 
 * 
 * @author yifan
 *
 */
abstract public class Block implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected HashMap<String, InputPin> inputPins = new HashMap<String, InputPin>();
    protected HashMap<String, OutputPin> outputPins = new HashMap<String, OutputPin>();
    
    /**
     * process input data, generate output data
     */
    abstract public void process();
    
    /**
     * add an output pin 
     * @param p output pin to be added
     */
    public void registerOutputPin(OutputPin p){
        this.outputPins.put(p.getName(), p);
    }
    
    /**
     * add an input pin 
     * @param p input pin to be added
     */
    public void registerInputPin(InputPin p){
        this.inputPins.put(p.getName(), p);
    }
    
    /**
     * get a certain output pin
     * @param name name of the pin 
     * @return output pin with certain name
     */
    public OutputPin getOutputPin(String name){
        OutputPin p = null;
        p = this.outputPins.get(name);
        return p;
    }
    
    /**
     * get a certain output pin
     * @param name name of the pin 
     * @return output pin with certain name
     */
    public InputPin getInputPin(String name){
        InputPin p = null;
        p = this.inputPins.get(name);
        return p;
    }
    
}
