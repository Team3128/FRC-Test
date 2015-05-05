package frctest.mock;

import edu.wpi.first.wpilibj.Encoder;

public class MockEncoder extends Encoder
{
	/**
	 * Construct mock encoder with the provided channels.
	 * @param channel
	 */
	public MockEncoder(int aChannel, int bChannel)
	{
		super(aChannel, bChannel);
	}
	
	/**
	 * Construct mock encoder with default channels of 0 and 1.
	 * @param channel
	 */
	public MockEncoder()
	{
		super(0, 1);
	}

}
