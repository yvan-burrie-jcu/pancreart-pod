package jcu.cp3407.pancreart.model;

public class Sensor {
    private static int currentReading = 0;

    Sensor() {
    }

    public static void setReading(int newReading) {
        currentReading = newReading;
    }

    public static int getReading() {
        return currentReading;
    }
}
