package com.rentalcars.vehicles;

/**
 * Class to represent a result for the first task
 * Created by Gurjit on 14/02/16.
 */
public class PriceVehicle extends Transport{

    private float price;

    public PriceVehicle(String name, float price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - " + price;
    }
}
