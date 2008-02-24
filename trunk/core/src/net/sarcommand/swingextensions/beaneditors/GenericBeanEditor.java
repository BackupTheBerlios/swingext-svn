package net.sarcommand.swingextensions.beaneditors;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

/**
 * The GenericBeanEditor implementation is a utility class using reflection to provide the advantages of a bean
 * editor for arbitrary properties and editors without forcing you to create a subclass of BeanEditor. In addition
 * to the bean and the property being edited, you also pass the edited value class, an appropriate widget and
 * the corresponding value property of this widget to a constructor. If you were, for instance, to create an editor
 * for a String property using a JTextField (just as the TextFieldEditor does), you can achieve it like this:
 * <pre>
 * final GenericBeanEditor<String, JTextField> myEditor = new GenericBeanEditor<String, JTextField>(
 *      myBean, //the target bean object being edited
 *      "name", //the property you wish to edit
 *      String.class, //the value class, required for casting the value to the appropriate type
 *      new JTextField(), //the widget being used as editor
 *      "text" //the JTextField's value property
 *      );
 * </pre>
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
public class GenericBeanEditor<T, V extends JComponent> extends BeanEditor<T, V> {
    private V _editor;
    private Class<T> _valueClass;
    private String _editorProperty;
    private Method _editorGetter;
    private Method _editorSetter;

    public GenericBeanEditor(Object targetBean, String property, final Class<T> valueClass,
                             final V editor, final String editorProperty) {
        super(targetBean, property);
        _valueClass = valueClass;
        _editor = editor;
        _editorProperty = editorProperty;
    }

    public GenericBeanEditor(Object targetBean, String property, String prefKey,
                             final Class<T> valueClass, final V editor, String editorProperty) {
        super(targetBean, property, prefKey);
        _valueClass = valueClass;
        _editor = editor;
        _editorProperty = editorProperty;
    }

    public GenericBeanEditor(Object targetBean, String property, String prefKey, Preferences prefs,
                             final Class<T> valueClass, final V editor, String editorProperty) {
        super(targetBean, property, prefKey, prefs);
        _editor = editor;
        _editorProperty = editorProperty;
        _valueClass = valueClass;
    }

    protected void initialize() {
        if (_editor == null)
            throw new IllegalArgumentException("Parameter '_editor' must not be null!");
        if (_editorProperty == null)
            throw new IllegalArgumentException("Parameter '_editorProperty' must not be null!");

        _editorGetter = SwingExtUtil.getGetter(_editor, _editorProperty);
        _editorSetter = SwingExtUtil.getSetter(_editor, _editorProperty);

        if (_editorGetter == null)
            throw new IllegalArgumentException("Could not find a suitable getter for property " + _editorProperty +
                    " on " + _editor);
        if (_editorSetter == null)
            throw new IllegalArgumentException("Could not find a suitable setter for property " + _editorProperty +
                    " on " + _editor);

        _editor.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    final T value = (T) _editorGetter.invoke(_editor);
                    setValue(value);
                } catch (Exception e1) {
                    throw new RuntimeException("Could not invoke getter " + _editorGetter.getName() + " on " + _editor, e1);
                }
            }
        });

        setLayout(new GridLayout(1, 1));
        add(_editor);
        super.initialize();
    }

    protected void beanValueUpdated() {
        try {
            _editorSetter.invoke(_editor, getValue());
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke setter " + _editorSetter.getName() + " on " + _editor, e);
        }
    }

    public V getEditor() {
        return _editor;
    }

    public Class<T> getValueClass() {
        return _valueClass;
    }
}
