import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * A Victor emulator for FRC.
 * @author Nick DiRienzo
 * @version 11.12.2010.2
 */
public class Victor {

    private double speed;
    private int channel;

    private JFrame frame;
    private JLabel victorNum;
    private JLabel victorSpeed;

    /**
     * Creates a new Victor speed controller.
     * @param channel The Digital Sidecar channel it should be connected to.
     */
    public Victor(int channel) {
        this.channel = channel;
        frame = new JFrame("Victor Emulator: " + channel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());
        frame.setSize(300, 50);

        victorNum = new JLabel("Victor " + channel + ": ");
        frame.add(victorNum);

        victorSpeed = new JLabel((speed*100) + "%");
        frame.add(victorSpeed);

        frame.setVisible(true);
    }

    /**
     * Sets the value of the Victor using a value between -1.0 and +1.0.
     * @param speed The speed value of the Victor between -1.0 and +1.0.
     */
    public void set(double speed) {
        this.speed = speed;
        victorSpeed.setText((int)((speed*100)*10)/10.0 + "%");
    }

    /**
     * Gets the most recent value of the Victor.
     * @return The most recent value of the Victor from -1.0 and +1.0.
     */
    public double get() {
        return speed;
    }

}