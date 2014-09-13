package robotemulator;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import robotemulator.Relay.Direction;
import robotemulator.Relay.Value;

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
 * This class is used to display the outputs of the currently active relays.
 *
 */
public class RelayWindow
{
	static RelayWindow instance;
	
	JFrame frame;
	
	JPanel panel;
	
	HashMap<Integer, Integer> relayHashToGUIIndexMap;
	
	//images for relay state
	ImageIcon relayForwardNoReverseNo;
	ImageIcon relayForwardNoReverseYes;
	ImageIcon relayForwardYesReverseNo;
	ImageIcon relayForwardYesReverseYes;
	
	int nextFreeGuiIndex = 0;
	
	public static RelayWindow instance()
	{
		if(instance == null)
		{
			instance = new RelayWindow();
		}
		
		return instance;
	}
	
	private RelayWindow()
	{
		frame = new JFrame("Relays");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(800, 0);
        frame.setLayout(new BorderLayout());
        //frame.setPreferredSize(new Dimension(200, 50));
        
        panel = new JPanel();
        frame.add(panel);
        
        relayHashToGUIIndexMap = new HashMap<Integer, Integer>();
        frame.setVisible(true);
        frame.toFront();
        
        relayForwardNoReverseNo = new ImageIcon(getClass().getClassLoader().getResource("images/relayForwardNoReverseNo.png"));
        relayForwardNoReverseYes = new ImageIcon(getClass().getClassLoader().getResource("images/relayForwardNoReverseYes.png"));

        relayForwardYesReverseNo = new ImageIcon(getClass().getClassLoader().getResource("images/relayForwardYesReverseNo.png"));
        relayForwardYesReverseYes = new ImageIcon(getClass().getClassLoader().getResource("images/relayForwardYesReverseYes.png"));

	}
	
	/**
	 * Get the unique ID of a relay from its module and channel
	 * @param module
	 * @param channel
	 * @return
	 */
	private int hashRelay(int module, int channel)
	{
		return module * (channel + 10);
	}
	
	/**
	 * Adds a relay to the relays window by module and channel number.
	 * @param module
	 * @param channel
	 */
	public void addRelay(int module, int channel, Relay.Value value, Relay.Direction direction)
	{
		int hash = hashRelay(module, channel);
		relayHashToGUIIndexMap.put(hash, nextFreeGuiIndex);
		panel.add(new JLabel(formatRelayName(module, channel), getImageFromValue(value, direction), JLabel.CENTER), nextFreeGuiIndex++);
		frame.pack();		
	}
	
	/**
	 * Returns which arrows image to use for the given relay value and direction
	 * @param value
	 * @param direction
	 * @return
	 */
	private ImageIcon getImageFromValue(Value value, Relay.Direction direction)
	{
		//this logic taken from WPILib
        switch(value)
        {
            case kOff:
                return relayForwardNoReverseNo;
            case kOn:
                return relayForwardYesReverseYes;
            case kForward:
                if (direction == Direction.kReverse)
                    throw new RuntimeException("A relay configured for reverse cannot be set to forward");
                return relayForwardYesReverseNo;
            case kReverse:
                if (direction == Direction.kForward)
                    throw new RuntimeException("A relay configured for forward cannot be set to reverse");
                return relayForwardNoReverseYes;
            default:
            //Cannot hit this, limited by Value enum
        }
		return null;
	}

	public void updateRelay(int module, int channel, Relay.Value value, Relay.Direction direction)
	{
		int relayIndex = relayHashToGUIIndexMap.get(hashRelay(module, channel));
		
		synchronized(panel.getTreeLock())
		{
			((JLabel)panel.getComponent(relayIndex)).setIcon(getImageFromValue(value, direction));;
			
		}
	}
	
	private String formatRelayName(int module, int channel)
	{
		return String.format("Relay (%d, %d)", module, channel);
	}
	
}
