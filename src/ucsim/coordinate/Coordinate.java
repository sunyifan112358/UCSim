package ucsim.coordinate;

public class Coordinate {
	private double x, y, z;
	  
	public Coordinate(double x, double y, double z){
	  this.x = x; 
	  this.y = y;
	  this.z = z;
	}
	  
	public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getScreenX(double scale){
	  return x*scale;
	}
	public double getScreenY(double scale){
	  return y*scale;
	}
	public double getScreenZ(double scale){
	  return z*scale;
	}
	 
	public double distance(Coordinate c){
	  return Math.pow(Math.pow(this.x-c.x, 2)+Math.pow(this.y-c.y,2)+Math.pow(this.z-c.z,2), 0.5);
	}
	  
	public Coordinate interpolate(double theta, Coordinate c){
	  return new Coordinate((1-theta)*this.x+(theta)*c.x, (1-theta)*this.y+(theta)*c.y, (1-theta)*this.z+(theta)*c.z);
	}

}
