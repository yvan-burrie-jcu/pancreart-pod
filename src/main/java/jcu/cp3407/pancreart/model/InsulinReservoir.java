package jcu.cp3407.pancreart.model;

/**
 * @author Jesse Kerridge-Byrne.
 * InsulinReservoir class includes the functionality of an insulin pump.
 * Field includes whether insulin is present or not.
 * Methods include removing and replacing insulin from the reservoir.
 */

public class InsulinReservoir {
    // Insulin is present or not
    private static boolean insulinPresent = true;
    public static final int RESERVOIR_CAPACITY = 100;
    private static int remainingInsulin = 100;

    // Remove insulin from reservoir
    public static void removeInsulin(int dose) {
        if (remainingInsulin <= dose) {
            // insulin low
            insulinPresent = false;
        } else {
            remainingInsulin -= dose;
        }
    }

    public static int getRemainingInsulin() {
        return remainingInsulin;
    }

    // Replace reservoir insulin
    public static void replaceInsulin() {
        insulinPresent = true;
        remainingInsulin = RESERVOIR_CAPACITY;
        // Resets insulin levels. Controller currently not implemented so commented for now
        // TODO: 20/09/2020 Add Controller to pod, and use this method for test and implementation
        //Controller.insulinReplaced();
    }

    // Check if insulin is present
    public static boolean isInsulinPresent() {
        return insulinPresent;
    }
}
