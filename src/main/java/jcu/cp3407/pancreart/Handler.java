package jcu.cp3407.pancreart;

public class Handler {

    void onBatteryPowerLow(double percent) {
        System.out.println("Battery power low!");
    }

    void onGlucoseDetected(double amount) {
        System.out.println("Glucose detected!");
    }

    void onInsulinInjected(double amount) {
        System.out.println("Insulin injected!");
    }

    void onInsulinReservoirLow() {
        System.out.println("Insulin reservoir low!");
    }

    void onInsulinReservoirRemoved() {
        System.out.println("Insulin reservoir removed!");
    }

    void onRegulateDosage() {
        System.out.println("Regulating dosage!");
    }
}
