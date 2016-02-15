package com.rentalcars;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rentalcars.compare.PriceComparator;
import com.rentalcars.compare.RatingComparator;
import com.rentalcars.compare.ScoreRatingComparator;

import java.io.FileReader;
import java.util.*;

import com.rentalcars.vehicles.*;
import spark.Spark;

public class Main {

    private static String filePath;

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
        initialize(args[0]);

        // Run the tasks and print the results
        ArrayList<PriceVehicle> byPrice = sortByPrice();
        printArrayList(byPrice);

        ArrayList<SIPPVehicle> bySipp = specBySIPP();
        printArrayList(bySipp);

        ArrayList<RatingVehicle> byRating = sortSuppliersByCarType();
        printArrayList(byRating);

        ArrayList<ScoreRatingVehicle> byScore = sortByScoreAndRating();
        printArrayList(byScore);

        // Spark API set up
        Gson gson = new Gson();
        // List all vehicles
        Spark.get("/vehicles", (req, res) -> gson.toJson(vehicles));
        // Results of the tasks
        Spark.get("/vehiclesByPrice", (req, res) -> gson.toJson(byPrice));
        Spark.get("/vehiclesBySIPP", (req, res) -> gson.toJson(bySipp));
        Spark.get("/vehiclesByRating", (req, res) -> gson.toJson(byRating));
        Spark.get("/vehiclesByScore", (req, res) -> gson.toJson(byScore));
    }

    public static void initialize(String file) {
        // Save the file path
        filePath = file;

        // Setup the hash maps from the table in the document
        setupHashMaps();

        // Parse the file into a list of vehicles
        parseJsonFile();
    }

    /**
     * Sorts the vehicles by price and returns the result
     * @return A list of PriceVehicles sorted by price (ascending order)
     */
    public static ArrayList<PriceVehicle> sortByPrice() {
        // Sort a copy of the vehicle list and print it out
        ArrayList<Vehicle> vehiclesCopy = copyList(vehicles);
        Collections.sort(vehiclesCopy, new PriceComparator());

        ArrayList<PriceVehicle> result = new ArrayList<>();
        for (int i = 0; i < vehiclesCopy.size(); i++) {
            Vehicle v = vehiclesCopy.get(i);
            result.add(new PriceVehicle(v.getName(), v.getPrice()));
        }
        return result;
    }

    /**
     * Returns a list of vehicles with their details based on the spec defined by the sipp
     * @return A list of SIPPVehicles
     */
    public static ArrayList<SIPPVehicle> specBySIPP() {
        // Get the spec for each vehicle in the list
        ArrayList<SIPPVehicle> result = new ArrayList<>();
        for(int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            result.add(getSpecForVehicle(vehicle.getName(), vehicle.getSipp()));
        }
        return result;
    }

    /**
     * Returns a new SIPPVehicle based on the specification
     * @param name The vehicles name
     * @param sipp The sipp of the vehicle
     * @return The SIPPVehicle for that vehicle
     */
    public static SIPPVehicle getSpecForVehicle(String name, String sipp) {
        // Get the letters from the sipp
        String carKey = sipp.substring(0,1);
        String doorsCarKey = sipp.substring(1,2);
        String transmissionKey = sipp.substring(2, 3);
        String fuelKey = sipp.substring(3, 4);

        // Find the values for each key
        String carType = carMap.get(carKey);
        String doorsCarType = doorsCarMap.get(doorsCarKey);
        String transmissionType = transmissionMap.get(transmissionKey);
        String fuelAirConType = fuelAirConMap.get(fuelKey);

        // Replace any null values with n/a
        carType = (carType == null) ? "n/a" : carType;
        doorsCarType = (doorsCarType == null) ? "n/a" : doorsCarType;
        transmissionType = (transmissionType == null) ? "n/a" : transmissionType;
        String[] fuelACArray = new String[]{"",""};
        if (fuelAirConType == null) {
            fuelACArray[0] = "n/a";
            fuelACArray[1] = "n/a";
        }else {
            // Split the fuel air con into two strings
            fuelACArray = fuelAirConType.split("/");
        }

        // Print the result
        return new SIPPVehicle(name, sipp, carType, doorsCarType, transmissionType, fuelACArray[0], fuelACArray[1]);
    }

    /**
     * Returns a list of highest rated suppliers by car type
     * @return A list of RatingVehicles sorted by rating
     */
    public static ArrayList<RatingVehicle> sortSuppliersByCarType() {
        ArrayList<RatingVehicle> result = new ArrayList<>();
        // For each car type
        for (String key : carMap.keySet()) {
            // Get the vehicles of that type in the list
            ArrayList<Vehicle> vehicleArrayList = getVehiclesForCarType(key);
            if (vehicleArrayList.size() == 0)
                continue;

            // Sort the list according to rating and get the highest
            Collections.sort(vehicleArrayList, new RatingComparator());
            Vehicle v = vehicleArrayList.get(0);
            result.add(new RatingVehicle(v.getName(), carMap.get(key), v.getSupplier(), v.getRating()));
        }
        return result;
    }

    /**
     * Returns a list of the vehicles that are of a given type
     * @param carTypeKey The type of vehicle to look for
     * @return The vehicles that are of that type
     */
    public static ArrayList<Vehicle> getVehiclesForCarType(String carTypeKey) {
        ArrayList<Vehicle> list = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            // Check if the sipp contains the right car type
            String sipp = vehicle.getSipp();
            if (sipp.substring(0,1).equals(carTypeKey))
                list.add(vehicle);
        }
        return list;
    }

    /**
     * Returns the vehicle list according to the combined score and rating
     * @return A list of ScoreRatingVehicles sorted by combined score and rating
     */
    public static ArrayList<ScoreRatingVehicle> sortByScoreAndRating() {
        ArrayList<ScoreRatingVehicle> result = new ArrayList<>();

        // Get the score for each vehicle
        ArrayList<Vehicle> vehiclesCopy = copyList(vehicles);
        for (Vehicle v : vehiclesCopy) {
            // Get the score for the vehicle and set it
            int score = getScoreForVehicle(v.getSipp());
            v.setScore(score);
        }

        // Sort by combined score & rating and print out the result
        Collections.sort(vehiclesCopy, new ScoreRatingComparator());
        for (int i = 0; i < vehiclesCopy.size(); i++) {
            Vehicle v = vehiclesCopy.get(i);
            result.add(new ScoreRatingVehicle(v.getName(), v.getScore(), v.getRating()));
        }
        return result;
    }

    /**
     * Calculates the score for the vehicle based on the criteria in the document
     * @param sipp The sipp for the vehicle
     * @return The vehicle's score
     */
    public static int getScoreForVehicle(String sipp) {
        String transmission = sipp.substring(2,3);
        String fuelAC = sipp.substring(3, 4);
        int total = 0;

        if (transmission.equals("M"))
            total += 1;
        else if (transmission.equals("A"))
            total += 5;

        if (fuelAC.equals("R"))
            total += 2;

        return total;
    }

    /**
     * Parses the filePath into a vehicle list
     */
    private static void parseJsonFile() {
        // Check if the vehicles list has been set, if so exit
        if (vehicles.size() > 0)
            return;

        // Parse the json file
        Gson gson = new Gson();
        try {
            // Get the vehicle list as a json array
            JsonObject jsonObject = gson.fromJson(new FileReader(filePath), JsonObject.class);
            JsonArray jsonVehicles = jsonObject.getAsJsonObject("Search").getAsJsonArray("VehicleList");

            // Create a new Vehicle object for each entry in the json list and add it to an array list
            Iterator iterator = jsonVehicles.iterator();
            while (iterator.hasNext()) {
                JsonObject jsonVehicle = (JsonObject) iterator.next();

                Vehicle vehicle = new Vehicle(
                        jsonVehicle.get("name").getAsString(),
                        jsonVehicle.get("sipp").getAsString(),
                        jsonVehicle.get("price").getAsFloat(),
                        jsonVehicle.get("supplier").getAsString(),
                        jsonVehicle.get("rating").getAsFloat()
                );
                vehicles.add(vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the maps according to the tables in the document
     */
    public static void setupHashMaps() {
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

    /**
     * Returns a copy of an array list of vehicles
     * @param list The array list to be copied
     * @return The copy of the array list
     */
    private static ArrayList<Vehicle> copyList(ArrayList<Vehicle> list) {
        ArrayList<Vehicle> copy = new ArrayList<>(list.size());
        for(Vehicle v : list)
            copy.add(v);
        return copy;
    }

    /**
     * Prints an array list to the console in the format:
     * "index". "element"
     * @param list The array list to print
     */
    private static void printArrayList(ArrayList list) {
        for (int i = 0; i < list.size() ; i++) {
            System.out.printf("%2d. %s\n", i+1, list.get(i).toString());
        }
        System.out.println("-----------------------");
    }


    /* Used for testing */
    public static ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
    public static void setVehicles(ArrayList<Vehicle> list) {
        vehicles = list;
    }
}
