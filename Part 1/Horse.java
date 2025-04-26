import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 * Write a description of class Horse here.
 * 
 * @author Avar Laylany
 * @version 24/3/25
 */
public class Horse
{
    //Fields of class Horse
    
    private String horseName;
    private char horseSymbol;
    private int distanceTravelled;
    private double horseConfidence;
    private boolean horseFallen;
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
       this.horseSymbol = horseSymbol;
       this.horseName = horseName;
       this.horseConfidence = horseConfidence;

       this.distanceTravelled = 0;
       this.horseFallen = false;
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
    public static Horse loadHorseFromFile(String filename, String horseName){

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

                    return new Horse(symbol, horseName, confidence);
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
}
