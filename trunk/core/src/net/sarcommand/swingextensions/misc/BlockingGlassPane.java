package net.sarcommand.swingextensions.misc;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;

/**
 * Implements a simple component which can be used as a glass pane to block windows while the application is working.
 * The BlockingGlassPane application will
 * <p/>
 * <li>Create a gray translucent overlay for the frame (background color)</li>
 * <li>Block all input events from mouse and keyboard</li>
 * <li>Display an animated JProgressIndicator in the center of the screen</li>
 * <li>Allow you to display a status message below the progress indicator which accepts plain and styled text</li>
 * <p/>
 * The progress indicator will stop automatically whenever the glass pane is hidden and will be reenabled as soon
 * as the component is shown on the screen.
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
public class BlockingGlassPane extends JPanel {
    /**
     * The progress indicator instance displayed in the center of the screen.
     */
    protected JProgressIndicator _indicator;

    /**
     * The text component used to display the message
     */
    protected JTextPane _messageSection;

    /**
     * The attribute set which will be applied to text when setMessage(String) is used - contains paragraphs
     * attributes only.
     */
    protected MutableAttributeSet _attributeSetCentered;

    /**
     * Creates a new BlockingGlassPane instance
     */
    public BlockingGlassPane() {
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        setOpaque(false);
        setBackground(new Color(100, 100, 100, 50));
        _indicator = new JProgressIndicator();
        _indicator.setPreferredSize(new Dimension(64, 64));

        _messageSection = new JTextPane();
        _messageSection.setEditable(false);
        _messageSection.setOpaque(false);

        _attributeSetCentered = new SimpleAttributeSet();
        StyleConstants.setAlignment(_attributeSetCentered, StyleConstants.ALIGN_CENTER);
    }

    protected void initLayout() {
        final JPanel northern = new JPanel(new GridBagLayout());
        northern.setOpaque(false);
        northern.add(Box.createGlue(), new GridBagConstraints(0, 0, 3, 1, 1.0, 0.6, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        northern.add(Box.createGlue(), new GridBagConstraints(0, 1, 1, 1, 0.4, 0.4, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        northern.add(_indicator, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.2, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        northern.add(Box.createGlue(), new GridBagConstraints(2, 1, 1, 1, 0.4, 0.2, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));


        _messageSection.setBorder(BorderFactory.createEmptyBorder(12, 24, 24, 24));
        setLayout(new GridLayout(2, 2));
        add(northern);
        add(_messageSection);
    }

    protected void setupEventHandlers() {
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });

        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                _indicator.stopProgress();
            }

            public void componentShown(ComponentEvent e) {
                _indicator.startProgress();
            }
        });
    }

    /**
     * Sets a message to be displayed at the center of the screen. By default, the message will be centered.
     *
     * @param message Message to display.
     */
    public void setMessage(final String message) {
        _messageSection.setText(message);
        _messageSection.setParagraphAttributes(_attributeSetCentered, false);
    }

    /**
     * Returns the styled document containing the message text. Use this method if you want to insert
     * styled content.
     *
     * @return the styled document containing the message text
     */
    public StyledDocument getMessageDocument() {
        return _messageSection.getStyledDocument();
    }

    /**
     * Overwritten to create a transparent gray overlay.
     *
     * @param g Graphics to use when painting the component.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
