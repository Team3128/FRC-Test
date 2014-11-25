package edu.wpi.first.wpilibj;

import robotemulator.gui.I2CDialog;
import edu.wpi.first.wpilibj.util.BoundaryException;

public class I2C
{
	
	private byte m_deviceAddress;

	 /**
	* Constructor.
	*
	* @param deviceAddress The address of the device on the I2C bus.
	* Bits 7-4 are the device type, 3-1 are the device address, and 0 signifies read or write.
	*/
	public I2C(byte deviceAddress)
	{
		m_deviceAddress = deviceAddress;
	}
	
	 /**
	* Destructor.
	*/
	public void free() {
	}
	
	/**
     * Generic transaction.
     *
     * This is a lower-level interface to the I2C hardware giving you more control over each transaction.
     *
     * @param dataToSend Buffer of data to send as part of the transaction.
     * @param sendSize Number of bytes to send as part of the transaction. [0..6]
     * @param dataReceived Buffer to read data into.
     * @param receiveSize Number of byted to read from the device. [0..7]
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public synchronized boolean transaction(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize)
    {
        BoundaryException.assertWithinBounds(sendSize, 0, 6);
        BoundaryException.assertWithinBounds(receiveSize, 0, 7);
        
        robotemulator.gui.I2CDialog dialog = new I2CDialog(m_deviceAddress, dataToSend, sendSize);
        
        //should block
        dialog.setVisible(true);
        
        if(dialog.canceled || dialog.responseResult == null)
        {
        	return false;
        }
        
        for(int index = 0; index < receiveSize; ++index)
        {
        	dataReceived[index] = dialog.responseResult.get(index);
        }

       return true;
    }

}
