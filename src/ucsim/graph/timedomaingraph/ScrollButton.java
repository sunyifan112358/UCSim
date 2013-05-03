package ucsim.graph.timedomaingraph;

import processing.core.PApplet;
import ucsim.graph.showable.Showable;
import ucsim.world.World;

public class ScrollButton implements Showable{
	
	private float position = 0;
	private float left = 0;
	private float top = 0;
	private float height = 16;
	private float width  = 30;
	
	private float mouseStartX;
	private boolean isPressed = false;
	
	@Override
	public void show(PApplet pApplet, World w) {
		pApplet.fill(150, 50);
		pApplet.noStroke();
		this.left = (pApplet.width-this.width)*getPosition();
		this.top = pApplet.height-height-2;
		pApplet.rect(
						this.left,
						this.top, 
						this.width, 
						this.height,
						5,5,5,5
					);	
	}
	public void dragged(PApplet pApplet) {
		if(this.isPressed){
			float xDiff = pApplet.mouseX - this.mouseStartX;
			float xDiff_p = xDiff/pApplet.width;
			this.setPosition(this.getPosition() + xDiff_p);
			if(getPosition()>1)setPosition(1);
			if(getPosition()<0)setPosition(0);
			this.mouseStartX = pApplet.mouseX;
		}
		
	}
	public void pressed(PApplet pApplet) {
		if(		pApplet.mouseY>this.top 
				&&  pApplet.mouseY<this.top+this.height 
				&&  pApplet.mouseX>this.left
				&&  pApplet.mouseX<this.left+this.width
			 ){
				this.mouseStartX = pApplet.mouseX;
				this.isPressed = true;
			}
	}
	public float getPosition() {
		return position;
	}
	public void setPosition(float position) {
		if(position>1) position = 1;
		if(position<0) position = 0;
		this.position = position;
	}
	public void released(TimeDomainGraph timeDomainGraph) {
		this.isPressed = false;
		
	}

}
