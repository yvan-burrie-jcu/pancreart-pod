package jcu.cp3407.pancreart;

import com.stormbots.MiniPID;

import java.util.*;

import jcu.cp3407.pancreart.model.*;

public class Simulator {

    public static void main(String[] args) {
        new Simulator();
    }

    ArrayList<Event> events = new ArrayList<>();

    long currentTime = 0;

    public enum State {
        NONE,
        MEAL,
        EXERCISE,
        SLEEP,
        STRESS
    }

    State state = State.MEAL;

    float insulinCapacity = 1000;

    float[] metabolicGravity = {0, 0, 0, 0, 0, 0};

    final long GLUCOSE_DETECTION_INTERVAL = 1;

    long lastGlucoseDetectionTime = 0;

    float lastGlucoseAmount = 0;

    Random random = new Random();

    MiniPID controller = new MiniPID(0.25, 0.01, 0.4);

    Thread thread = new Thread() {
        public void run() {
            try {
                while (true) {

                    if (lastGlucoseDetectionTime++ >= GLUCOSE_DETECTION_INTERVAL) {
                        lastGlucoseDetectionTime = 0;
                        lastGlucoseAmount += metabolicGravity[state.ordinal()] * (-random.nextFloat() + random.nextFloat());
                    }
                    double difference = controller.getOutput(lastGlucoseAmount, 130);
//                    metabolicGravity[state.ordinal()] += difference / 1000;

                    System.out.println("Glucose detection: " + lastGlucoseAmount);

                    System.out.println("Output: " + difference);

//                    if (lastGlucoseAmount >= 160) {
//                        metabolicGravity[state.ordinal()] -= metabolicGravity[state.ordinal()] / 100;
//                        System.out.println("Too high!");
//                    }
//
//                    if (lastGlucoseAmount <= 110) {
//                        metabolicGravity[state.ordinal()] += metabolicGravity[state.ordinal()] / 100;
//                        System.out.println("Too low!");
//                    }

                    sleep(10);
                    currentTime += 1;
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    };

    public void addInsulinInjection(float amount) {
        events.add(new Event(0, currentTime, amount));
    }

    public Simulator() {

        metabolicGravity[State.NONE.ordinal()] = +1.0f;
        metabolicGravity[State.MEAL.ordinal()] = +1.5f;
        metabolicGravity[State.EXERCISE.ordinal()] = -1.0f;
        metabolicGravity[State.SLEEP.ordinal()] = -0.5f;
        metabolicGravity[State.STRESS.ordinal()] = +0.8f;

        lastGlucoseAmount = 130;

        thread.start();
    }
}
