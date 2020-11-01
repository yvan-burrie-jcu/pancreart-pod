package jcu.cp3407.pancreart;

import java.util.*;

public class Simulator extends Task {

    ArrayList<Routine> routines = new ArrayList<>();

    static class Routine {

        String name;

        long start;

        long duration;

        double gravity; // There are 4 calories in 1g of glucose.

        final static double RATE = 0;

        Routine(String name, double startHour, double durationHours) {
            this.name = name;
            this.start = (long) (startHour * 60 * 60);
            this.duration = (long) (durationHours * 60 * 60);
        }

        double calculate(long now, double glucoseAmount) {
            double difference = 0;
            if (now >= start) {
                long end = start + duration;
                if (now < end) {
                    long where = end - now;
                    long half = duration / 2;
                    if (where > half) {

                    } else {

                    }
                    difference = where;
                }
            }
            return difference;
        }
    }

    static class Meal extends Routine {

        static double RATE = +1.8;

        Meal(String name, double startHour, double durationHours) {
            super(name, startHour, durationHours);
        }
    }

    static class Exercise extends Routine {

        static double RATE = -1.5;

        Exercise(String name, double startHour, double durationHours) {
            super(name, startHour, durationHours);
        }
    }

    static class Stress extends Routine {

        static double RATE = -0.5;

        Stress(String name, double startHour, double durationHours) {
            super(name, startHour, durationHours);
        }
    }

    static class Sleep extends Routine {

        static double RATE = -0.1;

        Sleep(String name, double startHour, double durationHours) {
            super(name, startHour, durationHours);
        }
    }

    BatteryPowerTask batteryPowerTask;

    static class BatteryPowerTask extends Task {

        double percent = 100;

        boolean charging = false;

        final double CHARGE_RATE = 0.01;

        final double CONSUMPTION_RATE = -0.001;

        final int LOW_PERCENT = 10;

        boolean lowPercentNotice = false;

        BatteryPowerTask(Timer timer, double speed) {
            super(timer, 10, speed);
        }

        public void run() {
            if (percent > 0) {
                percent += CONSUMPTION_RATE;
                if (percent < 0) {
                    percent = 0;
                }
                if (percent < 100 && charging) {
                    percent += CHARGE_RATE;
                }
            }
            if (lowPercentNotice) {
                if (percent > LOW_PERCENT) {
                    lowPercentNotice = false;
                }
            } else {
                if (percent <= LOW_PERCENT) {
                    lowPercentNotice = true;
                    handler.onBatteryPowerLow(percent);
                }
            }
        }
    }

    GlucoseDetectorTask glucoseDetectorTask;

    static class GlucoseDetectorTask extends Task {

        final static long DELAY = 60 * 5;

        double amount;

        GlucoseDetectorTask(Timer timer, double speed) {
            super(timer, DELAY, speed);
        }

        @Override
        public void run() {
            handler.onGlucoseDetected(amount);
        }
    }

    InsulinInjectorTask insulinInjectorTask;

    static class InsulinInjectorTask extends Task {

        final static long DELAY = 60 * 10;

        double reservoirAmount = 2000;

        boolean reservoirRemoved = false;

        boolean reservoirRemovedNotice = false;

        boolean reservoirLowNotice = false;

        final static double RESERVOIR_LOW = 50;

        InsulinInjectorTask(Timer timer, double speed) {
            super(timer, DELAY, speed);
        }

        @Override
        public void run() {
            if (reservoirRemoved) {
                if (reservoirRemovedNotice) {
                    return;
                }
                handler.onInsulinReservoirRemoved();
                reservoirRemovedNotice = true;
                return;
            }
            if (reservoirRemovedNotice) {
                reservoirRemovedNotice = false;
            }
            if (reservoirLowNotice) {
                if (reservoirAmount > RESERVOIR_LOW) {
                    reservoirLowNotice = false;
                }
            }
            if (reservoirAmount <= RESERVOIR_LOW) {
                // todo: notice
                handler.onInsulinReservoirLow();
                reservoirLowNotice = true;
            }
        }

        public void dose(double amount) {
            if (reservoirAmount >= amount) {
                reservoirAmount -= amount;
                handler.onInsulinInjected(amount);
            }
        }

        public void refill(double amount) {
            try {
                long oneSecond = (long) (speed * 1000);
                wait(oneSecond * 20); // wait for 20 seconds
                reservoirAmount = amount;
            } catch (InterruptedException ignored) {
            }
        }
    }

//    float[] metabolicGravity = {0, 0, 0, 0, 0, 0};
//
//    final long GLUCOSE_DETECTION_INTERVAL = 1;
//
//    long lastGlucoseDetectionTime = 0;
//
//    float lastGlucoseAmount = 0;
//
//    Random random = new Random();

    long currentTime = 0;

    Simulator(Timer timer, double speed) {
        super(timer, 60, speed);

        registerTask(batteryPowerTask = new BatteryPowerTask(timer, speed));
        registerTask(glucoseDetectorTask = new GlucoseDetectorTask(timer, speed));
        registerTask(insulinInjectorTask = new InsulinInjectorTask(timer, speed));
    }

    @Override
    public void run() {
        if (currentTime > 24 * 60 * 60) {
            currentTime = 0;
        }
//        long hours = currentTime / 3600;
//        long minutes = (currentTime - (hours * 3600)) / 60;
//        long second = currentTime - (hours * 3600) - (minutes * 60);
        System.out.println("CURRENT TIME: " + "(" + currentTime + ") " +
            (currentTime / 60 / 60 / 24) + ":" + (currentTime / 60 / 60) + ":" + (currentTime / 60));

        for (Routine routine : routines) {
            if (routine instanceof Meal) {
                Meal meal = (Meal) routine;
                System.out.println(meal.calculate(currentTime, glucoseDetectorTask.amount));
            }
        }

//        if (lastGlucoseDetectionTime++ >= GLUCOSE_DETECTION_INTERVAL) {
//            lastGlucoseDetectionTime = 0;
//            lastGlucoseAmount += metabolicGravity[state.ordinal()] * (-random.nextFloat() + random.nextFloat());
//        }
//        double difference = controller.getOutput(lastGlucoseAmount, 130);
//        metabolicGravity[state.ordinal()] += difference / 1000;
//
//        System.out.println("Glucose detection: " + lastGlucoseAmount);
//
//        System.out.println("Output: " + difference);

        currentTime += interval;
    }
}
