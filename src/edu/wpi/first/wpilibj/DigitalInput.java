package edu.wpi.first.wpilibj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import robotemulator.gui.DigitalInputWindow;



public class DigitalInput implements ActionListener
{
	
	boolean state;
	int m_channel;
	
	/**
     * Create an instance of a Digital Input class.
     * Creates a digital input given an channel and module.
     * @param channel the port for the digital input
     */
    public DigitalInput(int channel)
    {
        m_channel = channel;
        state = false;
        robotemulator.gui.DigitalInputWindow.instance().addInput(this, channel, state);
    }
    
    /**
     * Get the value from a digital input channel.
     * Retrieve the value of a single digital input channel from the FPGA.
     * @return the stats of the digital input
     */
    public boolean get() 
    {
        return state;
    }
    
    /**
     * Called by DigitalInputWindow to update state
     */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		state = !state;
		DigitalInputWindow.instance().updateInput(m_channel, state);
	}
}
