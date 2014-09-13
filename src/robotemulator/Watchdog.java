package robotemulator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
 * Watchdog timer class.
 * The watchdog timer is designed to keep the robots safe. The idea is that the robot program must
 * constantly "feed" the watchdog otherwise it will shut down all the motor outputs. That way if a
 * program breaks, rather than having the robot continue to operate at the last known speed, the
 * motors will be shut down.
 *
 * This is serious business.  Don't just disable the watchdog.  You can't afford it!
 *
 * http://thedailywtf.com/Articles/_0x2f__0x2f_TODO_0x3a__Uncomment_Later.aspx
 */
public class Watchdog implements Runnable {

    private static Watchdog m_instance;
    
    private long millisecondsLeftUntilExpiration;
    
    private Thread watchdogThread;
    
    private Lock expirationDateLock;

    private double watchdogExpiration = .5;
    
    private boolean isPaused = false;

    /**
     * The Watchdog is born.
     */
    protected Watchdog()
    {           
        expirationDateLock = new ReentrantLock();
        
        millisecondsLeftUntilExpiration = (long) (watchdogExpiration*1000);
        
        watchdogThread = new Thread(this, "Watchdog Thread");
        
        watchdogThread.start();
    }

    /**
     *  Get an instance of the watchdog
     * @return an instance of the watchdog
     */
    public static synchronized Watchdog getInstance()
    {
        if (m_instance == null) {
            m_instance = new Watchdog();
        }
        return m_instance;
    }

    /**
     * Throw the dog a bone.
     *
     * When everything is going well, you feed your dog when you get home.
     * Let's hope you don't drive your car off a bridge on the way home...
     * Your dog won't get fed and he will starve to death.
     *
     * By the way, it's not cool to ask the neighbor (some random task) to
     * feed your dog for you.  He's your responsibility!
     */
    public void feed()
    {
    	expirationDateLock.lock();
    	millisecondsLeftUntilExpiration = (long) (watchdogExpiration * 1000);
    	expirationDateLock.unlock();

    }

    /**
     * Put the watchdog out of its misery.
     *
     * Don't wait for your dying robot to starve when there is a problem.
     * Kill it quickly, cleanly, and humanely.
     */
    public void kill()
    {
    	watchdogThread.interrupt();
    }

    /**
     * Read how long it has been since the watchdog was last fed.
     *
     * @return The number of seconds since last meal.
     */
    public double getTimer()
    {
        return millisecondsLeftUntilExpiration / 1000.0;
    }

    /**
     * Read what the current expiration is.
     *
     * @return The number of seconds before starvation following a meal (watchdog starves if it doesn't eat this often).
     */
    public double getExpiration()
    {
        return watchdogExpiration;
    }

    /**
     * Configure how many seconds your watchdog can be neglected before it starves to death.
     *
     * @param expiration The number of seconds before starvation following a meal (watchdog starves if it doesn't eat this often).
     */
    public void setExpiration(double expiration)
    {
        watchdogExpiration = expiration;
    }

    /**
     * Find out if the watchdog is currently enabled or disabled (mortal or immortal).
     *
     * @return Enabled or disabled.
     */
    public boolean getEnabled()
    {
        return !isPaused;
    }

    /**
     * Enable or disable the watchdog timer.
     *
     * When enabled, you must keep feeding the watchdog timer to
     * keep the watchdog active, and hence the dangerous parts
     * (motor outputs, etc.) can keep functioning.
     * When disabled, the watchdog is immortal and will remain active
     * even without being fed.  It will also ignore any kill commands
     * while disabled.
     *
     * @param enabled Enable or disable the watchdog.
     */
    public void setEnabled(final boolean enabled)
    {
    	expirationDateLock.lock();
    	isPaused = !enabled;
    	expirationDateLock.unlock();
    }

    /**
     * Check in on the watchdog and make sure he's still kicking.
     *
     * This indicates that your watchdog is allowing the system to operate.
     * It is still possible that the network communications is not allowing the
     * system to run, but you can check this to make sure it's not your fault.
     * Check isSystemActive() for overall system status.
     *
     * If the watchdog is disabled, then your watchdog is immortal.
     *
     * @return Is the watchdog still alive?
     */
    public boolean isAlive()
    {
    	return watchdogThread.isAlive();
    }

    /**
     * Check on the overall status of the system.
     *
     * @return Is the system active (i.e. PWM motor outputs, etc. enabled)?
     */
    public boolean isSystemActive()
    {
        return true;
    }

	@Override
	public void run()
	{
		while(millisecondsLeftUntilExpiration > 0)
		{
			try
			{
				expirationDateLock.lock();
				
				if(!isPaused)
				{
					millisecondsLeftUntilExpiration -= 5;
				}
				
				expirationDateLock.unlock();
				
				Thread.sleep(5);
			}
			catch(InterruptedException error)
			{
				break;
			}
			

		}
		
		JOptionPane.showMessageDialog(new JFrame("Watchdog"),
			    "The watchdog died because you didn't feed it.",
			    "FRC Java Emulator",
			    JOptionPane.WARNING_MESSAGE);
	}
}
