package ucsim.core.packet;

import java.util.Random;

/**
 * @author yifan
 *
 */
public class Packet implements Cloneable {
    
    String data = "";
    
    /**
     * Generate a random string of certain length
     * @param length
     * @return random string
     */
    public static String randomString(int length){
    	String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();
    }
    
    /**
     * set data
     * @param data data
     */
    public void setData(String data){
    	this.data = data;
    }
    
    
}
