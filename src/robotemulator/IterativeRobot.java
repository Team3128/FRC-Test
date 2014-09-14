package robotemulator;

/**
 * WPILIBJ COMMENT:
 * ====================================================================================================
 * IterativeRobot implements a specific type of Robot Program framework, extending the RobotBase class.
 *
 * The IterativeRobot class is intended to be subclassed by a user creating a robot program.
 *
 * This class is intended to implement the "old style" default code, by providing
 * the following functions which are called by the main loop, startCompetition(), at the appropriate times:
 *
 * robotInit() -- provide for initialization at robot power-on
 *
 * init() functions -- each of the following functions is called once when the
 *                     appropriate mode is entered:
 *  - DisabledInit()   -- called only when first disabled
 *  - AutonomousInit() -- called each and every time autonomous is entered from another mode
 *  - TeleopInit()     -- called each and every time teleop is entered from another mode
 *  - TestInit()       -- called each and every time test mode is entered from anothermode
 *
 * Periodic() functions -- each of these functions is called iteratively at the
 *                         appropriate periodic rate (aka the "slow loop").  The period of
 *                         the iterative robot is synced to the driver station control packets,
 *                         giving a periodic frequency of about 50Hz (50 times per second).
 *   - disabledPeriodic()
 *   - autonomousPeriodic()
 *   - teleopPeriodic()
 *   - testPeriodoc()
 *
 */
public class IterativeRobot
{
	/**
     * Robot-wide initialization code should go here.
     *
     * Users should override this method for default Robot-wide initialization which will
     * be called when the robot is first powered on.  It will be called exactly 1 time.
     */
    public void robotInit() {
        System.out.println("Default IterativeRobot.robotInit() method... Overload me!");
    }

    /**
     * Initialization code for disabled mode should go here.
     *
     * Users should override this method for initialization code which will be called each time
     * the robot enters disabled mode.
     */
    public void disabledInit() {
        System.out.println("Default IterativeRobot.disabledInit() method... Overload me!");
    }

    /**
     * Initialization code for autonomous mode should go here.
     *
     * Users should override this method for initialization code which will be called each time
     * the robot enters autonomous mode.
     */
    public void autonomousInit() {
        System.out.println("Default IterativeRobot.autonomousInit() method... Overload me!");
    }

    /**
     * Initialization code for teleop mode should go here.
     *
     * Users should override this method for initialization code which will be called each time
     * the robot enters teleop mode.
     */
    public void teleopInit() {
        System.out.println("Default IterativeRobot.teleopInit() method... Overload me!");
    }
    
    private boolean dpFirstRun = true;
    /**
     * Periodic code for disabled mode should go here.
     *
     * Users should override this method for code which will be called periodically at a regular
     * rate while the robot is in disabled mode.
     */
    public void disabledPeriodic() {
        if (dpFirstRun) {
            System.out.println("Default IterativeRobot.disabledPeriodic() method... Overload me!");
            dpFirstRun = false;
        }
    }

    private boolean apFirstRun = true;

    /**
     * Periodic code for autonomous mode should go here.
     *
     * Users should override this method for code which will be called periodically at a regular
     * rate while the robot is in autonomous mode.
     */
    public void autonomousPeriodic() {
        if (apFirstRun) {
            System.out.println("Default IterativeRobot.autonomousPeriodic() method... Overload me!");
            apFirstRun = false;
        }
    }

    private boolean tpFirstRun = true;

    /**
     * Periodic code for teleop mode should go here.
     *
     * Users should override this method for code which will be called periodically at a regular
     * rate while the robot is in teleop mode.
     */
    public void teleopPeriodic() {
        if (tpFirstRun) {
            System.out.println("Default IterativeRobot.teleopPeriodic() method... Overload me!");
            tpFirstRun = false;
        }
    }
    
    private boolean tmpFirstRun = true;
    
    /**
     * Periodic code for test mode should go here
     * 
     * Users should override this method for code which will be called periodically at a regular rate
     * while the robot is in test mode.
     */
    public void testPeriodic() {
        if (tmpFirstRun) {
            System.out.println("Default IterativeRobot.testPeriodic() method... Overload me!");
            tmpFirstRun = false;
        }
    }
}
