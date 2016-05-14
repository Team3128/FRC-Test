package frctest.mock;

import edu.wpi.first.wpilibj.SpeedController;

public class MockSpeedController implements SpeedController
{
	/**
	 * Any functions called to set the motor power are applied to this variable
	 */
	
	public double speed;
	public boolean enabled;
	public boolean inverted;

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
		set(speed);
	}

	@Override
	public void set(double speed)
	{
		if(inverted)
		{
			speed = -speed;
		}
		
		this.speed = speed;
	}

	@Override
	public void disable()
	{
		enabled = false;
	}

	@Override
	public void setInverted(boolean isInverted)
	{
		inverted = isInverted;
	}

	@Override
	public boolean getInverted()
	{
		return inverted;
	}

	@Override
	public void stopMotor()
	{
		speed = 0;
	}

}
