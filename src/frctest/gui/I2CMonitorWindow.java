package frctest.gui;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import frctest.EmulatorMain;

public class I2CMonitorWindow extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2136047252288098395L;
	private JPanel contentPane;
	
	private HashMap<Long, StoredMessage> storedMessages;
	
	private static I2CMonitorWindow _instance;
	
	public static I2CMonitorWindow instance()
	{
		if(_instance == null)
		{
			_instance = new I2CMonitorWindow();
		}
		return _instance;
	}

	/**
	 * Create the frame.
	 */
	private I2CMonitorWindow()
	{
		storedMessages = new HashMap<Long, StoredMessage>();
		
		setTitle("I\u00B2C Saved Messages Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
        setIconImage(EmulatorMain.appIcon);
        
        setVisible(true);

	}
	
	/**
	 * Looks in the message cache for the given message and returns the response that was set for it if
	 * one was set.  If the message doesn't have a registered response, returns null.
	 * If a response was returned, this flashes the little gui indicator for it.
	 * Note that an empty but registered response is a zero-length array. 
	 * @param address
	 * @param data
	 * @return
	 */
	public byte[] getReponseForMessage(byte address, byte[] data)
	{
		long hash = StoredMessage.getHash(address, data);
		StoredMessage message = storedMessages.get(hash);
		
		if(message == null)
		{
			return null;
		}
		
		//flash the light
		
		return message._response;
	}
	
	public void addMessageToGUI(byte address, byte[] data, byte[] response)
	{
		StoredMessage message = new StoredMessage(address, data, response);		
		boolean isReadMessage = (address & 0b1) > 0;
		
		String responseText;
		
		if(isReadMessage)
		{			
			if(response.length == 0)
			{
				responseText = "none";
			}
			else
			{
				StringBuilder responseBuilder = new StringBuilder("Response: ");

				//assuming little endian
				for(int byteIndex = 0; byteIndex < response.length; ++byteIndex)
				{				
					String result = String.format("%02x", response[byteIndex]);
					
					responseBuilder.append(result);
					responseBuilder.append(" ");
				}
				responseText = responseBuilder.toString();
			}

		}
		else
		{
			responseText = "";
		}
		
		String dataString;
		if(data.length == 0)
		{
			dataString = "none";
		}
		else
		{
			StringBuilder dataBuilder = new StringBuilder();

			//assuming little endian
			for(int byteIndex = 0; byteIndex < data.length; ++byteIndex)
			{				
				String result = String.format("%02x", data[byteIndex]);
				
				dataBuilder.append(result);
				dataBuilder.append(" ");
			}
			
			dataString = dataBuilder.toString();
		}
		
		String messageText = String.format("Device Type: %s, Device Address: %s, Type: %s, Data: %s, %s",
				Integer.toBinaryString(address & 0b11110000),
				Integer.toBinaryString(address & 0b1110),
				isReadMessage ? "read" : "write",
				dataString,
				responseText);
		
		message._statusLabel = new JLabel(messageText);
				
		storedMessages.put(message.getHash(), message);
		
		contentPane.add(message._statusLabel);
		
		//repaint the window
		pack();
		revalidate();
		repaint();
	}

}
