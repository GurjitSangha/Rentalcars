package com.rentalcars.compare;

import com.rentalcars.vehicles.Vehicle;

import java.util.Comparator;

/**
 * Class to compare two vehicles by rating
 * Created by Gurjit on 11/02/16.
 */
public class RatingComparator implements Comparator<Vehicle>{
    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        if (o1.getRating() > o2.getRating())
            return -1;
        else if (o1.getRating() == o2.getRating())
            return 0;
        else if (o1.getRating() < o2.getRating())
            return 1;
        return 0;
    }
}
