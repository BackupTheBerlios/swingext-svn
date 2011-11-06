package net.sarcommand.swingextensions.internal;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class controls the logging facilities used by the swingExtensions package. You can enable/disable logging or
 * customize the used logging framework according to your needs. By default, logging is disabled. If you enable logging,
 * the library will start logging using the java.util.logging framework, which can be customized just as you would for
 * your own classes.
 * <p/>
 * You can turn on logging programmatically (you should do so in your main method, before any swingext classes are
 * loaded) or using the "-Dswingext.logging.enabled=true" vm switch.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SwingExtLogging {
    public static final String SWINGEXT_LOGGING_ENABLED_PROPERTY = "swingext.logging.enabled";
    public static final String SWINGEXT_LOGGER_CLASS_NAME = "swingext.logger.class.name";
    public static final String DEFAULT_LOGGER_CLASS = SwingExtJavaUtilLogger.class.getName();

    protected static String __loggerClassName;
    protected static boolean __loggingEnabled;

    static {
        setLoggingEnabled(Boolean.getBoolean(SWINGEXT_LOGGING_ENABLED_PROPERTY));
        setLoggerClassName(System.getProperty(SWINGEXT_LOGGER_CLASS_NAME, DEFAULT_LOGGER_CLASS));
    }

    /**
     * Returns whether the swingExtensions library will perform logging.
     *
     * @return whether the swingExtensions library will perform logging.
     */
    public static boolean isLoggingEnabled() {
        return __loggingEnabled;
    }

    /**
     * Turns logging on/off.
     *
     * @param loggingEnabled whether the library should perform logging.
     */
    public static void setLoggingEnabled(final boolean loggingEnabled) {
        __loggingEnabled = loggingEnabled;
    }

    /**
     * Returns the class name of the logger being used. Defaults to DEFAULT_LOGGER_CLASS.
     *
     * @return the class name of the logger being used.
     */
    public static String getLoggerClassName() {
        return __loggerClassName;
    }

    /**
     * Sets the name of the class used for logging.
     *
     * @param loggerClassName the name of the class used for logging.
     */
    public static void setLoggerClassName(final String loggerClassName) {
        if (loggerClassName == null)
            throw new IllegalArgumentException("Parameter 'loggerClassName' must not be null!");

        __loggerClassName = loggerClassName;
    }

    /**
     * Utility method converting an exception into a readable stacktrace.
     *
     * @param message message to which the stack trace will be appended.
     * @param t       throwable which's stack trace should be converted.
     * @return the given message with the throwable's stack trace appended.
     */
    public static String convertMessage(final String message, final Throwable t) {
        if (message == null)
            throw new IllegalArgumentException("Parameter 'message' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");

        final StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        final String stackTrace = writer.toString();
        return message + ": " + stackTrace;
    }

    /**
     * Returns the logger instance for the specified class.
     *
     * @param clazz class for which the logger is requested.
     * @return the logger instance for the specified class.
     */
    public static SwingExtLogger getLogger(final Class clazz) {
        try {
            final SwingExtLogger logger = (SwingExtLogger) Class.forName(__loggerClassName).newInstance();
            logger.init(clazz);
            return logger;
        } catch (Exception e) {
            System.err.println("Could not initialize swingext logging:");
            e.printStackTrace();
            return new SwingExtDummyLogger();
        }
    }
}
