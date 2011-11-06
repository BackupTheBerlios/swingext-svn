package net.sarcommand.swingextensions.menuitemfactory;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * A reflection based implementation of the MenuItemFactory interface. This class will simply access a property of the
 * given value and return a JMenuItem containing its toString() representation.
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
public class ReflectedMenuItemFactory<T> implements MenuItemFactory<T> {
    /**
     * The name of the property to retrieve.
     */
    protected String _propertyName;

    /**
     * Creates a new ReflectedMenuItemFactory. This constructor is inteded to be used by configuration frameworks like
     * spring. Make sure you invoke setPropertyName(String) before using the instance.
     */
    public ReflectedMenuItemFactory() {

    }

    /**
     * Creates a new RecflectedMenuItemFactory, using the given property name to obtain the nested value.
     *
     * @param propertyName name of the property to access.
     */
    public ReflectedMenuItemFactory(final String propertyName) {
        setPropertyName(propertyName);
    }

    /**
     * Returns the name of the property being accessed by this class.
     *
     * @return the name of the property being accessed by this class.
     */
    public String getPropertyName() {
        return _propertyName;
    }

    /**
     * Sets the name of the property being accessed by this class.
     *
     * @param propertyName the name of the property being accessed by this class.
     */
    public void setPropertyName(final String propertyName) {
        _propertyName = propertyName;
    }

    /**
     * This implementation will use reflection to access the nested property on the parameter value and return a new
     * JMenuItem containing this property's toString() value.
     *
     * @param value Value for which this item is created.
     * @return a JMenuItem containing the value's nested property's toString() value.
     */
    public JMenuItem createItem(final T value) {
        if (_propertyName == null || _propertyName.length() == 0)
            throw new RuntimeException(this + " missing a property name");
        final Method m = SwingExtUtil.getGetter(value, _propertyName);
        if (m == null)
            throw new RuntimeException("Could not access getter for property " + _propertyName + " on object " + value);
        try {
            final Object o = m.invoke(value);
            return new JMenuItem(o.toString());
        } catch (Exception e) {
            throw new RuntimeException("Could not access getter for property " + _propertyName + " on object " + value, e);
        }
    }
}
