package robotemulator;

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

public class Relay
{
    /**
     * This class represents errors in trying to set relay values contradictory
     * to the direction to which the relay is set.
     */
    public class InvalidValueException extends RuntimeException {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1255855583563380724L;

		/**
         * Create a new exception with the given message
         * @param message the message to pass with the exception
         */
        public InvalidValueException(String message) {
            super(message);
        }
    }
    
    /**
     * The state to drive a Relay to.
     */
    public static enum Value 
    {
        /**
         * value: off
         */
    	kOff(0),
        /**
         * value: on for relays with defined direction
         */
        kOn(1),
        /**
         * value: forward
         */
        kForward(2),
        /**
         * value: reverse
         */
        kReverse(3);
        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kOff_val = 0;
        static final int kOn_val = 1;
        static final int kForward_val = 2;
        static final int kReverse_val = 3;
        /**
         * value: off
         */

        private Value(int value) {
            this.value = value;
        }
    }
    
    /**
     * The Direction(s) that a relay is configured to operate in.
     */
    public static class Direction {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kBoth_val = 0;
        static final int kForward_val = 1;
        static final int kReverse_val = 2;
        /**
         * direction: both directions are valid
         */
        public static final Direction kBoth = new Direction(kBoth_val);
        /**
         * direction: Only forward is valid
         */
        public static final Direction kForward = new Direction(kForward_val);
        /**
         * direction: only reverse is valid
         */
        public static final Direction kReverse = new Direction(kReverse_val);

        private Direction(int value) {
            this.value = value;
        }
    }
   
    private Direction m_direction;
    private Value m_value;
    private int m_channel;
    private int m_module;
    
    
    /**
     * Relay constructor given the module and the channel.
     * @param moduleNumber The number of the digital module to use.
     * @param channel The channel number within the module for this relay.
     * @param direction The direction that the Relay object will control.
     */
    public Relay(final int moduleNumber, final int channel, Direction direction) {
        if (direction == null)
            throw new NullPointerException("Null Direction was given");
        m_direction = direction;
        m_channel = channel;
        m_module = moduleNumber;
        initRelay(moduleNumber, channel);
    }

    /**
     * Relay constructor given a channel only where the default digital module is used.
     * @param channel The channel number within the default module for this relay.
     * @param direction The direction that the Relay object will control.
     */
    public Relay(final int channel, Direction direction) {
        if (direction == null)
            throw new NullPointerException("Null Direction was given");
        m_direction = direction;
        m_channel = channel;
        m_module = 1;
        initRelay(1, channel);
    }

    /**
     * Relay constructor given the module and the channel, allowing both directions.
     * @param moduleNumber The number of the digital module to use.
     * @param channel The channel number within the module for this relay.
     */
    public Relay(final int moduleNumber, final int channel) {
        m_direction = Direction.kBoth;
        m_channel = channel;
        m_module = moduleNumber;
        initRelay(moduleNumber, channel);
    }

    /**
     * Relay constructor given a channel only where the default digital module is used,
     * allowing both directions.
     * @param channel The channel number within the default module for this relay.
     */
    public Relay(final int channel) {
        m_direction = Direction.kBoth;
        m_channel = channel;
        m_module = 1;
        initRelay(1, channel);
    }
    
    private void initRelay(final int moduleNumber, int channel)
    {
    	m_value = Value.kOff;
        RelayWindow.instance().addRelay(moduleNumber, channel, m_value, m_direction);
    }
    
    /**
     * Set the relay state.
     *
     * Valid values depend on which directions of the relay are controlled by the object.
     *
     * When set to kBothDirections, the relay can be set to any of the four states:
	 *		0v-0v, 12v-0v, 0v-12v, 12v-12v
     *
     * When set to kForwardOnly or kReverseOnly, you can specify the constant for the
     *    direction or you can simply specify kOff_val and kOn_val.  Using only kOff_val and kOn_val is
     *    recommended.
     *
     * @param value The state to set the relay.
     */
    public void set(Value value)
    {	
    	m_value = value;

        RelayWindow.instance().updateRelay(m_module, m_channel, value, m_direction);
    }
    
    /**
     * Get the Relay State
     * 
     * Gets the current state of the relay.
     * 
     * When set to kForwardOnly or kReverseOnly, value is returned as kOn/kOff not
     * kForward/kReverse (per the recommendation in Set)
     * 
     * @return The current state of the relay as a Relay.Value
     */
    public Value get() {
    	return m_value;
    }

    /**
     * Set the Relay Direction
     *
     * Changes which values the relay can be set to depending on which direction is
     * used
     *
     * Valid inputs are kBothDirections, kForwardOnly, and kReverseOnly
     *
     *@param direction The direction for the relay to operate in
     */
    public void setDirection(Direction direction) {
        if (direction == null)
            throw new NullPointerException("Null Direction was given");
        if (m_direction == direction) {
            return;
        }
        m_direction = direction;
    }

}
