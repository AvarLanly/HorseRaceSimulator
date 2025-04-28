import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HorseRacingSimulatorGUI implements Serializable {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private static final String horseFile = "HorseRaceSimulator/Part 2/horses.txt";

    //Main method to load GUI for program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HorseRacingSimulatorGUI().createAndShowGUI());
    }

    //Constructor for GUI
    private void createAndShowGUI() {

        //Create main window
        frame = new JFrame("Horse Racing Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        //Card layout to let us swap screens and main panel to act as container with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        //Add all screens to the main panel
        mainPanel.add(mainMenu(), "MainMenu");
        mainPanel.add(horseMenu(), "HorseMenu");
        mainPanel.add(raceMenu(), "RaceMenu");

        //Put main panel into the frame, and show it.
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //Main Method GUI
    private JPanel mainMenu() {

        //Create panel for the main menu
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        //Initialise buttons for the menu.
        JButton horseMenuButton = new JButton("Horse Menu");
        JButton raceMenuButton = new JButton("Race Menu");
        JButton exitButton = new JButton("Exit");

        //Implementing logic into the buttons.
        horseMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "HorseMenu"));
        raceMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "RaceMenu"));
        exitButton.addActionListener(e -> System.exit(0));

        //Add buttons to the panel of the main menu.
        panel.add(horseMenuButton);
        panel.add(raceMenuButton);
        panel.add(exitButton);

        return panel;
    }

    //Horse Management Menu Panel
    private JPanel horseMenu() {

        //Create panel for the horse menu
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        //Initialise buttons for the horse menu.
        JButton createHorseButton = new JButton("Create Horse");
        JButton viewHorsesButton = new JButton("View Horses");
        JButton deleteHorseButton = new JButton("Delete Horse");
        JButton compareHorsesButton = new JButton("Compare Horses");
        JButton viewHorseStatsButton = new JButton("View Horse Stats");
        JButton backButton = new JButton("Back to Main Menu");

        //Implement logic into the horse menu
        createHorseButton.addActionListener(e -> createHorse());
        viewHorsesButton.addActionListener(e -> viewHorses());
        deleteHorseButton.addActionListener(e -> deleteHorse());
        compareHorsesButton.addActionListener(e -> compareHorses());
        viewHorseStatsButton.addActionListener(e -> showHorseStats());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        //Add buttons to the panel.
        panel.add(createHorseButton);
        panel.add(viewHorsesButton);
        panel.add(viewHorseStatsButton);
        panel.add(deleteHorseButton);
        panel.add(compareHorsesButton);
        panel.add(backButton);

        return panel;
    }

    //Race Menu Panel
    private JPanel raceMenu() {

        //Create panel
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        //Initialise buttons
        JButton startRaceButton = new JButton("Start New Race");
        JButton backButton = new JButton("Back to Main Menu");

        //Add logic to buttons
        startRaceButton.addActionListener(e -> setupRace());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        //Add buttons and text to panel
        panel.add(new JLabel("Race Menu", SwingConstants.CENTER));
        panel.add(startRaceButton);
        panel.add(new JLabel(""));
        panel.add(backButton);

        return panel;
    }


    //Create Horse Function
    private void createHorse() {
        try {

            boolean validateHorseChosen = false;

            //While loop to ensure valid horse is created.
            while(!validateHorseChosen){

                //Get horse name from user
                String name = JOptionPane.showInputDialog(frame, "Enter horse name:");
                if (name == null || name.trim().isEmpty()){
                    return;
                }
                 

                HorsePart2 existingHorse = HorsePart2.loadHorseFromFile(horseFile, name);

                //Check to see if horse already exists
                if(existingHorse != null){
                    JOptionPane.showMessageDialog(frame, "Sorry! A Horse Already Exists With This Name.");
                    return;
                }
                else{

                    //Get horse symbol from user.
                    String symbolStr = JOptionPane.showInputDialog(frame, "Enter horse symbol (1 character):");
                    if (symbolStr == null || symbolStr.length() != 1){
                        return;
                    } 
                    char symbol = symbolStr.charAt(0);

                    //Get confidence from user.
                    double confidence = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter confidence (0.0 to 1.0):"));

                    HorsePart2 horse = new HorsePart2(symbol, name, confidence);
                    horse.saveToFile(horseFile);

                    JOptionPane.showMessageDialog(frame, "Horse created successfully!");
                }
            }

            

            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.");
        }
    }

    //View Horses Function
    private void viewHorses(){
        try {
            File file = new File(horseFile);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(frame, "No horses available.");
                return;
            }
    
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            while ((line = reader.readLine()) != null) {
                char symbol = line.charAt(0);
                String name = reader.readLine();
                String confidence = reader.readLine();
                
                // Load complete stats
                HorsePart2 horse = HorsePart2.loadCompleteHorse(
                    horseFile, 
                    "HorseRaceSimulator/Part 2/horse_stats.ser", 
                    name
                );
                
                builder.append("Name: ").append(name)
                       .append("\nSymbol: ").append(symbol)
                       .append("\nConfidence: ").append(confidence)
                       .append("\nWins: ").append(horse.getRacesWon())
                       .append("\nRaces: ").append(horse.getRacesParticipated())
                       .append("\n----------------\n");
            }
            reader.close();
            
            JTextArea textArea = new JTextArea(builder.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(frame, scrollPane, "All Horses", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading files.");
        }
    }


    //Delete Horse From File Function
    private void deleteHorse() {

        //Check to see if horse exists
        String name = JOptionPane.showInputDialog(frame, "Enter the name of the horse to delete:");
        if (name == null || name.trim().isEmpty()){
            JOptionPane.showMessageDialog(frame, "Horse Does Not Exist.");
            return;
        }

        try {

            //Check to see if file exists
            File file = new File(horseFile);
            if (!file.exists()){
                JOptionPane.showMessageDialog(frame, "File Does Not Exist.");
                return;
            } 

            //List of lines, a buffered reader, a line variable, and a horseFound variable
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean horseFound = false;

            //Loop through every line in the file
            while ((line = reader.readLine()) != null) {
                String symbol = line;
                String horseName = reader.readLine();
                String confidence = reader.readLine();

                //If the horse is not the horse to delete, add it to list
                if (!horseName.equals(name)) {
                    lines.add(symbol);
                    lines.add(horseName);
                    lines.add(confidence);
                }
                else{
                    //Horse to delete was found
                    horseFound = true;
                }
            }
            reader.close();


            //Output if horse name does not exist in the file
            if(!horseFound){
                JOptionPane.showMessageDialog(frame, "Horse Does Not Exist!");
                return;
            }

            //Rewrite the file using the list of lines
            //Should create a new version of the file with the chosen horse not in it.
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (String l : lines) {
                writer.println(l);
            }
            writer.close();

            JOptionPane.showMessageDialog(frame, "Horse deleted successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting horse.");
        }
    }

    //Compare Horses Method
    private void compareHorses() {

        //Get names of horses from user
        String name1 = JOptionPane.showInputDialog(frame, "Enter the first horse's name:");
        String name2 = JOptionPane.showInputDialog(frame, "Enter the second horse's name:");

        //End method if any name is empty.
        if (name1 == null || name2 == null || name1.trim().isEmpty() || name2.trim().isEmpty()){
            return;
        }

        //Create horse objects
        HorsePart2 horse1 = HorsePart2.loadHorseFromFile(horseFile, name1);
        HorsePart2 horse2 = HorsePart2.loadHorseFromFile(horseFile, name2);

        //Check to see if horses exist
        if (horse1 == null || horse2 == null) {
            JOptionPane.showMessageDialog(frame, "One or both horses not found.");
            return;
        }

        //Create comparision string
        String comparison = "Comparison:\n" +
            "1. " + horse1.getName() + " (" + horse1.getSymbol() + ")\n" +
            "   Confidence: " + String.format("%.2f", horse1.getConfidence()) + "\n" +
            "   Wins: " + horse1.getRacesWon() + "/" + horse1.getRacesParticipated() + "\n" +
            "2. " + horse2.getName() + " (" + horse2.getSymbol() + ")\n" +
            "   Confidence: " + String.format("%.2f", horse2.getConfidence()) + "\n" +
            "   Wins: " + horse2.getRacesWon() + "/" + horse2.getRacesParticipated();

        //Output comparision string
        JOptionPane.showMessageDialog(frame, comparison);
    }

    private void showHorseStats(){
        String horseName = JOptionPane.showInputDialog(frame, "Enter horse name:");
        HorsePart2 horse = HorsePart2.loadCompleteHorse(
            horseFile, 
            "HorseRaceSimulator/Part 2/horse_stats.ser", 
            horseName
        );
        
        JTextArea statsArea = new JTextArea();
        statsArea.append("Detailed Statistics:\n");
        statsArea.append("Name: " + horse.getName() + "\n");
        statsArea.append("Symbol: " + horse.getSymbol() + "\n");
        statsArea.append(String.format("Confidence: %.2f\n", horse.getConfidence()));
        statsArea.append("Total Races: " + horse.getRacesParticipated() + "\n");
        statsArea.append("Wins: " + horse.getRacesWon() + "\n");
        statsArea.append(String.format("Win Rate: %.1f%%\n", horse.getWinPercentage()));
        
        // Add race history button
        JButton historyButton = new JButton("Show Race History");
        historyButton.addActionListener(e -> {
            JTextArea historyArea = new JTextArea();
            for(RaceStats rs : horse.getRaceHistory()) {
                historyArea.append(rs.getSummary() + "\n");
            }
            JOptionPane.showMessageDialog(frame, new JScrollPane(historyArea));
        });
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        panel.add(historyButton, BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(frame, panel);
    }

    //Setup Race Method
    private void setupRace() {
        try {

            //Get number of lanes from user (max 5)
            int lanes = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter number of lanes (max 5):"));

            //Ensure that the maximum amount of lanes is 5
            lanes = Math.min(lanes, 5);

            //Get track length from user
            int trackLength = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter track length:"));

            //Create race object using track length and number of lanes
            RacePart2 race = new RacePart2(trackLength, lanes);

            //Loop through each lane allowinf user to place a horse in each lane.
            for (int i = 1; i <= lanes; i++) {
                String name = JOptionPane.showInputDialog(frame, "Enter name for horse in lane " + i + ":");
                HorsePart2 horse = HorsePart2.loadHorseFromFile(horseFile, name);

                if (horse != null) {
                    race.addHorse(horse, i);
                } else {
                    JOptionPane.showMessageDialog(frame, "Horse " + name + " not found!");
                    i--;
                }
            }

            showRaceGUI(race);

            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.");
        }
    }

    // Display for the race itself with lanes and symbols
    private void showRaceGUI(RacePart2 race){
        JFrame raceFrame = new JFrame("Race Track");
        raceFrame.setSize(800, 600);
        raceFrame.setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        JLabel timeLabel = new JLabel("Elapsed Time: 0s");
        JLabel leaderLabel = new JLabel("Current Leader: None");
        JLabel fallenLabel = new JLabel("Fallen Horses: 0");
        JLabel trackLengthLabel = new JLabel("Track Length: " + race.getRaceLength() + "m");
        statsPanel.add(trackLengthLabel);
        statsPanel.add(timeLabel);
        statsPanel.add(leaderLabel);
        statsPanel.add(fallenLabel);

        JPanel trackPanel = new JPanel(){
            private final int LANE_HEIGHT = 60;
            private final int MARGIN = 30;
            private final int NAME_AREA_WIDTH = 10;
            private final int TRACK_START_X = MARGIN + NAME_AREA_WIDTH;
            
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                setBackground(new Color(240, 240, 240));
                int y = MARGIN;
                
                int laneNumber = 1;
                for(HorsePart2 horse : race.getHorses()){
                    if(horse != null){
                        // Draw lane boundaries
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(MARGIN, y, getWidth() - 2*MARGIN, 3);
                        g.fillRect(MARGIN, y + LANE_HEIGHT - 3, getWidth() - 2*MARGIN, 3);

                        // Draw Lane number
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 14));
                    String laneString = Integer.toString(laneNumber);
                    g.drawString(laneString, MARGIN, y + LANE_HEIGHT/2 + 5);
                                
                        // Draw horse symbol
                        char symbolToDraw;
                        if(horse.hasFallen()){
                            symbolToDraw = 'X';
                        }
                        else{
                            symbolToDraw = horse.getSymbol();
                        }
                        int xPos = TRACK_START_X + (int)((double)horse.getDistanceTravelled() / race.getRaceLength() * (getWidth() - 2*MARGIN - 20));
                        g.setFont(new Font("Monospaced", Font.BOLD, 24));
                        g.drawString(String.valueOf(symbolToDraw), xPos, y + LANE_HEIGHT/2 + 8);
                        
        
                        y += LANE_HEIGHT;
                        laneNumber++;
                    }
                }

                // After drawing lanes, draw names at bottom
                y += 20;
                laneNumber = 1;
                for (HorsePart2 horse : race.getHorses()) {
                    if (horse != null) {
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.PLAIN, 12));
                        g.drawString("Lane " + laneNumber + ": " + horse.getName(), MARGIN + 10, y);
                        y += 20;
                        laneNumber++;
                    }
                }
            }
        };

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> new Thread(() -> {
            long startTime = System.currentTimeMillis();
            AtomicInteger fallenCount = new AtomicInteger();
            
            race.startRace(trackPanel, new RaceCallback() {
                public void updateStats(int elapsed, String leader, int fallen) {
                    SwingUtilities.invokeLater(() -> {
                        timeLabel.setText("Elapsed Time: " + elapsed + "s");
                        leaderLabel.setText("Current Leader: " + (leader != null ? leader : "None"));
                        fallenLabel.setText("Fallen Horses: " + fallen);
                        trackPanel.repaint();
                    });
                }
            }, startTime, fallenCount);
        }).start());

        JButton backButton = new JButton("Back To Main Menu");
        backButton.addActionListener(e -> raceFrame.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(backButton);

        raceFrame.add(statsPanel, BorderLayout.NORTH);
        raceFrame.add(new JScrollPane(trackPanel), BorderLayout.CENTER);
        raceFrame.add(buttonPanel, BorderLayout.SOUTH);
        raceFrame.setLocationRelativeTo(null);
        raceFrame.setVisible(true);
    }

    public interface RaceCallback {
        void updateStats(int elapsedTime, String leaderName, int fallenHorses);
    }
}
