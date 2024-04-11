package app;

/**
 * This is a program for the Lejos EV3 Robot.
 * The program controls various aspects of the robot's behavior, such as line following and obstacle detection.
 * Date: 4.4-2024
 * @author Teemu, Aapo, Ada, Laura
 * @version 1.0
 */
import lejos.hardware.Button;
import toimiiiiiii.*;

public class LejosProject {
    
    /** The line follower instance. */
    private static LineFollower lineFollower;
    
    /** The motor instance. */
    private static Motor motor;
    
    /** The data exchange instance. */
    
    private static DataExchange de;
    
    /** The OD instance. */
    private static OD od;
    
    /** Flag to track whether threads are started. */
    private static boolean threadsStarted = false;
    
    /** Flag to indicate whether to stop threads. */
    private static boolean stopThreads = false;

    /**
     * The main method to start the LejosProject program.
     * This method initializes necessary components, starts threads for different functionalities,
     * and listens for user input to stop the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Initialize data exchange
        de = new DataExchange();

        // Main loop to handle thread execution
        while (!stopThreads) {
            if (!threadsStarted) {
                // Initialize line follower, motor, and OD instances
                lineFollower = new LineFollower(de);
                motor = new Motor(de);
                od = new OD(de);

                // Create and start threads for line follower, motor, and OD
                Thread lineFollowerThread1 = new Thread(lineFollower);
                Thread motorThread = new Thread(motor);
                Thread odThread = new Thread(od);

                // Wait for any button press before starting threads
                Button.waitForAnyPress();

                // Set threads as daemon and start them
				lineFollowerThread1.setDaemon(true);
                lineFollowerThread1.start();
                motorThread.setDaemon(true);
                motorThread.start();
                odThread.setDaemon(true);
                odThread.start();

                // Set threadsStarted flag to true once threads are started
                threadsStarted = true;
            }

            // Check if UP button is pressed to stop threads
            if (Button.UP.isDown()) {
                stopThreads = true;
            }
        }

        // Stop the motor once threads are stopped
        motor.stop();
    }
}
