package net.sarcommand.swingextensions.event;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Base class for listeners that follow the 'glue' concepts. ListenerGlues follow a component's property when it is
 * changed. <hr/> Copyright 2006-2012 Torsten Heup
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
