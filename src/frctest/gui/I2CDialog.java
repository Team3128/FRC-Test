package frctest.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import frctest.EmulatorMain;

public class I2CDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4601377436131559457L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	ButtonGroup radioButtons;

	public boolean canceled;
	
	public boolean rememberResult;
	
	public ArrayList<Byte> responseResult;
	
	final static String padding = "00000000";
	
	JRadioButton rdbtnHex;
	
	JRadioButton rdbtnBin;
	
	JRadioButton rdbtnDec;
	
	JCheckBox chckbxRespondTheSame;
	
	/**
	 * Create the dialog from data about the I2C request.
	 */
	public I2CDialog(int deviceAddressCombined, byte[] sentData, int sentDataLength)
	{
        setIconImage(EmulatorMain.appIcon);

		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setTitle("Emulator: I\u00B2C Message");
		setBounds(100, 100, 450, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JLabel lblNewLabel = new JLabel("A read command was sent by the robot over I\u00B2C.");
			lblNewLabel.setToolTipText("I\u00B2C Message");
		
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel);
		}
		
		//extract data from device address
		byte deviceType = (byte) ((deviceAddressCombined & 0b11110000) >> 4);
		
		byte deviceAddress = (byte) ((deviceAddressCombined & 0b1110) >> 1);
		
		{
			JLabel lblDeviceTypex = new JLabel(String.format("<html>Device type: 0x%x (0b%s)<br>Device address: 0x%x (0b%s)</html>", deviceType, Integer.toString(deviceType, 2), deviceAddress, Integer.toString(deviceAddress, 2)));
			lblDeviceTypex.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblDeviceTypex);
		}
		{
			StringBuilder dataPanelText = new StringBuilder("<html><b>Data:</b><br>");
			StringBuilder binaryLineText = new StringBuilder("<b>Binary:</b> ");
			StringBuilder hexLineText = new StringBuilder("<b>Hex:</b> ");
			
			//assuming little endian
			for(int byteIndex = 0; byteIndex < sentDataLength; ++byteIndex)
			{
				//padding code from http://stackoverflow.com/questions/1901085/convert-number-to-binary-string-with-full-padding
				
				String result = padding + Integer.toBinaryString(sentData[byteIndex]);
				result = result.substring(result.length() - 8, result.length());  // take the right-most 8 digits
				
				binaryLineText.append(result);
				binaryLineText.append(" ");
			}
			
			binaryLineText.append("<br>");
			dataPanelText.append(binaryLineText);
			
			//assuming little endian
			for(int byteIndex = 0; byteIndex < sentDataLength; ++byteIndex)
			{				
				String result = String.format("%02x", sentData[byteIndex]);
				
				hexLineText.append(result);
				hexLineText.append(" ");
			}
			
			hexLineText.append("<br>");
			dataPanelText.append(hexLineText);
			
			
			long dataCombinedValue = 0;
			//assuming little endian
			for(int byteIndex = 0; byteIndex < sentDataLength; ++byteIndex)
			{
				dataCombinedValue = (dataCombinedValue << 8) | sentData[byteIndex];
			}
			
			
			dataPanelText.append("<b>Decimal (combined):</b> ");
			dataPanelText.append(Long.toString(dataCombinedValue));
			dataPanelText.append("</html>");
			
			JLabel lblData = new JLabel(dataPanelText.toString());
			contentPanel.add(lblData);
		}
		{
			JPanel responsePanel = new JPanel();
			contentPanel.add(responsePanel);
			{
				JLabel lblYourResponse = new JLabel("Your response:");
				responsePanel.add(lblYourResponse);
			}
			{
				textField = new JTextField();
				responsePanel.add(textField);
				textField.setColumns(10);
			}
			
			radioButtons = new ButtonGroup();
			
			{
				rdbtnHex = new JRadioButton("Hex", true);
				radioButtons.add(rdbtnHex);
				responsePanel.add(rdbtnHex);
			}
			{
				rdbtnBin = new JRadioButton("Bin");
				radioButtons.add(rdbtnBin);
				responsePanel.add(rdbtnBin);
			}
			{
				rdbtnDec = new JRadioButton("Dec");
				radioButtons.add(rdbtnDec);
				responsePanel.add(rdbtnDec);
			}
		}
		{
			chckbxRespondTheSame = new JCheckBox("Respond the same way next time for this same exact request");
			contentPanel.add(chckbxRespondTheSame);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener()
				{
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						try
						{
							I2CDialog.this.readAndParseValues();
						}
						catch(NumberFormatException error)
						{
							JOptionPane.showMessageDialog(rootPane, "Error: response field invalid!");
							return;
						}
						
						I2CDialog.this.setVisible(false);
						I2CDialog.this.dispatchEvent(new WindowEvent(I2CDialog.this, WindowEvent.WINDOW_CLOSING));
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
				cancelButton.addActionListener(new ActionListener()
				{
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						I2CDialog.this.canceled = true;
						I2CDialog.this.setVisible(false);
						I2CDialog.this.dispatchEvent(new WindowEvent(I2CDialog.this, WindowEvent.WINDOW_CLOSING));
					}
				});
			}
		}
	}

	//called when user hits OK on dialog
	private void readAndParseValues() throws NumberFormatException
	{
		long resultValue;
		
		if(rdbtnHex.isSelected())
		{
			resultValue = Long.parseLong(textField.getText(), 16);
		}
		else if(rdbtnDec.isSelected())
		{
			resultValue = Long.parseLong(textField.getText(), 10);
		}		
		else
		{
			resultValue = Long.parseLong(textField.getText(), 2);
		}
		
		responseResult = new ArrayList<Byte>();
		
		//break the result into byte chunks
		//again, assuming little endian
		//go for 7 bits, or until there's no more data
		for(int bitshift = 0; bitshift <= 56 && (resultValue >> bitshift) >= 1; bitshift += 8)
		{
			responseResult.add((byte) (resultValue >> bitshift));
		}
		
		//handle remember checkbox
		rememberResult = chckbxRespondTheSame.isSelected(); 
		
	}

}
