package net.sarcommand.swingextensions.beaneditors;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public abstract class BeanEditor<T, V extends JComponent> extends JComponent {
    private Preferences _preferences;
    private String _preferencesKey;
    private String _property;
    private Object _targetBean;
    private Method _getter;
    private Method _setter;

    protected BeanEditor(final Object targetBean, final String property, final String prefKey,
                         final Preferences prefs) {
        if (targetBean == null)
            throw new IllegalArgumentException("Parameter 'targetBean' must not be null!");
        if (property == null)
            throw new IllegalArgumentException("Parameter 'property' must not be null!");

        _preferences = prefs == null ? Preferences.userNodeForPackage(targetBean.getClass()) : prefs;
        _preferencesKey = prefKey;
        _targetBean = targetBean;
        _property = property;

        initialize();
    }

    protected BeanEditor(final Object targetBean, final String property, final String prefKey) {
        this(targetBean, property, prefKey, null);
    }

    protected BeanEditor(final Object targetBean, final String property) {
        this(targetBean, property, null, null);
    }

    protected void initialize() {
        _getter = SwingExtUtil.getGetter(_targetBean, _property);
        if (_getter == null)
            throw new IllegalArgumentException("Could not find a getter for " + _property + " on bean " + _targetBean);
        _setter = SwingExtUtil.getSetter(_targetBean, _property);
        if (_setter == null)
            throw new IllegalArgumentException("Could not find a setter for " + _property + " on bean " + _targetBean);

        final Method listenerInstallMethod = SwingExtUtil.getMethod(_targetBean, "addPropertyChangeListener", String.class,
                PropertyChangeListener.class);

        if (listenerInstallMethod != null) {
            final PropertyChangeListener listener = new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent evt) {
                    beanValueUpdated();
                }
            };
            try {
                listenerInstallMethod.invoke(_targetBean, _property, listener);
            } catch (Exception e) {
                throw new RuntimeException("Could not install property change listener on bean " + _targetBean);
            }
        }

        if (_preferences != null && _preferencesKey != null)
            restoreFromPreferences();
        else
            beanValueUpdated();
    }

    public T getValue() {
        try {
            return (T) _getter.invoke(_targetBean);
        } catch (Exception e) {
            throw new RuntimeException("Could not access getter " + _getter.getName() + " on bean " + _targetBean);
        }
    }

    public void setValue(final T value) {
        invokeSetter(value);
        if (_preferences != null && _preferencesKey != null)
            storeInPreferences();
        beanValueUpdated();
    }

    private void invokeSetter(final T value) {
        try {
            _setter.invoke(_targetBean, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not access getter " + _getter.getName() + " with value " + value +
                    " on bean " + _targetBean);
        }
    }

    public boolean restoreFromPreferences() {
        Object value = getValue();
        final Class c = getValueClass();
        if (c.equals(String.class))
            value = _preferences.get(_preferencesKey, (String) value);
        else if (c.equals(Integer.class))
            value = _preferences.getInt(_preferencesKey, (Integer) value);
        else if (c.equals(Float.class))
            value = _preferences.getFloat(_preferencesKey, (Float) value);
        else if (c.equals(Double.class))
            value = _preferences.getDouble(_preferencesKey, (Double) value);
        else if (c.equals(Boolean.class))
            value = _preferences.getBoolean(_preferencesKey, (Boolean) value);
        else if (c.equals(Long.class))
            value = _preferences.getLong(_preferencesKey, (Long) value);
        else
            value = convertToValue(_preferences.get(_preferencesKey, convertToString(value)));
        invokeSetter((T) value);
        beanValueUpdated();
        return true;
    }

    public boolean storeInPreferences() {
        final Object value = getValue();
        final Class c = getValueClass();
        if (c.equals(String.class))
            _preferences.put(_preferencesKey, (String) value);
        else if (c.equals(Integer.class))
            _preferences.putInt(_preferencesKey, (Integer) value);
        else if (c.equals(Float.class))
            _preferences.putFloat(_preferencesKey, (Float) value);
        else if (c.equals(Double.class))
            _preferences.putDouble(_preferencesKey, (Double) value);
        else if (c.equals(Boolean.class))
            _preferences.putBoolean(_preferencesKey, (Boolean) value);
        else if (c.equals(Long.class))
            _preferences.putLong(_preferencesKey, (Long) value);
        else
            _preferences.put(_preferencesKey, convertToString(value));
        return true;
    }

    protected String convertToString(final Object value) {
        return null;
    }

    protected Object convertToValue(final String string) {
        return null;
    }

    protected abstract void beanValueUpdated();

    public abstract Class<T> getValueClass();

    public abstract V getEditor();
}
