package net.sarcommand.swingextensions.demo;

import javax.swing.*;
import java.awt.*;

/**
 * A simple demo applications which displays some of the swing extensions along with a brief description.
 * <p/>
 * Copyright 2006 Torsten Heup
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
public class SwingExtensionsDemo {
    private static final Class[] DEMO_CLASSES = {
            JImagePanelDemo.class,
            SelectionTreeDemo.class,
            TypedInputFieldDemo.class,
            BlockingGlassPaneDemo.class,
            AccessoryManagerDemo.class,
            MultiCellSplitPaneDemo.class
    };

    private JTabbedPane _demoPanel;

    public SwingExtensionsDemo() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initialize();
            }
        });
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
        initFrame();
    }

    protected void initComponents() {
        _demoPanel = new JTabbedPane();

        for (Class demoClass : DEMO_CLASSES) {
            try {
                final DemoClass demo = (DemoClass) demoClass.newInstance();

                final JTextArea descArea = new JTextArea(demo.getDemoDescription());
                descArea.setLineWrap(true);
                descArea.setWrapStyleWord(true);
                descArea.setEditable(false);

                final JSplitPane panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                panel.setTopComponent(demo);
                final JScrollPane pane = new JScrollPane(descArea);
                pane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
                panel.setBottomComponent(pane);
                panel.setResizeWeight(0.7);

                _demoPanel.add(demo.getDemoName(), panel);
            } catch (Exception e) {
                System.err.println("Could not instanciate demo " + demoClass.getName());
                e.printStackTrace();
            }
        }
    }

    protected void initLayout() {
    }

    protected void setupEventHandlers() {
    }

    protected void initFrame() {
        final JFrame frame = new JFrame("SwingExtension demo application, v. " + getClass().getPackage().getImplementationVersion());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(_demoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system lookandfeel");
            e.printStackTrace();
        }
        new SwingExtensionsDemo();
    }
}
