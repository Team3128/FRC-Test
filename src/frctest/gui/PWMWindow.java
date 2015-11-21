package frctest.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import frctest.EmulatorMain;

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
 * This class is used to display the outputs of the active PWM channels
 *
 */
public class PWMWindow
{
	static PWMWindow instance;
	
	JFrame frame;
	
	JPanel panel;
	
	HashMap<Integer, JSlider> channelToSliderMap;
	HashMap<Integer, JLabel> channelToLabelMap;

	//images for relay state
	ImageIcon relayForwardNoReverseNo;
	ImageIcon relayForwardNoReverseYes;
	ImageIcon relayForwardYesReverseNo;
	ImageIcon relayForwardYesReverseYes;
	
	int nextFreeGuiIndex = 0;
	
	public static PWMWindow instance()
	{
		if(instance == null)
		{
			instance = new PWMWindow();
		}
		
		return instance;
	}
	
	private PWMWindow()
	{
		frame = new JFrame("Relays");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(800, 0);
        frame.setLayout(new BorderLayout());
        //frame.setPreferredSize(new Dimension(200, 50));
        
        frame.setIconImage(EmulatorMain.appIcon);
        
        panel = new JPanel();
        frame.add(panel);
        
        channelToSliderMap = new HashMap<Integer, JSlider>();
        channelToLabelMap = new HashMap<Integer, JLabel>();

        frame.setVisible(true);
        frame.toFront();
	}
	
	/**
	 * Adds a PWM channel to the PWM window
	 * @param module
	 * @param channel
	 */
	public void addPWM(int channel)
	{
		JPanel pwmPanel = new JPanel();
		JLabel pwmLabel = new JLabel(formatPWMLabel(channel, 0), JLabel.CENTER);
		channelToLabelMap.put(channel, pwmLabel);
		pwmPanel.add(pwmLabel);
		
		JSlider pwmSlider = new JSlider(0, 0x7ff);
		pwmSlider.setValue(0);
		pwmSlider.setEnabled(false);
		pwmSlider.setOrientation(JSlider.VERTICAL);
		channelToSliderMap.put(channel, pwmSlider);
		pwmPanel.add(pwmSlider);
		
		//figure out what index it should go in.  This is a bit complicated because we want the channels to be in order, so me may have to insert it in the middle.
		ArrayList<Integer> indexList = new ArrayList<Integer>(channelToSliderMap.keySet());
		Collections.sort(indexList);
		int guiIndex = indexList.indexOf(channel);
		
		panel.add(pwmPanel, guiIndex);
		frame.pack();	
				
	}
	
	/**
	 * Constructs a label for a PWM channel showing its number and value.
	 * @param channel
	 * @param value
	 * @return
	 */
	private String formatPWMLabel(int channel, int value)
	{
		return String.format("PWM %d: %04d", channel, value);
	}

	public void updatePWM(int channel, short value)
	{
		JSlider slider = channelToSliderMap.get(channel);
		slider.setValue(value);
		
		JLabel label = channelToLabelMap.get(channel);
		label.setText(formatPWMLabel(channel, value));
	}
	
}
