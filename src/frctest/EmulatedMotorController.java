package frctest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.wpi.first.wpilibj.SpeedController;
import frctest.gui.SpeedGrapher;

/**
 * Class to emulate the three non-CAN motor controllers.
 * @author Jamie
 *
 */
public class EmulatedMotorController implements ComponentListener, ActionListener, SpeedController
{

    private double speed;
    private long startTime;
    private boolean isGraphRunning;

    private JFrame frame;
    private JLabel speedLabel;
    private JButton startStop;
    
    private SpeedGrapher graph;

    /**
     * Creates a new speed controller.
     * @param channel the channel it is connected to
     * @param name the capitalized name shown in the title bar, e.g "Talon".
     */
    public EmulatedMotorController(int channel, String name)
    {
        frame = new JFrame(name + " Emulator: channel "  + channel);
        
        frame.setIconImage(EmulatorMain.appIcon);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setLocation(510, 0);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(300, 320));
        
        //tells the current speed of the talon in % above the graph.
        speedLabel = new JLabel("Current Speed: " + (speed*100) + "%");
        frame.add(speedLabel, BorderLayout.NORTH);
        
        //allows user to stop the movement of the graph. button located under the graph.
        startStop = new JButton("Stop Graph");
        startStop.addActionListener(this);
        frame.add(startStop, BorderLayout.SOUTH);
        
        //makes the actual graph.
        graph = new SpeedGrapher(300, 300);
        frame.add(graph, BorderLayout.CENTER);
        
        startTime = 0;
        isGraphRunning = true;
        
        frame.addComponentListener(this);

        frame.pack();
        frame.setVisible(true);
    }

	/**
     * Sets the value of the Talon using a value between -1.0 and +1.0.
     * @param speed The speed value of the Talon between -1.0 and +1.0.
     */
    public void set(double speed) {
    	if (System.currentTimeMillis() - startTime > 35 && isGraphRunning) {
    		graph.appendSpeed(speed);
    		startTime = System.currentTimeMillis();
    	}
        this.speed = speed;
        speedLabel.setText((int)((speed*100)*10)/10.0 + "%");
    }

    /**
     * Gets the most recent value of the Talon.
     * @return The most recent value of the Talon from -1.0 and +1.0.
     */
    public double get() {
        return speed;
    }
    
    //add pidWrite method?
    
	public void componentResized(ComponentEvent e) {
		graph.setGraphSize(frame.getWidth(), frame.getHeight());
		graph.repaint();
	}
    
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startStop) {
			startStop.setText((isGraphRunning ? "Start" : "Stop") + " Graph");
			isGraphRunning = !isGraphRunning;
		}
	}
	
	//extra stuffs
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void pidWrite(double output)
	{
		//TODO make sure this is right
		set(speed + output);
	}

	@Override
	public void set(double speed, byte syncGroup)
	{
		set(speed);
	}

	@Override
	public void disable()
	{
		//TODO implement this
	}
}
