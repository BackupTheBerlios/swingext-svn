package net.sarcommand.swingextensions.binding;

import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;
import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import java.beans.PropertyChangeListener;
import static java.lang.String.format;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class represents one element within a key path. It provides a suitable accessor for a property, either using a
 * field or a method, whatever is better suiting.
 * <p/>
 * <b>This is an internal class. You should never have to deal with it directly</b>
 * <p/>
 * <i>This class is unit-tested in net.sarcommand.swingextensions.test.binding.KeypathTest#testKeypathElement</i>
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
public class KeypathElement {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(KeypathElement.class);

    private Class _entryClass;
    private Class _valueClass;
    private String _property;
    private Method _getMethod;
    private Method _setMethod;
    private Field _getField;
    private Field _setField;
    private boolean _ignoreAccessControl;

    protected KeypathElement() {
    }

    public KeypathElement(final Class entryClass, final String property,
                          final boolean ignoreAccessControl) throws MalformedKeypathException {
        initialize(entryClass, property, ignoreAccessControl);
    }

    protected void initialize(final Class clazz, final String property,
                              final boolean ignoreAccessControl) throws MalformedKeypathException {
        if (clazz == null)
            throw new IllegalArgumentException("Parameter 'clazz' must not be null!");
        if (property == null)
            throw new IllegalArgumentException("Parameter 'property' must not be null!");
        if (property.length() == 0)
            throw new IllegalArgumentException("Parameter 'property' has length 0");

        _entryClass = clazz;
        _property = property;
        _ignoreAccessControl = ignoreAccessControl;

        lookUpGetter();
        lookUpSetter();

        if (_getMethod != null) {
            __log.config(format("Resolved getter for property %s or class %s as method %s",
                    _property, clazz, _getMethod.getName()));
        } else if (_getField != null) {
            __log.config(format("Resolved getter for property %s or class %s as field %s",
                    _property, clazz, _getField.getName()));
        } else
            throw new MalformedKeypathException(format("Failed to obtain a suitable getter for property " +
                    "%s of class %s", _property, clazz));

        if (_setMethod != null) {
            __log.config(format("Resolved setter for property %s or class %s as method %s",
                    _property, clazz, _setMethod.getName()));
        } else if (_setField != null) {
            __log.config(format("Resolved getter for property %s or class %s as field %s",
                    _property, clazz, _setField.getName()));
        } else
            __log.info(format("Failed to obtain a suitable setter for property " +
                    "%s of class %s, bindings will be read-only.", _property, clazz));
    }

    protected void lookUpSetter() {
        final Method setMethod = SwingExtUtil.getSetter(_entryClass, _property);
        final Field setField = SwingExtUtil.getField(_entryClass, _property);
        if (setMethod != null && setField != null) {
            if (Modifier.isPublic(setMethod.getModifiers()))
                _setMethod = setMethod;
            else if (Modifier.isPublic(setField.getModifiers()))
                _setField = setField;
            else if (Modifier.isProtected(setMethod.getModifiers()))
                _setMethod = setMethod;
            else if (Modifier.isProtected(setField.getModifiers()))
                _setField = setField;
            else if (Modifier.isPrivate(setMethod.getModifiers()))
                _setMethod = setMethod;
            else if (Modifier.isPrivate(setField.getModifiers()))
                _setField = setField;
        } else {
            if (setMethod != null)
                _setMethod = setMethod;
            else
                _setField = setField;
        }

        if (_setMethod != null) {
            if (!_setMethod.isAccessible() && _ignoreAccessControl)
                _setMethod.setAccessible(true);
            if (!_setMethod.isAccessible())
                _setMethod = null;
        } else if (_setField != null) {
            if (!_setField.isAccessible() && _ignoreAccessControl)
                _setField.setAccessible(true);
            if (!_setField.isAccessible())
                _setField = null;
        }
    }

    protected void lookUpGetter() {
        final Method getMethod = SwingExtUtil.getGetter(_entryClass, _property);
        final Field getField = SwingExtUtil.getField(_entryClass, _property);
        if (getMethod != null && getField != null) {
            if (Modifier.isPublic(getMethod.getModifiers()))
                _getMethod = getMethod;
            else if (Modifier.isPublic(getField.getModifiers()))
                _getField = getField;
            else if (Modifier.isProtected(getMethod.getModifiers()))
                _getMethod = getMethod;
            else if (Modifier.isProtected(getField.getModifiers()))
                _getField = getField;
            else if (Modifier.isPrivate(getMethod.getModifiers()))
                _getMethod = getMethod;
            else if (Modifier.isPrivate(getField.getModifiers()))
                _getField = getField;
        } else {
            if (getMethod != null)
                _getMethod = getMethod;
            else
                _getField = getField;
        }

        if (_getMethod != null) {
            _valueClass = _getMethod.getReturnType();

            if (_ignoreAccessControl)
                _getMethod.setAccessible(true);

            if (!Modifier.isPublic(_getMethod.getModifiers()) || _ignoreAccessControl)
                _getMethod = null;
        } else if (_getField != null) {
            _valueClass = _getField.getType();

            if (_ignoreAccessControl)
                getField.setAccessible(true);

            if (!Modifier.isPublic(getField.getModifiers()) || _ignoreAccessControl)
                _getField = null;
        }
    }

    public Object get(final Object entryObject) {
        if (_getMethod != null) {
            try {
                return _getMethod.invoke(entryObject);
            } catch (Exception e) {
                throw new KeypathAccessException("Could not access method " + _getMethod.getName() + " on class "
                        + _entryClass.getName(), e);
            }
        } else if (_getField != null) {
            try {
                return _getField.get(entryObject);
            } catch (IllegalAccessException e) {
                throw new KeypathAccessException("Could not access field " + _getField.getName() + " on class "
                        + _entryClass.getName(), e);
            }
        } else
            throw new MalformedKeypathException("Can't invoke get for property " + _property + " on objects of type "
                    + _entryClass);
    }

    public void set(final Object entryObject, final Object value) {
        if (_setMethod != null) {
            if (value == null && _setMethod.getParameterTypes()[0].isPrimitive()) {
                __log.warn("Trying to assign a null value to a primitive type - will use '0' instead");
                try {
                    _setMethod.invoke(entryObject, 0);
                } catch (Exception e) {
                    throw new KeypathAccessException("Could not access method " + _setMethod.getName() + " on class "
                            + _entryClass.getName(), e);
                }
            } else {
                try {
                    _setMethod.invoke(entryObject, value);
                } catch (Exception e) {
                    throw new KeypathAccessException("Could not access method " + _setMethod.getName() + " on class "
                            + _entryClass.getName(), e);
                }
            }
        } else if (_setField != null) {
            if (value == null && _setField.getType().isPrimitive()) {
                __log.warn("Trying to assign a null value to a primitive type - will use '0' instead");
                try {
                    _setField.set(entryObject, 0);
                } catch (Exception e) {
                    throw new KeypathAccessException("Could not access method " + _setMethod.getName() + " on class "
                            + _entryClass.getName(), e);
                }
            } else {
                try {
                    _setField.set(entryObject, value);
                } catch (IllegalAccessException e) {
                    throw new KeypathAccessException("Could not access field " + _setField.getName() + " on class "
                            + _entryClass.getName(), e);
                }
            }
        } else
            throw new MalformedKeypathException("Can't invoke set for property " + _property + " on objects of type "
                    + _entryClass);
    }

    public boolean isObservable() {
        final Method method = SwingExtUtil.getMethod(_entryClass, "addPropertyChangeListener", String.class,
                PropertyChangeListener.class);
        return method != null;
    }

    public Class getValueClass() {
        return _valueClass;
    }

    public String getProperty() {
        return _property;
    }

    public boolean canPerformGet() {
        return _getMethod != null || _getField != null;
    }

    public boolean canPerformSet() {
        return _setMethod != null || _setField != null;
    }

    public Object getAccessorForGet() {
        return _getMethod != null ? _getMethod : _getField;
    }

    public Object getAccessorForSet() {
        return _setMethod != null ? _setMethod : _setField;
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final KeypathElement that = (KeypathElement) o;

        if (_entryClass != null ? !_entryClass.equals(that._entryClass) : that._entryClass != null) return false;
        if (_property != null ? !_property.equals(that._property) : that._property != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (_entryClass != null ? _entryClass.hashCode() : 0);
        result = 31 * result + (_valueClass != null ? _valueClass.hashCode() : 0);
        result = 31 * result + (_property != null ? _property.hashCode() : 0);
        result = 31 * result + (_getMethod != null ? _getMethod.hashCode() : 0);
        result = 31 * result + (_setMethod != null ? _setMethod.hashCode() : 0);
        result = 31 * result + (_getField != null ? _getField.hashCode() : 0);
        result = 31 * result + (_setField != null ? _setField.hashCode() : 0);
        return result;
    }
}
