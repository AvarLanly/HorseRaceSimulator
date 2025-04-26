
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
}
