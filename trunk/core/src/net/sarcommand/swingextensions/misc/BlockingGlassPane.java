package net.sarcommand.swingextensions.misc;

import net.sarcommand.swingextensions.utilities.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * Implements a simple component which can be used as a glass pane to block windows while the application is working.
 * The BlockingGlassPane application will
 * <p/>
 * <li>Create a gray translucent overlay for the frame (background color), blurring the content pane (this is
 * optional)</li>
 * <li>Block all input events from mouse and keyboard</li>
 * <li>Display an animated JProgressIndicator in the center of the screen</li>
 * <li>Allow you to display a status message below the progress indicator which accepts plain and styled text</li>
 * <p/>
 * The progress indicator will stop automatically whenever the glass pane is hidden and will be reenabled as soon
 * as the component is shown on the screen.
 * <p/>
 * KNOWN BUG: The java 6 dp1 for mac seems to have a rendering issue when using image buffers. The blurred version
 * will therefore not draw properly.
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
    public static final String DEFAULT_STYLE = "defaultStyle";

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
     * Determines whether the contents of the contentPane should be drawn blurred.
     */
    protected boolean _blurringContentPane;

    /**
     * A hardware accelerated image buffer used when blurring the content pane.
     */
    protected VolatileImage _imageBuffer;

    /**
     * The filter operation used to create the blur effect if _blurringContentPane has been set.
     */
    protected BufferedImageOp _blurFilter;

    /**
     * Creates a new BlockingGlassPane instance
     */
    public BlockingGlassPane() {
        initialize();
        setBlurringContentPane(true);
    }

    /**
     * Returns whether the content pane will be blurred when the glasspane is visible. Defaults to true.
     *
     * @return whether the content pane will be blurred.
     */
    public boolean isBlurringContentPane() {
        return _blurringContentPane;
    }

    /**
     * Sets whether or not the content pane should be blurred when the glasspane is visible. Defaults to true. Note
     * that setting this property does not guarantee that a blur effect will be used: For technical reasons, this will
     * only work when the glasspane is used within a JRootPane (which will be the case for all swing frames and windows.
     *
     * @param blurringContentPane whether or not the content pane should be blurred.
     */
    public void setBlurringContentPane(boolean blurringContentPane) {
        _blurringContentPane = blurringContentPane;
        if (blurringContentPane)
            recreateImageBuffer();
        else
            _imageBuffer = null;
    }

    /**
     * Recreates the internal image buffer used to store the blurred version of the underlying content pane. This method
     * will be invoked whenever:
     * <p/>
     * <li>The glasspane is resized</li>
     * <li>The glasspane is shown after being hidden</li>
     * <li>The contents of the hardware-accelerated buffer have been lost since the last repaint. This should never
     * happen unless the screensaver turns on</li>
     */
    protected void recreateImageBuffer() {
        if (getWidth() > 0 && getHeight() > 0 && getParent() instanceof JRootPane) {
            final BufferedImage tempImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            _imageBuffer = createVolatileImage(getWidth(), getHeight());

            final JRootPane parent = (JRootPane) getParent();
            final Graphics2D tempGraphics = tempImage.createGraphics();
            parent.getContentPane().paint(tempGraphics);
            tempGraphics.dispose();

            _imageBuffer.createGraphics().drawImage(tempImage, _blurFilter, 0, 0);
        }
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

        _blurFilter = ImageOperations.createSimpleBoxBlurFilter();

        _messageSection.addStyle(DEFAULT_STYLE, null);
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
                _imageBuffer = null;
            }

            public void componentShown(ComponentEvent e) {
                _indicator.startProgress();
                recreateImageBuffer();
            }

            public void componentResized(ComponentEvent e) {
                recreateImageBuffer();
            }
        });
    }

    /**
     * Sets a message to be displayed below the progress indicator. By default, the message will be centered.
     *
     * @param message Message to display.
     */
    public void setMessage(final String message) {
        setMessage(message, DEFAULT_STYLE);
        _messageSection.setParagraphAttributes(_attributeSetCentered, false);
    }

    /**
     * Sets a message to be displayed below the progress indicator, using a given style. In order to refer to a named
     * style, you have to install it first using the getMessageDocument() method.
     *
     * @param message   Message to display.
     * @param styleName Name of the style to be used.
     */
    public void setMessage(final String message, final String styleName) {
        final StyledDocument styledDocument = _messageSection.getStyledDocument();
        try {
            styledDocument.remove(0, styledDocument.getLength());
            styledDocument.insertString(0, message, styledDocument.getStyle(styleName));
        } catch (BadLocationException e) {
            //really should never happen
            e.printStackTrace();
        }
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
     * Overwritten to create a transparent gray overlay. If the according property is set, the content pane will
     * also be blurred.
     *
     * @param g Graphics to use when painting the component.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        if (_blurringContentPane && _imageBuffer != null) {
            while (_imageBuffer.contentsLost())
                recreateImageBuffer();
            g2.drawImage(_imageBuffer, 0, 0, null);
        }

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
