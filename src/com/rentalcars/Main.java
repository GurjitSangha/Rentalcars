package com.rentalcars;

import com.rentalcars.compare.PriceComparator;
import com.rentalcars.compare.RatingComparator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Main {

    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static HashMap<String, String> carMap = new HashMap<>();
    private static HashMap<String, String> doorsCarMap = new HashMap<>();
    private static HashMap<String, String> transmissionMap = new HashMap<>();
    private static HashMap<String, String> fuelAirConMap = new HashMap<>();

    public static void main(String[] args) {
        // Exit if json file path not specified
        if (args.length != 1) {
            System.out.println("Please specify the json file");
            System.exit(0);
        }

        // Setup the hash maps from the table in the document
        setupHashMaps();

        parseJsonFile(args[0]);
//        sortByPrice();
//        specBySIPP();
        sortSuppliersByCarType();
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
        ArrayList<Vehicle> vehiclesCopy = copyList(vehicles);
        Collections.sort(vehiclesCopy, new PriceComparator());
        System.out.println("Vehicles in ascending price order");
        for (int i = 0; i < vehiclesCopy.size(); i++) {
            System.out.printf("%2d. %s - %s\n", i+1, vehiclesCopy.get(i).getName(), vehiclesCopy.get(i).getPrice());
        }
    }

    private static void specBySIPP() {
        // Get the spec for each vehicle in the list
        System.out.println("Vehicle specifications");
        for(int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            getSpecForVehicle(i + 1, vehicle.getName(), vehicle.getSipp());
        }
    }

    private static void setupHashMaps() {
        // Car type map
        carMap.put("M", "Mini");
        carMap.put("E", "Economy");
        carMap.put("C", "Compact");
        carMap.put("I", "Intermediate");
        carMap.put("S", "Standard");
        carMap.put("F", "Full size");
        carMap.put("P", "Premium");
        carMap.put("L", "Luxury");
        carMap.put("X", "Special");

        // Doors/car type map
        doorsCarMap.put("B", "2 doors");
        doorsCarMap.put("C", "4 doors");
        doorsCarMap.put("D", "5 doors");
        doorsCarMap.put("W", "Estate");
        doorsCarMap.put("T", "Convertible");
        doorsCarMap.put("F", "SUV");
        doorsCarMap.put("P", "Pick up");
        doorsCarMap.put("V", "Passenger Van");

        // Transmission map
        transmissionMap.put("M", "Manual");
        transmissionMap.put("A", "Automatic");

        // Fuel/Air con map
        fuelAirConMap.put("N", "Petrol/no AC");
        fuelAirConMap.put("R", "Petrol/AC");
    }

    private static void getSpecForVehicle(int index, String name, String sipp) {
        // Get the letters from the sipp
        String carKey = sipp.substring(0,1);
        String doorsCarKey = sipp.substring(1,2);
        String transmissionKey = sipp.substring(2,3);
        String fuelKey = sipp.substring(3,4);

        // Find the values for each key
        String carType = carMap.get(carKey);
        String doorsCarType = doorsCarMap.get(doorsCarKey);
        String transmissionType = transmissionMap.get(transmissionKey);
        String fuelAirConType = fuelAirConMap.get(fuelKey);

        // Split the fuel air con into two strings
        String[] fuelACArray = fuelAirConType.split("/");

        // Replace any null values with n/a
        carType = (carType == null) ? "n/a" : carType;
        doorsCarType = (doorsCarType == null) ? "n/a" : doorsCarType;
        transmissionType = (transmissionType == null) ? "n/a" : transmissionType;
        fuelACArray[0] = (fuelACArray[0] == null) ? "n/a" : fuelACArray[0];
        fuelACArray[1] = (fuelACArray[1] == null) ? "n/a" : fuelACArray[1];

        // Print the result
        System.out.printf("%2d. %s - %s - %s - %s - %s - %s - %s\n",
                index, name, sipp, carType, doorsCarType, transmissionType, fuelACArray[0], fuelACArray[1]);
    }

    private static void sortSuppliersByCarType() {
        for (String key : carMap.keySet()) {
            ArrayList<Vehicle> vehicleArrayList = getVehiclesForCarType(key);
            if (vehicleArrayList.size() == 0)
                continue;

            Collections.sort(vehicleArrayList, new RatingComparator());
            for (int i = 0; i < vehicleArrayList.size(); i++) {
                Vehicle v = vehicleArrayList.get(i);
                System.out.printf("%2d. %s - %s - %s - %s\n",
                        i+1, v.getName(), carMap.get(key), v.getSupplier(), v.getRating());
            }
        }
    }

    private static ArrayList<Vehicle> getVehiclesForCarType(String carTypeKey) {
        ArrayList<Vehicle> list = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            String sipp = vehicle.getSipp();
            if (sipp.substring(0,1).equals(carTypeKey))
                list.add(vehicle);
        }
        return list;
    }

    private static ArrayList<Vehicle> copyList(ArrayList<Vehicle> list) {
        ArrayList<Vehicle> copy = new ArrayList<>(list.size());
        for(Vehicle v : list)
            copy.add(v);
        return copy;
    }
}
