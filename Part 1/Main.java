import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    public static String inputString(String message){

        Scanner scanner = new Scanner(System.in);

        System.out.println(message);

        String input = scanner.nextLine();
        
        return input;
    }



    public static void main(String[] args) throws IOException {
        

        boolean userFinished = false;

        while(!userFinished){

            String userChoice = inputString("Would you like to? \nCreate a horse (1) \nView all horses (2) \nStart a race (3) \nOr exit the program (Exit) \n");

            if(userChoice.equals("1")){
                //Create horse
                createHorse();
            }

            else if (userChoice.equals("2")){
                //View all horses that have been created
                viewHorses();

            }

            else if(userChoice.equals("3")){
                //Create a race
                createRace();
            }

            else if(userChoice.equalsIgnoreCase("Exit")){
                //Exits the program
                System.out.println("Succesfully Exited Program.");
                userFinished = true;
            }

            else{
                //Else statement to check for any wrongful inputs.
                System.out.println("Please enter a valid option.");
            }
        }
        
    }



    public static void createHorse(){

        //User decides to create a horse

        boolean validateHorseChosen = false;

        //While Loop to ensure a valid horse is eventually created.
        while (!validateHorseChosen){

            String horseName = inputString("Enter a horse name");

            //Checks to see if horse already exists.
            Horse existingHorse = Horse.loadHorseFromFile("HorseRaceSimulator/Part 1/horses.txt", horseName);

            if(existingHorse != null){

                //Checks if user wants to override horse with new data.
                System.out.println("A horse with the name " + horseName + " already exists.");
                String response = inputString("Do you want to override it? (yes/no): ");

                if (response.equalsIgnoreCase("yes")){
                    
                    //User wants to override horse
                    validateHorseChosen = true;
                    System.out.println("You have chosen to override the existing horse.");

                    char horseSymbol = inputString("Enter a new symbol (one character) for " +horseName).charAt(0);
                    
                    try{

                        double horseConfidence = Double.parseDouble(inputString("Enter a new confidence rating (between 0 and 1) for " + horseName));

                        //New horse object is created and saved to file.
                        Horse newHorse = new Horse(horseSymbol, horseName, horseConfidence);
                        newHorse.saveToFile("HorseRaceSimulator/Part 1/horses.txt");

                    }
                    catch (NumberFormatException e){
                        throw new IllegalArgumentException("Confidence rating was not entered correctly.");
                    }
                }


                else{

                    //User decides to not override and create a new horse

                    System.out.println("Please enter a different name");
                }
            }

            else{

                //No duplicate found, new horse

                validateHorseChosen = true;
                
                char horseSymbol = inputString("Enter a new symbol (one character) for " +horseName).charAt(0);
                    
                try{

                    double horseConfidence = Double.parseDouble(inputString("Enter a new confidence rating (between 0 and 1) for " + horseName));

                    Horse newHorse = new Horse(horseSymbol, horseName, horseConfidence);
                    newHorse.saveToFile("HorseRaceSimulator/Part 1/horses.txt");

                }
                catch (NumberFormatException e){
                    throw new IllegalArgumentException("Confidence rating was not entered correctly.");
                }
            }
        }
    }



    //Outputs all horses that have been created
    public static void viewHorses(){

        try{

            //Create file object and temporary list to store lines (same code as in Horse.java)
            File file = new File("HorseRaceSimulator/Part 1/horses.txt");
            List<String> lines = new ArrayList<>();

            if(file.exists()){

                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;

                while((line = reader.readLine()) != null){
                    lines.add(line);
                }
                reader.close();
            }

            System.out.println("The current available horses are: ");

            for(int counter = 0; counter < lines.size(); counter+=3){

                String symbol = lines.get(counter);
                String name = lines.get(counter+1);
                String confidence = lines.get(counter+2);

                System.out.println("Name: " + name);
                System.out.println("Symbol: " + symbol);
                System.out.println("Confidence Rating: " + confidence);

                System.out.println("");

            }
        }

        catch (IOException e){

            System.out.println("Error finding horses: " + e.getMessage());
        }
    }


    //Create a race and start it
    public static void createRace(){

        try{

            int raceTrackLength = Integer.parseInt(inputString("Enter an integer for the length of the race track"));

            Race race = new Race(raceTrackLength);

            race = fillInRace(race);

            race.startRace();
        } 
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Race track length was not entered correctly.");
        }
    }


    //Fill in all lanes in the race by choice of user.
    public static Race fillInRace (Race race){


        boolean lane1Filled = false;
        boolean lane2Filled = false;
        boolean lane3Filled = false;

        //While loop to ensure all 3 lanes are filled
        while(!lane1Filled || !lane2Filled || !lane3Filled){

            String horseName = inputString("Name the horse: ");

            Horse horse = Horse.loadHorseFromFile("HorseRaceSimulator/Part 1/horses.txt", horseName);

            //If loop to ensure only valid horses are added.
            if(horse != null){

                try{

                    //Get lane number
                    int laneNumber = Integer.parseInt(inputString("Enter the lane number you wish to place " + horseName + " in. (1/2/3): "));
    
                    //Add horse to lane
                    race.addHorse(horse, laneNumber);
    
                    //If statements to determine which lane has been filled.
                    if(laneNumber == 1){
    
                        lane1Filled = true;
                    }
                    else if(laneNumber == 2){
    
                        lane2Filled = true;
                    }
                    else{
    
                        lane3Filled = true;
                    }

        
                } 
                catch (NumberFormatException e){
                    throw new IllegalArgumentException("Lane number was not entered correctly.");
                }

                
            } 
        }
        return race;
    }
}
