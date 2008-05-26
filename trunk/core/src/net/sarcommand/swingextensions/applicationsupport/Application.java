package net.sarcommand.swingextensions.applicationsupport;

import java.io.File;
import java.util.*;

/**
 * This class provides a simple set of utilities to help with initializing desktop applications. It offers support for
 * obtaining and processing runtime arguments, managing preferences and getting basic information on the application's
 * environment.
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public abstract class Application {
    /**
     * Returns the appropriate working directory for storing application data. The result of this method is platform
     * dependant: On linux, it will return ~/applicationName, on windows, the working directory will be located in the
     * user's application data folder. For Mac OS systems, the working directory will be placed in the proper location
     * in "Library/Application Support".
     * <p/>
     * This method will also make sure that the working directory exists. When invoked, the directory and all required
     * subfolders will be created.
     *
     * @param applicationName Name of the application, used to determine the working directory.
     * @return the appropriate working directory for storing application data.
     */
    public static File getWorkingDirectory(final String applicationName) {
        final String userHome = System.getProperty("user.home", ".");
        final File workingDirectory;
        switch (SysInfo.getPlatform()) {
            case LINUX:
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case WINDOWS:
                final String applicationData = System.getenv("APPDATA");
                if (applicationData != null)
                    workingDirectory = new File(applicationData, "." + applicationName + '/');
                else
                    workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case MACOS:
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            default:
                return new File(".");
        }
        if (!workingDirectory.exists())
            if (!workingDirectory.mkdirs())
                throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        return workingDirectory;
    }

    /**
     * Caches the working directory.
     */
    protected File _workingDirectory;

    /**
     * Stores the application's name. Since this name is used in the file system and the preferences, it should not
     * contain special characters or control sequences.
     */
    protected String _applicationName;

    /**
     * Stores the application's display name.
     */
    protected String _displayName;

    protected HashMap<String, ApplicationParameter> _parameters;
    protected HashMap<String, ApplicationParameter> _parameterKeyMap;

    /**
     * Creates a new Application instance. It will initialize the application name with the actual implementing class's
     * name.
     */
    public Application() {
        _parameters = new HashMap<String, ApplicationParameter>(4);
        _parameterKeyMap = new HashMap<String, ApplicationParameter>(8);

        String name = getClass().getName();
        if (name.contains("."))
            name = name.substring(name.lastIndexOf('.') + 1);
        setApplicationName(name);
        setDisplayName(name);
    }

    /**
     * Returns the name of this application.
     *
     * @return the name of this application.
     */
    public String getApplicationName() {
        return _applicationName;
    }

    /**
     * Sets the name for this application. If no display name is defined, the name will be used for that purpose as
     * well.
     *
     * @param applicationName the name for this application.
     */
    public void setApplicationName(final String applicationName) {
        _applicationName = applicationName;
        if (getDisplayName() == null)
            setDisplayName(applicationName);
    }

    /**
     * Returns the display name of this application.
     *
     * @return the display name of this application.
     */
    public String getDisplayName() {
        return _displayName;
    }

    /**
     * Sets the display name of this application.
     *
     * @param displayName the display name of this application.
     */
    public void setDisplayName(final String displayName) {
        _displayName = displayName;
    }

    /**
     * Sets the available application parameters.
     *
     * @param parameters the available application parameters.
     */
    public void setParameters(final ApplicationParameter... parameters) {
        _parameterKeyMap.clear();
        _parameters.clear();
        for (ApplicationParameter param : parameters) {
            _parameters.put(param.getName(), param);
            for (String key : param.getKeys()) {
                if (_parameterKeyMap.containsKey(key))
                    throw new RuntimeException("Duplicate parameter key: " + key);
                _parameterKeyMap.put(key, param);
            }
        }
    }

    /**
     * Returns whether the given parameter has been specified.
     *
     * @param name name of the parameter being queried.
     * @return whether the given parameter has been specified.
     */
    public boolean isParameterPresent(final String name) {
        return _parameters.containsKey(name) && _parameters.get(name).isPresent();
    }

    /**
     * Processes the given runtime arguments. All arguments which could not be matched will be returned.
     * <p/>
     * Arguments are expected to either take the form
     * <pre>-<paramName> <paramValue></pre>
     * or
     * <pre>-<paramName>=<paramValue></pre>
     *
     * @param arguments the list of arguments to process.
     * @return the list of unmatched runtime arguments.
     * @throws ParameterValueException If one of the parameter values was illegal.
     */
    public Collection<String> processArguments(final String... arguments) throws ParameterValueException {
        return processArguments(null, arguments);
    }

    /**
     * Processes the given runtime arguments, using the specified properties list as default. All arguments which could
     * not be matched will be returned.
     * <p/>
     * Arguments are expected to either take the form
     * <pre>-<paramName> <paramValue></pre>
     * or
     * <pre>-<paramName>=<paramValue></pre>
     *
     * @param properties a list of default values, may be null.
     * @param arguments  the list of arguments to process.
     * @return the list of unmatched runtime arguments.
     * @throws ParameterValueException If one of the parameter values was illegal.
     */
    public Collection<String> processArguments(final Properties properties,
                                               final String... arguments) throws ParameterValueException {
        final LinkedList<String> unmatchedArguments = new LinkedList<String>();
        if (properties != null) {
            /* Process the property file */
            for (Object o : properties.keySet()) {
                final String s = (String) o;
                final ApplicationParameter param = getParameter(s);
                if (param == null)
                    continue;
                param.setValueAsString(properties.getProperty(s));
            }
        }

        /* Process the runtime arguments */
        for (int i = 0; i < arguments.length; i++) {
            final String s = arguments[i];
            /* Check if this argument should be processed or passed down to the application */
            if (!s.startsWith("-"))
                unmatchedArguments.add(s);
            else {
                final boolean directAssignment = s.contains("=");
                final String key = directAssignment ? s.substring(1, s.indexOf('=')) : s.substring(1);
                final ApplicationParameter parameter = _parameterKeyMap.get(key);
                if (parameter == null) {
                    unmatchedArguments.add(s);
                    continue;
                }

                final String value;
                if (parameter.getType() == ApplicationParameter.Type.BOOLEAN)
                    value = directAssignment ? s.substring(s.indexOf('=') + 1, s.length()) : "true";
                else
                    value = directAssignment ? s.substring(s.indexOf('=') + 1, s.length()) : arguments[++i];

                parameter.setValueAsString(value);
                parameter.setPresent(true);
            }
        }

        return unmatchedArguments;
    }

    /**
     * Returns an unmodifiable collection of the registered parameters.
     *
     * @return an unmodifiable collection of the registered parameters.
     */
    public Collection<ApplicationParameter> getParameters() {
        return Collections.unmodifiableCollection(_parameters.values());
    }

    /**
     * Returns the ApplicationParameter instance for the parameter with the given name.
     *
     * @param name name property of the queried parameter.
     * @return the ApplicationParameter instance with the given name, or null if none was defined.
     */
    public ApplicationParameter getParameter(final String name) {
        if (name == null)
            throw new IllegalArgumentException("ApplicationParameter 'name' must not be null!");

        return _parameters.get(name);
    }

    /**
     * Returns the specified value for a parameter. This is a conveniance method for invoking getParameter(paramName)
     * followed by getValue, and will return null if the parameter is not set or present.
     *
     * @param paramName The name of the parameter to be checked.
     * @return the parameter's value, or null if the parameter is not set or does not exist.
     */
    public Object getParameterValue(final String paramName) {
        final ApplicationParameter param = _parameters.get(paramName);
        return param == null ? null : param.getValue();
    }

    /**
     * Returns a suitable working directory for this application depending on the platform. By default, the working
     * directory will be located in <user.home>/.<applicationName> for windows, solaris and linux system and in
     * <user.home>/Library/Application Support/<applicationName> for Mac OSX.
     *
     * @return a suitable working directory for this application
     */
    public File getWorkingDirectory() {
        if (_workingDirectory == null)
            _workingDirectory = getWorkingDirectory(getApplicationName());
        return _workingDirectory;
    }

    /**
     * Returns the current application version. In order to do so, the main class's implementation version will be
     * checked (this property is specified in the jar manifest as 'implementation.version'. If no implementation version
     * was given, null will be returned.
     *
     * @return the current application version
     */
    public String getVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        return version == null ? null : version;
    }

    /**
     * This method will ensure that all runtime parameters marked 'required' have been specified. Otherwise, a
     * ParameterValueException will be thrown.
     *
     * @throws ParameterValueException if a required parameter is missing
     */
    public void ensureRequiredParametersArePresent() throws ParameterValueException {
        for (ApplicationParameter param : _parameters.values()) {
            if (param.isRequired() && !param.isPresent())
                throw new ParameterValueException(param, null, "Required parameter " + param.getName() + " not set.");
        }
    }

    /**
     * Returns a String explaining the runtime parameter usage. It will take the form
     * <pre>
     * command - description <dataType> [<required>]
     * </pre>
     *
     * @return a String explaining the runtime parameter usage.
     */
    public String getParameterUsageTable() {
        int minLen = 0;

        final Collection<ApplicationParameter> params = getParameters();
        final HashMap<ApplicationParameter, String> verboseKeys =
                new HashMap<ApplicationParameter, String>(params.size());

        for (ApplicationParameter param : params) {
            final String[] keys = param.getKeys();
            final StringBuilder keyString = new StringBuilder(64);
            for (int i = 0; i < keys.length; i++) {
                keyString.append(keys[i]);
                if (i < keys.length - 1)
                    keyString.append(", ");
            }
            verboseKeys.put(param, keyString.toString());
            minLen = Math.max(minLen, keyString.length());
        }

        final StringBuilder builder = new StringBuilder(256);
        for (Iterator<ApplicationParameter> it = params.iterator(); it.hasNext();) {
            final ApplicationParameter param = it.next();
            final String str = verboseKeys.get(param);
            builder.append(str);
            for (int i = str.length(); i < minLen; i++)
                builder.append(' ');
            builder.append(" - ").append(param.getName());
            if (param.isRequired())
                builder.append("[required]");
            builder.append(" (").append(param.getType()).append(')');
            if (it.hasNext())
                builder.append('\n');
        }

        return builder.toString();
    }
}
