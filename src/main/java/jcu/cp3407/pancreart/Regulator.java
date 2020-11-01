package jcu.cp3407.pancreart;

import jcu.cp3407.pancreart.model.Event;

import java.util.*;

import com.stormbots.MiniPID;
import jcu.cp3407.pancreart.model.PodHandler;

public class Regulator extends Task {

    Regulator(Timer timer, double speed) {
        super(timer, 60 * 10 / Simulator.DELAY_RATE, speed);
    }

    @Override
    public void run() {
        handler.onRegulateDosage();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {

        // Instantiate a timer that is used to control the threads
        Timer timer = new Timer();

        // Specify the speed to run threads
        double speed = 1d / 1000;

        // Setup simulator with various settings
        Simulator simulator = new Simulator(timer, speed);
        simulator.batteryPowerTask.charging = false;
        simulator.batteryPowerTask.percent = 100;
        simulator.glucoseDetectorTask.amount = 100;
        simulator.insulinInjectorTask.reservoirAmount = 2000;
        simulator.currentTime = 4 * 60 * 60;

        // Setup a 24-hour daily routine
        simulator.routines.add(new Simulator.Sleep(
            "Morning Sleep",
            0,
            6)
        );
        simulator.routines.add(new Simulator.Meal(
            "Breakfast",
            7,
            1)
        );
        simulator.routines.add(new Simulator.Exercise(
            "Ride to Work",
            8,
            1)
        );
        simulator.routines.add(new Simulator.Stress(
            "Morning Stress",
            10,
            1)
        );
        simulator.routines.add(new Simulator.Meal(
            "Lunch",
            12,
            1)
        );
        simulator.routines.add(new Simulator.Stress(
            "Afternoon Stress",
            13,
            2)
        );
        simulator.routines.add(new Simulator.Exercise(
            "Ride back Home",
            16,
            1)
        );
        simulator.routines.add(new Simulator.Meal(
            "Dinner",
            18,
            2)
        );
        simulator.routines.add(new Simulator.Sleep(
            "Night Sleep",
            21,
            3)
        );

        Regulator regulator = new Regulator(timer, speed);

        GregorianCalendar calendar = new GregorianCalendar();

        ArrayList<Event> events = new ArrayList<>();

        MiniPID controller = new MiniPID(0.25, 0.01, 0.4);

        final double[] lastGlucoseAmount = new double[1];

        // Setup all the callbacks
        PodHandler handler = new PodHandler() {
            @Override
            public void onGlucoseDetected(double amount) {
                super.onGlucoseDetected(amount);
                events.add(new Event(
                    0,
                    Event.Type.GLUCOSE_READING,
                    calendar.getTime().getTime(),
                    amount)
                );
                lastGlucoseAmount[0] = amount;

                if (simulator.batteryPowerTask.percent > 90) {
                    simulator.batteryPowerTask.charging = false;
                }
            }
            @Override
            public void onInsulinInjected(double amount) {
                super.onInsulinInjected(amount);
                events.add(new Event(
                    0,
                    Event.Type.INSULIN_INJECTION,
                    calendar.getTime().getTime(),
                    amount)
                );
            }
            @Override
            public void onBatteryPowerLow(double percent) {
                super.onBatteryPowerLow(percent);
                simulator.batteryPowerTask.charging = true;
            }
            @Override
            public void onInsulinReservoirLow() {
                super.onInsulinReservoirLow();
                simulator.insulinInjectorTask.refill(2000);
            }
            @Override
            public void onRegulateDosage() {
                if (simulator.batteryPowerTask.percent <= 0) {
                    return;
                }
                super.onRegulateDosage();
                if (lastGlucoseAmount[0] > 150) {
                    simulator.insulinInjectorTask.dose(lastGlucoseAmount[0] / 10);
                }
            }
        };
        simulator.assignHandler(handler);
        regulator.assignHandler(handler);

        // Threads can now start
        simulator.initiate();
        regulator.initiate();
    }
}
