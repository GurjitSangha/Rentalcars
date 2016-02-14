package com.rentalcars.vehicles;

/**
 * Class to represent the result of task four
 * Created by Gurjit on 14/02/16.
 */
public class ScoreRatingVehicle extends Transport {

    private float score;
    private float rating;
    private float sum;

    public ScoreRatingVehicle(String name, float score, float rating) {
        this.name = name;
        this.score = score;
        this.rating = rating;
        this.sum = score + rating;
    }

    @Override
    public String toString() {
        return name + " - " + score + " - " + rating + " - " + sum;
    }
}
