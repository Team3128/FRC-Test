/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2016. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;


/**
 * Implement a Robot Program framework. The RobotBase class is intended to be
 * subclassed by a user creating a robot program. Overridden autonomous() and
 * operatorControl() methods are called at the appropriate time as the match
 * proceeds. In the current implementation, the Autonomous code will run to
 * completion before the OperatorControl code could start. In the future the
 * Autonomous code might be spawned as a task, then killed at the end of the
 * Autonomous period.
 */
public abstract class RobotBase {
  /**
   * The VxWorks priority that robot code should work at (so Java code should
   * run at)
   */
  public static final int ROBOT_TASK_PRIORITY = 101;

  protected final DriverStation m_ds;

  /**
   * Constructor for a generic robot program. User code should be placed in the
   * constructor that runs before the Autonomous or Operator Control period
   * starts. The constructor will run to completion before Autonomous is
   * entered.
   *
   * This must be used to ensure that the communications code starts. In the
   * future it would be nice to put this code into it's own task that loads on
   * boot so ensure that it runs.
   */
  public RobotBase() {
	  m_ds = new DriverStation();
  }

  /**
   * Free the resources for a RobotBase class.
   */
  public void free() {}

  /**
   * @return If the robot is running in simulation.
   */
  public static boolean isSimulation() {
    return true;
  }

  /**
   * @return If the robot is running in the real world.
   */
  public static boolean isReal() {
    return false;
  }

  /**
   * Determine if the Robot is currently disabled.
   *$
   * @return True if the Robot is currently disabled by the field controls.
   */
  public boolean isDisabled() {
    return m_ds.isDisabled();
  }

  /**
   * Determine if the Robot is currently enabled.
   *$
   * @return True if the Robot is currently enabled by the field controls.
   */
  public boolean isEnabled() {
    return m_ds.isEnabled();
  }

  /**
   * Determine if the robot is currently in Autonomous mode.
   *$
   * @return True if the robot is currently operating Autonomously as determined
   *         by the field controls.
   */
  public boolean isAutonomous() {
    return m_ds.isAutonomous();
  }

  /**
   * Determine if the robot is currently in Test mode
   *$
   * @return True if the robot is currently operating in Test mode as determined
   *         by the driver station.
   */
  public boolean isTest() {
    return m_ds.isTest();
  }

  /**
   * Determine if the robot is currently in Operator Control mode.
   *$
   * @return True if the robot is currently operating in Tele-Op mode as
   *         determined by the field controls.
   */
  public boolean isOperatorControl() {
    return m_ds.isOperatorControl();
  }

  /**
   * Indicates if new data is available from the driver station.
   *$
   * @return Has new data arrived over the network since the last time this
   *         function was called?
   */
  public boolean isNewDataAvailable() {
    return m_ds.isNewControlData();
  }

  /**
   * Provide an alternate "main loop" via startCompetition().
   */
  public abstract void startCompetition();

  public static boolean getBooleanProperty(String name, boolean defaultValue) {
    String propVal = System.getProperty(name);
    if (propVal == null) {
      return defaultValue;
    }
    if (propVal.equalsIgnoreCase("false")) {
      return false;
    } else if (propVal.equalsIgnoreCase("true")) {
      return true;
    } else {
      throw new IllegalStateException(propVal);
    }
  }

}
