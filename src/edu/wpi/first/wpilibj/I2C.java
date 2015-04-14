package edu.wpi.first.wpilibj;

import robotemulator.gui.I2CDialog;
import robotemulator.gui.I2CMonitorWindow;
import edu.wpi.first.wpilibj.util.BoundaryException;

public class I2C
{
	public enum Port {kOnboard(0), kMXP(1);
	private int value;

	private Port(int value){
		this.value = value;
	}

	public int getValue(){
		return this.value;
	}
};

	private byte m_deviceAddress;

	 /**
	* Constructor.
	*
	* @param deviceAddress The address of the device on the I2C bus.
	* Bits 7-4 are the device type, 3-1 are the device address, and 0 signifies read or write.
	*/
	public I2C(Port port, byte deviceAddress)
	{
		//TODO add port parameter to window display
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
        
        //if it is a read message, show a dialog asking for a response
        if(dataReceived != null && ((m_deviceAddress & ((byte)0b1)) > 0))
        {
        	//check cached messages
        	byte[] cachedResponse = I2CMonitorWindow.instance().getReponseForMessage(m_deviceAddress, dataToSend);
        	if(cachedResponse != null)
        	{
        		System.arraycopy(cachedResponse, 0, dataReceived, 0, receiveSize);
        	}
        	else
        	{
        		//the message is not in the cache, we'll need to ask the user
		        robotemulator.gui.I2CDialog dialog = new I2CDialog(m_deviceAddress, dataToSend, sendSize);
		        
		        //should block until dialog is closed
		        dialog.setVisible(true);
		        
		        if(dialog.canceled || dialog.responseResult == null)
		        {
		        	return false;
		        }
		        
		        for(int index = 0; index < receiveSize; ++index)
		        {
		        	dataReceived[index] = dialog.responseResult.get(index);
		        }
		        
		        if(dialog.rememberResult)
		        {
		        	I2CMonitorWindow.instance().addMessageToGUI(m_deviceAddress, dataToSend, dataReceived);;
		        }
        	 }
        }
        else
        {
        	//it is a write message, so just show it in the GUI
        	I2CMonitorWindow.instance().addMessageToGUI(m_deviceAddress, dataToSend, dataReceived);
        }

       return true;
    }
    
    /**
     * Attempt to address a device on the I2C bus.
     *
     * This allows you to figure out if there is a device on the I2C bus that
     * responds to the address specified in the constructor.
     *
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public boolean addressOnly() {
        return transaction(null, (byte) 0, null, (byte) 0);
    }
    
    /**
     * Execute a write transaction with the device.
     *
     * Write a single byte to a register on a device and wait until the
     *   transaction is complete.
     *
     * @param registerAddress The address of the register on the device to be written.
     * @param data The byte to write to the register on the device.
     */
    public synchronized boolean write(int registerAddress, int data) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) registerAddress;
        buffer[1] = (byte) data;
        return transaction(buffer, buffer.length, null, 0);
    }
    
    /**
     * Execute a read transaction with the device.
     *
     * Read 1 to 7 bytes from a device.
     * Most I2C devices will auto-increment the register pointer internally
     *   allowing you to read up to 7 consecutive registers on a device in a
     *   single transaction.
     *
     * @param registerAddress The register to read first in the transaction.
     * @param count The number of bytes to read in the transaction. [1..7]
     * @param buffer A pointer to the array of bytes to store the data read from the device.
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public boolean read(int registerAddress, int count, byte[] buffer) {
        BoundaryException.assertWithinBounds(count, 1, 7);
        if (buffer == null) {
            throw new NullPointerException("Null return buffer was given");
        }
        byte[] registerAddressArray = {(byte) registerAddress};

        return transaction(registerAddressArray, registerAddressArray.length, buffer, count);
    }
    
    /**
     * SetCompatabilityMode
     *
     * Enables bitwise clock skewing detection.  This will reduce the I2C interface speed,
     * but will allow you to communicate with devices that skew the clock at abnormal times.
     *
     * @param enable Enable compatability mode for this sensor or not.
     */
    public void setCompatabilityMode(boolean enable) {
            //do nothing
    }

}
