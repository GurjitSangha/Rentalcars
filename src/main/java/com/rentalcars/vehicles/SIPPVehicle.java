package com.rentalcars.vehicles;

/**
 * Class to represent the result of task two
 * Created by Gurjit on 14/02/16.
 */
public class SIPPVehicle extends Transport {

    private String sipp;
    private String carType;
    private String doorsType;
    private String transmission;
    private String fuel;
    private String aircon;

    public SIPPVehicle(String name, String sipp, String carType, String doorsType, String transmission, String fuel, String ac) {
        this.name = name;
        this.sipp = sipp;
        this.carType = carType;
        this.doorsType = doorsType;
        this.transmission = transmission;
        this.fuel = fuel;
        this.aircon = ac;
    }

    @Override
    public String toString() {
        return name + " - " + sipp + " - " + carType + " - " + doorsType + " - " + transmission + " - " + fuel + " - " + aircon;
    }

    @Override
    public boolean equals(Object obj) {
        SIPPVehicle other = (SIPPVehicle) obj;
        return this.name.equals(other.name) && this.carType.equals(other.carType) && this.doorsType.equals(other.doorsType) &&
                this.transmission.equals(other.transmission) && this.fuel.equals(other.fuel) && this.aircon.equals(other.aircon);
    }
}
