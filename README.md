# Rentalcars.com technical test
Built using Intellij and Maven

### Usage
Use Maven to load the required libraries ([GSON](https://github.com/google/gson) and [Spark](http://sparkjava.com/))

Pass the desired JSON file to the program as a command line argument

### API endpoints
* `/vehicles` - A list of all the vehicles parsed by the program
* `/vehiclesByPrice` - The vehicle list sorted by price
* `/vehiclesBySIPP` - The specification of each vehicle based on its SIPP
* `/vehiclesByRating` - The top supplier per vehicle type
* `/vehiclesByScore` - The vehicle list sorted by combined score and supplier rating
