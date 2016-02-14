package test;

import com.rentalcars.Main;
import com.rentalcars.vehicles.PriceVehicle;
import com.rentalcars.vehicles.SIPPVehicle;
import com.rentalcars.vehicles.ScoreRatingVehicle;
import com.rentalcars.vehicles.Vehicle;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Main test class
 * Created by Gurjit on 14/02/16.
 */
public class TestMain {

    @Test
    public void testParseJSON() {
        Main.initialize("vehicles.json");
        assertEquals("Should read all vehicles", 31, Main.getVehicles().size());
    }

    @Test
    public void testSortByPrice() {
        // Create a vehicles list
        Vehicle v1 = new Vehicle("Vehicle 1", "CCMR", 123.45f, "Cars", 7.5f);
        Vehicle v2 = new Vehicle("Vehicle 2", "CCMR", 245.42f, "Cars", 7.5f);
        Vehicle v3 = new Vehicle("Vehicle 3", "CCMR", 74.83f, "Cars", 7.5f);

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(v1);
        vehicles.add(v2);
        vehicles.add(v3);
        Main.setVehicles(vehicles);

        // Sort and check if correct
        ArrayList<PriceVehicle> sorted = Main.sortByPrice();
        assertEquals(sorted.get(0).getName(), v3.getName());
        assertEquals(sorted.get(1).getName(), v1.getName());
        assertEquals(sorted.get(2).getName(), v2.getName());
    }

    @Test
    public void testGetSpecForVehicle() {
        Main.setupHashMaps();

        // Create test SIPPVehicles
        SIPPVehicle sippVehicle1 = new SIPPVehicle(
                "Vehicle 1", "MBMN", "Mini", "2 doors", "Manual", "Petrol", "no AC");
        SIPPVehicle actual1 = Main.getSpecForVehicle("Vehicle 1", "MBMN");
        assertEquals(sippVehicle1, actual1);

        SIPPVehicle sippVehicle2 = new SIPPVehicle(
                "Vehicle 2", "IWAN", "Intermediate", "Estate", "Automatic", "Petrol", "no AC");
        SIPPVehicle actual2 = Main.getSpecForVehicle("Vehicle 2", "IWAN");
        assertEquals(sippVehicle2, actual2);

        SIPPVehicle sippVehicle3 = new SIPPVehicle(
                "Vehicle 3", "XTMR", "Special", "Convertible", "Manual", "Petrol", "AC");
        SIPPVehicle actual3 = Main.getSpecForVehicle("Vehicle 3", "XTMR");
        assertEquals(sippVehicle3, actual3);

        SIPPVehicle sippVehicle4 = new SIPPVehicle(
                "Error", "QAFE", "n/a", "n/a", "n/a", "n/a", "n/a");
        SIPPVehicle actual4 = Main.getSpecForVehicle("Error", "QAFE");
        assertEquals(sippVehicle4, actual4);
    }

    @Test
    public void testSpecBySIPP() {
        Main.setupHashMaps();

        // Create an array list of test Vehicles and there corresponding SIPPVehicles
        Vehicle vehicle1 = new Vehicle("Vehicle 1", "MBMN", 321.21f, "Cars", 8.6f);
        Vehicle vehicle2 = new Vehicle("Vehicle 2", "IWAN", 214.54f, "MoreCars", 8f);
        Vehicle vehicle3 = new Vehicle("Vehicle 3", "XTMR", 123.84f, "EvenMoreCars", 7.9f);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
        Main.setVehicles(vehicles);

        SIPPVehicle sippVehicle1 = new SIPPVehicle(
                "Vehicle 1", "MBMN", "Mini", "2 doors", "Manual", "Petrol", "no AC");
        SIPPVehicle sippVehicle2 = new SIPPVehicle(
                "Vehicle 2", "IWAN", "Intermediate", "Estate", "Automatic", "Petrol", "no AC");
        SIPPVehicle sippVehicle3 = new SIPPVehicle(
                "Vehicle 3", "XTMR", "Special", "Convertible", "Manual", "Petrol", "AC");

        ArrayList<SIPPVehicle> testList = new ArrayList<>();
        testList.add(sippVehicle1);
        testList.add(sippVehicle2);
        testList.add(sippVehicle3);

        ArrayList<SIPPVehicle> actualList = Main.specBySIPP();
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), testList.get(i));
        }
    }

    @Test
    public void testGetVehiclesForCarType() {
        Main.initialize("vehicles.json");

        // There are fifteen vehicles with type compact
        ArrayList<Vehicle> result = Main.getVehiclesForCarType("C");
        assertEquals(15, result.size());

        // There are no vehicles with type luxury
        result = Main.getVehiclesForCarType("L");
        assertEquals(0, result.size());

        // There is one mini type vehicle
        result = Main.getVehiclesForCarType("M");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetScoreForVehicle() {
        // Create test sipp strings
        String sipp1 = "CCMN"; // 1 + 0
        String sipp2 = "PDMR"; // 1 + 2
        String sipp3 = "IFAN"; // 5 + 0
        String sipp4 = "EVAR"; // 5 + 2

        assertEquals(1, Main.getScoreForVehicle(sipp1));
        assertEquals(3, Main.getScoreForVehicle(sipp2));
        assertEquals(5, Main.getScoreForVehicle(sipp3));
        assertEquals(7, Main.getScoreForVehicle(sipp4));
    }

//    @Test
//    public void testSortByScoreAndRating() {
//        Main.initialize("vehicles.json");
//
//        // Create an array list of test Vehicles
//        Vehicle vehicle1 = new Vehicle("Vehicle 1", "MBMN", 321.21f, "Cars", 8.6f);         // sum = 1 + 8.6 = 9.6
//        Vehicle vehicle2 = new Vehicle("Vehicle 2", "IWAN", 214.54f, "MoreCars", 8f);       // sum = 5 + 8 = 13
//        Vehicle vehicle3 = new Vehicle("Vehicle 3", "XTMR", 123.84f, "EvenMoreCars", 7.9f); // sum = 3 + 7.9 = 10.9
//        ArrayList<Vehicle> vehicles = new ArrayList<>();
//        vehicles.add(vehicle1);
//        vehicles.add(vehicle2);
//        vehicles.add(vehicle3);
//        Main.setVehicles(vehicles);
//
//        ArrayList<ScoreRatingVehicle> result = Main.sortByScoreAndRating();
//        assertEquals(result.get(0).getName(), vehicle2.getName());
//        assertEquals(result.get(1).getName(), vehicle3.getName());
//        assertEquals(result.get(2).getName(), vehicle1.getName());
//    }


}
