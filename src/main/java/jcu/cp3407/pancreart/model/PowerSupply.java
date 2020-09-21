package jcu.cp3407.pancreart.model;

/**
 * @author Jesse Kerridge-Byrne.
 * PowerSupply class includes the functionality of a power supply.
 * Field includes the battery level.
 * Methods include setting and retrieving the power level.
 */

public class PowerSupply {
    // Initial set battery level to high
    private static Level batteryLevel = Level.HIGH;

    // Return the enum value of battery level
    public static Level batteryLevel() {
        return batteryLevel;
    }

    public static void setBatteryLevel() {
        // Set battery levels to loop in value. Set level to next lowest value
        // Assume setting when battery low is to reset battery
        if (batteryLevel == Level.LOW) {
            batteryLevel = Level.HIGH;
        } else if (batteryLevel == Level.MEDIUM) {
            batteryLevel = Level.LOW;
        } else if (batteryLevel == Level.HIGH) {
            batteryLevel = Level.MEDIUM;
        }
    }

    // Check if battery is low, else battery still fine
    public static boolean isBatteryLevelOK() {
        return batteryLevel != Level.LOW;
    }

    // Enum of battery levels
    public enum Level {
        LOW, MEDIUM, HIGH
    }
}
