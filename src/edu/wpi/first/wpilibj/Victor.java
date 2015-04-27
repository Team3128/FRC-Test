package edu.wpi.first.wpilibj;

import frctest.EmulatedMotorController;

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
 * A Victor speed controller emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.12.2010.3
 */
public class Victor extends EmulatedMotorController
{
    /**
     * Constructor.
     *
     * @param channel The PWM channel that the Victor is attached to. 0-9 are on-board, 10-19 are on the MXP port
     */
    public Victor(final int channel) 
    {
        super(channel, "Victor");
    }

}