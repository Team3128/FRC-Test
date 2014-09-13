package robotemulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DigitalInput implements ActionListener
{
	
	int module;
	boolean state;
	int m_channel;
	
	/**
     * Create an instance of a Digital Input class.
     * Creates a digital input given an channel and module.
     * @param moduleNumber The number of the digital module to use for this input 
     * @param channel the port for the digital input
     */
    public DigitalInput(int moduleNumber, int channel)
    {
        m_channel = channel;
        module = moduleNumber;
        state = false;
        DigitalInputWindow.instance().addInput(this, moduleNumber, channel, state);
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
		DigitalInputWindow.instance().updateInput(module, m_channel, state);
	}
}
