package ucsim.packet;

import java.awt.Color;

import processing.core.PApplet;

import ucsim.world.World;

public class ControlPacket extends Packet {
     
    public ControlPacket(String data, int source, int destination, int length, World w) {
        super(data, source, destination, length, w);
    }
    
    @Override
    public void show(PApplet pApplet, World w){
        Color c = this.findColor();
        this.show(pApplet, w, c);
    }
    
    @Override
    public Color findColor(){
        Color color = new Color(0);
        
        if(this.getData().contains("RTS")){
            color = new Color(224, 208, 27);
        }else if(this.getData().contains("CTS")){
            color = new Color(214, 21, 232);
        }else if(this.getData().contains("Data")){
            color = new Color(24, 58, 196);
        }else if(this.getData().contains("ACK")){
            color = new Color(22, 224, 53);
        }else{
            color = new Color(232, 21, 81);
        }
        if(this.destination!=this.to){
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
        }else{
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
        }
        return color;
    }

}
