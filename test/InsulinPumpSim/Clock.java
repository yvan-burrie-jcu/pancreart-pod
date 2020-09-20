package InsulinPumpSim;

import java.util.*;

public class Clock extends Thread
{
	private int seconds;
	private int clockReset;
	private int bloodTest;
	private int hardwareTest;
	private int alarmCheck;

	//Variables for multiple message displaying
	private String messages[] = new String[5];
	private int messagePointer = 0;
	private int outputPointer = 0;
	private int arraySize = 0;
	
	//Constructor
	public Clock()
	{
		//Set default values for finished working software
		seconds = 0;
		bloodTest = 600;
		hardwareTest = 30;
		alarmCheck = 10;

		//Intialise Message buffer
		for (int i = 0; i < 5 ; i++)
		{
			messages[i] = "";
		}
	}

	//Constructor
	public Clock(int bloodTestValue, int hardwareTestValue, int alarmCheckValue)
	{
		//Allow values to be set, to speed up the simulation
		seconds = 0;
		bloodTest = bloodTestValue;
		hardwareTest = hardwareTestValue;
		alarmCheck = alarmCheckValue;

		//Intialise Message buffer
		for (int i = 0; i < 5 ; i++)
		{
			messages[i] = "";
		}
	}

	public synchronized void run()
	{
		// Simulate clock timer
		while (true)
		{
			// Reset the clock every 24 hours (86400 seconds in 24 hours)
			if (seconds == 86400)
			{
				seconds = 0;
				Controller.resetCumulativeDose();
			}

			// Every 10 mins (600 seconds) trigger controller to
			// determine whether any insulin should be admiinistered
			if ((seconds % bloodTest) == 0)
			{
				Controller.changeState(2); //State 2 = the run state
			}

			// Every 30 seconds run hardware test
			if ((seconds % hardwareTest) == 0)
			{
				Controller.changeState(4); //State 4 = the test state
			}

			// Every 10 seconds check the alarm
			if ((seconds % alarmCheck) == 0)
			{
				Controller.changeState(5); //State 5 = the alarm check state
			}
	
			if ((seconds % 5) == 0)
			{
				Display.displayOutput(messages[outputPointer]);

				//Select next message to be output
				if (outputPointer < arraySize -1)
				{
					outputPointer = outputPointer + 1;
				}
				else
				{
					outputPointer = 0;
				}
			}		
				
			// wait 1000 milliseconds (1 second)
			try
			{
				wait(1000);
			}
			catch (InterruptedException ie) {}

			// Increase the clock by 1 second
			seconds = seconds + 1;
			
			//Update the clock displayed to the user
			Controller.updateClock(seconds);
		}
	}

	public synchronized void displayOutput(String output)
	{
			messages[messagePointer] = output;

			//Increment messagePointer
			if (messagePointer < 4)
			{
				messagePointer = messagePointer + 1;
			}
			else
			{
				messagePointer = 0;
			}

			if (arraySize < 4)
			{
				arraySize = arraySize + 1;
			}
	}
}