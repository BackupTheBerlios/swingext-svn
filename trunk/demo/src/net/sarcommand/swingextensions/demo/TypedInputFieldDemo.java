package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.typedinputfields.DoubleInputField;
import net.sarcommand.swingextensions.typedinputfields.IntegerInputField;
import net.sarcommand.swingextensions.typedinputfields.TypedTextField;
import sun.beans.editors.StringEditor;

import javax.swing.*;
import java.awt.*;

/**
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
public class TypedInputFieldDemo extends DemoClass {
    private TypedTextField _emailField;
    private TypedTextField _lowercaseField;

    public TypedInputFieldDemo() {
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _emailField = new TypedTextField(new StringEditor(), TypedTextField.PATTERN_EMAIL_RFC_2282);
        _lowercaseField = new TypedTextField(new StringEditor(), "[a-z0-9]*");
    }

    protected void initLayout() {
        setLayout(new GridBagLayout());

        add(new JLabel("An input field only accepting whole numbers: "), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(new IntegerInputField(), new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        add(new JLabel("An input field only accepting double values: "), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(new DoubleInputField(), new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        add(new JLabel("An input field accepting an email address: "), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(_emailField, new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(3, 3, 3, 3), 0, 0));

        add(new JLabel("An input field accepting lowercase characters and numbers: "), new GridBagConstraints(0, 3,
                1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(_lowercaseField, new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        add(Box.createGlue(), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {

    }

    public String getDemoName() {
        return "Typed input fields";
    }
}
