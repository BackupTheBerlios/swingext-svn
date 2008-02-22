package net.sarcommand.swingextensions.beaneditors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

/**
 * A bean editor implementation for boolean properties, using a JCheckBox as editor widget.
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
public class CheckBoxEditor extends BeanEditor<Boolean, JCheckBox> {
    private JCheckBox _checkBox;

    public CheckBoxEditor(final Object targetBean, final String property) {
        super(targetBean, property);
        initialize();
    }

    public CheckBoxEditor(final Object targetBean, final String property, final String prefKey) {
        super(targetBean, property, prefKey);
        initialize();
    }

    public CheckBoxEditor(final Object targetBean, final String property, final String prefKey, final Preferences prefs) {
        super(targetBean, property, prefKey, prefs);
        initialize();
    }

    protected void initialize() {
        _checkBox = new JCheckBox();
        setLayout(new GridLayout(1, 1));
        add(_checkBox);
        _checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                setValue(_checkBox.isSelected());
            }
        });

        super.initialize();
    }

    protected void beanValueUpdated() {
        _checkBox.setSelected(getValue());
    }

    public Class<Boolean> getValueClass() {
        return Boolean.class;
    }

    public JCheckBox getEditor() {
        return _checkBox;
    }
}