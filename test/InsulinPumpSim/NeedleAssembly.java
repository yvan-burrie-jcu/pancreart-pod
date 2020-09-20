package InsulinPumpSim;

class NeedleAssembly
{
	//Set needle present to true in constructor
	//We do this because we have no actual needle assembly
	//and therefore must simulate values, otherwise, the 
	//the hardware would specify this value
	private static boolean needlePresent = true;

	public static boolean isNeedlePresent()
	{
		return needlePresent;
	}

	public static void setNeedlePresent(boolean needleValue)
	{
		needlePresent = needleValue;
	}
}
