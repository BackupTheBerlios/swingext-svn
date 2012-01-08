package net.sarcommand.swingextensions.binding;

import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;
import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

/**
 * Observer class listening for changes along a set Keypath instance. It will delegate any change notification to a
 * PropertyChangeListener, building the property name from the KeypathElements.
 * <p/>
 * <b>Note: This is an internal class. You should not have to deal with it directly</b> <hr/> Copyright 2006-2012
 * Torsten Heup
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
public class KeypathObserver {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(KeypathObserver.class);

    private Object _entryPoint;
    private Keypath _keypath;
    private PropertyChangeListener _changeListener;
    private PropertyChangeListener _listenerDelegate;
    private Object[] _values;
    protected KeypathElement[] _keypathElements;

    protected KeypathObserver(final Object entryPoint, final Keypath keypath,
                              final PropertyChangeListener listenerDelegate) {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");
        if (listenerDelegate == null)
            throw new IllegalArgumentException("Parameter 'listenerDelegate' must not be null!");
        if (keypath == null)
            throw new IllegalArgumentException("Parameter 'keypath' must not be null!");

        _entryPoint = entryPoint;
        _keypath = keypath;
        _listenerDelegate = listenerDelegate;

        _changeListener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                processPropertyChangeEvent(evt);
            }
        };

        _keypathElements = _keypath.resolveElements(_entryPoint);
        _values = _keypath.resolveValues(_entryPoint);

        for (int i = 0; i < _keypathElements.length; i++) {
            if (_values[i] == null)
                break;
            addChangeListener(_keypathElements[i].getProperty(), _values[i]);
        }
    }

    protected void processPropertyChangeEvent(final PropertyChangeEvent evt) {
        if (evt == null)
            throw new IllegalArgumentException("Parameter 'evt' must not be null!");
        final Object source = evt.getSource();
        if (source == null)
            throw new IllegalArgumentException("Parameter 'evt.getSource()' must not be null!");

        int elementIndex = getEventSourceIndex(source, _keypathElements);
        if (elementIndex < 0) {
            __log.error("Could not locate source of property change event " + evt + " on keypath " + _keypath);
            return;
        }

        final Object previousValue = _values[elementIndex + 1];

        removeListenersAfterIndex(_keypathElements, elementIndex);

        _values = _keypath.resolveValues(_entryPoint);

        addListenersAfterIndex(_keypathElements, elementIndex);

        final StringBuilder builder = new StringBuilder(128);
        for (int i = 0; i <= elementIndex; i++) {
            builder.append(_keypathElements[i].getProperty());
            if (i <= elementIndex - 1)
                builder.append('.');
        }

        _listenerDelegate.propertyChange(new PropertyChangeEvent(_entryPoint, builder.toString(), previousValue,
                _values[elementIndex + 1]));
    }

    private int getEventSourceIndex(final Object source, final KeypathElement[] keypathElements) {
        int elementIndex = 0;
        Object runner = _entryPoint;
        KeypathElement element;
        boolean foundSource = false;
        for (; elementIndex < keypathElements.length; elementIndex++) {
            element = keypathElements[elementIndex];
            runner = element.get(runner);

            if (runner == null)
                break;

            if (source.equals(runner)) {
                foundSource = true;
                break;
            }
        }

        if (!foundSource)
            elementIndex = -1;
        return elementIndex + 1;
    }

    private void addListenersAfterIndex(final KeypathElement[] keypathElements, final int elementIndex) {
        for (int i = elementIndex + 1; i < _values.length - 1; i++) {
            final Object value = _values[i];
            if (value == null)
                break;

            addChangeListener(keypathElements[i].getProperty(), value);
        }
    }

    protected void addChangeListener(final String property, final Object value) {
        final Method method = SwingExtUtil.getMethod(value, "addPropertyChangeListener",
                String.class, PropertyChangeListener.class);
        try {
            method.invoke(value, property, _changeListener);
        } catch (Exception e) {
            __log.error("Could not add property change listener to " + value, e);
        }
    }

    private void removeListenersAfterIndex(final KeypathElement[] keypathElements, final int elementIndex) {
        for (int i = elementIndex + 1; i < _values.length - 1; i++) {
            final Object val = _values[i];
            if (val == null)
                break;
            removeChangeListener(keypathElements[i].getProperty(), val);
        }
    }

    private void removeChangeListener(final String property, final Object val) {
        final Method method = SwingExtUtil.getMethod(val, "removePropertyChangeListener",
                String.class, PropertyChangeListener.class);

        try {
            method.invoke(val, property, _changeListener);
        } catch (Exception e) {
            __log.error("Could not remove property change listener from " + val, e);
        }
    }

    public void dispose() {
        for (int i = 0; i < _values.length - 1; i++) {
            if (_values[i] == null)
                break;
            removeChangeListener(_keypathElements[i].getProperty(), _values[i]);
        }
    }
}
