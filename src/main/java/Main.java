/*
 * @author Hannes Tarach (hantar-3)
 * Link to replit: https://replit.com/@hannestarach/Main-Exam-Project-Main-Exam-2024-05-17#src/main/java/Main.java
 */

import java.util.Scanner;

public class Main {
    // Constants
    public static final int MAXIMUM_CARS = 100;
    public static final int MAXIMUM_RIDES = 100;
    public static final int BASE_FARE = 200;
    public static final int COST_PER_KM = 35;
    
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // List to store the details of cars.
        String[] carRegistrationNumbers = new String[MAXIMUM_CARS];
        String[] carModels = new String[MAXIMUM_CARS];
        boolean[] carAvailability = new boolean[MAXIMUM_CARS]; // false for available, true for taken
        int carCounter = 0; // Number of cars added to the fleet

        // List to register ride details.
        String[] rideRegistrationNumbers = new String[MAXIMUM_RIDES];
        String[] customerNames = new String[MAXIMUM_RIDES];
        String[] pickupAddresses = new String[MAXIMUM_RIDES];
        int[] rideDistances = new int[MAXIMUM_RIDES];
        int rideCounter = 0; // Number of rides started

        while (true) {
            Menu();
            String userOption = scanner.nextLine();

            switch (userOption) {
                case "1":
                    // Adds cars to the fleet, calls on the addCarToFleet function and adds details 
                    // from user input which is added to the fleet. Incriments the carCounter with the new cars.
                    carCounter = addCarToFleet(carRegistrationNumbers, carModels, carAvailability, carCounter);
                    break;
                case "2":
                    // Starts rides, calls startRide function to get destination details from user input, 
                    // incriments the rideCounter with new ride details.
                    rideCounter = startRide(carRegistrationNumbers, carModels, carAvailability, carCounter, rideRegistrationNumbers, customerNames, pickupAddresses, rideDistances, rideCounter);
                    break;
                case "3":
                    // Ends a ride, calls on endRide function to get user input on ride length, 
                    // counts cost of ride, updates ride info and prints a receipt.
                    endRide(carRegistrationNumbers, carModels, carAvailability, carCounter, rideRegistrationNumbers, customerNames, rideDistances, rideCounter);
                    break;
                case "4":
                    // Prints the car fleet, calls on printCarFleet function to print details of all taxi cars and get status returned.
                    printCarFleet(carRegistrationNumbers, carModels, carAvailability, carCounter);
                    break;
                case "5":
                    // Prints ride summary, calls the printAllRideSummary function to call various summary reports,
                    // such as ride details, distance travelled, cost, total rides and revenues. 
                    printAllRideSummary(rideRegistrationNumbers, customerNames, rideDistances, rideCounter);
                    break;
                case "q":
                    // Quits the program.
                    System.out.println("Ending program.");
                    return;
                default:
                    // Handle invalid inputs from user.
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    // Main menu to display user invalid inputs.
    public static void Menu() {
        System.out.println("----------------------------------");
        System.out.println("# LTU Taxi");
        System.out.println("----------------------------------");
        System.out.println("1. Add car to fleet");
        System.out.println("2. Start a ride");
        System.out.println("3. End a ride");
        System.out.println("4. Print car fleet");
        System.out.println("5. Print ride summary");
        System.out.println("q. End program");
        System.out.print("> Enter your option: ");
    }

    /**
     * Adds new car to the fleet if there is space available and the registration number is valid and unique.
     *
     * @param carRegistrationNumbers the list of car reg numbers.
     * @param carModels the list of models.
     * @param carAvailability the list of car statuses (False if available, True if taken).
     * @param carCounter the current counter of cars in the taxi fleet.
     * @return the updated counter of cars in taxi fleet.
     */
    public static int addCarToFleet(String[] carRegistrationNumbers, String[] carModels, boolean[] carAvailability, int carCounter) {
        // If more cars than maximum allowed, print a warning.
        if (carCounter >= MAXIMUM_CARS) {
            System.out.println("Fleet full. Cannot add more cars");
            return carCounter;
        }
        // Reads user input and stores data in registrationNumber variable for later use. 
        System.out.print("> Enter registration number: ");
        String registrationNumber = scanner.nextLine();

        // Validation that the format of the car's registration number entered by the user is correct. 
        // Returns True if registration number does NOT match with the requirments because of the not operator.
        // [A-Z]{3} forces regplate to match uppercase 3 letters, //d{3} forces regplate to match and end with 3 numbers
        if (!registrationNumber.matches("[A-Z]{3}\\d{3}")) {
            System.out.println("Invalid registration number format.");
            return carCounter;
        }
        // This code block make sure the given registration number is unique, 
        // it loops through all cars, checks if current regnumber matches with new one. 
        for (int i = 0; i < carCounter; i++) {
            // If a car reg number duplicate is found it prints an error to user and returns carCounter.
            if (carRegistrationNumbers[i].equals(registrationNumber)) {
                System.out.println("Car with this registration number already exists in the LTU taxi fleet.");
                return carCounter;
            }
        }
        // If entered registration number is correct and not a duplicate program continues and asks for taxi make and model. 
        // This code block is reading the cars make and model from the user, 
        // storing the cars details in the relevant lists, updating the car counter, and incrimenting new car to the fleet.
        System.out.print("> Enter car make and car model: ");
        String model = scanner.nextLine(); // Stores user input in the model variable for later use.

        //  stores the car's registration number in the carRegistrationNumbers list at the index specified by carCounter.
        carRegistrationNumbers[carCounter] = registrationNumber;
        // storing the taxi make and model in the carModels list at the same index.
        carModels[carCounter] = model;
        // sets the status of the car to false (available) in the carAvailability list at the same index.
        carAvailability[carCounter] = false;

        carCounter++;

        System.out.println(model + " with registration number " + registrationNumber + " was added to car fleet.");
        return carCounter;
    }

    /**
     * Starts a ride by reading the cars registration number, pickup address, and customer name.
     * Validates that the car is available and updates its status to taken if not available.
     *
     * @param various lists and counters.
     * @return the updated counter of rides.
     */
    public static int startRide(String[] carRegistrationNumbers, String[] carModels, boolean[] carAvailability, int carCounter, String[] rideRegistrationNumbers, String[] customerNames, String[] pickupAddresses, int[] rideDistances, int rideCounter) {
        System.out.print("> Enter car's registration number: ");
        String registrationNumber = scanner.nextLine();

        // Find and get the index of the car in the taxi fleet using the registration number.
        int carIndex = getCarIndexByRegistrationNumber(carRegistrationNumbers, carCounter, registrationNumber);
        if (carIndex == -1) { // If car not found, print error message.
            System.out.println("Car was not found.");
            return rideCounter;
        }

        // Check if car is available, if not available, prints an error message and returns rideCounter. 
        if (carAvailability[carIndex]) {
            System.out.println("Car is right now on another ride and not available.");
            return rideCounter;
        }

        System.out.print("> Enter pickup address: ");
        String address = scanner.nextLine();

        System.out.print("> Enter rider's name: ");
        String customerName = scanner.nextLine();

        if (rideCounter >= MAXIMUM_RIDES) {
            System.out.println("Ride list is full. Cant add more rides.");
            return rideCounter;
        }

        // Stores ride details in the lists below and updates car status 
        // if it's taken now and incriment the rideCounter variable. 
        rideRegistrationNumbers[rideCounter] = registrationNumber;
        pickupAddresses[rideCounter] = address;
        customerNames[rideCounter] = customerName;
        carAvailability[carIndex] = true; // car is now taken
        rideCounter++;

        System.out.println("Taxi cab with registration number " + registrationNumber + " picked up " + customerName + " at " + address + ".");
        return rideCounter;
    }

    /**
     * Ends a ride, updating the car and ride details, calculating the cab cost, and printing a receipt to the customer.
     * 
     * @param various lists and counters.
     */
    public static void endRide(String[] carRegistrationNumbers, String[] carModels, boolean[] carAvailability, int carCounter, String[] rideRegistrationNumbers, String[] customerNames, int[] rideDistances, int rideCounter) {
        System.out.print("> Enter registration number: ");
        String registrationNumber = scanner.nextLine();

        // Gets the ride index based on the registration number given. If ride not found, prints an error message.
        int rideIndex = getRideIndexbyRegistrationNumber(rideRegistrationNumbers, rideDistances, rideCounter, registrationNumber);
        if (rideIndex == -1) { // If no such ride is found, prints an error and exits the program. 
            System.out.println("Ride could not be found or car is not on a current ride.");
            return;
        }

        System.out.print("> Distance covered in km: ");
        int distance = scanner.nextInt();
        scanner.nextLine(); // Fixes the new line issue, consuming it from the nextInt.

        // Gets the car index based on the registration number given. If car not found, prints an error message.
        int carIndex = getCarIndexByRegistrationNumber(carRegistrationNumbers, carCounter, registrationNumber);
        if (carIndex == -1) {
            System.out.println("Car not found.");
            return;
        }

        // Updates ride distance and car availibility. 
        rideDistances[rideIndex] = distance;
        carAvailability[carIndex] = false; // car is now available

        // Calculates cost of the fair and prints a receipt to customer. 
        int cost = BASE_FARE + (distance * COST_PER_KM);
        System.out.println("===================================");
        System.out.println("LTU Taxi");
        System.out.println("===================================");
        System.out.println("Name: " + customerNames[rideIndex]);
        System.out.println("Car: " + carModels[carIndex] + " (" + registrationNumber + ")");
        System.out.println("Distance: " + distance + " km");
        System.out.println("Total cost: " + cost + " SEK");
        System.out.println("===================================");
    }

    /**
     * Prints the details of the Taxi car fleet, prints the model, registration number, and availability status.
     * Also prints the total number of cars and the number of available cars.
     *
     * @param carRegistrationNumbers the list of car registration numbers.
     * @param carModels the list of car models.
     * @param carAvailability the list of car availability statuses (False for available, True for taken).
     * @param carCounter the current counter of cars in the taxi fleet.
     */
    public static void printCarFleet(String[] carRegistrationNumbers, String[] carModels, boolean[] carAvailability, int carCounter) {
        System.out.println("LTU Taxi car fleet:\n");
        System.out.println("Fleet:");
        System.out.printf("%-20s %-12s %-10s%n", "Model", "Numberplate", "Status");

        int availableCars = 0; // Initializes the counts of available cars.

        // For loop that loops through each car in the fleet and determines wether the car status is taken or available.
        // Then it prints the cars to user. 
        for (int i = 0; i < carCounter; i++) {
            String status;
            if (carAvailability[i]) {
                status = "Taken";
            } else {
                status = "Available";
            }
            //If carAvailability[i] is true, meaning the car is taken, !carAvailability[i] evaluates to false and vice versa.
            // This condition helps count the available cars and incriment the availableCars variable.
            if (!carAvailability[i]) {
                availableCars++;
            }
            // Print the car model, registration number, and status with formatting
            System.out.printf("%-20s %-12s %-10s%n", carModels[i], carRegistrationNumbers[i], status);
        }

            // Print the total number of cars in the fleet
            System.out.println();
            System.out.println("Total number of cars: " + carCounter);

            // Print the total number of available cars in the fleet
            System.out.println("Total number of available cars: " + availableCars);
    }

    /**
     * Prints summary of all rides to user, including customer name, car registration number,
     * distance traveled, and cost.
     * By bubble sorting, the list is sorted by name.
     * Also prints the total number of rides and the total revenue generated from the cab drives.
     *
     * @param rideRegistrationNumbers the list of ride registration numbers.
     * @param customerNames the list of passenger names.
     * @param rideDistances the list of ride distances.
     * @param rideCounter the current count of rides.
     */
    public static void printAllRideSummary(String[] rideRegistrationNumbers, String[] customerNames, int[] rideDistances, int rideCounter) {
        //Bubble sort, inspired by assignment 4 and AI help to make it work without 2d Array and error fixes.
        // Outer loop
        for (int i = 0; i < rideCounter - 1; i++) {
            // Inner loop
            for (int j = 0; j < rideCounter - i - 1; j++) {
                // Each pair of nearby elements, if the current customers name is "lexicographically"
                // greater than the next one, the elements are swapped.
                if (customerNames[j].compareTo(customerNames[j + 1]) > 0) { //compares, does not sort, following exam instructions.
                    // Swap the customer names.
                    String tempName = customerNames[j];
                    customerNames[j] = customerNames[j + 1];
                    customerNames[j + 1] = tempName;

                    // Swap the corresponding registration numbers.
                    String tempReg = rideRegistrationNumbers[j];
                    rideRegistrationNumbers[j] = rideRegistrationNumbers[j + 1];
                    rideRegistrationNumbers[j + 1] = tempReg;

                    // Swap the corresponding ride distances
                    int tempDistance = rideDistances[j];
                    rideDistances[j] = rideDistances[j + 1];
                    rideDistances[j + 1] = tempDistance;
                }
            }
        }
        // Menu for the ride summary.
        System.out.println("LTU Taxi ride summary:\n");
        System.out.println("Riders:");
        System.out.printf("%-20s %-12s %-10s %-10s%n", "Name", "Numberplate", "Distance", "Cost");

        // Initializes a counter stored in this variable to keep track of the total revenue generated from all rides.
        int totalRevenue = 0; 

        // Loops through each ride. 
        for (int i = 0; i < rideCounter; i++) {
            // Calculates distance and empty if zero.
            String distance = rideDistances[i] == 0 ? "" : rideDistances[i] + " km";
            //Calculates cost of ride, empty if zero.
            String cost = rideDistances[i] == 0 ? "" : (BASE_FARE + (rideDistances[i] * COST_PER_KM)) + " SEK";
            // If cost = not empty, add cost to total revenue.
            if (!cost.isEmpty()) {
                totalRevenue += BASE_FARE + (rideDistances[i] * COST_PER_KM);
            }
            System.out.printf("%-20s %-12s %-10s %-10s%n", customerNames[i], rideRegistrationNumbers[i], distance, cost);
        }

        System.out.println("Total number of rides: " + rideCounter);
        System.out.println("Total revenue: " + totalRevenue + " SEK");
    }

    /**
     * Finds the index of a car in the fleet by its registration number.
     *
     * @param carRegistrationNumbers the list of car registration numbers.
     * @param carCounter the current counter of cars in the taxi fleet.
     * @param registrationNumber the registration number to search for.
     * @return the index of the car with the given registration number, or -1 if not found.
     */
    public static int getCarIndexByRegistrationNumber(String[] carRegistrationNumbers, int carCounter, String registrationNumber) {
        for (int i = 0; i < carCounter; i++) {
            // Checks if entered car reg number matches any in the list, if true, returns the index of the car.
            if (carRegistrationNumbers[i].equals(registrationNumber)) {
                return i;
            }
        }
        // If no match was found in the for loop, returns -1. 
        return -1;
    }

    /**
     * Finds the index of a current ride by the cars registration number.
     * A current ride is identified by a ride distance of zero.
     *
     * @param rideRegistrationNumbers the list of ride registration numbers.
     * @param rideDistances the list of ride distances.
     * @param rideCounter the current counter of rides.
     * @param registrationNumber the registration number to search for.
     * @return the index of the current ride with the given registration number, or -1 if not found.
     */
    public static int getRideIndexbyRegistrationNumber(String[] rideRegistrationNumbers, int[] rideDistances, int rideCounter, String registrationNumber) {
        for (int i = 0; i < rideCounter; i++) {
            // Check if the current ride registration number matches the given registration number
            // and if the ride distance is zero (indicating the ride is ongoing), if both true, returns index of the ride.
            if (rideRegistrationNumbers[i].equals(registrationNumber) && rideDistances[i] == 0) {
                return i;
            }
        }
        return -1; // Return -1 if no match is found
    }
}
