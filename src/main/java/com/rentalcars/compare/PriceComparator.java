package com.rentalcars.compare;

import com.rentalcars.vehicles.Vehicle;

import java.util.Comparator;

/**
 * Class to compare two vehicles by price
 * Created by Gurjit on 10/02/16.
 */
public class PriceComparator implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        if (o1.getPrice() > o2.getPrice())
            return 1;
        else if (o1.getPrice() == o2.getPrice())
            return 0;
        else if (o1.getPrice() < o2.getPrice())
            return -1;
        return 0;
    }
}
