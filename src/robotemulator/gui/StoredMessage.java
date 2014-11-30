package robotemulator.gui;

import javax.swing.JLabel;

/**
 * Class to store the hash of a message, its related GUI control, and the stored response
 * @author Jamie
 *
 */

class StoredMessage
{
	public JLabel _statusLabel;
	private long _hash;
	public byte[] _response;
	
	public StoredMessage(byte address, byte[] data, byte[] response)
	{
		
		//create hash from address and data
		_hash = address;
		
		for(int counter = 1; counter < data.length; ++counter)
		{
			_hash = (_hash << 8) | data[counter];
		}
		
		
	}
	
	public long getHash()
	{
		return _hash;
	}
	
	public static long getHash(byte address, byte[] data)
	{
		//create hash from address and data
		long hash = address;
		
		for(int counter = 1; counter < data.length; ++counter)
		{
			hash = (hash << 8) | data[counter];
		}
		
		return hash;
	}
	
}
