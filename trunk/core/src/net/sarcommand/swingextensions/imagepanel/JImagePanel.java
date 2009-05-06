package net.sarcommand.swingextensions.imagepanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;

/**
 * A JImagePanel is a component which allows you to display images with swing. Other than the JLabel class, which is
 * more suitable for displaying icons or small gifs, the JImagePanel is meant to be used for larger images. It also
 * offers a basic user interaction as it allows to scale and translate the image directly (these options can of course
 * be disabled). By default, the image can be rescaled usig the mouse wheel and translated by dragging the mouse with
 * the first mouse button pressed.<br><br>
 * <p/>
 * The JImagePanel takes advantage of hardware acceleration as far as it's possible. Note that performance may differ
 * greatly between the different platforms, especially linux based systems without hardware acceleration have been known
 * to perform particularly sluggish. Please bear in mind that the {@link Transparency} of an image also greatly
 * influences the drawing performance.<br><br>
 * <p/>
 * The JImagePanel class offers several static conveniance methods to create preconfigured instances and frames or
 * dialogs wrapping a JImagePanel.
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
public class JImagePanel extends JPanel implements Scrollable {
    /**
     * Constant used to tell the JImagePanel to scale the image horizontally.
     */
    public static final int SCALE_HORIZONTAL = 100;
    /**
     * Constant used to tell the JImagePanel to scale the image vertically.
     */
    public static final int SCALE_VERTICAL = 101;
    /**
     * Constant used to tell the JImagePanel to attempt to find the best fit for the image.
     */
    public static final int SCALE_BOTH = 102;
    /**
     * Constant used to tell the JImagePanel to scale according to the value specified by the user's input.
     */
    public static final int SCALE_MANUALLY = 103;

    /**
     * Returns a JFrame instance wrapping a JImagePanel. The JImagePanel itself can be accessed using the {@link
     * javax.swing.JFrame#getContentPane()} method. The frame will appear centered on the screen.
     *
     * @param image     The image to display.
     * @param scalable  Determines whether or not the image should be scalable.
     * @param draggable Determines whether or not the image should be draggable.
     * @return JFrame instance.
     */
    public static JFrame getFrameInstance(final Image image, final boolean scalable, final boolean draggable) {
        final JImagePanel panel = new JImagePanel();
        panel.setImage(image);
        panel.setScalable(scalable);
        panel.setDraggable(draggable);

        final JFrame frame = new JFrame();
        frame.setContentPane(panel);

        final int width = Math.min(image.getWidth(null), 400);
        final int height = Math.min(image.getHeight(null), 400);
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);

        return frame;
    }

    /**
     * Displays a JFrame wrapping a JImagePanel. The user will be able to scale and translate the image manually.
     *
     * @param title Title fo the new frame.
     * @param image Image to display.
     */
    public static void showInFrame(final String title, final Image image) {
        final JFrame frame = getFrameInstance(image, true, true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(title);
        frame.setVisible(true);
    }

    /**
     * Returns a JDialog instance wrapping a JImagePanel. The JImagePanel itself can be accessed using the {@link
     * javax.swing.JDialog#getContentPane()} method. The dialog will appear centered on the screen.
     *
     * @param parent    Parent frame for the dialog to create. May be <code>null</code>
     * @param modal     Whether or not the dialog should be modal.
     * @param image     The image to display.
     * @param scalable  Determines whether or not the image should be scalable.
     * @param draggable Determines whether or not the image should be draggable.
     * @return JFrame instance.
     */
    public static JDialog getDialogInstance(final Frame parent, final boolean modal, final Image image, final boolean scalable, final boolean draggable) {
        final JImagePanel panel = new JImagePanel();
        panel.setImage(image);
        panel.setScalable(scalable);
        panel.setDraggable(draggable);

        final JDialog dlg = new JDialog(parent, modal);
        dlg.setContentPane(panel);

        final int width = Math.min(image.getWidth(null), 400);
        final int height = Math.min(image.getHeight(null), 400);
        dlg.setSize(width, height);

        dlg.setLocationRelativeTo(null);

        return dlg;
    }

    /**
     * Displays a JDialog wrapping a JImagePanel. The displayed image will not be manually scalable or translatable and
     * will be scaled to fit the dialog.
     *
     * @param parent Parent frame for the dialog. May be null.
     * @param modal  Whether or not the dialog should be modal
     * @param title  Titel for the new dialog.
     * @param image  Image to display.
     */
    public static void showInDialog(final Frame parent, final boolean modal, final String title, final Image image) {
        final JDialog dlg = getDialogInstance(parent, modal, image, true, true);
        dlg.setTitle(title);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setVisible(true);
    }

    /**
     * Returns a new 'static' JImagePanel instance, meaning the user will not be allowed to translate or rescale the
     * image by mouse interaction. This is a mere conveniance method.
     *
     * @param image Image to display.
     * @return JImagePanel instance.
     */
    public static JImagePanel newStaticInstance(final Image image) {
        final JImagePanel panel = new JImagePanel();
        panel.setImage(image);
        panel.setScalable(false);
        panel.setDraggable(false);
        return panel;
    }

    /**
     * Displayed image
     */
    protected transient Image _image;

    /**
     * Volatile image buffer. The image buffer is necessary as some image types would preform more than lousy if painted
     * directly.
     */
    protected transient VolatileImage _buffer;

    /**
     * The image's original width.
     */
    protected int _imageWidth;

    /**
     * The image's original height.
     */
    protected int _imageHeight;

    /**
     * MouseListener instance used to track mouse clicks.
     */
    protected MouseListener _mouseListener;

    /**
     * MouseWheelListener instance used to track wheel movement and rescale the image accordingly.
     */
    protected MouseWheelListener _mouseWheelListener;

    /**
     * MouseMotionListener instance used to track drag events and translate the image accordingly.
     */
    protected MouseMotionListener _mouseMotionListener;

    /**
     * Marks the last known position of the mouse.
     */
    protected Point _lastMousePos;

    /**
     * Transform applied to the image buffer when painting.
     */
    protected AffineTransform _transform;

    /**
     * Scale factor.
     */
    protected double _scaleFactor;

    /**
     * Horizontal translation.
     */
    protected double _translationX;

    /**
     * Vertical translation.
     */
    protected double _translationY;

    /**
     * Rate at which scaling is performed. A rate of 1 is considered scaling by 3% for each mouse wheel event.
     */
    protected double _scaleRate;

    /**
     * Event mask used to determine whether the image should be translated upon mouseDragged events.
     */
    protected int _dragMask;

    /**
     * Determines whether or not the image should be scalable.
     */
    protected boolean _scalable;

    /**
     * Determines whether the image can be translated manually by the user.
     */
    protected boolean _draggable;

    /**
     * Determines whether the image should be scaled manually or accordant to a rule.
     */
    protected int _scaleMode;

    /**
     * Creates a new JImagePanel instance.
     */
    public JImagePanel() {
        super();
        initialize();
    }

    /**
     * Creates a new JImagePanel instance displaying the specified image.
     *
     * @param image Image to display.
     */
    public JImagePanel(final Image image) {
        super();
        initialize();
        setImage(image);
    }

    /**
     * Initializes the image panel. All constructors should (really) invoke this method.
     */
    protected void initialize() {
        setupEventHandlers();

        _lastMousePos = new Point();

        /* Initialize properties */
        _scaleRate = 1.;
        _dragMask = MouseEvent.BUTTON1_MASK;
        setDraggable(true);
        setScalable(true);

        /* Initialize internal variables */
        _scaleFactor = 1.;
        _translationX = 0.;
        _translationY = 0.;
        _transform = new AffineTransform();
    }

    /**
     * Installs the event handlers responsible for reacting to mouse input.
     */
    protected void setupEventHandlers() {
        _mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                _lastMousePos = e.getPoint();
            }
        };
        _mouseWheelListener = new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (_scalable) {
                    _scaleMode = SCALE_MANUALLY;
                    _scaleFactor *= e.getUnitsToScroll() > 0 ? 1 + 0.03 * _scaleRate : 1 - 0.03 * _scaleRate;
                    if (_scaleFactor < 0)
                        _scaleFactor = 0.01;
                    repaint();
                }
            }
        };
        _mouseMotionListener = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (_draggable && contains(e.getPoint())) {
                    if ((e.getModifiers() & _dragMask) != 0) {
                        final Point p = e.getPoint();
                        final int width = getWidth();
                        final int height = getHeight();

                        final double dx = _lastMousePos.x - p.x;
                        final double dy = _lastMousePos.y - p.y;
                        final double scaledWidth = _imageWidth * _scaleFactor;
                        final double scaledHeight = _imageHeight * _scaleFactor;
                        final double newX = (width - scaledWidth) / 2 + _translationX - dx;
                        final double newY = (height - scaledHeight) / 2 + _translationY - dy;

                        if (dx != 0) {
                            if (width > scaledWidth) {
                                if (dx > 0) {
                                    if (newX > 0)
                                        _translationX -= dx;
                                } else if (newX + scaledWidth < width)
                                    _translationX -= dx;
                            } else {
                                if (dx > 0) {
                                    if (newX + scaledWidth > width)
                                        _translationX -= dx;
                                } else if (newX <= 0)
                                    _translationX -= dx;
                            }
                        }

                        if (dy != 0) {
                            if (height > scaledHeight) {
                                if (dy > 0) {
                                    if (newY > 0)
                                        _translationY -= dy;
                                } else if (newY + scaledHeight < height)
                                    _translationY -= dy;
                            } else {
                                if (dy > 0) {
                                    if (newY + scaledHeight > height)
                                        _translationY -= dy;
                                } else if (newY <= 0)
                                    _translationY -= dy;
                            }
                        }
                        _lastMousePos = p;
                        repaint();
                    }
                }
            }
        };

        addMouseListener(_mouseListener);
        addMouseMotionListener(_mouseMotionListener);
        addMouseWheelListener(_mouseWheelListener);
    }

    /**
     * Set's the image to be displayed. The JImagePanel will be updated automatically. Note that the image will be
     * copied to an image buffer to increase performance, so the JImagePanel will not react to changes you apply to the
     * original image by modifying it's data buffer. As this approach is generally discouraged for performance reasons,
     * you will have to invoke setImage again when you altered the image. As this method performs very fast this
     * shouldn't be much of a inconveniance.
     *
     * @param image Image to display.
     */
    public void setImage(final Image image) {
        _image = image;
        _imageWidth = image != null ? image.getWidth(null) : 0;
        _imageHeight = image != null ? image.getHeight(null) : 0;

        if (_image != null) {
            final int transparency = (image instanceof Transparency) ? ((Transparency) image).getTransparency() : Transparency.TRANSLUCENT;
            final GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
            if (graphicsConfiguration != null)
                _buffer = graphicsConfiguration.createCompatibleVolatileImage(_imageWidth, _imageHeight, transparency);
            else
                _buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleVolatileImage(_imageWidth, _imageHeight, transparency);
            final Graphics g2 = _buffer.createGraphics();
            g2.drawImage(image, 0, 0, null);

            final Container parent = getParent();
            if (parent != null)
                parent.validate();

            setPreferredSize(new Dimension(_imageWidth, _imageHeight));
        } else
            _buffer = null;

        setToIdentity();
        repaint();
    }

    /**
     * Returns the image which is currently displayed.
     *
     * @return Current image.
     */
    public Image getImage() {
        return _image;
    }

    /**
     * Overriden to display the image according to the scale and translation parameters set.
     *
     * @param g Graphics to use for rendering
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_buffer != null) {

            /* Update the image if necessary */
            switch (_buffer.validate(getGraphicsConfiguration())) {
                case VolatileImage.IMAGE_INCOMPATIBLE:
                    final int transparency = (_image instanceof Transparency) ? ((Transparency) _image).getTransparency() : Transparency.TRANSLUCENT;
                    _buffer = getGraphicsConfiguration().createCompatibleVolatileImage(_image.getWidth(null), _image.getHeight(null), transparency);
                    /* intended fall-through, we need to restore the image anyway */
                case VolatileImage.IMAGE_RESTORED:
                    do {
                        _buffer.getGraphics().drawImage(_image, 0, 0, null);
                    } while (_buffer.contentsLost());
            }

            final Graphics2D g2 = (Graphics2D) g;

            final int width = getWidth();
            final int height = getHeight();

            /* find suitable image position */
            double x = (width - _imageWidth * _scaleFactor) / 2 + _translationX;
            double y = (height - _imageHeight * _scaleFactor) / 2 + _translationY;

            /* Modify transform */
            _transform.setToIdentity();
            _transform.translate(x, y);
            _transform.scale(_scaleFactor, _scaleFactor);

            /* Draw the image buffer */
            g2.drawImage(_buffer, _transform, null);
        }
    }

    /**
     * If the set scale mode indicates that the image size should adapt automatically following a given rule, this
     * method adapts the scale factor to the current canvas size. If the current scale mode is set to
     * {@link net.sarcommand.swingextensions.imagepanel.JImagePanel#SCALE_MANUALLY}, this method has no effect.
     *
     * This method will be invoked whenever the scale mode is modified or the component size changes.
     */
    private void updateScaleFactor() {
        switch (_scaleMode) {
            case SCALE_VERTICAL:
                _scaleFactor = ((double) getHeight()) / _imageHeight;
                break;
            case SCALE_HORIZONTAL:
                _scaleFactor = ((double) getWidth()) / _imageWidth;
                break;
            case SCALE_BOTH:
                final double scaleX = ((double) getHeight()) / _imageHeight;
                final double scaleY = ((double) getWidth()) / _imageWidth;
                _scaleFactor = Math.min(scaleX, scaleY);
                break;
        }
    }

    /**
     * Returns the rate at which the scale factor changes when the user moves the mouse wheel. A rate of 1 is
     * considered a change of 3% per wheel event.
     *
     * @return Rate at which scale factor changes.
     */
    public double getScaleRate() {
        return _scaleRate;
    }

    /**
     * Sets the rate at which the scale factor changes when the user moved the mouse wheel. A rate of 1 is considered
     * a change of 3% per wheel event.
     *
     * @param scaleRate the rate at which the scale factor changes when the user moves the mouse wheel
     */
    public void setScaleRate(final double scaleRate) {
        _scaleRate = scaleRate;
    }

    /**
     * Returns the event mask currently used to determine when the image should be dragged.
     *
     * @return Current event mask.
     * @see JImagePanel#setDragMask
     */
    public int getDragMask() {
        return _dragMask;
    }

    /**
     * Sets the event mask used to determine whether the image should be tanslated when receiving a mouseDragged event.
     * The default mask is {@link MouseEvent#BUTTON1_MASK}, which means the image will be translated when the user
     * drags the mouse with the first mouse button pressed. You could for example change this behaviour to {@link
     * KeyEvent#CTRL_MASK} & {@link MouseEvent#BUTTON1_MASK} if you wanted the image to translate only if the control
     * key was pressed.
     *
     * @param dragMask New event mask.
     */
    public void setDragMask(final int dragMask) {
        _dragMask = dragMask;
    }

    /**
     * Returns true if the image may be scaled manually, false otherwise.
     *
     * @return whether the image may be scaled manually.
     */
    public boolean isScalable() {
        return _scalable;
    }

    /**
     * Determines whether the image may be scaled manually.
     *
     * @param scalingAllowed true if scaling should be allowed, false otherwise.
     */
    public void setScalable(final boolean scalingAllowed) {
        _scalable = scalingAllowed;
    }

    /**
     * Returns true if the image may be dragged using the mouse, false otherwise.
     *
     * @return whether the image may be dragged.
     */
    public boolean isDraggable() {
        return _draggable;
    }

    /**
     * Determines whether the image may be dragged using the mouse.
     *
     * @param draggable true if dragging should be allowed, false otherwise
     */
    public void setDraggable(final boolean draggable) {
        _draggable = draggable;
        setCursor(Cursor.getPredefinedCursor(draggable ? Cursor.MOVE_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    /**
     * Reverts the affine transform which is used when display the image to the identity transform. This means: <li>The
     * scale factor will be reset to 1, scaling the image to it's original size</li> <li>The translation in both
     * directions will be set to 0, centering the image in the panel</li> <li>The scale mode will be reset to manual
     * scaling.</li>
     */
    public void setToIdentity() {
        _translationX = 0;
        _translationY = 0;
        _scaleFactor = 1;
        _scaleMode = SCALE_MANUALLY;
        repaint();
    }

    /**
     * Returns the current scale mode.
     *
     * @see JImagePanel#setScaleMode
     * @return the current scale mode.
     */
    public int getScaleMode() {
        return _scaleMode;
    }

    /**
     * Sets the scale mode property. The scale mode determines in which way the JImagePanel will try to fit the image to
     * the panels size: <li>{@link JImagePanel#SCALE_MANUALLY} scales the image according to the user's mouse wheel
     * interaction</li> <li>{@link JImagePanel#SCALE_HORIZONTAL} attempts to fit the image horizontally, regardless of
     * it's height</li> <li>{@link JImagePanel#SCALE_VERTICAL} attempts to fit the image vertically, regardless of it's
     * width. <li>{@link JImagePanel#SCALE_BOTH} tries to find the best fit for the image in both dimensions</li>
     * <p/>
     * Note that as long as scaling is allowed, the user will still be able to change the scale factor using the mouse
     * wheel. The scale mode will then be reset to SCALE_MANUALLY. If you want to 'lock' one of the three automatic
     * scale modes you will have to invoke {@link JImagePanel#setScalable(boolean)} with a 'false' argument.
     *
     * @param scaleMode new scale mode.
     */
    public void setScaleMode(final int scaleMode) {
        _scaleMode = scaleMode;
        updateScaleFactor();
        repaint();
    }

    /**
     * Returns the current scale factor.
     *
     * @return the current scale factor.
     */
    public double getScaleFactor() {
        return _scaleFactor;
    }

    /**
     * Set's the scale factor to be used by this image panel.
     *
     * @param scaleFactor new scale factor
     */
    public void setScaleFactor(final double scaleFactor) {
        _scaleFactor = scaleFactor;
        repaint();
    }

    /**
     * Returns the translation in x-direction.
     *
     * @return the translation in x-direction.
     */
    public double getTranslationX() {
        return _translationX;
    }

    /**
     * Sets the translation in x-direction.
     *
     * @param translationX the translation in x-direction.
     */
    public void setTranslationX(final double translationX) {
        _translationX = translationX;
    }

    /**
     * Returns the translation in y-direction.
     *
     * @return the translation in y-direction.
     */
    public double getTranslationY() {
        return _translationY;
    }

    /**
     * Sets the translation in y-direction.
     *
     * @param translationY the translation in y-direction.
     */
    public void setTranslationY(final double translationY) {
        _translationY = translationY;
    }

    /**
     * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
     */
    public Dimension getPreferredScrollableViewportSize() {
        return _image == null ? new Dimension(20, 20) : new Dimension(_imageWidth, _imageHeight);
    }

    /**
     * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    /**
     * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight() {
        return true;
    }

    /**
     * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    /**
     * @see Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    /**
     * @see Component#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        updateScaleFactor();
    }
}
