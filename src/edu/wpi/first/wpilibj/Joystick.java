package edu.wpi.first.wpilibj;

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.centralnexus.input.JoystickListener;

import frctest.EmulatorMain;

/**
 * Joystick emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson, Jamie Smith
 * @version 11.11.2010.7
 */
public class Joystick implements KeyListener, JoystickListener {
    
    private final static int JSHEIGHT = 500;//joy stick area height
    private final static int JSWIDTH = 500;//joy stick area width

    private double[] axes;
    
    //index for standard three axes
    private static final int X=0, Y=1, Z=2;
    
    private boolean[] buttons;
    
    //index for trigger
    private static final int TRIGGER = 0;
    
    private int xpos, ypos, zpos;//pixel position. z is 0-460.

    private boolean mouseClicked = false;

    private JFrame frame;
    private Grid grid;
    
    boolean hardJoystickEnabled;
    
    private com.centralnexus.input.Joystick hardwareJoystick;

    /**
     * Creates a new Joystick window based on the Cartesian coordinate system.
     * @param port The port the Joystick is connected to on the Driver Station.
     */
    public Joystick(int port) 
    {
        if(com.centralnexus.input.Joystick.getNumDevices() > port && com.centralnexus.input.Joystick.isPluggedIn(port))
        {
        	try
			{
				hardwareJoystick = com.centralnexus.input.Joystick.createInstance(port);
				hardJoystickEnabled = true;
				System.out.println("[FRC-Test] Hardware joystick found for port " + port);
			}
			catch(IOException e)
			{
				System.err.println("[FRC-Test] Error opening joystick");
				e.printStackTrace();
			}
        }
        
        if(hardJoystickEnabled)
        {
        	axes = new double[hardwareJoystick.getNumAxes()];
        	buttons = new boolean[hardwareJoystick.getNumButtons()];
        }
        else
        {
        	//need to initialize this before the window, otherwise we will get an NPE because the AWT thread
        	//tries to paint the window before the arrays are initialized
        	axes = new double[3];
        	buttons = new boolean[11];
        }
        
    	if(EmulatorMain.enableGUI)
    	{
	        frame = new JFrame("Joystick Emulator: " + port);
	        
	        frame.setLayout(new BorderLayout());
	        frame.setPreferredSize(new Dimension(JSWIDTH, JSHEIGHT+100));
	        frame.setResizable(false);
	        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setIconImage(EmulatorMain.appIcon);
	
	
	        grid = new Grid();
	        frame.add(grid, BorderLayout.CENTER);
	        
	        frame.addKeyListener(this);
	    	        
	        frame.pack();
	        frame.setVisible(true);
    	}
    	
        if(hardJoystickEnabled)
        {
        	//need to do this now so that grid will be initialized
			hardwareJoystick.addJoystickListener(this);
        }
    }
    

    /**
     * The X value of the Joystick.
     * @return The X value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getX() {
        return axes[X];
    }

    /**
     * The Y value of the Joystick.
     * @return The Y value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getY() {
        return axes[Y];
    }
    
    /**
     * The Z value of the Joystick.
     * @return The Z value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getZ() {
        return axes[Z];
    }
    
    /**
     * Gets a value for a controller axis by number
     * 0 indexed
     * @param axisIndex
     * @return The axis value, from -1.0 to 1.0
     */
    public double getRawAxis(int axisIndex) {
        if(axisIndex >= 0 && axisIndex < axes.length)
        {
        	return axes[axisIndex];
        }
        
        return 0;
    }
    
     /**
     * The current state of the trigger on the Joystick.
     * @return True if the trigger is being pressed down, false if not.
     */
    public boolean getTrigger() {
        return buttons[TRIGGER];
    }
    
    public int getPOV(int pov)
    {
    	//TODO not implemented
    	return 0;
    }

    
    /**
     * For the current joystick, return the number of POVs
     */
    public int getPOVCount() {
    	//TODO not implemented
    	return 0;
    }
    
    public float getThrottle()
    {
    	//TODO not implemented
    	return 0;
    }
    
    public float getTwist()
    {
    	//TODO not implemented
    	return 0;
    }
    
    /**
     * For the current joystick, return the number of buttons
     */
    public int getButtonCount() {
    	if(hardJoystickEnabled)
    	{
    		return hardwareJoystick.getNumButtons();
    	}
    	else
    	{
    		return buttons.length;
    	}
    }
    
    /**
     * For the current joystick, return the number of axis
     */
    public int getAxisCount() {
    	if(hardJoystickEnabled)
    	{
    		return hardwareJoystick.getNumAxes();
    	}
    	else
    	{
    		return axes.length;
    	}
    }
    
    /**
     * Get whether the button is pressed.
     * 
     * If a controller is plugged in, button information will come for it.  Otherwise, there will be 10 buttons (index 1-10), each corresponding to a number key.
     * @return True if the provided button is being pressed, false if not.  If the provided index is out of range, returns false.
     */
    public boolean getRawButton(int buttonIndex)
    {
    	
        if(buttonIndex >= 1 && buttonIndex <= axes.length)
        {
        	return buttons[buttonIndex - 1];
        }
        
        return false;
    }

    /**
     * For a given key character, get the corresponding index in the buttons array
     * 
     * If the character is not a number key, returns -1
     * @param keycode
     * @return
     */
    private int getButtonIndexForKeyChar(KeyEvent event)
    {
        int keycode = event.getKeyChar() - 48;
        if (keycode >= 0 && keycode <= 9) {
        	return keycode + 1;
        }
       
        return -1;
    }
    
    public void keyPressed(KeyEvent e) 
    {
        int index = getButtonIndexForKeyChar(e);
        
        if(index > -1)
        {
            buttons[index] = true;
            
            if(grid != null)
            {
            	grid.repaint();
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int index = getButtonIndexForKeyChar(e);
        
        if(index > -1)
        {
            buttons[index] = false;
            
            if(grid != null)
            {
            	grid.repaint();
            }
        }
    }
    public void keyTyped(KeyEvent e) {}
    
	@Override
	public void joystickAxisChanged(com.centralnexus.input.Joystick j)
	{
		for(int index = 0; index < axes.length; ++index)
		{
			//YES, this is a use of the famed for-case antipattern!
			//I need a loop because I need to only do axes below axes.length
			switch(index)
			{
			case 0:
				axes[index] = j.getX();
				break;
			case 1:
				axes[index] = j.getY();
				break;
			case 2:
				axes[index] = j.getZ();
				break;
			case 3:
				axes[index] = j.getR();
				break;
			case 4:
				axes[index] = j.getU();
				break;
			case 5:
				axes[index] = j.getV();
				break;
			}
		}
		
        if(grid != null)
        {
        	grid.repaint();
        }

	}
	
	@Override
	public void joystickButtonChanged(com.centralnexus.input.Joystick j)
	{
		int buttonBitmask = 1;
		int buttonValues = j.getButtons();
		
		for(int counter = 0; counter < buttons.length; ++counter)
		{
			buttons[counter] = (buttonValues & buttonBitmask) > 0;
						
			buttonBitmask *= 2;
		}
		
        if(grid != null)
        {
        	grid.repaint();
        }
	}

    @SuppressWarnings("serial")
    class Grid extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
    	
    	private static final int ZPOS_MAX = 460;
        Grid() 
        {
        	if(!hardJoystickEnabled)
        	{
                addMouseListener(this);
                addMouseMotionListener(this);
                addMouseWheelListener(this);
        	}

        }
        public void paintComponent(Graphics g) {
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            
            //clears graph.
            g.setColor(Color.white);
            g.fillRect(0, 0, grid.getWidth(), grid.getHeight());
            g.setColor(Color.black);
            
            //checks if trigger is set and draws a red filled rectangle if it is.
            if (buttons[TRIGGER]) 
            {
                g.setColor(Color.red);
                g.fillRect(xpos-20, ypos-20, 40, 40);
                g.setColor(Color.black);
            }
            
            //draws x and y axis and bottom border of grid.
            g.drawLine(0, JSHEIGHT/2, getWidth(), JSHEIGHT/2);
            g.drawLine(getWidth()/2, 0, getWidth()/2, JSHEIGHT);
            g.drawLine(0, JSHEIGHT, getWidth(), JSHEIGHT);
            
            //draws z axis
            g.drawLine(20,  JSHEIGHT+50, 480, JSHEIGHT+50);
            g.drawLine(20,  JSHEIGHT+25, 20,  JSHEIGHT+75);
            g.drawLine(480, JSHEIGHT+25, 480, JSHEIGHT+75);
            
            if(hardJoystickEnabled)
            {
            	//set these manually, as the mouse listener won't run if we have a hard joystick
            	xpos = (int) ((axes[X] + 1) * .5 * JSHEIGHT);
            	ypos = (int) ((axes[Y] + 1) * .5 * JSWIDTH);
            	zpos = (int) ((axes[Z] + 1) * .5 * ZPOS_MAX);
            }
            
            //draws zpos
            g.setColor(Color.red);
            g.drawLine(20+(int)zpos, JSHEIGHT+25, 20+(int)zpos, JSHEIGHT+75);
            g.setColor(Color.black);
            g.drawString("z = " + round(axes[Z], 3), 225, JSHEIGHT+25);
            
            //drawing joystick and mouse positions
            if(hardJoystickEnabled)
            {
            	g.drawString("Hardware joystick in use", 5, 40);
            }
            else
            {
            	g.drawString("Mouse: (" + xpos + ", " + ypos + ")", 5, 40);
            }
            
            StringBuilder joystickString = new StringBuilder();
            joystickString.append("Joystick Axes: (");
            
            for(int index = 0; index < axes.length; ++index)
            {
            	joystickString.append(round(axes[index], 3));
        		
        		if(index != axes.length - 1)
        		{
        			joystickString.append(", ");
        		}
            }
            
            joystickString.append(')');
            
            g.drawString(joystickString.toString(), 5, 20);
            g.drawString("Joystick is " + (mouseClicked?"":"not ") + "locked", 5, 60);
            
            StringBuilder buttonsString = new StringBuilder();
            buttonsString.append("Buttons: ");
            
            boolean anyButtonsPressed = false;
            
            for(int index = 0; index < buttons.length; ++index)
            {
            	if(buttons[index])
            	{
            		buttonsString.append(index);
            		buttonsString.append("  ");
            		
            		anyButtonsPressed = true;
            	}
            }
            
            if(!anyButtonsPressed)
            {
            	buttonsString.append("(none)");
            }
            
            g.drawString(buttonsString.toString(), 5, 80);
            
            //box around cursor
            g.drawRect(xpos-20, ypos-20, 40, 40);
            
            //crosshair
            g.drawLine(xpos, ypos-20, xpos, ypos+20);
            g.drawLine(xpos-20, ypos, xpos+20, ypos);
        }
        
        public void determineMousePos(MouseEvent e) {
            if(!mouseClicked) {
                xpos = e.getX();
                ypos = e.getY();
                if (ypos > JSHEIGHT)
                    ypos = JSHEIGHT;
                axes[X] = (double)(xpos-JSHEIGHT/2.0)/(JSHEIGHT/2.0);
                axes[Y] = (double)((getWidth()/2.0)-ypos)/(getWidth()/2.0);
            }
            repaint();
        }
        
        public double round(double preNum, int decPlaces) {
            return (double)Math.round((preNum*Math.pow(10, decPlaces)))/Math.pow(10, decPlaces);
        }

        public void mouseMoved(MouseEvent e) {
            determineMousePos(e);
        }
        
        public void mouseDragged(MouseEvent e) {
            determineMousePos(e);
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 1)
                mouseClicked = !mouseClicked;
            else if (e.getButton() == 3)
                buttons[TRIGGER] = true;
            repaint();
        }
        
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == 3) {
                buttons[TRIGGER] = false;
                repaint();
            }
        }
        
        public void mouseWheelMoved(MouseWheelEvent e) {
            zpos-=e.getWheelRotation()*10;
            if (zpos < 0)
                zpos = 0;
            else if (zpos > ZPOS_MAX)
                zpos = ZPOS_MAX;
            axes[Z] = ((double)zpos/ZPOS_MAX*2)-1;
            repaint();
        }
        
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
        
    }
}