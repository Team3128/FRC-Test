package frctest.mock;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Mock analog input class.  It returns the voltage set by the voltage class variable.
 * @author Jamie
 *
 */
public class MockAnalogInput extends AnalogInput
{

	/**
	 * Construct mock analog input, providing channel.
	 * @param channel
	 */
	public MockAnalogInput(int channel)
	{
		super(channel);
	}
	
	/**
	 * Construct mock digital input with default channel of 0.
	 * @param channel
	 */
	public MockAnalogInput()
	{
		super(0);
	}

}
