/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;

/**
 * DigitalSource Interface. The DigitalSource represents all the possible inputs
 * for a counter or a quadrature encoder. The source may be either a digital
 * input or an analog input. If the caller just provides a channel, then a
 * digital input will be constructed and freed when finished for the source. The
 * source can either be a digital input or analog trigger but not both.
 */
public abstract class DigitalSource extends InterruptableSensorBase
{
	protected ByteBuffer m_port;
	protected int m_channel;

	protected void initDigitalPort(int channel, boolean input)
	{

		m_channel = channel;
	}

	public void free() 
	{
		
	}

	/**
	 * Get the channel routing number
	 *
	 * @return channel routing number
	 */
	public int getChannelForRouting() 
	{
		return m_channel;
	}

	/**
	 * Get the module routing number
	 *
	 * @return 0
	 */
	public byte getModuleForRouting() 
	{
		return 0;
	}

	/**
	 * Is this an analog trigger
	 * @return true if this is an analog trigger
	 */
	public boolean getAnalogTriggerForRouting()
	{
		return false;
	}
}
