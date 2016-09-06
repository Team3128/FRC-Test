package frctest.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import edu.wpi.first.wpilibj.SpeedController;

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

/**
 * Handles a graphing environment for the speeds of speed controllers such as Victors and Jaguars.
 * 
 * @author Patrick Jameson
 * @version 11.20.2010.0
 */
public class SpeedGrapher extends Graph implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6388043799343706701L;
	
	private Timer updateTimer;
	
	private final int delayMs;

	private final static int GRAPH_LENGTH_SEC = 10;	
	private final static int POINTS_PER_SEC = 10;
	private final static int LINES_PER_UNIT = 10; //number of gridlines per 1 motor power

	long startTime;
	
	int timerTimesFired = 1;
	
	private SpeedController speedController;
	
	/**
	 * Constructs a SpeedGrapher with desired dimensions.
	 * 
	 * @param _width Desired width of the graph.
	 * @param _height Desired height of the graph.
	 */
	public SpeedGrapher(int width, int height, SpeedController speedController) {
		super(width, height, GRAPH_LENGTH_SEC, POINTS_PER_SEC, LINES_PER_UNIT, height / 2);
		
		this.speedController = speedController;
		
		delayMs = (int)(1000.0 / POINTS_PER_SEC);
		
		updateTimer = new Timer(delayMs, this);
		updateTimer.setRepeats(true);
		updateTimer.start();
		
		startTime = System.currentTimeMillis();
	}

	@Override
	//called when the timer fires
	public void actionPerformed(ActionEvent e)
	{		
		//the graph is scaled by LINES_PER_UNIT because the graph is in 1/LINES_PER_UNIT scale so that the gridlines are correct
		addPoint(speedController.get() * LINES_PER_UNIT);
		
		//correct for timer drift
		long exactTime = System.currentTimeMillis() - startTime;

		long timeOffset = exactTime - (timerTimesFired * delayMs);
		
		int newDelay = delayMs - (int)timeOffset;
				
		if(newDelay < 0)
		{
			newDelay = 0;
		}
		
		updateTimer.setDelay(newDelay);
		++timerTimesFired;
	}
}
