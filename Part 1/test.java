public class test {
    

    public static void main(String[] args) {
        
        Horse testHorse = new Horse('A', "Test Horse", 0.8);

        System.out.println(testHorse.getName()); //Expected: Test Horse
        System.out.println(testHorse.getSymbol()); //Expected: A
        System.out.println(testHorse.getConfidence()); //Expected: 0.8
        System.out.println(testHorse.getDistanceTravelled()); //Expected: 0
        System.out.println(testHorse.hasFallen()); //Expected: false

        testHorse.moveForward();
        System.out.println(testHorse.getDistanceTravelled()); //Expected: 1

        testHorse.fall();
        System.out.println(testHorse.hasFallen()); //Expected: true

        testHorse.goBackToStart();
        System.out.println(testHorse.getDistanceTravelled()); //Expected: 0
        System.out.println(testHorse.hasFallen()); //Expected: false

        testHorse.setConfidence(0.9);
        System.out.println(testHorse.getConfidence()); //Expected: 0.9

        testHorse.setConfidence(-5);
        System.out.println(testHorse.getConfidence()); //Expected: 0.0

        testHorse.setConfidence(1.5);
        System.out.println(testHorse.getConfidence()); //Expected: 1.0

    }
}
