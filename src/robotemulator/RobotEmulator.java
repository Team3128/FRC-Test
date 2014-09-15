
package robotemulator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.reflections.Reflections;

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
    static IterativeRobot robot;
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args)
    {
    	//do this stuff in a different thread while the dialog is running because it takes like 3 seconds
    	ArrayList<Class<? extends IterativeRobot>> mainClasses = new ArrayList<Class<? extends IterativeRobot>>();
        Thread robotClassFinder = new Thread(new Runnable()
    	{
			@Override
			public void run()
			{
	    		Reflections reflections = new Reflections();
	    		mainClasses.addAll(reflections.getSubTypesOf(IterativeRobot.class));
			}
    	}, "Robot Main Class Finder Thread");
        
        robotClassFinder.start();
        
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
        
        try
		{
			robotClassFinder.join();
		}
        catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
        
        //init the robot main class
        if(mainClasses.isEmpty())
        {
        	System.out.println("Oops! There aren't any classes that extend IterativeRobot on the classpath!");
        }
        
        //figure out which class to use
        int mainClassIndex = 0;
        if(mainClasses.size() > 1)
        {
            //asks which main class to use
            Object[] classes = new Object[mainClasses.size()];
            
            for(int counter = 0; counter < classes.length; ++counter)
            {
            	classes[counter] = mainClasses.get(counter).getSimpleName();
            }
            
            mainClassIndex = JOptionPane.showOptionDialog(new JFrame(),
                            "Which IterativeRobot class to start?",
                            "Robot Emulator",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            classes,
                            classes[0]);
        }
        
        try
		{
			Class<? extends IterativeRobot> clazz = (Class<? extends IterativeRobot>) mainClasses.toArray()[mainClassIndex];
			Constructor<?> constructor = clazz.getConstructor();
			robot = (IterativeRobot) constructor.newInstance();
		}
        catch (Exception e)
		{
        	System.out.println("Error instantiating the robot main class:");
			e.printStackTrace();
			return;
		}
        
        //register our handler for when the app is closed
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {

			@Override
			public void run()
			{
				robot.disabledInit();
			}
        	
        }));
        
        robot.robotInit();
        Watchdog.getInstance();
        //runs autonomous and autonomous periodic
        if(n == JOptionPane.YES_OPTION)
        {
        	robot.autonomousInit();
            while(true)
            {
                robot.autonomousPeriodic(); 
            }
            //runs teleop and teleop periodic
        } 
        else if(n == JOptionPane.NO_OPTION)
        {	
        	robot.teleopInit();
            while(true)
            {    
                robot.teleopPeriodic();
            }
        }
    }
}
