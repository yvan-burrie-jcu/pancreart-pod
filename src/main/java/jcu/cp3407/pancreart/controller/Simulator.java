package jcu.cp3407.pancreart.controller;

import jcu.cp3407.pancreart.model.InsulinPump;
import jcu.cp3407.pancreart.model.InsulinReservoir;
import jcu.cp3407.pancreart.model.PowerSupply;
import jcu.cp3407.pancreart.model.Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulator extends Thread {

    public synchronized void run() {
        String bloodSugarLevel = "";
        String tempBloodSugarLevel = "m";
        boolean prevMenu = false;
        int newBloodSugarLevel = 0;

        //Marker denoting current simulator state
        boolean insulinPresent = InsulinReservoir.isInsulinPresent();
        boolean pumpWorking = InsulinPump.isPumpWorking();
        boolean batteryLevelOk = PowerSupply.isBatteryLevelOK();
        boolean batteryOK = true;
        boolean deliveryOK = true;
        int maxDailyDose = 25;
        int maxSingleDose = 4;

        String reservoirMessage = "";
        String batteryMessage = "";
        String pumpMessage = "";
        String deliveryMessage = "";

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader bloodInput = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader readingInput = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Output menu system to the user
            //System.err.println("******************************************************************");
            //System.err.println("********************INSULIN PUMP SIMULATOR************************");
            //System.err.println("");
//            System.err.println("Please input the interval (in seconds) at which you wish the following actions to be performed");
//            System.err.print  ("Testing of the blood sugar level: ");
            //Read in the users input
            //System.err.flush();
//            String tempBloodTest = bloodInput.readLine().trim();
//            int bloodTest = Integer.parseInt(tempBloodTest);

//            System.err.print  ("Testing of the hardware: ");
//            //Read in the users input
//            System.err.flush();
//            String tempHardwareTest = hardwareInput.readLine().trim();
//            int hardwareTest = Integer.parseInt(tempHardwareTest);

            //Start the clock with the new periodic test interval values set
            Controller.startClock();

            // Boolean variable to store whether the user wishes to use the algorithm again.
            boolean contLoop = true;
            int userChoice = 0;

            // Loop while the user wishes to keep the program running
            Controller.changeState(1);
            while (contLoop == true) {
                //Develop output message
                if (insulinPresent == true) {
                    reservoirMessage = ("Remove Insulin Reservoir");
                } else {
                    reservoirMessage = ("Replace Insulin Reservoir");
                }

                if (batteryOK == true) {
                    batteryMessage = ("Simulate Low Battery");
                } else {
                    batteryMessage = ("Replace Battery");
                }

                if (pumpWorking == true) {
                    pumpMessage = ("Simulate Pump Failure");
                } else {
                    pumpMessage = ("Repair Pump");
                }

                if (deliveryOK == true) {
                    deliveryMessage = ("Simulate Delivery Failure");
                } else {
                    deliveryMessage = ("Ensure Delivery");
                }

                prevMenu = false;

                System.err.println("******************************************************************");
                System.err.println("************************SIMULATOR OPTIONS*************************");
                System.err.println();
                System.err.println("Please select one of the following numbers to simulate the corresponding action:");
                System.err.println("Type 1 To " + reservoirMessage);
                System.err.println("Type 2 To " + batteryMessage);
                System.err.println("Type 3 To " + pumpMessage);
                System.err.println("Type 4 To " + deliveryMessage);
                System.err.println("Type 5 To Enter a new blood sugar reading");
                System.err.println("Type 6 To exit the program");
                System.err.print("Option Chosen: ");

                System.err.flush();
                System.out.println(PowerSupply.batteryLevel());
                System.out.println(batteryLevelOk);
                System.out.println(InsulinReservoir.isInsulinPresent());
                System.out.println(insulinPresent);
                System.out.println(InsulinReservoir.getRemainingInsulin());
                String userInput = input.readLine().trim();
                System.out.println("Remaining Insulin: " + Controller.getRemainingInsulin());
                System.out.println("Reading 0: " + Controller.getReading0() + " Reading 1: " + Controller.getReading1() + " Reading 2: " + Controller.getReading2());

                userChoice = Integer.parseInt(userInput);

                switch (userChoice) {
                    case 1: {
                        if (insulinPresent == true) {
                            Controller.changeState(2);
                            //System.out.println("Reading 0: " + Controller.getReading0() + " Reading 1: " + Controller.getReading1() + " Reading 2: " + Controller.getReading2());
                            //Simulate the reservoir being removed
                            InsulinReservoir.removeInsulin(Controller.getDose());
                            insulinPresent = InsulinReservoir.isInsulinPresent();
                        } else {
                            //Simulate the reservoir being replaced
                            InsulinReservoir.replaceInsulin();
                            insulinPresent = InsulinReservoir.isInsulinPresent();
                        }
                        break;
                    }
                    case 2: {
                        if (batteryOK == true) {
                            //Simulate the battery running low
                            PowerSupply.setBatteryLevel();
                            batteryLevelOk = PowerSupply.isBatteryLevelOK();
                        } else {
                            //Simulate the battery being replaced
                            batteryOK = true;
                        }
                        break;
                    }
                    case 3: {
                        if (pumpWorking == true) {
                            //Simulate pump failure
                            InsulinPump.setPumpWorking(false);
                            pumpWorking = false;
                        } else {
                            //Simulate the pump being fixed
                            InsulinPump.setPumpWorking(true);
                            pumpWorking = true;
                        }
                        break;
                    }
                    case 4: {
                        if (deliveryOK == true) {
                            //Simulate delivery failure
                            Controller.setDeliveryWorking(false);
                            deliveryOK = false;
                        } else {
                            //Simulate delivery being fixed
                            Controller.setDeliveryWorking(true);
                            deliveryOK = true;
                        }
                        break;
                    }
                    case 5: {
                        while (prevMenu == false) {
                            System.err.println();
                            System.err.println("Please enter the blood sugar reading to be simulated next");
                            System.err.println("Type m to return to previous menu");
                            System.err.print("New Blood Sugar Level: ");

                            System.err.flush();
                            bloodSugarLevel = readingInput.readLine().trim();

                            if (bloodSugarLevel.equals("m") || bloodSugarLevel.equals("M")) {
                                prevMenu = true;
                            } else {
                                //System.out.println();
                                System.out.println(newBloodSugarLevel);
                                newBloodSugarLevel = Integer.parseInt(bloodSugarLevel);

                                Sensor.setReading(newBloodSugarLevel);
                            }

                            System.err.println();
                        }
                        break;
                    }
                    case 7: {
                        System.err.println("***************INSULIN PUMP SIMULATOR STOPPED*****************");
                        contLoop = false;

                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred when inoutting a value");
        }
    }
}
