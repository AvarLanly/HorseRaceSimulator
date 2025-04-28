import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.Serializable;
import java.lang.Math;
import java.text.DecimalFormat;

/**
 * A two to five-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Avar Laylany
 * @version 27/3/25
 */
public class RacePart2 implements Serializable
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

    //Get current leader method
    private String getCurrentLeader() {
        HorsePart2 leader = null;
        int maxDistance = -1;
    
        //Loop through every horse and see who is furthest ahead
        for (HorsePart2 horse : horses) {
            if (horse != null && !horse.hasFallen()) {
                if (horse.getDistanceTravelled() > maxDistance) {
                    maxDistance = horse.getDistanceTravelled();
                    leader = horse;
                }
            }
        }
    
        //Return name of horse who is leading
        if (leader != null) {
            return leader.getName();
        } else {
            return "No Leader";
        }
    }


    //Callback interface
    public interface RaceCallback {
        void updateStats(int elapsed, String leader, int fallen);
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
    public void startRace(JPanel panel, HorseRacingSimulatorGUI.RaceCallback callback, long startTime, AtomicInteger fallenCount)
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        
        for (HorsePart2 horse : horses){

            if (horse != null){

                horse.goBackToStart();
            }
        }

        //Get original confidence for each horse
        Map<HorsePart2, Double> originalConfidence = new HashMap<>();
        for(HorsePart2 horse : horses) {
            if(horse != null) {
                originalConfidence.put(horse, horse.getConfidence());
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
                        fallenCount.set(0);
                    }
                }

                panel.repaint();
            }

            //move each horse
            for(HorsePart2 horse: horses){

                if(horse != null){
                    moveHorse(horse, fallenCount);
                }
            }

            int elapsed = (int)((System.currentTimeMillis() - startTime)/1000);
            String leader = getCurrentLeader();
            callback.updateStats(elapsed, leader, fallenCount.get());
                        
            //Refresh GUI
            panel.repaint();
            
            //if any of the horses have won, the race is finished
            for (HorsePart2 horse: horses){

                if(horse != null && raceWonBy(horse)){

                    finished = true;
                    break;
                }
            }

           
            //wait for 100 milliseconds
            try{ 
                Thread.sleep(100);
            }catch(Exception e){}
        }

        //After the race ends, find winners
        List<HorsePart2> winners = new ArrayList<>();

        for(HorsePart2 horse: horses){

            if(horse != null && raceWonBy(horse)){

                winners.add(horse);
            }
        }

        //Stats collection
        List<HorsePart2> finishers = new ArrayList<>();
        for(HorsePart2 horse : horses) {
            if(horse != null && raceWonBy(horse)) {
                finishers.add(horse);
            }
        }

        //Sort finishers by distance travelled (descending)
        Collections.sort(finishers, (h1, h2) -> 
            Integer.compare(h2.getDistanceTravelled(), h1.getDistanceTravelled()));

        //Calculate positions
        double raceTime = (System.currentTimeMillis() - startTime)/1000.0;

        for(HorsePart2 horse : horses) {
            if(horse != null) {
                // Determine final position
                int finalPosition = finishers.contains(horse) ? 
                    finishers.indexOf(horse) + 1 : 
                    horses.size(); // Non-finishers get last position
                    
                // Calculate metrics
                double speed = horse.getDistanceTravelled() / raceTime;
                boolean fell = horse.hasFallen();
                double confidenceChange = horse.getConfidence() - originalConfidence.get(horse);
                
                // Add race stats
                horse.addRaceStats(new RaceStats(
                    new Date(),
                    finalPosition,
                    speed,
                    raceTime,
                    fell,
                    confidenceChange
                ));
                
                // Update participation count
                horse.incrementRacesParticipated();
                if(finalPosition == 1) {
                    horse.incrementRacesWon();
                }
            }
        }

        //Update confidence of winners
        if (winners.size() == 1){

            //One clear winner of the race
            HorsePart2 winner = winners.get(0);

            double horseConfidence = winner.getConfidence() + 0.05;
            DecimalFormat df = new DecimalFormat("#.00");
            horseConfidence = Double.parseDouble(df.format(horseConfidence));

            winner.setConfidence(horseConfidence);
            winner.saveToFile("HorseRaceSimulator/Part 2/horses.txt");

            JOptionPane.showMessageDialog(panel, winner.getName() + " has won!");

        }

        else{

            //Increase and save new confidence
            for (HorsePart2 horse : winners){

                double horseConfidence = horse.getConfidence() + 0.05;
                DecimalFormat df = new DecimalFormat("#.00");
                horseConfidence = Double.parseDouble(df.format(horseConfidence));
                
                horse.setConfidence(horseConfidence);
                horse.saveToFile("HorseRaceSimulator/Part 2/horses.txt");
            }


            //String builder to create message of who tied.
            StringBuilder tieMessage = new StringBuilder("It's a tie between ");

            for(int counter = 0; counter < winners.size(); counter++){

                tieMessage.append(winners.get(counter).getName());
                if( counter != winners.size() -1){
                    tieMessage.append(",");
                }
            }

            //Output tie message.
            JOptionPane.showMessageDialog(panel, tieMessage.toString());
            

        }

        
        //Get updated versions of horses after race has ended
        for(HorsePart2 horse: horses){

            if(horse != null){

                //Get save values for horse
                horse.saveStatsToFile();

                //Output updated confidence value after race has ended.
                JOptionPane.showMessageDialog(panel, horse.getName() + " Condfidence Rating: " + horse.getConfidence());
            }
        }

        //Reset positon of horses after they have won.
        for(HorsePart2 horse: horses){

            if(horse != null){

                horse.goBackToStart();
                fallenCount.set(0);
            }
        }

        panel.repaint();
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(HorsePart2 theHorse, AtomicInteger fallenCount)
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
                fallenCount.incrementAndGet();
                //Reduce confidence of horse and save it to file.

                double horseConfidence = theHorse.getConfidence() - 0.03;
                DecimalFormat df = new DecimalFormat("#.00");
                horseConfidence = Double.parseDouble(df.format(horseConfidence));

                theHorse.setConfidence(horseConfidence);
                theHorse.saveToFile("HorseRaceSimulator/Part 2/horses.txt");
            }

            //Minimal fall chance in case the horse has no confidence.
            if(theHorse.getConfidence() == 0){
                
                if(Math.random() < (0.01)){

                    theHorse.fall();
                    fallenCount.incrementAndGet();

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


    //Get list of horses in race
    public List<HorsePart2> getHorses() {
        return horses;
    }

    //Get race track length
    public int getRaceLength() {
        return raceLength;
    }
}
