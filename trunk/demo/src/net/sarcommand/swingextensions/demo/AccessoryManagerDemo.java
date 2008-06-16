package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.filechooser.ImagePreviewAccessory;
import net.sarcommand.swingextensions.filechooser.SimpleAccessoryManager;
import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import static javax.swing.SwingConstants.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

/**
 * Demo for the different AccessoryManager implementations.
 * <p/>
 * <hr/> Copyright 2006 Torsten Heup
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
public class AccessoryManagerDemo extends DemoClass {
    private JCheckBox _simpleNorthCB;
    private JCheckBox _simpleSouthCB;
    private JCheckBox _simpleWestCB;
    private JCheckBox _simpleEastCB;

    private JDesktopPane _desktop;

    private JFileChooser _simpleChooser;
    private SimpleAccessoryManager _simpleManager;
    protected JRadioButton _simpleAccessoryRadio;
    protected JRadioButton _prebuiltAccessoryRadio;
    protected JPanel _prebuiltAccessoryPanel;
    protected JPanel _simpleAccessoryPanel;
    protected JCheckBox _imagePreviewCB;

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

        switchSelectionPanels();
    }

    protected void initComponents() {
        _simpleNorthCB = new JCheckBox("North");
        _simpleSouthCB = new JCheckBox("South");
        _simpleEastCB = new JCheckBox("East");
        _simpleWestCB = new JCheckBox("West");

        _desktop = new JDesktopPane();

        _simpleChooser = new JFileChooser();
        _simpleManager = new SimpleAccessoryManager(_simpleChooser);

        _simpleAccessoryRadio = new JRadioButton("", true);
        _prebuiltAccessoryRadio = new JRadioButton("");
        final ButtonGroup group = new ButtonGroup();
        group.add(_simpleAccessoryRadio);
        group.add(_prebuiltAccessoryRadio);

        _simpleAccessoryPanel = new JPanel();
        _prebuiltAccessoryPanel = new JPanel();

        _imagePreviewCB = new JCheckBox("ImagePreviewAccessory");

        initInternalFrame();
    }

    protected void initInternalFrame() {
        final JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(_simpleChooser);
        final JInternalFrame frame = new JInternalFrame("Simple accessory manager");
        frame.setContentPane(panel);
        frame.setClosable(false);
        frame.setResizable(true);
        frame.setSize(600, 300);
        frame.setLocation(100, 30);
        _desktop.add(frame);
        frame.setVisible(true);
    }

    protected void initLayout() {
        final JPanel checkBoxPanel = new JPanel(new GridBagLayout());

        _simpleAccessoryPanel.setLayout(new GridLayout(1, 4));
        _simpleAccessoryPanel.setBorder(BorderFactory.createTitledBorder("Accessory installation spots"));
        _simpleAccessoryPanel.add(_simpleNorthCB);
        _simpleAccessoryPanel.add(_simpleSouthCB);
        _simpleAccessoryPanel.add(_simpleEastCB);
        _simpleAccessoryPanel.add(_simpleWestCB);

        _prebuiltAccessoryPanel.setLayout(new GridBagLayout());
        _prebuiltAccessoryPanel.setBorder(BorderFactory.createTitledBorder("Prebuilt accessories"));
        _prebuiltAccessoryPanel.add(_imagePreviewCB, new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        _prebuiltAccessoryPanel.add(Box.createGlue(), new GridBagConstraints(-1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        checkBoxPanel.add(_simpleAccessoryRadio, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        checkBoxPanel.add(_simpleAccessoryPanel, new GridBagConstraints(-1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        checkBoxPanel.add(_prebuiltAccessoryRadio, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        checkBoxPanel.add(_prebuiltAccessoryPanel, new GridBagConstraints(-1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        setLayout(new GridBagLayout());
        add(_desktop, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(checkBoxPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {
        final ActionListener listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JToggleButton source = (JToggleButton) e.getSource();

                if (source.getParent() == _simpleAccessoryPanel)
                    updateBasicAccessories();
                else if (source.getParent() == _prebuiltAccessoryPanel)
                    updatePrebuiltAccessories();
                else if (source == _simpleAccessoryRadio || source == _prebuiltAccessoryRadio)
                    switchSelectionPanels();
            }
        };

        final Collection<JToggleButton> buttonlist = Arrays.asList(_simpleAccessoryRadio, _prebuiltAccessoryRadio,
                _simpleNorthCB, _simpleSouthCB, _simpleWestCB, _simpleEastCB, _imagePreviewCB);
        for (JToggleButton b : buttonlist)
            b.addActionListener(listener);
    }

    protected void updatePrebuiltAccessories() {
        _simpleManager.setAccessory(_imagePreviewCB.isSelected() ? new ImagePreviewAccessory() : null, EAST);
    }

    protected void updateBasicAccessories() {
        _simpleManager.setAccessory(_simpleNorthCB.isSelected() ? new JTextField("North") : null, NORTH);
        _simpleManager.setAccessory(_simpleSouthCB.isSelected() ? new JTextField("North") : null, SOUTH);
        _simpleManager.setAccessory(_simpleEastCB.isSelected() ? new JTextField("North") : null, EAST);
        _simpleManager.setAccessory(_simpleWestCB.isSelected() ? new JTextField("North") : null, WEST);
    }

    protected void switchSelectionPanels() {
        if (_simpleAccessoryRadio.isSelected()) {
            for (Component c : SwingExtUtil.getChildComponents(_prebuiltAccessoryPanel, JToggleButton.class)) {
                ((JToggleButton) c).setSelected(false);
                c.setEnabled(false);
            }
            for (Component c : SwingExtUtil.getChildComponents(_simpleAccessoryPanel, JToggleButton.class))
                c.setEnabled(true);
            updatePrebuiltAccessories();
        } else if (_prebuiltAccessoryRadio.isSelected()) {
            for (Component c : SwingExtUtil.getChildComponents(_simpleAccessoryPanel, JToggleButton.class)) {
                ((JToggleButton) c).setSelected(false);
                c.setEnabled(false);
            }
            for (Component c : SwingExtUtil.getChildComponents(_prebuiltAccessoryPanel, JToggleButton.class))
                c.setEnabled(true);
            updateBasicAccessories();
        }
    }
}
