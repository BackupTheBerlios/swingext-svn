package net.sarcommand.swingextensions.glasspane;

import net.sarcommand.swingextensions.image.ImageUtilities;
import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.VolatileImage;

/**
 * A glasspane implementation which will grey out your application and blur its graphics for a visual 'disabled' effect.
 * This implementation is basically a JPanel, so you are free to add components to it.
 * <p/>
 * You might want to take a look at the GlassPaneNotification class for adding progess indicators.
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @see GlassPaneNotification
 */
public class BlurringGlassPane extends JPanel {
    /**
     * A hardware accelerated image buffer used when blurring the content pane.
     */
    protected VolatileImage _imageBuffer;

    /**
     * The filter operation used to create the blur effect if _blurringContentPane has been set.
     */
    protected BufferedImageOp _blurFilter;

    /**
     * The GlassPaneNotification currently shown, if there is one.
     */
    protected GlassPaneNotification _notification;

    private JComponent _peerComponent;

    public BlurringGlassPane() {
        initialize();
    }

    public JComponent getPeerComponent() {
        return _peerComponent;
    }

    public void setPeerComponent(final JComponent peerComponent) {
        _peerComponent = peerComponent;
    }

    /**
     * Initializes the glasspane by installing the required listeners and setting up the blur filter.
     */
    private void initialize() {
        setOpaque(false);
        setBackground(new Color(100, 100, 100, 50));
        setLayout(new GridBagLayout());
        _blurFilter = ImageUtilities.createSimpleBoxBlurFilter();

        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });

        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                _imageBuffer = null;
            }

            public void componentShown(ComponentEvent e) {
                recreateImageBuffer();
            }

            public void componentResized(ComponentEvent e) {
                if (isShowing())
                    recreateImageBuffer();
            }
        });
    }

    /**
     * Recreates the internal image buffer used to store the blurred version of the underlying content pane. This method
     * will be invoked whenever:
     * <p/>
     * <li>The glasspane is resized</li> <li>The glasspane is shown after being hidden</li> <li>The contents of the
     * hardware-accelerated buffer have been lost since the last repaint. This should never happen unless the
     * screensaver turns on or you're switching to fullscreen Direct3d/OpenGL games</li>
     */
    protected void recreateImageBuffer() {
        if (getWidth() > 0 && getHeight() > 0) {
            final BufferedImage tempImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            _imageBuffer = createVolatileImage(getWidth(), getHeight());

            final Component contentPane;
            final Graphics2D tempGraphics = tempImage.createGraphics();

            if (_peerComponent == null) {
                final JMenuBar bar;
                final Window w = SwingExtUtil.getWindowForComponent(this);

                if (w == null)
                    bar = null;
                else if (w instanceof JFrame)
                    bar = ((JFrame) w).getJMenuBar();
                else if (w instanceof JDialog)
                    bar = ((JDialog) w).getJMenuBar();
                else
                    bar = null;

                if (bar != null) {
                    bar.paint(tempGraphics);
                    tempGraphics.translate(0, bar.getHeight());
                }

                final JRootPane parent = (JRootPane) getParent();
                contentPane = parent.getContentPane();
            } else
                contentPane = _peerComponent;

            contentPane.paint(tempGraphics);
            tempGraphics.dispose();

            final Graphics2D g2 = _imageBuffer.createGraphics();
            g2.drawImage(tempImage, _blurFilter, 0, 0);
            g2.dispose();

            repaint();
        }
    }

    /**
     * Overwritten to create a transparent gray overlay.
     *
     * @param g Graphics to use when painting the component.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        if (_imageBuffer != null) {
            while (_imageBuffer.contentsLost())
                recreateImageBuffer();
            g2.drawImage(_imageBuffer, 0, 0, null);
        }

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    public GlassPaneNotification getNotification() {
        return _notification;
    }

    /**
     * Sets a GlassPaneNotification, which will be shown at the center of the panel. Note that you shouldn't try to mix
     * custom content and GlassPaneNotifications, or you might get some rather strange results.
     *
     * @param notification Notification to set, or null if the current notification should be removed.
     */
    public void setNotification(final GlassPaneNotification notification) {
        if (_notification != null)
            remove(_notification);
        _notification = notification;
        if (_notification != null)
            add(_notification, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        revalidate();
    }
}
