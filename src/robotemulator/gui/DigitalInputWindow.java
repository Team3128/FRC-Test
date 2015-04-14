package robotemulator.gui;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import robotemulator.RobotEmulator;
import edu.wpi.first.wpilibj.DigitalInput;

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
 * This class is used to display the outputs of the currently active digital inputs.
 *
 */
public class DigitalInputWindow
{
	static DigitalInputWindow instance;
	
	JFrame frame;
	
	JPanel panel;
	
	HashMap<Integer, Integer> inputHashToGUIIndexMap;
	
	int nextFreeGuiIndex = 0;
	
	public static DigitalInputWindow instance()
	{
		if(instance == null)
		{
			instance = new DigitalInputWindow();
		}
		
		return instance;
	}
	
	private DigitalInputWindow()
	{
		frame = new JFrame("Digital Inputs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(800, 0);
        frame.setLayout(new BorderLayout());        
        frame.setIconImage(RobotEmulator.appIcon);

        panel = new JPanel();
        frame.add(panel);
        
        inputHashToGUIIndexMap = new HashMap<Integer, Integer>();
        frame.setVisible(true);
        frame.toFront();

	}
	
	/**
	 * Adds an input to the inputs window by and channel number.
	 * @param module
	 * @param channel
	 */
	public void addInput(DigitalInput input, int channel, boolean value)
	{
		inputHashToGUIIndexMap.put(channel, nextFreeGuiIndex);
		JButton button = new JButton(formatInputName(channel, value));
		button.addActionListener(input);
		panel.add(button, nextFreeGuiIndex++);
		frame.pack();		
	}

	public void updateInput(int channel, boolean value)
	{
		int inputIndex = inputHashToGUIIndexMap.get(channel);
		
		synchronized(panel.getTreeLock())
		{
			((JButton)panel.getComponent(inputIndex)).setText(formatInputName(channel, value));
			
		}
	}
	
	private String formatInputName(int channel, boolean isOn)
	{
		return String.format("Input %d: currently %s", channel, isOn ? "on" : "off");
	}
	
}
