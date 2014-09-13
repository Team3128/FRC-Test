package robotemulator;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class is used to display the outputs of the currently active relays.
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
        panel = new JPanel();
        frame.add(panel);
        
        inputHashToGUIIndexMap = new HashMap<Integer, Integer>();
        frame.setVisible(true);
        frame.toFront();

	}
	
	/**
	 * Get the unique ID of an input from its module and channel
	 * @param module
	 * @param channel
	 * @return
	 */
	private int hashInput(int module, int channel)
	{
		return module * (channel + 10);
	}
	
	/**
	 * Adds a relay to the relays window by module and channel number.
	 * @param module
	 * @param channel
	 */
	public void addInput(DigitalInput input, int module, int channel, boolean value)
	{
		int hash = hashInput(module, channel);
		inputHashToGUIIndexMap.put(hash, nextFreeGuiIndex);
		JButton button = new JButton(formatInputName(module, channel, value));
		button.addActionListener(input);
		panel.add(button, nextFreeGuiIndex++);
		frame.pack();		
	}

	public void updateInput(int module, int channel, boolean value)
	{
		int inputIndex = inputHashToGUIIndexMap.get(hashInput(module, channel));
		
		synchronized(panel.getTreeLock())
		{
			((JButton)panel.getComponent(inputIndex)).setText(formatInputName(module, channel, value));
			
		}
	}
	
	private String formatInputName(int module, int channel, boolean isOn)
	{
		return String.format("Input (%d, %d): currently %s", module, channel, isOn ? "on" : "off");
	}
	
}
