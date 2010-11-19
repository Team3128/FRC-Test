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

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * A Jaguar speedcontroller emulation for FRC.
 * @author Nick DiRienzo
 * @version 11.18.2010.0
 */
public class Jaguar {

    private double speed;

    private JFrame frame;
    private JLabel jaguarNum;
    private JLabel jaguarSpeed;

    /**
     * Creates a new Jaguar speed controller.
     * @param channel The Digital Sidecar channel it should be connected to.
     */
    public Jaguar(int channel) {
        frame = new JFrame("Jaguar Emulator: " + channel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());
        frame.setSize(300, 50);

        jaguarNum = new JLabel("Jaguar " + channel + ": ");
        frame.add(jaguarNum);

        jaguarSpeed = new JLabel((speed*100) + "%");
        frame.add(jaguarSpeed);

        frame.setVisible(true);
    }

    /**
     * Sets the value of the Jaguar using a value between -1.0 and +1.0.
     * @param speed The speed value of the Jaguar between -1.0 and +1.0.
     */
    public void set(double speed) {
        this.speed = speed;
        jaguarSpeed.setText((int)((speed*100)*10)/10.0 + "%");
    }

    /**
     * Gets the most recent value of the Jaguar.
     * @return The most recent value of the Jaguar from -1.0 and +1.0.
     */
    public double get() {
        return speed;
    }
    
    //add pidWrite method?

}