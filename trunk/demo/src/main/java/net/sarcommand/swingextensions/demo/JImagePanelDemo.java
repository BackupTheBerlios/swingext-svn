package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.imagepanel.JImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Creates a simple demo for the JImagePanel component. The frame offers two checkboxes to allow the user to choose
 * whether or not scaling and dragging should be allowed. Four other buttons cover the setScaleMode(int) and
 * setToIdentity() methods.
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
public class JImagePanelDemo extends DemoClass {
    private JImagePanel _imagePanel;

    private JCheckBox _allowScalingCB;
    private JCheckBox _allowDragCB;

    private JButton _scaleToFitButton;
    private JButton _origSizeButton;
    private JButton _scaleHorizontalButton;
    private JButton _scaleVerticalButton;

    public JImagePanelDemo() throws HeadlessException {
        init();
    }

    public String getDemoName() {
        return "ImagePanel demo";
    }

    /**
     * Initializes this component. This method should be invoked by every constructor as it take's care of setting up
     * the required fields and layout definitons.
     */
    protected void init() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    /**
     * Initializes all graphical subcomponents and defines their behavious by setting the accordant properties.
     */
    protected void initComponents() {
        _imagePanel = new JImagePanel();
        _allowDragCB = new JCheckBox("Allow dragging", true);
        _allowScalingCB = new JCheckBox("Allow scaling", true);
        _scaleHorizontalButton = new JButton("Fit horizontally");
        _scaleVerticalButton = new JButton("Fit vertically");
        _scaleToFitButton = new JButton("Scale to fit");
        _origSizeButton = new JButton("Original size");

        _imagePanel.setBorder(BorderFactory.createRaisedBevelBorder());
        try {
            final BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jImagePanelDemo.jpg"));
            _imagePanel.setImage(image);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Missing demo resource:\njimagepaneldemo.jpg", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initializes the layout definitions for this class.
     */
    protected void initLayout() {
        setLayout(new GridBagLayout());
        add(_imagePanel,
                new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        add(_allowDragCB,
                new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 10, 3, 3), 0, 0));
        add(_allowScalingCB,
                new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 10), 0, 0));
        add(_origSizeButton,
                new GridBagConstraints(0, 2, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 10, 3, 3), 0, 0));
        add(_scaleToFitButton,
                new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 10), 0, 0));
        add(_scaleHorizontalButton,
                new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 10, 3, 3), 0, 0));
        add(_scaleVerticalButton,
                new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 10), 0, 0));
    }

    /**
     * Creates and installs the required event handlers.
     */
    protected void setupEventHandlers() {
        final ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Object source = e.getSource();
                if (source == _allowDragCB)
                    _imagePanel.setDraggable(_allowDragCB.isSelected());
                else if (source == _allowScalingCB)
                    _imagePanel.setScalable(_allowScalingCB.isSelected());
                else if (source == _scaleHorizontalButton)
                    _imagePanel.setScaleMode(JImagePanel.SCALE_HORIZONTAL);
                else if (source == _scaleVerticalButton)
                    _imagePanel.setScaleMode(JImagePanel.SCALE_VERTICAL);
                else if (source == _scaleToFitButton)
                    _imagePanel.setScaleMode(JImagePanel.SCALE_BOTH);
                else if (source == _origSizeButton)
                    _imagePanel.setToIdentity();
            }
        };
        _allowDragCB.addActionListener(actionListener);
        _allowScalingCB.addActionListener(actionListener);
        _scaleHorizontalButton.addActionListener(actionListener);
        _scaleVerticalButton.addActionListener(actionListener);
        _scaleToFitButton.addActionListener(actionListener);
        _origSizeButton.addActionListener(actionListener);
    }
}
