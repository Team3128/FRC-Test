
package robotemulator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.team3128.RobotTemplate;

/*
 *  This file is part of frcjcss.
 *
 *  frcjcss is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  frcjcss is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with frcjcss.  If not, see <http://www.gnu.org/licenses/>.
 */

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
