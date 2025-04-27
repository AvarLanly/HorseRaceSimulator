import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Avar Laylany
 * @version 24/3/25
 */
public class Race
{
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber == 1)
        {
            lane1Horse = theHorse;
        }
        else if (laneNumber == 2)
        {
            lane2Horse = theHorse;
        }
        else if (laneNumber == 3)
        {
            lane3Horse = theHorse;
        }
        else
        {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
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
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        lane3Horse.goBackToStart();

        
                      

        while (!finished)
        {
            //Initialise the horses' fallen status
            boolean lane1HorseFallen = lane1Horse.hasFallen();
            boolean lane2HorseFallen = lane2Horse.hasFallen();
            boolean lane3HorseFallen = lane3Horse.hasFallen();

            //If statement to restart match if all horses have fallen
            if(lane1HorseFallen && lane2HorseFallen && lane3HorseFallen){
                
                lane1Horse.goBackToStart();
                lane2Horse.goBackToStart();
                lane3Horse.goBackToStart();

                printRace();
            }

            //move each horse
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            moveHorse(lane3Horse);
                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            if ( raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse) )
            {
                finished = true;
            }

           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }

        // After the race ends
        if (raceWonBy(lane1Horse)) {

            System.out.println(lane1Horse.getName() + " has won!");

            lane1Horse.setConfidence(lane1Horse.getConfidence() + 0.05);
            lane1Horse.saveToFile("HorseRaceSimulator/Part 1/horses.txt");
        }
        else if (raceWonBy(lane2Horse)) {

            System.out.println(lane2Horse.getName() + " has won!");

            lane2Horse.setConfidence(lane2Horse.getConfidence() + 0.05);
            lane2Horse.saveToFile("HorseRaceSimulator/Part 1/horses.txt");
        }
        else if (raceWonBy(lane3Horse)) {

            System.out.println(lane3Horse.getName() + " has won!");

            lane3Horse.setConfidence(lane3Horse.getConfidence() + 0.05);
            lane3Horse.saveToFile("HorseRaceSimulator/Part 1/horses.txt");

        }

        // Now print the updated confidence values
        System.out.println(lane1Horse.getName() + " Confidence: " + lane1Horse.getConfidence());
        System.out.println(lane2Horse.getName() + " Confidence: " + lane2Horse.getConfidence());
        System.out.println(lane3Horse.getName() + " Confidence: " + lane3Horse.getConfidence());
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
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
                theHorse.setConfidence(theHorse.getConfidence() -0.05);
                theHorse.saveToFile("HorseRaceSimulator/Part 1/horses.txt");
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
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
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
        
        printLane(lane1Horse);
        System.out.println();
        
        printLane(lane2Horse);
        System.out.println();
        
        printLane(lane3Horse);
        System.out.println();
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    

    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
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
