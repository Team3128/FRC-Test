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
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyListener;

/**
 * Joystick emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.11.2010.4
 */
public class Joystick {

    private double x, y, z;
    private int xpos, ypos, zpos;
    private double xOffset, yOffset;
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
        frame.setPreferredSize(new Dimension(500, 600));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grid = new Grid();
        frame.add(grid, BorderLayout.CENTER);
        
        frame.pack();
        frame.setVisible(true);        
    }
    
    private void createDrift() {}

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
    
    public double getZ() {
        return z;
    }

    @SuppressWarnings("serial")
    class Grid extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    	Grid() {
    		addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
    	}
        protected void paintComponent(Graphics g) {
        	g.setFont(new Font("Helvetica", Font.BOLD, 14));
        	
        	//clears graph.
        	g.setColor(Color.white);
        	g.fillRect(0, 0, grid.getWidth(), grid.getHeight());
        	g.setColor(Color.black);
        	
        	//checks if trigger is set and draws a red filled rectangle if it is.
            if (trigger) {
            	g.setColor(Color.red);
            	g.fillRect(xpos-20, ypos-20, 40, 40);
            	g.setColor(Color.black);
            }
        	
        	//draws x and y axis and bottom border of grid.
            g.drawLine(0, 500/2, getWidth(), 500/2);
            g.drawLine(getWidth()/2, 0, getWidth()/2, 500);
            g.drawLine(0, 500, getWidth(), 500);
            
            //draws z axis
            g.drawLine(20, 550, 480, 550);
            g.drawLine(20, 525, 20, 575);
            g.drawLine(480, 525, 480, 575);
            
            //draws zpos
            g.setColor(Color.red);
            g.drawLine(20+(int)zpos, 525, 20+(int)zpos, 575);
            g.setColor(Color.black);
            g.drawString("z = " + round(z, 3), 225, 525);
            
            //drawing joystick and mouse positions
            g.drawString("Mouse: (" + xpos + ", " + ypos + ")", 5, 40);
            g.drawString("Joystick: (" + round(x,3) + ", " + round(y, 3) + ")", 5, 20);
            g.drawString("Trigger is " + (trigger?"on.":"off."), 5, 60);
            g.drawString("Joystick is " + (mouseClicked?"":"not ") + "locked", 5, 80);
            
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
                if (ypos > 500)
                	ypos = 500;
                x = (double)(xpos-500/2.0)/(500/2.0);
                y = (double)((getWidth()/2.0)-ypos)/(getWidth()/2.0);
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
        		trigger = !trigger;
        }
        
        public void mouseWheelMoved(MouseWheelEvent e) {
			zpos-=e.getWheelRotation()*10;
			if (zpos < 0)
				zpos = 0;
			else if (zpos > 460)
				zpos = 460;
			z = (double)zpos/460;
			repaint();
		}
        
        public void mouseClicked(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
		public void keyPressed(KeyEvent e) {//TODO
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		
    }
}