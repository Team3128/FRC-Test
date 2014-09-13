
package robotemulator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.team3128.RobotTemplate;



public class RobotEmulator 
{
    static RobotTemplate robot = new RobotTemplate();
            
            
    public static void main(String[] args) {
        //asks which mode, teleop or autonomous
        Object[] options = {"Autonomous", "Teleop"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                        "Run Autonomous or Teleop?",
                        "Robot Emulator",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
        
        robot.robotInit();
        Watchdog.getInstance();
        //runs autonomous and autonomous periodic
        if(n == JOptionPane.YES_OPTION)
        {
        	robot.autonomousInit();
            while (true)
            {
                robot.autonomousPeriodic(); 
            }
            //runs teleop and teleop periodic
        } 
        else if(n == JOptionPane.NO_OPTION)
        {	
        	robot.teleopInit();
            while (true)
            {    
                robot.teleopPeriodic();
            }
        }
    }
}
