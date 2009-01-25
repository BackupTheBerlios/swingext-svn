package net.sarcommand.swingextensions.treetable;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * todo [heup] add docs
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
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
