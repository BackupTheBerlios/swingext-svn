package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.glasspane.BlurringGlassPane;
import net.sarcommand.swingextensions.glasspane.DefaultProgressNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo class for the BlurringGlassPane class. <hr/> Copyright 2006 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class GlassPaneDemo extends DemoClass {
    private JDesktopPane _desktop;
    private JInternalFrame _internalFrame;

    private BlurringGlassPane _glassPane;
    private DefaultProgressNotification _notification;

    private JCheckBox _visibleCB;

    public GlassPaneDemo() {
        initialize();
    }

    public String getDemoName() {
        return "BlurringGlassPane Demo";
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initDemoFrame() {
        _internalFrame = new JInternalFrame("An exemplary frame");

        final JTextPane textPane = new JTextPane();
        textPane.setText("Some text...");

        final JPanel cPane = new JPanel(new GridBagLayout());
        cPane.setBorder(BorderFactory.createTitledBorder("Just a random bunch of components"));

        cPane.add(new JTree(), new GridBagConstraints(0, 0, 1, 2, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        cPane.add(textPane, new GridBagConstraints(1, 0, 2, 1, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        cPane.add(new JLabel("A Label", JLabel.CENTER), new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        cPane.add(new JButton("A Button"), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        _internalFrame.setContentPane(cPane);
        _internalFrame.setGlassPane(_glassPane);
        _internalFrame.setResizable(true);
        _internalFrame.setLocation(100, 30);
        _internalFrame.setSize(600, 300);
        _internalFrame.setVisible(true);
    }

    protected void initComponents() {
        _glassPane = new BlurringGlassPane();

        _notification = new DefaultProgressNotification();
        _notification.setText("Importing data...");

        _glassPane.setNotification(_notification);

        initDemoFrame();
        _desktop = new JDesktopPane();
        _desktop.add(_internalFrame);

        _visibleCB = new JCheckBox("GlassPane visible");
    }

    protected void initLayout() {
        setLayout(new GridBagLayout());
        add(_desktop, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(3, 3, 3, 3), 0, 0));
        add(_visibleCB, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {
        _visibleCB.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                _glassPane.setVisible(_visibleCB.isSelected());
            }
        });
    }

}
