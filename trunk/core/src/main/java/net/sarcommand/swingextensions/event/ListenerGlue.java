package net.sarcommand.swingextensions.event;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 */
public abstract class ListenerGlue<T> {
    private JComponent _targetComponent;
    private String _property;
    private PropertyChangeListener _propertyChangeListener;

    protected ListenerGlue() {
    }

    protected void initialize(JComponent targetComponent, String property) {
        _targetComponent = targetComponent;
        _property = property;
        _propertyChangeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (_property.equals(evt.getPropertyName())) {
                    final T previousModel = (T) evt.getOldValue();
                    if (previousModel != null)
                        detachNestedListener(previousModel);

                    final T newModel = (T) evt.getNewValue();
                    if (newModel != null)
                        attachNestedListener(newModel);
                }
            }
        };

        targetComponent.addPropertyChangeListener(_propertyChangeListener);
        attachNestedListener(null);
    }

    public void dispose() {
        _targetComponent.removePropertyChangeListener(_propertyChangeListener);
        detachNestedListener(null);
    }

    protected abstract void attachNestedListener(final T model);

    protected abstract void detachNestedListener(final T model);
}
