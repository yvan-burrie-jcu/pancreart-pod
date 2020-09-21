package jcu.cp3407.pancreart.model;

/**
 * @author Jesse Kerridge-Byrne.
 * InsulinPump class includes the functionality of an insulin pump.
 * Fields include the pump state and the quanitity of insulin to be delivered.
 * Methods include manipulating the pump state and delivering insulin.
 */

public class InsulinPump {
    // Set pump state and insulin delivery amount
    private static boolean pumpWorking = true;
    private static int quantityToDeliver = 0;

    // Check pump state
    public static boolean isPumpWorking() {
        return pumpWorking;
    }

    // Change pump state
    public static void setPumpWorking(boolean working) {
        pumpWorking = working;
    }

    // Deliver insulin, success depends on pump state
    public static boolean deliverInsulin(int quantity) {
        quantityToDeliver = quantity;
        return pumpWorking;
    }
}
