package ucsim.node.energymodel;

import ucsim.packet.Packet;

public class EnergyModule {
    private double energyConsumption = 0;

    private double listenEnergy = 0.02;
    private double receiveEnergy = 0.03;
    private double transmitEnergy = Packet.startPower;

    public static double packetPreambleTime = 0.1;


    public EnergyModule(){}

    protected void consumeEnergy(double amount){
      
        this.setEnergyConsumption(this.getEnergyConsumption() + amount);
    }

    public void listenConsumeEnergy(double time){
      
        this.consumeEnergy(time*this.listenEnergy);
    }

    public void receiveConsumeEnergy(double time){
        this.consumeEnergy(time*this.receiveEnergy);
    }

    public void transmitConsumeEnergy(double time){
        this.consumeEnergy(time*this.transmitEnergy);
    }

    public void transmitConsumeEnergy(int bits, double dataRate){
        this.transmitConsumeEnergy(bits/dataRate+packetPreambleTime);
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

}
