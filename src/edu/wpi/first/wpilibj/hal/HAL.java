package edu.wpi.first.wpilibj.hal;

/**
 * JNI Wrapper for HAL<br>.
 */
public class HAL {
  public static void waitForDSData()
  {
	try
	{
		Thread.sleep(5);
	}
	catch(InterruptedException e)
	{
		e.printStackTrace();
	}
  }

  public static int initialize(int mode)
  {
	  return 0;
  }

  public static void observeUserProgramStarting() {}

  public static void observeUserProgramDisabled() {}

  public static void observeUserProgramAutonomous() {}

  public static void observeUserProgramTeleop() {}

  public static void observeUserProgramTest() {}

  public static void report(int resource, int instanceNumber) {
    report(resource, instanceNumber, 0, "");
  }

  public static void report(int resource, int instanceNumber, int context) {
    report(resource, instanceNumber, context, "");
  }

  /**
   * Report the usage of a resource of interest. <br>
   *
   * <p>Original signature: <code>uint32_t report(tResourceType, uint8_t, uint8_t, const
   * char*)</code>
   *
   * @param resource       one of the values in the tResourceType above (max value 51). <br>
   * @param instanceNumber an index that identifies the resource instance. <br>
   * @param context        an optional additional context number for some cases (such as module
   *                       number). Set to 0 to omit. <br>
   * @param feature        a string to be included describing features in use on a specific
   *                       resource. Setting the same resource more than once allows you to change
   *                       the feature string.
   */
  public static int report(int resource, int instanceNumber, int context, String feature)
  {
	  return 0;
  }
  
}