package com.rentalcars.vehicles;

/**
 * Class to represent the result of task three
 * Created by Gurjit on 14/02/16.
 */
public class RatingVehicle extends Transport {

    private String carType;
    private String supplier;
    private float rating;

    public RatingVehicle(String name, String carType, String supplier, float rating) {
        this.name = name;
        this.carType = carType;
        this.supplier = supplier;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return name + " - " + carType + " - " + supplier + " - " + rating;
    }
}
