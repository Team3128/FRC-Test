/*
 *  This file is part of frcjcss.
 *
 *  frcjcss is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  frcjcss is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with frcjcss.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Joystick emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.11.2010.3
 */
public class Joystick {

    private double x;
    private double y;
    private int xpos;
    private int ypos;
    private double xOffset;
    private double yOffset;
    private double drift;

    private boolean mouseClicked = false;
    private boolean trigger = false;

    private JFrame frame;
    private Grid grid;

    /**
     * Creates a new Joystick window based on the cartesian coordinate system.
     * @param port The "port" the Joystick is connected to on the Driver Station.
     */
    public Joystick(int port) {
        frame = new JFrame("Joystick Emulator: " + port);
        
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grid = new Grid();
        frame.add(grid, BorderLayout.CENTER);
        
        frame.pack();
        frame.setVisible(true);        
    }
    
    private void createDrift() {
    	//TODO: Add Joystick drift
    }

    /**
     * The X value of the Joystick.
     * @return The X value of the Joystick, ranges from -1.0 to +1.0.
     */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @SuppressWarnings("serial")
    class Grid extends JPanel implements MouseListener, MouseMotionListener {
    	Grid() {
    	addMouseListener(this);
            addMouseMotionListener(this);
    	}
        protected void paintComponent(Graphics g) {
        	//clears graph.
        	g.setColor(Color.white);
        	g.fillRect(0, 0, grid.getWidth(), grid.getHeight());
        	g.setColor(Color.black);
        	
        	//draws x and y axis.
            g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
            g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
            
            //checks if trigger is set and draws a red filled rectangle if it is.
            if (trigger) {
            	g.setColor(Color.red);
            	g.fillRect(xpos-20, ypos-20, 40, 40);
            	g.setColor(Color.black);
            }
            
            //drawing joystick and mouse positions
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            g.drawString("(MouseX: " + xpos + ", MouseY: " + ypos + ")", 0, getHeight()-20);
            g.drawString("(JoystickX: " + (((int)(x*1000))/1000.0) + ", JoystickY: " + (((int)(y*1000))/1000.0) + ")", 0, 20);
            
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
                x = (double)(xpos-getHeight()/2.0)/(getHeight()/2.0);
                y = (double)((getWidth()/2.0)-ypos)/(getWidth()/2.0);
            }
            repaint();
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
        		trigger = !trigger;
        }
        
        public void mouseClicked(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}