package frctest.mock;

import java.util.HashMap;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

/**
 * Class that implements the ITable interface and stores values in an internal HashMap.
 * 
 * It is used in the SmartDashboard class as well as for testing purposes.
 * @author Jamie
 *
 */
public class MockTable implements ITable
{
	
	public HashMap<String, Object> table;

	@Override
	public boolean containsKey(String key)
	{
		return table.containsKey(key);
	}

	@Override
	public boolean containsSubTable(String key)
	{
		return table.get(key) instanceof ITable;
	}

	@Override
	public ITable getSubTable(String key)
	{
		Object subTableObject = table.get(key);
		if(subTableObject != null && subTableObject instanceof ITable)
		{
			return (ITable)subTableObject;
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getValue(String key) throws TableKeyNotDefinedException
	{
		return table.get(key);
	}

	@Override
	public void putValue(String key, Object value) throws IllegalArgumentException
	{
		table.put(key, value);
	}

	@Override
	public void retrieveValue(String key, Object externalValue)
	{
		// TODO find out what this does and implement it
	}

	@Override
	public void putNumber(String key, double value)
	{
		table.put(key, Double.valueOf(value));
	}

	@Override
	public double getNumber(String key) throws TableKeyNotDefinedException
	{
		Object doubleObject = table.get(key);
		if(doubleObject != null && doubleObject instanceof ITable)
		{
			return ((Double)doubleObject).doubleValue();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public double getNumber(String key, double defaultValue)
	{
		Object doubleObject = table.get(key);
		if(doubleObject != null && doubleObject instanceof ITable)
		{
			return ((Double)doubleObject).doubleValue();
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public void putString(String key, String value)
	{
		table.put(key, value);
	}

	@Override
	public String getString(String key) throws TableKeyNotDefinedException
	{
		Object stringObject = table.get(key);
		if(stringObject != null && stringObject instanceof ITable)
		{
			return (String)stringObject;
		}
		else
		{
			return null;
		}
	}

	@Override
	public String getString(String key, String defaultValue)
	{
		Object stringObject = table.get(key);
		if(stringObject != null && stringObject instanceof ITable)
		{
			return (String)stringObject;
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public void putBoolean(String key, boolean value)
	{
		table.put(key, Boolean.valueOf(value));
	}

	@Override
	public boolean getBoolean(String key) throws TableKeyNotDefinedException
	{
		Object booleanObject = table.get(key);
		if(booleanObject != null && booleanObject instanceof ITable)
		{
			return ((Boolean)booleanObject).booleanValue();
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue)
	{
		Object booleanObject = table.get(key);
		if(booleanObject != null && booleanObject instanceof ITable)
		{
			return ((Boolean)booleanObject).booleanValue();
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public void addTableListener(ITableListener listener)
	{
		// TODO implement this
	}

	@Override
	public void addTableListener(ITableListener listener,
			boolean immediateNotify)
	{
		// TODO implement this
	}

	@Override
	public void addTableListener(String key, ITableListener listener,
			boolean immediateNotify)
	{
		// TODO implement this

	}

	@Override
	public void addSubTableListener(ITableListener listener)
	{
		// TODO implement this
	}

	@Override
	public void removeTableListener(ITableListener listener)
	{
		// TODO implement this
	}

	@Override
	public void putInt(String key, int value)
	{
		table.put(key, Integer.valueOf(value));
	}

	@Override
	public int getInt(String key) throws TableKeyNotDefinedException
	{
		return getInt(key, 0);
	}

	@Override
	public int getInt(String key, int defaultValue)
			throws TableKeyNotDefinedException
	{
		Object integerValue = table.get(key);
		if(integerValue != null && integerValue instanceof ITable)
		{
			return ((Integer)integerValue).intValue();
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public void putDouble(String key, double value)
	{
		putNumber(key, value);
	}

	@Override
	public double getDouble(String key) throws TableKeyNotDefinedException
	{
		return getNumber(key);
	}

	@Override
	public double getDouble(String key, double defaultValue)
	{
		return getNumber(key, defaultValue);
	}

}
