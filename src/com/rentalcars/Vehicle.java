package com.rentalcars;

/**
 * Class to represent a vehicle from the json file
 * Created by Gurjit on 10/02/16.
 */
public class Vehicle implements Cloneable{

    // Instance variables
    private String sipp;
    private String name;
    private double price;
    private String supplier;
    private double rating;

    private int score;

    // Constructor
    public Vehicle(String sipp, String name, double price, String supplier, double rating) {
        this.sipp = sipp;
        this.name = name;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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
