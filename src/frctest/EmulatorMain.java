
package frctest;

import java.awt.Image;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.reflections.Reflections;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SoftwareTimer;
import edu.wpi.first.wpilibj.Timer;

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

public class EmulatorMain
{	
    
	static RobotBase robot;
	static RobotStateProxy stateProxy;
	
    //if we are running in test mode, we want to be able to instantiate objects without
    //popping up GUI windows everywhere.  This boolean acts as a master switch for these
    //windows.  In emulation mode, it is turned on by main(), which enables the GUI.  
    //main() is not called by junit, so in test mode it stays initialized to false.
    public static boolean enableGUI = false;
    
    //true if the JavaJoystic native library has been loaded and it is safe to create com.centralnexus.input.Joystick classes
    public static boolean hasNativeJoystickLibrary = true;
    
    public static Image appIcon;
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args)
    {

    	enableGUI = true;
    	appIcon = new ImageIcon(EmulatorMain.class.getClassLoader().getResource("images/Icon_attempt3.png")).getImage();
    	
    	//do this stuff in a different thread while the dialog is running because it takes like 3 seconds
    	final ArrayList<Class<? extends RobotBase>> mainClasses = new ArrayList<Class<? extends RobotBase>>();
        Thread robotClassFinder = new Thread(new Runnable()
    	{
			@Override
			public void run()
			{
				try
				{
					Reflections reflections = new Reflections();
					mainClasses.addAll(reflections.getSubTypesOf(RobotBase.class));
				}
				catch(NoClassDefFoundError error)
				{
					JOptionPane.showMessageDialog(new JFrame(),
						"<html>Unfortunately, you've setup the FRC-Test classpath wrong. <br>"
						+ "Make sure that the libraries Reflections, Guava, JavaJoystick, and javassist are available.</html>",
						"Class Loading Error", 
						JOptionPane.ERROR_MESSAGE);
					error.printStackTrace();
					System.exit(1);
				}
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
        
        //may as well start up NetworkTables while we wait
        
        //need to sort out native library issue first
        //NetworkTable.setClientMode();
        //NetworkTable.initialize();
        
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
        	System.out.println("Oops! There aren't any classes that extend RobotBase on the classpath!");
        	return;
        }
        
        //remove the built-in classes
        mainClasses.remove(IterativeRobot.class);
        mainClasses.remove(SampleRobot.class);
        
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
                            "Which robot class to start?",
                            "Robot Emulator",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            classes,
                            classes[0]);
        }
        
        if(mainClassIndex < 0)
        {
        	//user closed dialog box
        	return;
        }
        
        //Try loading the hardware joystick library
        try
        {
        	com.centralnexus.input.Joystick.getNumDevices();
        }
        catch(UnsatisfiedLinkError err)
        {
        	System.out.println("JavaJoystick native library not present for this platform.  Hardware joystick support disabled");
        	hasNativeJoystickLibrary = false;
        }
        
        try
		{
			Class<? extends RobotBase> clazz = (Class<? extends RobotBase>) mainClasses.toArray()[mainClassIndex];
			Constructor<?> constructor = clazz.getConstructor();
			robot = (RobotBase) constructor.newInstance();
		}
        catch (Exception e)
		{
        	System.out.println("Error instantiating the robot main class:");
			e.printStackTrace();
			return;
		}
        
        //initialize WPILib static objects
        
        stateProxy = new RobotStateProxy();
        stateProxy.autonomous = n == JOptionPane.YES_OPTION;
        stateProxy.teleop = n == JOptionPane.NO_OPTION;
        
        RobotState.SetImplementation(stateProxy);
        
        Timer.SetImplementation(new SoftwareTimer());
        
        Thread robotCodeThread = new Thread(() ->
        {
        	 try
             {
             	robot.startCompetition();
             }
             catch(Exception ex)
             {
             	System.err.println("Robot code threw an " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
             	ex.printStackTrace();
             	System.exit(2);
             }
        });
        
        robotCodeThread.setDaemon(true);
        robotCodeThread.start();
        
    }
    
    static class RobotStateProxy implements RobotState.Interface
    {
    	boolean enabled = true;
    	boolean autonomous = false;
    	boolean teleop = false;

		@Override
		public boolean isDisabled()
		{
			return !enabled;
		}

		@Override
		public boolean isEnabled()
		{
			return enabled;
		}

		@Override
		public boolean isOperatorControl()
		{
			return teleop;
		}

		@Override
		public boolean isAutonomous()
		{
			return autonomous;
		}

		@Override
		public boolean isTest()
		{
			return false;
		}
    	
    }
  
}
