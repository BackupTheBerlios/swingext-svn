package net.sarcommand.swingextensions.treetable;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 */
public class ReflectingTreeTableColumn {
    protected String _label;
    protected HashMap<Class, Method> _getterMethods;
    protected HashMap<Class, Method> _setterMethods;

    public ReflectingTreeTableColumn() {
        this(null);
    }

    public ReflectingTreeTableColumn(final String label) {
        _label = label;
        _getterMethods = new HashMap<Class, Method>(4);
        _setterMethods = new HashMap<Class, Method>(4);
    }

    public String getLabel() {
        return _label;
    }

    public void setLabel(final String label) {
        _label = label;
    }

    public void setGetter(final Class clazz, final Method getter) {
        _getterMethods.put(clazz, getter);
    }

    public Method getGetter(final Class clazz) {
        return _getterMethods.get(clazz);
    }

    public void setSetter(final Class clazz, final Method setter) {
        _setterMethods.put(clazz, setter);
    }

    public Method getSetter(final Class clazz) {
        return _setterMethods.get(clazz);
    }
}
