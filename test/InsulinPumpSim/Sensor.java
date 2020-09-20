package InsulinPumpSim;

class Sensor
{
	private static int currentReading = 0;
	private static boolean sensorWorking = true;

	public static void setReading(int bloodSugarLevel)
	{
		//At this point a simulated reading is supplied
		//if the hardware was present, it would produce blood
		//sugar value.
		currentReading = bloodSugarLevel;
	}

	public static int getReading()
	{
		//At this point a simulated reading is supplied
		//if the hardware was present, it would produce blood
		//sugar value.
		return currentReading;
	}

	public static void setSensorWorking(boolean working)
	{
		sensorWorking = working;
	}

	public static boolean isSensorWorking()
	{
		return sensorWorking;
	}
}
