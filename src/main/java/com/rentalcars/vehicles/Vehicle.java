package com.rentalcars.vehicles;

/**
 * Class to represent a vehicle from the json file
 * Created by Gurjit on 10/02/16.
 */
public class Vehicle extends Transport implements Cloneable{

    // Instance variables
    private String sipp;
    private float price;
    private String supplier;
    private float rating;

    private int score;

    // Constructor
    public Vehicle(String name, String sipp, float price, String supplier, float rating) {
        this.name = name;
        this.sipp = sipp;
        this.price = price;
        this.supplier = supplier;
        this.rating = rating;
        this.score = 0;
    }

    // Getters and setters

    public String getSipp() {
        return sipp;
    }

    public void setSipp(String sipp) {
        this.sipp = sipp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // toString for debugging

    @Override
    public String toString() {
        return "Vehicle{" +
                "sipp='" + sipp + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", supplier='" + supplier + '\'' +
                ", rating=" + rating +
                ", score=" + score +
                '}';
    }
}
