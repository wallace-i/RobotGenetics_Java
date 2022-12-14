/**
 *
 * @author ianWallace, 2022
 */

package flc.cisp400.robotevolution;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Simulation {
    private final int maxSimulations;
    private final int halfMaxRobots;
    private final int maxRobots;

    private final Random randomInt = new Random();
    private final ArrayList<Robot> robotArray = new ArrayList<>();
    private final RobotData dataObj = new RobotData();

    // Constructor
    public Simulation(int maxSimulations, int maxRobots) {
        this.maxSimulations = maxSimulations;
        this.maxRobots = maxRobots;
        halfMaxRobots = maxRobots / 2;
        
        robotInitialization();
    }

    public void simLoop() {
        for (int i = 0; i < maxSimulations; i++) {
            robotTesting();
            dataObj.setFitnessScore();
            cullAndCreate();
            resetRobots();
        }
    }

    public void printData() {
        // Output as JavaFX line chart
        Graph.printGraph(dataObj, maxRobots);

//        // Output as Text in Terminal
//        System.out.println("Printing Results...");
//
//        for (int i = 0; i < maxSimulations; i++) {
//            double fitness = dataObj.getRobotFitness(i);
//            double average = fitness / maxRobots;
//            if (i < 9) {
//                System.out.printf("0%d: %.2f\t", i + 1, average);
//            }
//            else {
//                System.out.printf("%d: %.2f\t", i + 1, average);
//            }
//
//            if ((i + 1) % 5 == 0) {
//                System.out.println();
//            }
//        }
//
//        System.out.println();
    } 
    
    private void robotInitialization() {
        for (int i = 0; i < maxRobots; i++) {
            // Create new robot in array
            robotArray.add(new Robot());
        }
    }
    
    private void robotTesting() {
        dataObj.resetAccumulator();
        // create robot, run each through map, loop maxSimulation times for maxRobots robots
        for (int i = 0; i < maxRobots; i++) {
            Map mapObj = new Map();
            
            do {
                robotArray.get(i).move(mapObj);
                
            } while (robotArray.get(i).getBattery() > 0);

            dataObj.setAccumulator(robotArray.get(i).getTurns() - 5);
        }
    }
    
    private void cullAndCreate() {

        // Sort robotArray in ascending order
        Collections.sort(robotArray, new TurnsComparator());

        // Cull and create new robots in bottom half of array
        for (int i = 0; i < halfMaxRobots; i += 2) {
            if (i == 0) {
                robotArray.set(0, new Robot(robotArray.get(maxRobots - 1), robotArray.get(maxRobots - 2), i));
                robotArray.set(1, new Robot(robotArray.get(maxRobots - 1), robotArray.get(maxRobots - 2), (i + 1)));
            }
            
            else {
                robotArray.set(i, new Robot(robotArray.get(maxRobots - i), robotArray.get(maxRobots - (i + 1)), i));
                robotArray.set((i + 1), new Robot(robotArray.get(maxRobots - i), robotArray.get(maxRobots - (i + 1)), (i + 1)));
            }
        }
    }
    
    private void resetRobots() {
        for (int i = halfMaxRobots - 1; i < maxRobots; i++) {
            robotArray.get(i).resetBattery();
            robotArray.get(i).resetTurns();
            robotArray.get(i).resetPosition();
        }
    }
}
