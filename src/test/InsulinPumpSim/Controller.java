package InsulinPumpSim;

//import Clock;
//import Display;
//import InsulinPump;

public class Controller {
    //Constant status values
    public static final int RUNNING = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    private static int doseSpecified = 0;
    private static int cumulativeDose = 0;
    // Input device definition
    private static boolean manDeliveryButPressed = false;
    // Output device definition
    private static boolean alarm_ON = false;
    private static int dose = 0;
    // Dose computation variables
    private static int reading0 = 0;
    private static int reading1 = 0;
    private static int reading2 = 0;
    private static int remainingInsulin = 100;
    private static int reservoirCapacity = 100;
    private static int maxDailyDose = 25;
    private static int maxSingleDose = 4;
    private static int minDose = 1;
    private static int safeMin = 6;
    private static int safeMax = 14;
    private static int compDose = 0;
    private static int status = RUNNING;
    private static boolean deliveryWorking = true;

    private static Clock pumpTimer;

    //Start clock with default values
    public static void startClock() {
        // Instantiate the clock
        pumpTimer = new Clock();
        pumpTimer.start();

        //Run the startup code
        Controller.changeState(1);
    }

    //Start clock with user input values
    public static void startClock(int bloodTest, int hardwareTest, int alarmTest) {
        // Instantiate the clock
        pumpTimer = new Clock(bloodTest, hardwareTest, alarmTest);
        pumpTimer.start();

        //Run the startup code
        Controller.changeState(1);
    }

    public static void changeState(int state) {
        reading2 = Sensor.getReading();

        switch (state) {
            case 1://START_UP
            {
                //State entered when the system is either started up for the 1st time,
                //when state is changed from manual to run and when insulin reservoir is replaced
                //System.out.println("Startup Boss");

                reading0 = safeMin;
                reading1 = safeMax;
                Controller.changeState(4); //change state to TEST

                break;
            }
            case 2://RUN
            {
                //State entered periodically in-order to determine whether insulin
                //should be administered

                if (manDeliveryButPressed == false) {
                    if (status != ERROR && remainingInsulin >= maxSingleDose && cumulativeDose < maxDailyDose) {
                        //retrieve current blood sugar level
                        reading2 = Sensor.getReading();

                        /*
                         * Sugar level low
                         */

                        if (reading2 < safeMin) {
                            compDose = 0;
                            alarm_ON = true;
                            status = WARNING;
                            pumpTimer.displayOutput("Sugar Low");
                        }

                        /*
                         * Sugar level OK
                         */

                        else if (reading2 >= safeMin && reading2 <= safeMax) {
                            // If sugar level is stable or falling
                            if (reading2 <= reading1) {
                                compDose = 0;
                            }
                            // If sugar level increasing
                            else {
                                // If rate of increase is falling
                                if ((reading2 - reading1) < (reading1 - reading0)) {
                                    compDose = 0;
                                }
                                // If rate of increase is increasing
                                else if ((reading2 - reading1) >= (reading1 - reading0)) {
                                    // If dose is rounded to zero, deliver the min dose
                                    if ((reading2 - reading1) / 4 == 0) {
                                        //set the amount to deliver to the min dose
                                        compDose = minDose;
                                    } else if ((reading2 - reading1) / 4 > 0) {
                                        //Set the amount to deliver
                                        compDose = (reading2 - reading1) / 4;
                                    }
                                }
                            }
                        }

                        /*
                         * Sugar level High
                         */

                        else if (reading2 > safeMax) {
                            // If Sugar level increasing
                            if (reading2 > reading1) {
                                // If dose is rounded to zero, deliver the min dose
                                if ((reading2 - reading1) / 4 == 0) {
                                    compDose = minDose;
                                } else if ((reading2 - reading1) / 4 > 0) {
                                    //Set the amount to deliver
                                    compDose = (reading2 - reading1) / 4;
                                }
                            }
                            // If the Sugar level is stable
                            else if (reading2 == reading1) {
                                compDose = minDose;
                            }
                            // If the Sugar level is falling
                            else if (reading2 < reading1) {
                                // If rate of decrease increasing
                                if ((reading2 - reading1) <= (reading1 - reading0)) {
                                    compDose = 0;
                                }
                                // If rate of decrease decreasing
                                else {
                                    compDose = minDose;
                                }
                            }
                        }

                        /*
                         * Check that dose computed is allowed to be administered
                         */

                        //If dose calculated is 0, don't deliver any insulin
                        if (compDose == 0) {
                            dose = 0;
                        }
                        //If dose exceeds the remaining Insulin left
                        else if (dose > remainingInsulin) {
                            alarm_ON = true;
                            status = WARNING;
                            pumpTimer.displayOutput("Insulin empty");
                        } else {
                            //If max daily dose will be exceeded by the dose calculated
                            if ((compDose + cumulativeDose) > maxDailyDose) {
                                //Alert the user and set status to Warning
                                alarm_ON = true;
                                status = WARNING;
                                dose = maxDailyDose - cumulativeDose;
                                InsulinPump.deliverInsulin(dose);
                                pumpTimer.displayOutput("Close to Max Daily Dose");
                            }
                            //Normal situation
                            else if ((compDose + cumulativeDose) < maxDailyDose) {
                                if (compDose <= maxSingleDose) {
                                    dose = compDose;
                                    InsulinPump.deliverInsulin(dose);
                                }
                                //If the single dose computed is too high
                                else {
                                    dose = maxSingleDose;
                                    InsulinPump.deliverInsulin(dose);
                                }
                            }
                        }

                        //Increment and decrement the necessary counters
                        remainingInsulin = remainingInsulin - dose;
                        cumulativeDose = cumulativeDose + dose;

                        //If the remaining level of insulin is low
                        if (remainingInsulin <= (maxSingleDose * 4)) {
                            status = WARNING;
                            pumpTimer.displayOutput("Insulin Low");
                        }

                        if (dose > 0) {
                            //Display to the user the dose delivered
                            Display.displayDose("Dose Administered = " + dose);
                        }

                        //Adjust the reading values to accomodate for the new reading
                        reading1 = reading2;
                        reading0 = reading1;
                    }
                }

                break;
            }
            case 3://MANUAL
            {
                //State entered when user specifies to run in the manual state
                //System.out.println("Manual Boss");

                //If this dose will exceed the daily dose warn the user
                if ((cumulativeDose + doseSpecified) > maxDailyDose) {
                    alarm_ON = true;
                    status = WARNING;
                    pumpTimer.displayOutput("Exceeds Max Daily Dose");
                }
                //If dose exceeds the remaining Insulin left
                else if (dose > remainingInsulin) {
                    alarm_ON = true;
                    status = WARNING;
                    pumpTimer.displayOutput("Insulin empty");
                } else {
                    dose = doseSpecified;
                    cumulativeDose = cumulativeDose + doseSpecified;
                    remainingInsulin = remainingInsulin - doseSpecified;

                    pumpTimer.displayOutput("Insulin remaining = " + remainingInsulin);
                    Display.displayDose("Dose Administered = " + doseSpecified);
                }
                break;
            }
            case 4:// HARDWARE TEST
            {
                //State entered periodically to ensure the system isn't faulty

                if (NeedleAssembly.isNeedlePresent() == false) {
                    Display.displayOutput("No Needle Unit!");
                    status = ERROR;
                    alarm_ON = true;
                }
                if (InsulinReservoir.isInsulinPresent() == false || remainingInsulin < maxSingleDose) {
                    pumpTimer.displayOutput("No Insulin Remaining");
                    status = ERROR;
                    alarm_ON = true;
                }
                if (PowerSupply.isBatteryLevelOK() == false) {
                    pumpTimer.displayOutput("Low Battery");
                    status = ERROR;
                    alarm_ON = true;
                }
                if (InsulinPump.isPumpWorking() == false) {
                    pumpTimer.displayOutput("Pump Failure");
                    status = ERROR;
                    alarm_ON = true;
                }
                if (Sensor.isSensorWorking() == false) {
                    pumpTimer.displayOutput("Sensor Failure");
                    status = ERROR;
                    alarm_ON = true;
                }
                if (Controller.isDeliveryWorking() == false) {
                    pumpTimer.displayOutput("Needle Failure");
                    status = ERROR;
                    alarm_ON = true;
                }

                break;
            }
            case 5://ALARM CHECK
            {
                //State entered Periodically to check whether the alarm needs sounding
                //System.out.println("Alarm test boss");

                // If the alarm should be switched on, activate it
                if (alarm_ON == true) {
                    //Activate alarm
                } else {
                    //DeActivate alarm();
                }
                break;
            }
            case 6://RESET
            {
                //State entered when insulin is replaced
                //Set the amount of insulin available to full
                remainingInsulin = reservoirCapacity;
                //Test the system
                Controller.changeState(4);
                break;
            }
        }
    }// End of method changeState

    public static void SetDose(int dose) {
        if (dose <= maxSingleDose && dose >= minDose && cumulativeDose + dose <= maxDailyDose && dose <= remainingInsulin) {
            doseSpecified = dose;
            Controller.changeState(3);
        } else {
            pumpTimer.displayOutput("Dose not delivered");
        }
    }

    public static void resetCumulativeDose() {
        cumulativeDose = 0;
    }

    //Update the clock display
    public static void updateClock(int seconds) {
        //Duration elapsed is passed in, in seconds
        int tempSeconds = seconds;

        //Derive number of hours elapsed
        int hours = tempSeconds / 3600;

        //Derive number of minutes elapsed
        tempSeconds = tempSeconds - (hours * 3600);
        int minutes = tempSeconds / 60;

        //Derive remaining seconds elapsed
        tempSeconds = tempSeconds - (minutes * 60);
        seconds = tempSeconds;

        //Display the new clock value
        Display.updateClock("              " + hours + ":" + minutes + ":" + seconds);
    }

    public static void insulinReplaced() {
        Controller.changeState(6);
    }

    public static void manualButtonPressed(boolean pressed) {
        manDeliveryButPressed = pressed;
    }

    public static boolean isDeliveryWorking() {
        return deliveryWorking;
    }

    public static void setDeliveryWorking(boolean working) {
        deliveryWorking = working;
    }
}// End of class Controller