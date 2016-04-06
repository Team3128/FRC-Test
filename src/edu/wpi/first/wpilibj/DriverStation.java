/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2016. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;



/**
 * Provide access to the network communication data to / from the Driver
 * Station.
 */
public class DriverStation implements RobotState.Interface {

  /**
   * The robot alliance that the robot is a part of
   */
  public enum Alliance {
    Red, Blue, Invalid
  }

  private boolean inTeleop;
  private boolean inAutonomous;

  private static DriverStation instance = new DriverStation();

  /**
   * Gets an instance of the DriverStation
   *
   * @return The DriverStation.
   */
  public static DriverStation getInstance() {
    return DriverStation.instance;
  }

  /**
   * DriverStation constructor.
   *
   * The single DriverStation instance is created statically with the instance
   * static member variable.
   */
  protected DriverStation() {
  }

  /**
   * Read the battery voltage.
   *
   * @return The battery voltage in Volts.
   */
  public double getBatteryVoltage() 
  {
	  //TODO not implemented
	  return 12.4;
  }

  /**
   * Gets a value indicating whether the Driver Station requires the robot to be
   * enabled.
   *
   * @return True if the robot is enabled, false otherwise.
   */
  public boolean isEnabled() {
	  return inAutonomous || inTeleop;
  }

  /**
   * Gets a value indicating whether the Driver Station requires the robot to be
   * disabled.
   *
   * @return True if the robot should be disabled, false otherwise.
   */
  public boolean isDisabled() {
    return !isEnabled();
  }

  /**
   * Gets a value indicating whether the Driver Station requires the robot to be
   * running in autonomous mode.
   *
   * @return True if autonomous mode should be enabled, false otherwise.
   */
  public boolean isAutonomous() {
    return inAutonomous;
  }

  /**
   * Gets a value indicating whether the Driver Station requires the robot to be
   * running in test mode.
   *$
   * @return True if test mode should be enabled, false otherwise.
   */
  public boolean isTest() {
	  //TODO not implemented
	  return false;
  }

  /**
   * Gets a value indicating whether the Driver Station requires the robot to be
   * running in operator-controlled mode.
   *
   * @return True if operator-controlled mode should be enabled, false
   *         otherwise.
   */
  public boolean isOperatorControl() {
    return !(isAutonomous() || isTest());
  }

  /**
   * Get the current alliance from the FMS
   *$
   * @return the current alliance
   */
  public Alliance getAlliance() {
	  //TODO not implemented
	  return Alliance.Red;
  }

 
  /**
   * Is the driver station attached to a Field Management System? Note: This
   * does not work with the Blue DS.
   *$
   * @return True if the robot is competing on a field being controlled by a
   *         Field Management System
   */
  public boolean isFMSAttached() {
	  //TODO not implemented
	  return false;
  }

  public boolean isDSAttached() {
	  //TODO not implemented
	  return true;
  }

  /**
   * Return the approximate match time The FMS does not send an official match
   * time to the robots, but does send an approximate match time. The value will
   * count down the time remaining in the current period (auto or teleop).
   * Warning: This is not an official time (so it cannot be used to dispute ref
   * calls or guarantee that a function will trigger before the match ends) The
   * Practice Match function of the DS approximates the behaviour seen on the
   * field.
   *$
   * @return Time remaining in current match period (auto or teleop) in seconds
   */
  public double getMatchTime() {
	  //TODO not implemented
	  
	  return 120;
  }

  /**
   * Report error to Driver Station. Also prints error to System.err Optionally
   * appends Stack trace to error message
   *$
   * @param printTrace If true, append stack trace to error string
   */
  public static void reportError(String error, boolean printTrace) {
    reportErrorImpl(true, 1, error, printTrace);
  }

  /**
   * Report warning to Driver Station. Also prints error to System.err Optionally
   * appends Stack trace to warning message
   *$
   * @param printTrace If true, append stack trace to warning string
   */
  public static void reportWarning(String error, boolean printTrace) {
    reportErrorImpl(false, 1, error, printTrace);
  }
  
  private static void reportErrorImpl(boolean is_error, int code, String error, boolean printTrace) {
	    StackTraceElement[] traces = Thread.currentThread().getStackTrace();
	    String locString;
	    if (traces.length > 3)
	      locString = traces[3].toString();
	    else
	      locString = new String();
	    boolean haveLoc = false;
	    String traceString = new String();
	    traceString = "Stacktrace: \n";
	    for (int i = 3; i < traces.length; i++) {
	      String loc = traces[i].toString();
	      traceString += loc + "\n";
	      // get first user function
	      if (!haveLoc && !loc.startsWith("edu.wpi.first.wpilibj")) {
	        locString = loc;
	        haveLoc = true;
	      }
	    }
	    StringBuilder messageBuilder = new StringBuilder();
	    
	    if(is_error)
	    {
	    	messageBuilder.append("ERROR: ");
	    }
	    else
	    {
	    	messageBuilder.append("WARNING: ");
	    }
	    
	    messageBuilder.append(error);
	    
	    messageBuilder.append(" [Code ");
	    messageBuilder.append(code);
	    messageBuilder.append("] ");
	    
	    if(haveLoc)
	    {
	    	messageBuilder.append(" at ");
	    	messageBuilder.append(locString);
	    }
    	messageBuilder.append('\n');
	    messageBuilder.append(traceString);
	    
	    System.err.println(messageBuilder.toString());
	  }

  /**
   * Only to be used to tell the Driver Station what code you claim to be
   * executing for diagnostic purposes only
   *$
   * @param entering If true, starting disabled code; if false, leaving disabled
   *        code
   */
  public void InDisabled(boolean entering) {
	  //TODO not implemented
  }

  /**
   * Only to be used to tell the Driver Station what code you claim to be
   * executing for diagnostic purposes only
   *$
   * @param entering If true, starting autonomous code; if false, leaving
   *        autonomous code
   */
  public void InAutonomous(boolean entering) {
	  inAutonomous = entering;
  }

  /**
   * Only to be used to tell the Driver Station what code you claim to be
   * executing for diagnostic purposes only
   *$
   * @param entering If true, starting teleop code; if false, leaving teleop
   *        code
   */
  public void InOperatorControl(boolean entering) {
	  inTeleop = entering;
  }

  /**
   * Only to be used to tell the Driver Station what code you claim to be
   * executing for diagnostic purposes only
   *$
   * @param entering If true, starting test code; if false, leaving test code
   */
  public void InTest(boolean entering) {
	  //TODO not implemented
  }
}
