package net.sarcommand.swingextensions.applicationsupport;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents an application runtime parameter. A parameter will have a name, a type, and a set of possible
 * keys. For instance, the "verbose" option might be triggered using the key "-v" or "--verbose". The application class
 * is reponsible for processing runtime arguments and assigning values to the correct instances of this class.
 */
public class ApplicationParameter {
    /**
     * Declares the available parameter types.
     */
    public static enum Type {
        STRING, BOOLEAN, INTEGER, DOUBLE, EXISTING_FILE, FILE, EXISTING_FOLDER, FOLDER, URL
    }

    protected boolean _required;
    protected boolean _present;
    protected Type _type;
    protected String[] _keys;
    protected Object _value;
    protected String _name;

    /**
     * Creates a new ApplicationParameter with the given name, type and keys. It will not be flagged 'required'.
     *
     * @param name Name of the parameter.
     * @param type The expected type of this parameter.
     * @param keys the keys used to trigger this parameter.
     */
    public ApplicationParameter(final String name, final Type type, final String... keys) {
        this(name, type, false, keys);
    }

    /**
     * Creates a new ApplicationParameter with the given name, type and keys.
     *
     * @param name     Name of the parameter.
     * @param type     The expected type of this parameter.
     * @param required Whether this parameter is required.
     * @param keys     the keys used to trigger this parameter.
     */
    public ApplicationParameter(final String name, final Type type, final boolean required, final String... keys) {
        if (name == null)
            throw new IllegalArgumentException("ApplicationParameter 'name' must not be null!");
        if (type == null)
            throw new IllegalArgumentException("ApplicationParameter 'type' must not be null!");
        if (keys == null)
            throw new IllegalArgumentException("ApplicationParameter 'keys' must not be null!");
        if (keys.length == 0)
            throw new IllegalArgumentException("ApplicationParameter 'keys' must not be empty!");

        _name = name;
        _keys = keys;
        _type = type;
        _required = required;
        if (type == Type.BOOLEAN)
            _value = false;
    }

    /**
     * Indicates whether this parameter has been set.
     *
     * @param present whether this parameter has been set.
     */
    public void setPresent(final boolean present) {
        _present = present;
    }

    /**
     * Returns whether this parameter has been set.
     *
     * @return whether this parameter has been set.
     */
    public boolean isPresent() {
        return _present;
    }

    /**
     * Returns the type of this parameter.
     *
     * @return the type of this parameter.
     */
    public Type getType() {
        return _type;
    }

    /**
     * Returns the name of this parameter.
     *
     * @return the name of this parameter.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the keys which trigger this parameter.
     *
     * @return the keys which trigger this parameter.
     */
    public String[] getKeys() {
        return _keys;
    }

    /**
     * Returns whether this parameter is required.
     *
     * @return whether this parameter is required.
     */
    public boolean isRequired() {
        return _required;
    }

    /**
     * Sets the value of this parameter, passing the string defined as a runtime argument. This method will handle the
     * required value conversions, and throw a ParameterValueException if the value could not be resolved to the
     * expected type.
     *
     * @param argument the value of this parameter in its string representation
     * @throws ParameterValueException If the given string value could not be converted to a parameter of the expected
     *                                 type
     */
    public void setValueAsString(final String argument) throws ParameterValueException {
        if (argument == null && takesValues())
            throw new ParameterValueException(this, null, "ApplicationParameter " + getName() + " is missing a valid value");

        switch (getType()) {
            case STRING:
                _value = argument;
                break;
            case BOOLEAN:
                _value = argument == null || argument.toLowerCase().equals("true");
                break;
            case INTEGER:
                try {
                    _value = Integer.valueOf(argument);
                } catch (NumberFormatException e) {
                    throw new ParameterValueException(this, argument, "Expected parameter " + getName() + " to be " +
                            "of type integer, argument " + argument + " could not be parsed.");
                }
                break;
            case DOUBLE:
                try {
                    _value = Double.valueOf(argument);
                } catch (NumberFormatException e) {
                    throw new ParameterValueException(this, argument, "Expected parameter " + getName() + " to be " +
                            "of type double, argument " + argument + " could not be parsed.");
                }
                break;
            case FILE:
            case FOLDER:
                _value = new File(argument);
                break;
            case EXISTING_FILE:
                final File file = new File(argument);
                if (!(file.exists() && file.isFile()))
                    throw new ParameterValueException(this, argument, "ApplicationParameter " + getName() + " does not denote " +
                            "an existing file");
                _value = file;
                break;
            case EXISTING_FOLDER:
                final File folder = new File(argument);
                if (!(folder.exists() && folder.isDirectory()))
                    throw new ParameterValueException(this, argument, "ApplicationParameter " + getName() + " does not denote " +
                            "an existing folder");
                _value = folder;
                break;
            case URL:
                try {
                    _value = new URL(argument);
                } catch (MalformedURLException e) {
                    throw new ParameterValueException(this, argument, "The URL argument given for parameter " +
                            getName() + " is malformed.");
                }
                break;
            default:
                throw new RuntimeException("Arguments of type " + getType() + " do not take values");
        }
    }

    /**
     * Returns whether this parameter takes values. All parameter types except for Type.Boolean do.
     *
     * @return whether this parameter takes values.
     */
    public boolean takesValues() {
        return _type != Type.BOOLEAN;
    }

    /**
     * Returns this parameters value.
     *
     * @return this parameters value.
     */
    public Object getValue() {
        return _value;
    }
}
