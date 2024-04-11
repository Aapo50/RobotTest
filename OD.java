package toimiiiiiii;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * The OD (Obstacle Detection) class represents a component responsible for detecting obstacles using an ultrasonic sensor.
 * It implements the Runnable interface to enable concurrent execution.
 */
public class OD implements Runnable {
    /** The data exchange instance. */
    private DataExchange de;

    /** The ultrasonic sensor instance. */
    private static EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S2);

    /**
     * Constructs a new OD instance.
     *
     * @param distanceExchange the data exchange object to communicate distance data with other components
     */
    public OD(DataExchange distanceExchange) {
        this.de = distanceExchange;
    }

    /**
     * Runs the OD thread.
     * This method continuously reads distance values from the ultrasonic sensor and updates the distance value in the data exchange object.
     */
    @Override
    public void run() {
        final SampleProvider sp = us.getDistanceMode();
        int distanceValue = 0;

        while (true) {
            // Fetch distance sample from the ultrasonic sensor
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            distanceValue = (int) (sample[0] * 100);

            // Display distance value on the LCD
            LCD.drawString("DV: " + distanceValue, 0, 3);

            // Update distance value in the data exchange object
            de.setDistance(distanceValue);

            // Delay before next iteration
            Delay.msDelay(500);

            // Check if any button is pressed to break out of the loop
            if (Button.getButtons() != 0) {
                break;
            }
        }
    }
}
