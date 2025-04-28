import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Write a description of class Horse here.
 * 
 * @author Avar Laylany
 * @version 24/3/25
 */
public class HorsePart2 implements Serializable
{
    //Fields of class Horse
    
    private String horseName;
    private char horseSymbol;
    private int distanceTravelled;
    private double horseConfidence;
    private boolean horseFallen;

    private List<RaceStats> raceHistory = new ArrayList<>();
    private int racesWon;
    private int racesParticipated;
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public HorsePart2(char horseSymbol, String horseName, double horseConfidence)
    {
       this.horseSymbol = horseSymbol;
       this.horseName = horseName;
       this.horseConfidence = horseConfidence;

       this.distanceTravelled = 0;
       this.horseFallen = false;

       this.racesWon = 0;
       this.racesParticipated = 0;
    }
    
    
    //Other methods of class Horse
    public void fall()
    {
        horseFallen = true;
    }
    
    public double getConfidence()
    {
        return horseConfidence;   
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return horseName;
    }
    
    public char getSymbol()
    {
        return horseSymbol;
    }

    public List<RaceStats> getRaceHistory(){
        return raceHistory;
    }

    public int getRacesWon(){
        return racesWon;
    }

    public int getRacesParticipated(){
        return racesParticipated;
    }

    public double getWinPercentage(){
        return (racesParticipated == 0?0 : (double) racesWon/racesParticipated * 100);
    }



    
    public void goBackToStart()
    {
        distanceTravelled = 0;

        horseFallen = false;
    }
    
    public boolean hasFallen()
    {
        if(horseFallen){

            return true;
        }

        else{

            return false;
        }
    }

    public void moveForward()
    {
        distanceTravelled+=1;
    }

    public void setConfidence(double newConfidence)
    {

        if( newConfidence < 0.0){

            horseConfidence = 0.0;
        }

        else if( newConfidence > 1.0){

            horseConfidence = 1.0;
        } 

        else{
            horseConfidence = newConfidence;
        }
    }
    
    public void setSymbol(char newSymbol)
    {
        horseSymbol = newSymbol;   
    }   

    public void incrementRacesWon() {
        racesWon++;
    }

    public void incrementRacesParticipated(){
        racesParticipated++;
    }

    public void addRaceStats(RaceStats stats){
        raceHistory.add(stats);
    }

    //Save details of the horse to text file
    public void saveToFile(String filename){

        try{

            //Create file object and temporary list to store lines
            File file = new File(filename);
            List<String> lines = new ArrayList<>();

            if(file.exists()){

                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;

                while((line = reader.readLine()) != null){
                    lines.add(line);
                }
                reader.close();
            }

            boolean foundHorse = false;
            for(int counter = 0; counter< lines.size(); counter+=3){

                if(lines.get(counter + 1).equals(this.horseName)){

                    //Update horse if it already exists

                    lines.set(counter, String.valueOf(this.horseSymbol));
                    lines.set(counter + 2, String.valueOf(this.horseConfidence));
                    
                    foundHorse = true;
                    break;
                }

            }

            if(!foundHorse){

                //Add horse to the end of the file if it doesn't exist
                lines.add(String.valueOf(this.horseSymbol));
                lines.add(this.horseName);
                lines.add(String.valueOf(this.horseConfidence));
            }

            //Now rewrite the file with the updated lines from list
            PrintWriter writer = new PrintWriter(new FileWriter(file, false));
            
            for(String line: lines){
                writer.println(line);
            }

            writer.close();
        }

        catch (IOException e){

            System.out.println("Error saving horse: " + e.getMessage());
        }

    }

    //Load horse details from text file
    public static HorsePart2 loadHorseFromFile(String filename, String horseName){

        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String symbolLine;


            while ((symbolLine = reader.readLine()) != null){

                String nameLine = reader.readLine();
                String confidenceLine = reader.readLine();

                if (nameLine != null && nameLine.equals(horseName)){

                    char symbol = symbolLine.charAt(0);
                    double confidence = Double.parseDouble(confidenceLine);
                    reader.close();

                    return new HorsePart2(symbol, horseName, confidence);
                }
            }

            reader.close();
            System.out.println("Horse with name " + horseName + " not found.");
        }

        catch (IOException e){

            System.out.println("Error loading horse: " + e.getMessage());
        }

        //return null if horse is not found
        return null;
    }


    //Save Statistics To File
    public void saveStatsToFile(){
        try{

            //Create file object and hash map for all stats
            File statsFile = new File("HorseRaceSimulator/Part 2/horse_stats.ser");
            Map<String, HorsePart2> allStats = new HashMap<>();

            //If file exists, load existing horses
            if(statsFile.exists()){
                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(statsFile))){
                    allStats =(Map<String, HorsePart2>) ois.readObject();
                }
                catch(Exception e){
                    //If error occurs, assume all stats is empty
                    allStats = new HashMap<>();
                }
            }

            //Update specific horse
            allStats.put(this.horseName, this);

            //Save all horses back
            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(statsFile))){
                oos.writeObject(allStats);
            }
        }

        catch (IOException e){
            System.out.println("Error saving horse stats: " + e.getMessage());
        }

        
    } 

    //Load Statistics From File
    public void loadStatsFromFile(){
        
        try{

            File statsFile = new File("HorseRaceSimulator/Part 2/horse_stats.ser");
            if(statsFile.exists()){
                
                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(statsFile))){

                    Map<String, HorsePart2> allStats = (Map<String, HorsePart2>) ois.readObject();
                    HorsePart2 savedHorse = allStats.get(this.horseName);

                    if(savedHorse != null){
                        this.raceHistory = savedHorse.raceHistory;
                        this.racesWon = savedHorse.racesWon;
                        this.racesParticipated = savedHorse.racesParticipated;
                    }
                }
            }
        }
        catch (Exception e){
            //Ignore errors, assume no stats yet.
        }
    }

    //Updates both data in horse object and statistics file after race is done.
    public void completeRace (RaceStats stats) throws FileNotFoundException, IOException {

        this.raceHistory.add(stats);

        this.racesParticipated++;

        if(stats.getPosition() == 1){
            this.racesWon++;
        }
        this.saveStatsToFile();
    }


    //Load Complete Horse Method
    public static HorsePart2 loadCompleteHorse(String textFile, String statsFile, String horseName){

        //Load horse object without stats
        HorsePart2 horse = loadHorseFromFile("HorseRaceSimulator/Part 2/horses.txt", horseName);

        if(horse != null){
            //Add stats to horse object if horse exists
            horse.loadStatsFromFile();
        }

        return horse;
    }
}
