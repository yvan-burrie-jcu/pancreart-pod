package jcu.cp3407.pancreart.model;

/**
 * @author Jesse Kerridge-Byrne.
 * PowerSupply class includes the functionality of a power supply.
 * Field includes the battery level.
 * Methods include setting and retrieving the power level.
 */

public class PowerSupply {
    private static final int LEVEL_INCREMENT = 5;
    private static final int BATTERY_LOW = 20;
    // Initial set battery level to high
    private static int batteryLevel = 100;

    // Return the enum value of battery level
    public static int batteryLevel() {
        return batteryLevel;
    }

    public static void setBatteryLevel() {
        // Set battery levels to loop in value. Set level to next lowest value
        // Assume setting when battery low is to reset battery
        if (batteryLevel >= 5) {
            batteryLevel = (batteryLevel - LEVEL_INCREMENT);
        } else {
            // Flat battery here
            batteryLevel = 0;
        }
    }

    // Check if battery is low, else battery still fine
    public static boolean isBatteryLevelOK() {
        return batteryLevel > BATTERY_LOW;
    }
}
