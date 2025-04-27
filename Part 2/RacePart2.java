import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A two to five-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Avar Laylany
 * @version 27/3/25
 */
public class RacePart2
{
    private int raceLength;
    private List<HorsePart2> horses;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     * @param horses the array list of horses in the race
     */
    
    public RacePart2(int distance, int numberOfLanes)
    {
        //Ensure that number of lanes is between 2 and 5
        if (numberOfLanes < 2 || numberOfLanes > 5){
            throw new IllegalArgumentException("Race must have between 2 and 5 lanes.");
        }

        // initialise instance variables
        raceLength = distance;
        horses = new ArrayList<>(numberOfLanes);
    
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(HorsePart2 theHorse, int laneNumber)
    {
        //Check if lane number entered is valid.
        if (laneNumber < 1 || laneNumber > horses.size() + 1){
            System.out.println("Invalid Lane Number: " + laneNumber);
            return;
        }

        //Expand list if necessary
        while (horses.size() < laneNumber){
            horses.add(null);
        }
        horses.set((laneNumber - 1), theHorse);
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        
        for (HorsePart2 horse : horses){

            if (horse != null){

                horse.goBackToStart();
            }
        }

        
                      

        while (!finished)
        {

            //Assume all horses have already fallen
            boolean areAllHorsesFallen = true;

            //If at least one horse hasn't fallen, change areAllHorsesFallen to false
            for(HorsePart2 horse: horses){

                if(horse != null){

                    if(!horse.hasFallen()){
                        areAllHorsesFallen = false;
                    }
                }
            }


            //If all horses fell, restart race
            if(areAllHorsesFallen){

                for(HorsePart2 horse: horses){

                    if(horse != null){
    
                        horse.goBackToStart();
                    }
                }

                printRace();
            }

            //move each horse
            for(HorsePart2 horse: horses){

                if(horse != null){
                    moveHorse(horse);
                }
            }
                        
            //print the race positions
            printRace();
            
            //if any of the horses have won, the race is finished
            for (HorsePart2 horse: horses){

                if(horse != null && raceWonBy(horse)){

                    finished = true;
                    break;
                }
            }

           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }

        // After the race ends
        List<HorsePart2> winners = new ArrayList<>();

        for(HorsePart2 horse: horses){

            if(horse != null && raceWonBy(horse)){

                winners.add(horse);
            }
        }

        if (winners.size() == 1){

            //One clear winner of the race
            HorsePart2 winner = winners.get(0);
            System.out.println(winner.getName() + " has won!");

            winner.setConfidence(winner.getConfidence() + 0.05);
            winner.saveToFile("HorseRaceSimulator/Part 2/horses.txt");
        }

        else{

            System.out.println("It's a tie between: ");
            
            //Output horses who tied, increase and save new confidence
            for (HorsePart2 horse : winners){
                
                System.out.println(horse.getName());
                horse.setConfidence(horse.getConfidence() + 0.05);
                horse.saveToFile("HorseRaceSimulator/Part 2/horses.txt");
            }
            System.out.println("");
        }

        //Get updated versions of horses after race has ended
        for(HorsePart2 horse: horses){

            if(horse != null){

                //Get updated values for horse
                horse = HorsePart2.loadHorseFromFile("HorseRaceSimulator/Part 2/horses.txt", horse.getName());

                //Output updated confidence value after race has ended.
                System.out.println(horse.getName() + " Condfidence Rating: " + horse.getConfidence());
            }
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(HorsePart2 theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }

            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                
                //Reduce confidence of horse and save it to file.
                theHorse.setConfidence(theHorse.getConfidence() -0.03);
                theHorse.saveToFile("HorseRaceSimulator/Part 2/horses.txt");
            }

            //Minimal fall chance in case the horse has no confidence.
            if(theHorse.getConfidence() == 0){
                
                if(Math.random() < (0.01)){

                    theHorse.fall();

                    //No need to reduce confidence and update it if confidence is already at 0.

                }
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(HorsePart2 theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength && !theHorse.hasFallen())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        for(HorsePart2 horse: horses){

            if (horse != null){
                
                printLane(horse);
                System.out.println();
            }
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    

    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(HorsePart2 theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('X');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');

        //Determine horse's status
        String hasFallen;

        if(theHorse.hasFallen()){
            hasFallen = " (Fallen)";
        }
        else{
            hasFallen = " (Running)";
        }

        //Print the horse's name, distance travelled and current status
        System.out.print(" " + theHorse.getName() + " (" + theHorse.getDistanceTravelled() + ")" + " " + hasFallen);
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
