package net.sarcommand.swingextensions.beaneditors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.prefs.Preferences;

/**
 * BeanEditor implementation for String properties. This implementation is backed by a normal JTextField.
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
public class TextFieldEditor extends BeanEditor<String, JTextField> {
    private JTextField _textField;

    public TextFieldEditor(final Object targetBean, final String propertyName) {
        super(targetBean, propertyName);
    }

    public TextFieldEditor(final Object targetBean, final String property, final String prefKey) {
        super(targetBean, property, prefKey);
    }

    public TextFieldEditor(final Object targetBean, final String property, final String prefKey, final Preferences prefs) {
        super(targetBean, property, prefKey, prefs);
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
        super.initialize();
    }

    protected void initComponents() {
        _textField = new JTextField();
    }

    protected void initLayout() {
        setLayout(new GridLayout(1, 1));
        add(_textField);
    }

    protected void setupEventHandlers() {
        _textField.addFocusListener(new FocusAdapter() {
            public void focusLost(final FocusEvent e) {
                setValue(_textField.getText());
            }
        });
    }

    protected void beanValueUpdated() {
        _textField.setText(getValue());
    }

    public Class<String> getValueClass() {
        return String.class;
    }

    public JTextField getEditor() {
        return _textField;
    }
}
