package net.sarcommand.swingextensions.binding;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
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
public abstract class SwingBinding {
    private PropertyChangeListener _listener;

    protected SwingBinding() {
        _listener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                targetBeanChanged();
            }
        };
    }

    protected PropertyChangeListener getPropertyChangeListener() {
        return _listener;
    }

    protected void dispatchOnEDT(final Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread())
            runnable.run();
        else
            SwingUtilities.invokeLater(runnable);
    }

    protected void setClientPropertyOnTarget(final JComponent target) {
        final SwingBinding previous = (SwingBinding) target.getClientProperty(getClientProperty());
        if (previous != null)
            previous.detach();
        target.putClientProperty(getClientProperty(), this);
    }

    protected abstract String getClientProperty();

    protected abstract void targetBeanChanged();

    public abstract void componentChanged();

    public abstract void detach();
}
