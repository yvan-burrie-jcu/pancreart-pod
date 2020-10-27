package jcu.cp3407.pancreart.model;

import java.util.Random;

public class Sensor {
    // TODO: 27/10/2020 add test methods for Sensor class
    private static int currentReading = 0;
    private static final Random randomReading = new Random();

    Sensor() {
    }

    public static void setReading(int start, int end) {
        currentReading = randomReading.nextInt(12 + 15);
    }

    public static void setReading(int newReading) {
        currentReading = newReading;
    }

    public static int getReading() {
        return currentReading;
    }
}
