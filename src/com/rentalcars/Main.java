package com.rentalcars;

import com.rentalcars.compare.PriceComparator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Main {

    private static ArrayList<Vehicle> vehicles = new ArrayList<>();

    public static void main(String[] args) {
        // Exit if json file path not specified
        if (args.length != 1) {
            System.out.println("Please specify the json file");
            System.exit(0);
        }

        parseJsonFile(args[0]);
        sortByPrice();

    }

    private static void parseJsonFile(String jsonFile) {
        // Parse the json file
        JSONParser parser = new JSONParser();
        try {
            // Get the vehicle list as a json array
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
            JSONArray jsonVehicles = (JSONArray) ((JSONObject) jsonObject.get("Search")).get("VehicleList");

            // Create a new Vehicle object for each entry in the json list
            Iterator iterator = jsonVehicles.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonVehicle = (JSONObject) iterator.next();

                // rating value can be parsed as either long or double
                Object ratingValueObject = jsonVehicle.get("rating");
                double ratingValue;
                if (ratingValueObject instanceof Long)
                    ratingValue = ((Long) ratingValueObject).doubleValue();
                else
                    ratingValue = (double) ratingValueObject;

                Vehicle vehicle = new Vehicle(
                        (String) jsonVehicle.get("sipp"),
                        (String) jsonVehicle.get("name"),
                        (double) jsonVehicle.get("price"),
                        (String) jsonVehicle.get("supplier"),
                        ratingValue
                );
                vehicles.add(vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sortByPrice() {
        // Sort the vehicle list and print it out
        Collections.sort(vehicles, new PriceComparator());
        System.out.println("Vehicles in ascending price order");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.printf("%2d. %s - %s\n", i+1, vehicles.get(i).getName(), vehicles.get(i).getPrice());
        }
    }
}
