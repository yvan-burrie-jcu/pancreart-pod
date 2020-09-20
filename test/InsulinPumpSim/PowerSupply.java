package InsulinPumpSim;// The PowerSupply Class is used to simulate the
// current battery level

class PowerSupply 
{
	private static boolean batteryLevelOK = true;

	public static boolean batteryLevel() 
	{
		return batteryLevelOK;
	}

	public static void setBatteryLevel(boolean levelOK) 
	{
		batteryLevelOK = levelOK;
	}

	public static boolean isBatteryLevelOK() 
	{
		return batteryLevelOK;
	}
}
