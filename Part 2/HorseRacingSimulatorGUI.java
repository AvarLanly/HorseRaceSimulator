import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class HorseRacingSimulatorGUI {

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
        JButton backButton = new JButton("Back to Main Menu");

        //Implement logic into the horse menu
        createHorseButton.addActionListener(e -> createHorse());
        viewHorsesButton.addActionListener(e -> viewHorses());
        deleteHorseButton.addActionListener(e -> deleteHorse());
        compareHorsesButton.addActionListener(e -> compareHorses());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        //Add buttons to the panel.
        panel.add(createHorseButton);
        panel.add(viewHorsesButton);
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
    private void viewHorses() {
        try {

            //Checks to see if file exists.
            File file = new File(horseFile);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(frame, "No horses available.");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();

            String line;
            //Loops through file and uses String Builder to create list of horses.
            while ((line = reader.readLine()) != null) {
                String symbol = line;
                String name = reader.readLine();
                String confidence = reader.readLine();

                builder.append("Name: ").append(name)
                       .append(", Symbol: ").append(symbol)
                       .append(", Confidence: ").append(confidence).append("\n");
            }
            reader.close();

            //Text Area created to show horses
            JTextArea textArea = new JTextArea(builder.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(frame, scrollPane, "All Horses", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading horse file.");
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
        String comparison = "Comparing Horses:\n" +
                             horse1.getName() + " (Confidence: " + horse1.getConfidence() + ")\n" +
                             horse2.getName() + " (Confidence: " + horse2.getConfidence() + ")";

        //Output comparision string
        JOptionPane.showMessageDialog(frame, comparison);
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

    //Display for the race itself
    private void showRaceGUI(RacePart2 race){

        JFrame raceFrame = new JFrame("Race Track");
        raceFrame.setSize(800,600);
        raceFrame.setLayout(new BorderLayout());

        //Label showing track length
        JLabel trackLengthLabel = new JLabel("Track Length: " + race.getRaceLength());
        trackLengthLabel.setFont(new Font("Arial", Font.BOLD, 18));

        //Custom panel to draw horse progress visually
        JPanel trackPanel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                setBackground(Color.white);

                //Start position for first horse
                int y = 50;

                for(HorsePart2 horse: race.getHorses()){
                    if(horse != null){
                        g.setColor(Color.BLACK);
                        g.drawLine(50, y + 10, 700, y + 10);

                        //Get position of horse in the race
                        //Despite different length of track, gui shows it at a constant width
                        //Therefore calculations need to be made to determine position of horse relative to race track length.
                        int xPosition = 50 + (int)((double)horse.getDistanceTravelled() / race.getRaceLength() * 650);
                        g.setColor(Color.red);
                        g.fillOval(xPosition, y, 20, 20);
                        g.drawString(horse.getName(), 10, y + 15);

                        //Increase y position for next horse
                        y+=50;
                    }
                }
            }
        };

        //Button to start the race
        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> new Thread(() -> {
            race.startRace(trackPanel);
        }).start());



        //Adding components to frame
        raceFrame.add(trackLengthLabel, BorderLayout.NORTH);
        raceFrame.add(trackPanel, BorderLayout.CENTER);
        raceFrame.add(startButton, BorderLayout.SOUTH);
        raceFrame.setLocationRelativeTo(null);
        raceFrame.setVisible(true);
    }
}
