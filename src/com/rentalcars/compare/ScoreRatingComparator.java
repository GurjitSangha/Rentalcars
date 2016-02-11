package com.rentalcars.compare;

import com.rentalcars.Vehicle;

import java.util.Comparator;

/**
 * Class to compare combined vehicle score + supplier rating
 * Created by Gurjit on 11/02/16.
 */
public class ScoreRatingComparator implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        double o1sum = o1.getScore() + o1.getRating();
        double o2sum = o2.getScore() + o2.getRating();

        if (o1sum > o2sum)
            return -1;
        else if (o1sum == o2sum)
            return 0;
        else if (o1sum < o2sum)
            return 1;
        return 0;
    }
}
