package InsulinPumpSim;

class InsulinReservoir {
    private static boolean insulinPresent = true;

    public static void removeInsulin() {
        insulinPresent = false;
    }

    public static void replaceInsulin() {
        insulinPresent = true;
        Controller.insulinReplaced();
    }

    public static boolean isInsulinPresent() {
        return insulinPresent;
    }
}
