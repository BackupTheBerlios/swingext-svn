package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.filechooser.SimpleAccessoryManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo for the different AccessoryManager implementations.
 * <p/>
 * <hr/>
 * Copyright 2006 Torsten Heup
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
public class AccessoryManagerDemo extends DemoClass {
    private JCheckBox _simpleNorthCB;
    private JCheckBox _simpleSouthCB;
    private JCheckBox _simpleWestCB;
    private JCheckBox _simpleEastCB;

    private JDesktopPane _desktop;
    private JInternalFrame _frame;

    private JFileChooser _simpleChooser;
    private SimpleAccessoryManager _simpleManager;

    public AccessoryManagerDemo() {
        initialize();
    }

    public String getDemoName() {
        return "Accessory Manager Demo";
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _simpleNorthCB = new JCheckBox("North");
        _simpleSouthCB = new JCheckBox("South");
        _simpleEastCB = new JCheckBox("East");
        _simpleWestCB = new JCheckBox("West");

        _desktop = new JDesktopPane();

        _simpleChooser = new JFileChooser();
        _simpleManager = new SimpleAccessoryManager(_simpleChooser);

        initInternalFrame();
    }

    protected void initInternalFrame() {
        final JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(_simpleChooser);
        _frame = new JInternalFrame("Simple accessory manager");
        _frame.setContentPane(panel);
        _frame.setClosable(false);
        _frame.setResizable(true);
        _frame.setSize(600, 300);
        _frame.setLocation(100, 30);
        _desktop.add(_frame);
        _frame.setVisible(true);
    }

    protected void initLayout() {
        final JPanel simpleCBPanel = new JPanel(new GridLayout(1, 4));
        simpleCBPanel.setBorder(BorderFactory.createTitledBorder("Simple accessory manager"));
        simpleCBPanel.add(_simpleNorthCB);
        simpleCBPanel.add(_simpleSouthCB);
        simpleCBPanel.add(_simpleEastCB);
        simpleCBPanel.add(_simpleWestCB);

        setLayout(new GridBagLayout());
        add(_desktop, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(simpleCBPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {
        final ActionListener listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JCheckBox source = (JCheckBox) e.getSource();
                if (source == _simpleNorthCB) {
                    _simpleManager.setAccessory(source.isSelected() ? new JTextField("North") : null,
                            SwingConstants.NORTH);
                } else if (source == _simpleSouthCB) {
                    _simpleManager.setAccessory(source.isSelected() ? new JTextField("South") : null,
                            SwingConstants.SOUTH);
                } else if (source == _simpleEastCB) {
                    _simpleManager.setAccessory(source.isSelected() ? new JTextField("East") : null,
                            SwingConstants.EAST);
                } else if (source == _simpleWestCB) {
                    _simpleManager.setAccessory(source.isSelected() ? new JTextField("West") : null,
                            SwingConstants.WEST);
                }
            }
        };
        _simpleNorthCB.addActionListener(listener);
        _simpleSouthCB.addActionListener(listener);
        _simpleWestCB.addActionListener(listener);
        _simpleEastCB.addActionListener(listener);
    }
}
