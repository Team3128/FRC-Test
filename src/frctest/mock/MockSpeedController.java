package frctest.mock;

import edu.wpi.first.wpilibj.SpeedController;

public class MockSpeedController implements SpeedController
{
	/**
	 * Any functions called to set the motor power are applied to this variable
	 */
	
	public double speed;

	@Override
	public void pidWrite(double output)
	{
		speed = output;
	}

	@Override
	public double get()
	{
		return speed;
	}

	@Override
	public void set(double speed, byte syncGroup)
	{
		this.speed = speed;
	}

	@Override
	public void set(double speed)
	{
		this.speed = speed;
	}

	@Override
	public void disable()
	{
		
	}

}
