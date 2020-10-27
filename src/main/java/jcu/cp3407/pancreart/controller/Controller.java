package jcu.cp3407.pancreart.controller;

import jcu.cp3407.pancreart.model.*;

public class Controller {
    private static final int doseSpecified = 0;
    private static int cumulativeDose = 0;
    //    private static boolean manDeliveryButPressed = false;
//    private static boolean alarm_ON = false;
    private static int dose = 0;
    private static int reading0 = 0;
    private static int reading1 = 0;
    private static int reading2 = 0;
    private static int remainingInsulin = InsulinReservoir.getRemainingInsulin();
    private static final int reservoirCapacity = InsulinReservoir.RESERVOIR_CAPACITY;
    private static final int maxDailyDose = 25;
    private static final int maxSingleDose = 4;
    private static final int minDose = 1;
    private static final int safeMin = 6;
    private static final int safeMax = 14;
    private static int compDose = 0;
    public static final int RUNNING = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    private static int status = 1;
    private static boolean deliveryWorking = true;
    private static Clock pumpTimer;

    public static int getDose() {
        return dose;
    }

    public static int getReading0() {
        return reading0;
    }

    public static int getReading1() {
        return reading1;
    }

    public static int getReading2() {
        return reading2;
    }

    public static int getRemainingInsulin() {
        return remainingInsulin;
    }

    public Controller() {
    }

    public static void startClock() {
        pumpTimer = new Clock();
        pumpTimer.start();
        changeState(1);
    }

//    public static void startClock(int var0, int var1, int var2) {
//        pumpTimer = new Clock(var0, var1, var2);
//        pumpTimer.start();
//        changeState(1);
//    }

    public static void changeState(int var0) {
        Sensor.setReading(safeMin, safeMax);
        reading2 = Sensor.getReading();
        switch (var0) {
            case 1:
                reading0 = safeMin;
                reading1 = safeMax;
                changeState(4);
                break;
            case 2:
                if (status != 3 && remainingInsulin >= maxSingleDose && cumulativeDose < maxDailyDose) {
                    reading2 = Sensor.getReading();
                    if (reading2 < safeMin) {
                        compDose = 0;
                        //alarm_ON = true;
                        status = 2;
                        pumpTimer.displayOutput("Sugar Low");
                    } else if (reading2 >= safeMin && reading2 <= safeMax) {
                        if (reading2 <= reading1) {
                            compDose = 0;
                        } else if (reading2 - reading1 < reading1 - reading0) {
                            compDose = 0;
                        } else if (reading2 - reading1 >= reading1 - reading0) {
                            if ((reading2 - reading1) / 4 == 0) {
                                compDose = minDose;
                            } else if ((reading2 - reading1) / 4 > 0) {
                                compDose = (reading2 - reading1) / 4;
                            }
                        }
                    } else if (reading2 > safeMax) {
                        if (reading2 > reading1) {
                            if ((reading2 - reading1) / 4 == 0) {
                                compDose = minDose;
                            } else if ((reading2 - reading1) / 4 > 0) {
                                compDose = (reading2 - reading1) / 4;
                            }
                        } else if (reading2 == reading1) {
                            compDose = minDose;
                        } else if (reading2 < reading1) {
                            if (reading2 - reading1 <= reading1 - reading0) {
                                compDose = 0;
                            } else {
                                compDose = minDose;
                            }
                        }
                    }

                    if (compDose == 0) {
                        dose = 0;
                    } else if (dose > remainingInsulin) {
                        //alarm_ON = true;
                        status = 2;
                        pumpTimer.displayOutput("Insulin empty");
                    } else if (compDose + cumulativeDose > maxDailyDose) {
                        //alarm_ON = true;
                        status = 2;
                        dose = maxDailyDose - cumulativeDose;
                        pumpTimer.displayOutput("Close to Max Daily Dose");
                    } else if (compDose + cumulativeDose < maxDailyDose) {
                        if (compDose <= maxSingleDose) {
                            dose = compDose;
                        } else {
                            dose = maxSingleDose;
                        }
                    }

                    InsulinReservoir.removeInsulin(dose);
                    remainingInsulin = InsulinReservoir.getRemainingInsulin();
                    cumulativeDose += dose;
                    if (remainingInsulin <= maxSingleDose * 4) {
                        status = 2;
                        pumpTimer.displayOutput("Insulin Low");
                    }

//                    if (dose > 0) {
//                        Display.displayDose("Dose Administered = " + dose);
//                    }

                    reading1 = reading2;
                    reading0 = reading1;
                }
                break;
            case 3:
                if (cumulativeDose + doseSpecified > maxDailyDose) {
                    //alarm_ON = true;
                    status = 2;
                    pumpTimer.displayOutput("Exceeds Max Daily Dose");
                } else if (dose > remainingInsulin) {
                    //alarm_ON = true;
                    status = 2;
                    pumpTimer.displayOutput("Insulin empty");
                } else {
                    dose = doseSpecified;
                    cumulativeDose += doseSpecified;
                    remainingInsulin -= doseSpecified;
                    pumpTimer.displayOutput("Insulin remaining = " + remainingInsulin);
                    //Display.displayDose("Dose Administered = " + doseSpecified);
                }
                break;
            case 4:
//                if (!NeedleAssembly.isNeedlePresent()) {
//                    Display.displayOutput("No Needle Unit!");
//                    status = 3;
//                    alarm_ON = true;
//                }

                if (!InsulinReservoir.isInsulinPresent() || remainingInsulin < maxSingleDose) {
                    pumpTimer.displayOutput("No Insulin Remaining");
                    status = 3;
                    //alarm_ON = true;
                }

                if (!PowerSupply.isBatteryLevelOK()) {
                    pumpTimer.displayOutput("Low Battery");
                    status = 3;
                    //alarm_ON = true;
                }

                if (!InsulinPump.isPumpWorking()) {
                    pumpTimer.displayOutput("Pump Failure");
                    status = 3;
                    //alarm_ON = true;
                }

//                if (!Sensor.isSensorWorking()) {
//                    pumpTimer.displayOutput("Sensor Failure");
//                    status = 3;
//                    alarm_ON = true;
//                }

                if (!isDeliveryWorking()) {
                    pumpTimer.displayOutput("Needle Failure");
                    status = 3;
                    //alarm_ON = true;
                }
                break;
            case 5:
//                if (alarm_ON) {
//                }
                break;
            case 6:
                InsulinReservoir.replaceInsulin();
                remainingInsulin = InsulinReservoir.getRemainingInsulin();
                changeState(4);
        }

    }

//    public static void SetDose(int var0) {
//        if (var0 <= maxSingleDose && var0 >= minDose && cumulativeDose + var0 <= maxDailyDose && var0 <= remainingInsulin) {
//            doseSpecified = var0;
//            changeState(3);
//        } else {
//            pumpTimer.displayOutput("Dose not delivered");
//        }
//
//    }

    public static void resetCumulativeDose() {
        cumulativeDose = 0;
    }

//    public static void updateClock(int var0) {
//        int var2 = var0 / 3600;
//        int var1 = var0 - var2 * 3600;
//        int var3 = var1 / 60;
//        var1 -= var3 * 60;
//        Display.updateClock("              " + var2 + ":" + var3 + ":" + var1);
//    }

    public static void insulinReplaced() {
        changeState(6);
    }

//    public static void manualButtonPressed(boolean var0) {
//        manDeliveryButPressed = var0;
//    }

    public static void setDeliveryWorking(boolean var0) {
        deliveryWorking = var0;
    }

    public static boolean isDeliveryWorking() {
        return deliveryWorking;
    }
}
