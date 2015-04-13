/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.smartdashboard;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The {@link SendableChooser} class is a useful tool for presenting a selection
 * of options to the {@link SmartDashboard}.
 *
 * <p>For instance, you may wish to be able to select between multiple
 * autonomous modes. You can do this by putting every possible {@link Command}
 * you want to run as an autonomous into a {@link SendableChooser} and then put
 * it into the {@link SmartDashboard} to have a list of options appear on the
 * laptop. Once autonomous starts, simply ask the {@link SendableChooser} what
 * the selected value is.</p>
 *
 * @author Joe Grinstead
 */
public class SendableChooser<T> implements Sendable {

    /**
     * The key for the default value
     */
    private String defaultChoice;
    /**
     * The key for the selected option
     */
    private String selectedChoice;
    
    /**
     * A table linking strings to the objects they represent
     */
    private HashMap<String, T> values = new HashMap<String, T>();

    /**
     * Instantiates a {@link SendableChooser}.
     */
    public SendableChooser() {
    }

    /**
     * Adds the given object to the list of options. On the
     * {@link SmartDashboard} on the desktop, the object will appear as the
     * given name.
     *
     * @param name the name of the option
     * @param object the option
     */
    public void addObject(String name, T object) 
    {
        //if we don't have a default, set the default automatically
        if (defaultChoice == null) {
            addDefault(name, object);
            return;
        }
        values.put(name, object);
    }

    /**
     * Add the given object to the list of options and marks it as the default.
     * Functionally, this is very close to
     * {@link SendableChooser#addObject(java.lang.String, java.lang.Object) addObject(...)}
     * except that it will use this as the default option if none other is
     * explicitly selected.
     *
     * @param name the name of the option
     * @param object the option
     */
    public void addDefault(String name, T object)
    {
        if (name == null)
        {
            throw new NullPointerException("Name cannot be null");
        }
        defaultChoice = name;
        addObject(name, object);
    }

    /**
     * Returns the selected option. If there is none selected, it will return
     * the default. If there is none selected and no default, then it will
     * return {@code null}.
     *
     * @return the option selected
     */
    public Object getSelected() {
       //TODO implement this with a GUI
    	return values.get(selectedChoice);
    }

    public String getSmartDashboardType() 
    {
        return "String Chooser";
    }
    private ITable table;

    public void initTable(ITable table) {

    }

    /**
     * {@inheritDoc}
     */
    public ITable getTable() {
        return table;
    }
}
