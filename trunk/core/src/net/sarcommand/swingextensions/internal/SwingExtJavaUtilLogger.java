package net.sarcommand.swingextensions.internal;

import static net.sarcommand.swingextensions.internal.SwingExtLogging.convertMessage;

import java.util.logging.Logger;

/**
 * An implementation for the SwingExtLogger interface using the java.util.logging framework.
 * <p/>
 * <b>This is an internal class, you should not have to deal with it.</b>
 */
public class SwingExtJavaUtilLogger implements SwingExtLogger {
    protected Logger _delegate;

    public SwingExtJavaUtilLogger() {
    }

    public void init(final Class clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("Parameter 'c' must not be null!");

        _delegate = Logger.getLogger(clazz.getName());
    }

    public void config(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.config(msg);
    }

    public void config(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.config(convertMessage(msg, t));
    }

    public void trace(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.finest(msg);
    }

    public void trace(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.finest(convertMessage(msg, t));
    }

    public void debug(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.fine(msg);
    }

    public void debug(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.fine(convertMessage(msg, t));
    }

    public void error(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.severe(msg);
    }

    public void error(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.severe(convertMessage(msg, t));
    }

    public void fatal(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.severe(msg);
    }

    public void fatal(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.severe(convertMessage(msg, t));
    }

    public void info(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.info(msg);
    }

    public void info(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.info(convertMessage(msg, t));
    }

    public void warn(final String msg) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.warning(msg);
    }

    public void warn(final String msg, final Throwable t) {
        if (msg == null)
            throw new IllegalArgumentException("Parameter 'msg' must not be null!");
        if (t == null)
            throw new IllegalArgumentException("Parameter 't' must not be null!");
        if (SwingExtLogging.isLoggingEnabled())
            _delegate.warning(convertMessage(msg, t));
    }

    public boolean isTraceEnabled() {
        return false;
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public boolean isWarnEnabled() {
        return false;
    }
}
