package frctest.mock;

import edu.wpi.first.wpilibj.DigitalInput;

public class MockDigitalInput extends DigitalInput
{
	
	/**
	 * Construct mock digital input, providing channel.
	 * @param channel
	 */
	public MockDigitalInput(int channel)
	{
		super(channel);
	}
	
	/**
	 * Construct mock digital input with default channel of 0.
	 * @param channel
	 */
	public MockDigitalInput()
	{
		super(0);
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
    

}
