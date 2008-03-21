package net.sarcommand.swingextensions.beaneditors;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.util.Arrays;
import java.util.prefs.Preferences;

/**
 * A BeanEditor implementation used to handle passwords. The bean property this editor is wired to has to be of type
 * char[], which is consistent to the underlying JPasswordField.
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
public class PasswordEditor extends BeanEditor<char[], JPasswordField> {
    private JPasswordField _editor;

    private boolean _internalUpdate;

    public PasswordEditor(final Object targetBean, final String property) {
        super(targetBean, property);
        initialize();
    }

    public PasswordEditor(final Object targetBean, final String property, final String prefKey) {
        super(targetBean, property, prefKey);
        initialize();
    }

    public PasswordEditor(final Object targetBean, final String property, final String prefKey, final Preferences prefs) {
        super(targetBean, property, prefKey, prefs);
        initialize();
    }

    protected void initialize() {
        _editor = new JPasswordField();

        setLayout(new GridLayout(1, 1));
        add(_editor);

        setupEventHandlers();
        super.initialize();
    }

    protected void beanValueUpdated() {
        if (_internalUpdate)
            return;
        final char[] pw = getValue();
        if (pw == null)
            _editor.setText("");
        else {
            final char[] echoes = new char[pw.length];
            Arrays.fill(echoes, '*');
            _editor.setText(new String(echoes));
        }
    }

    public JPasswordField getEditor() {
        return _editor;
    }

    public Class<char[]> getValueClass() {
        return char[].class;
    }

    protected void setupEventHandlers() {
        _editor.addCaretListener(new CaretListener() {
            public void caretUpdate(final CaretEvent e) {
                if (!_internalUpdate) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            _internalUpdate = true;
                            setValue(_editor.getPassword());
                            _internalUpdate = false;
                        }
                    });
                }
            }
        });
    }


}
