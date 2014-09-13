package robotemulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
 *
 * @author aubrey
 */
public class Solenoid {
    private JFrame frame;
    private JLabel solenoidstate;
    private boolean state = false;
    
    
    public Solenoid(int channel) {
        frame = new JFrame("Solenoid: " + channel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(800, 0);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(200, 50));
        solenoidstate = new JLabel("State: " + state );
        frame.add(solenoidstate, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void set(boolean statein) {
        state = statein;
        solenoidstate.setText("State: " + state);
        solenoidstate.validate();
        solenoidstate.repaint();
    }
    
    public boolean get() {
        return state;
    }
}
