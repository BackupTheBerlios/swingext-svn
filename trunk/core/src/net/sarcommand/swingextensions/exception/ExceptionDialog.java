package net.sarcommand.swingextensions.exception;

import static net.sarcommand.swingextensions.resources.SwingExtResources.getActionResource;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ExceptionDialog {
    public static final String ACTIONS_PROPERTY = "actionsProperty";
    public static final String EXCEPTION_PROPERTY = "exceptionsProperty";
    public static final String MESSAGE_PROPERTY = "messageProperty";

    public static final String OPTION_CONTINUE = "optionContinue";
    public static final String OPTION_SHUTDOWN = "optionShutdown";

    protected static final String RESOURCE_ACTION_CONTINUE = "ExceptionDialog.actionContinue";
    protected static final String RESOURCE_ACTION_SHUTDOWN = "ExceptionDialog.actionShutdown";

    private JDialog _dialog;
    private ExceptionDialogPane _pane;

    private Action _actionContinue;
    private Action _actionShutdown;

    private Action[] _actions;
    private PropertyChangeSupport _changeSupport;

    private Throwable _exception;
    private String _message;

    private Object _returnValue;

    public ExceptionDialog() {
        initialize();
    }

    private void initialize() {
        _changeSupport = new PropertyChangeSupport(this);

        _actionContinue = getActionResource(RESOURCE_ACTION_CONTINUE, this, "handleContinue");
        _actionShutdown = getActionResource(RESOURCE_ACTION_SHUTDOWN, this, "handleShutdown");

        setActions(_actionShutdown, _actionContinue);

        _pane = new ExceptionDialogPane(this);
        _dialog = new JDialog();
        _dialog.setContentPane(_pane);
        _dialog.setModal(true);
    }

    public Action getActionContinue() {
        return _actionContinue;
    }

    public Action getActionShutdown() {
        return _actionShutdown;
    }

    public void setActions(final Action... actions) {
        if (actions == null)
            throw new IllegalArgumentException("Parameter 'actions' must not be null!");

        final Action[] previous = _actions;
        _actions = actions;
        _changeSupport.firePropertyChange(ACTIONS_PROPERTY, previous, actions);
    }

    public Action[] getActions() {
        return _actions;
    }

    public Object display(final Component owner, final String message) {
        setMessage(message);
        display(owner);
        return _returnValue;
    }

    public Object display(final Component owner) {
        _dialog.pack();
        _dialog.setSize(700, _dialog.getHeight());
        _dialog.setLocationRelativeTo(owner);
        _dialog.setVisible(true);

        return _returnValue;
    }

    public Object display(final Component owner, final Throwable exception) {
        setException(exception);
        setMessage(null);
        display(owner);
        return _returnValue;
    }

    public Object display(final Component owner, final String message, final Throwable exception) {
        setException(exception);
        setMessage(message);
        display(owner);
        return _returnValue;
    }

    public void handleContinue() {
        _dialog.setVisible(false);
    }

    public void handleShutdown() {
        System.exit(0);
    }


    public void setReturnValue(final String retVal) {

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public Throwable getException() {
        return _exception;
    }

    public void setException(final Throwable exception) {
        final Throwable previous = _exception;
        _exception = exception;
        _changeSupport.firePropertyChange(EXCEPTION_PROPERTY, previous, exception);
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        final String previous = _message;
        _message = message;
        _changeSupport.firePropertyChange(MESSAGE_PROPERTY, previous, message);
    }

    public void setSize(Dimension d) {
        _dialog.setSize(d);
    }

    public void setTitle(String title) {
        _dialog.setTitle(title);
    }
}
