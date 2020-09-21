package InsulinPumpSim;

class InsulinPump {
    private static boolean pumpWorking = true;
    private static int quantityToDeliver = 0;

    public static boolean isPumpWorking() {
        return pumpWorking;
    }

    public static void setPumpWorking(boolean working) {
        pumpWorking = working;
    }

    public static boolean deliverInsulin(int quantity) {
        quantityToDeliver = quantity;
        //Return whether the delivery was sucessful or not
        return pumpWorking;
    }
}
