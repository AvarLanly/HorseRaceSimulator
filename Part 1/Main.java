public class Main {
    

    public static void main(String[] args) {
        
        Race race = new Race(10);

        Horse horse1 = new Horse('A', "Horse A", 0.8);
        Horse horse2 = new Horse('B', "Horse B", 0.6);
        Horse horse3 = new Horse('C', "Horse C", 0.7);

        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        race.startRace();
    }
}
