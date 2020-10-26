package jcu.cp3407.pancreart.model;

/**
 * @author Jesse Kerridge-Byrne.
 * Clock class includes the functionality of a clock.
 * Fields include seconds, reset, tests, and messages.
 * Methods include setting and retrieving the power level.
 */

public class Clock extends Thread {
    private final int bloodTest;
    private final int hardwareTest;
    //Variables for multiple message displaying
    private final String[] messages = new String[5];
    //Initialise seconds, reset, and tests
    private int seconds;
    private int clockReset;
    private int messagePointer = 0;
    // TODO: 20/09/2020 outputPointer may be useful for log transmission 
    //private int outputPointer = 0;
    private int arraySize = 0;

    //Constructor
    public Clock() {
        //Set default values, set blood test frequency to 10mins, according to specs
        seconds = 0;
        bloodTest = 600;
        hardwareTest = 30;

        //Intialise Message buffer
        for (int i = 0; i < 5; i++) {
            messages[i] = "";
        }
    }

    //Constructor
    public Clock(int bloodTestValue, int hardwareTestValue) {
        //Allow values to be set, to speed up the simulation
        seconds = 0;
        bloodTest = bloodTestValue;
        hardwareTest = hardwareTestValue;

        //Intialise Message buffer
        for (int i = 0; i < 5; i++) {
            messages[i] = "";
        }
    }

    public synchronized void run() {
        // Simulate clock timer
        while (true) {
            // Reset the clock every 24 hours (86400 seconds in 24 hours)
            // TODO: 21/09/2020 Add logic to set reset to when it has been downloaded from the pod. 
            if (seconds == 86400) {
                seconds = 0;
                // TODO: 20/09/2020 Add Controller class to pod, implement and test below method 
                //Controller.resetCumulativeDose();
            }

            // Every 10 mins (600 seconds) trigger controller to
            // determine whether any insulin should be administered
            if ((seconds % bloodTest) == 0) {
                // TODO: 20/09/2020 Add Controller class to pod, implement and test below method 
                //Controller.changeState(2); //State 2 = the run state
            }

            // Every 30 seconds run hardware test
            if ((seconds % hardwareTest) == 0) {
                // TODO: 20/09/2020 Add Controller class to pod, implement and test below method 
                //Controller.changeState(4); //State 4 = the test state
            }

            // TODO: 20/09/2020 Remove alarm references. Project does not include alarm 
            // Every 10 seconds check the alarm
//            if ((seconds % alarmCheck) == 0)
//            {
//                Controller.changeState(5); //State 5 = the alarm check state
//            }

//            if ((seconds % 5) == 0)
//            {
//                // TODO: 20/09/2020 Display class not entierly relevant. But add a way to log events for external display
//                Display.displayOutput(messages[outputPointer]);
//
//                //Select next message to be output
//                if (outputPointer < arraySize -1)
//                {
//                    outputPointer = outputPointer + 1;
//                }
//                else
//                {
//                    outputPointer = 0;
//                }
//            }

            // wait 1000 milliseconds (1 second)
            try {
                wait(1000);
            } catch (InterruptedException ie) {
            }

            // Increase the clock by 1 second
            // TODO: 21/09/2020 Seconds may be too large a value for timestamps, consider changing to smaller values 
            seconds = seconds + 1;

            //Update the clock displayed to the user
            // TODO: 20/09/2020 Add Controller class to pod, implement and test below method 
            //Controller.updateClock(seconds);
        }
    }

    // TODO: 20/09/2020 This method may be useful for storing and outputting logs 
    public synchronized void displayOutput(String output) {
        messages[messagePointer] = output;

        //Increment messagePointer
        if (messagePointer < 4) {
            messagePointer = messagePointer + 1;
        } else {
            messagePointer = 0;
        }

        if (arraySize < 4) {
            arraySize = arraySize + 1;
        }
    }
}
