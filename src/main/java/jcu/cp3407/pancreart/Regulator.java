package jcu.cp3407.pancreart;

import jcu.cp3407.pancreart.model.Event;

import java.util.*;

import com.stormbots.MiniPID;

public class Regulator extends Task {

    Regulator(Timer timer, double speed) {
        super(timer, 60 * 10, speed);
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
        double speed = 1d / 100;

        // Setup simulator with various settings
        Simulator simulator = new Simulator(timer, speed);
        simulator.batteryPowerTask.charging = false;
        simulator.batteryPowerTask.percent = 20;
        simulator.glucoseDetectorTask.amount = 120;

        // Setup a 24-hour daily routine
        simulator.routines.add(new Simulator.Meal(
            "Breakfast",
            0,
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
            "Sleep",
            21,
            8)
        );

        Regulator regulator = new Regulator(timer, speed);

        GregorianCalendar calendar = new GregorianCalendar();

        ArrayList<Event> events = new ArrayList<>();

        MiniPID controller = new MiniPID(0.25, 0.01, 0.4);

        final double[] lastGlucoseAmount = new double[1];

        // Setup all the callbacks
        Handler handler = new Handler() {
            @Override
            void onGlucoseDetected(double amount) {
                super.onGlucoseDetected(amount);
                events.add(new Event(
                    0,
                    Event.Type.GLUCOSE_READING,
                    calendar.getTime().getTime(),
                    amount)
                );
                lastGlucoseAmount[0] = amount;
            }
            @Override
            void onInsulinInjected(double amount) {
                super.onInsulinInjected(amount);
                events.add(new Event(
                    0,
                    Event.Type.INSULIN_INJECTION,
                    calendar.getTime().getTime(),
                    amount)
                );
            }
            @Override
            void onBatteryPowerLow(double percent) {
                super.onBatteryPowerLow(percent);
            }
            @Override
            void onInsulinReservoirLow() {
                super.onInsulinReservoirLow();
            }
            @Override
            void onRegulateDosage() {
                super.onRegulateDosage();
                if (lastGlucoseAmount[0] < 100) {
                    simulator.insulinInjectorTask.dose(20);
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
