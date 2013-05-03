package ucsim.world.propagationmodel;

public class PropagationModel {
    private double frequency            = 10;      // in kHz
    private double propagationSpeed     = 1500;
    private double k                    = 1.5;
    
    public PropagationModel(){}
    
    public double attenuation(double d){
      d = d/1000;
      double f2 = Math.pow(frequency,2.0);
      double a = 0.11*(f2/(1+f2))+44*(f2/(4100+f2))+2.75e-4*f2+0.003;
      a = Math.pow(10,a/10);
      
      double A = Math.pow(d, k)*Math.pow(a, d);
      
      return A;
    }
    
    public double noise(){
        double noise = 0;
        double f = this.frequency;
        double s = 0.1;             //Ship activity
        double w = 5;               //wind speed;
        double df = 0.3;              //bandwidth
        double nt_Db  = 17 - 30 * Math.log10(f);
        double na_Db  = 40 + 20*(s-0.5) + 26*Math.log10(f) - 60*Math.log10(f+0.03);
        double nw_Db  = 50 + 7.5*Math.pow(w,0.5) + 20*Math.log10(f) - 40*Math.log10(f+0.4);
        double nth_Db = -15 + 20*Math.log10(f);
        double n_Db = nt_Db+na_Db+nw_Db+nth_Db;
        
        double n_pa = Math.pow(10, n_Db/10.0);
      
        double rho = 1025;
        double l = Math.pow(n_pa, 2) / (rho*this.propagationSpeed);
        
        double size = 0.01;
        noise = l * size;

        noise = noise*df;
//        System.out.printf("Noise: %f\n", noise);
        return noise;
    }

    public double getPropagationSpeed() {
        return propagationSpeed;
    }

    public void setPropagationSpeed(double propagationSpeed) {
        this.propagationSpeed = propagationSpeed;
    }
    

}
