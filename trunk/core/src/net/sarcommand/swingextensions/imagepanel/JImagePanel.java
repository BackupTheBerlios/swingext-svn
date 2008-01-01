package net.sarcommand.swingextensions.imagepanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;

/**
 * A JImagePanel is a component which allows you to display images with swing.
 * Other than the JLabel class, which is more suitable for displaying icons or small gifs, the JImagePanel is meant to
 * be used for larger images. It also offers a basic shared interaction as it allows to scale and translate the image
 * directly (these options can of course be disabled). By default, the image can be rescaled usig the mouse wheel and
 * translated by dragging the mouse with the first mouse button pressed.<br><br>
 * <p/>
 * The JImagePanel takes advantage of hardware acceleration as far as it's possible. Note that performance may differ
 * greatly between the different platforms, especially linux based systems without hardware acceleration have been known
 * to perform particularly sluggish. Please bear in mind that the {@link Transparency} of an image also greatly
 * influences the drawing performance.<br><br>
 * <p/>
 * The JImagePanel class offers several static conveniance methods to create preconfigured instances and frames
 * or dialogs wrapping a JImagePanel.
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
     * Constant used to tell the JImagePanel to scale according to the value specified by the shared's input.
     */
    public static final int SCALE_MANUALLY = 103;

    /**
     * Returns a JFrame instance wrapping a JImagePanel. The JImagePanel itself can be accessed using the
     * {@link javax.swing.JFrame#getContentPane()} method. The frame will appear centered on the screen.
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
     * Displays a JFrame wrapping a JImagePanel. The displayed image will not be manually scalable or translatable
     * and will be scaled to fit the frame.
     *
     * @param title Title for the new frame.
     * @param image Image to display.
     */
    public static void showStaticImageFrame(final String title, final Image image) {
        final JFrame frame = getFrameInstance(image, false, false);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Displays a JFrame wrapping a JImagePanel. The shared will be able to scale and translate the image
     * manually.
     *
     * @param title Title fo the new frame.
     * @param image Image to display.
     */
    public static void showDynamicImageFrame(final String title, final Image image) {
        final JFrame frame = getFrameInstance(image, true, true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(title);
        frame.setVisible(true);
    }

    /**
     * Returns a JDialog instance wrapping a JImagePanel. The JImagePanel itself can be accessed using the
     * {@link javax.swing.JDialog#getContentPane()} method. The dialog will appear centered on the screen.
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
     * Displays a JDialog wrapping a JImagePanel. The displayed image will not be manually scalable or translatable
     * and will be scaled to fit the dialog.
     *
     * @param parent Parent frame for the dialog. May be null.
     * @param modal  Whether or not the dialog should be modal
     * @param title  Titel for the new dialog.
     * @param image  Image to display.
     */
    public static void showStaticDialog(final Frame parent, final boolean modal, final String title, final Image image) {
        final JDialog dlg = getDialogInstance(parent, modal, image, false, false);
        dlg.setTitle(title);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setVisible(true);
    }

    /**
     * Displays a JDialog wrapping a JImagePanel. The displayed image will not be manually scalable or translatable
     * and will be scaled to fit the dialog.
     *
     * @param parent Parent frame for the dialog. May be null.
     * @param modal  Whether or not the dialog should be modal
     * @param title  Titel for the new dialog.
     * @param image  Image to display.
     */
    public static void showDynamicDialog(final Frame parent, final boolean modal, final String title, final Image image) {
        final JDialog dlg = getDialogInstance(parent, modal, image, true, true);
        dlg.setTitle(title);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setVisible(true);
    }

    /**
     * Returns a new 'static' JImagePanel instance, meaning the shared will not be allowed to translate or rescale
     * the image by mouse interaction. This is a mere conveniance method.
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
     * Volatile image buffer. The image buffer is necessary as some image types would preform more than lousy
     * if painted directly.
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
    protected double _scaleAmount;

    /**
     * Horizontal translation.
     */
    protected double _transX;

    /**
     * Vertical translation.
     */
    protected double _transY;

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
     * Determines whether the image can be translated manually by the shared.
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
        _scalable = true;

        /* Initialize internal variables */
        _scaleAmount = 1.;
        _transX = 0.;
        _transY = 0.;
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
                    _scaleAmount *= e.getUnitsToScroll() > 0 ? 1 + 0.03 * _scaleRate : 1 - 0.03 * _scaleRate;
                    if (_scaleAmount < 0)
                        _scaleAmount = 0.01;
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
                        final double scaledWidth = _imageWidth * _scaleAmount;
                        final double scaledHeight = _imageHeight * _scaleAmount;
                        final double newX = (width - scaledWidth) / 2 + _transX - dx;
                        final double newY = (height - scaledHeight) / 2 + _transY - dy;

                        if (dx != 0) {
                            if (width > scaledWidth) {
                                if (dx > 0) {
                                    if (newX > 0)
                                        _transX -= dx;
                                } else if (newX + scaledWidth < width)
                                    _transX -= dx;
                            } else {
                                if (dx > 0) {
                                    if (newX + scaledWidth > width)
                                        _transX -= dx;
                                } else if (newX <= 0)
                                    _transX -= dx;
                            }
                        }

                        if (dy != 0) {
                            if (height > scaledHeight) {
                                if (dy > 0) {
                                    if (newY > 0)
                                        _transY -= dy;
                                } else if (newY + scaledHeight < height)
                                    _transY -= dy;
                            } else {
                                if (dy > 0) {
                                    if (newY + scaledHeight > height)
                                        _transY -= dy;
                                } else if (newY <= 0)
                                    _transY -= dy;
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
     * Set's the image to be displayed. The JImagePanel will be updated automatically. Note that the image will
     * be copied to an image buffer to increase performance, so the JImagePanel will not react to changes you
     * apply to the original image by modifying it's data buffer. As this approach is generally discouraged for
     * performance reasons, you will have to invoke setImage again when you altered the image. As this method
     * performs very fast this shouldn't be much of a inconveniance.
     *
     * @param image Image to display.
     */
    public void setImage(final Image image) {
        _image = image;
        _imageWidth = image.getWidth(null);
        _imageHeight = image.getHeight(null);

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

            switch (_scaleMode) {
                case SCALE_VERTICAL:
                    _scaleAmount = ((double) getHeight()) / _imageHeight;
                    break;
                case SCALE_HORIZONTAL:
                    _scaleAmount = ((double) getWidth()) / _imageWidth;
                    break;
                case SCALE_BOTH:
                    final double scaleX = ((double) getHeight()) / _imageHeight;
                    final double scaleY = ((double) getWidth()) / _imageWidth;
                    _scaleAmount = Math.min(scaleX, scaleY);
                    break;
            }

            /* find suitable image position */
            double x = (width - _imageWidth * _scaleAmount) / 2 + _transX;
            double y = (height - _imageHeight * _scaleAmount) / 2 + _transY;

            /* Modify transform */
            _transform.setToIdentity();
            _transform.translate(x, y);
            _transform.scale(_scaleAmount, _scaleAmount);

            /* Draw the image buffer */
            g2.drawImage(_buffer, _transform, null);
        }
    }

    /**
     * Returns the rate at which the scale factor changes when the shared moved the mouse wheel. A rate of
     * 1 is considered a change of 3% per wheel event.
     *
     * @return Rate at which scale factor changes.
     */
    public double getScaleRate() {
        return _scaleRate;
    }

    /**
     * Sets the rate at which the scale factor changes when the shared moved the mouse wheel. A rate of
     * 1 is considered a change of 3% per wheel event.
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
     * Sets the event mask used to determine whether the image should be tanslated when receiving a
     * mouseDragged event. The default mask is {@link MouseEvent#BUTTON1_MASK}, which means the image will
     * be translated when the shared drags the mouse with the first mouse button pressed. You could for example
     * change this behaviour to {@link KeyEvent#CTRL_MASK} & {@link MouseEvent#BUTTON1_MASK} if you wanted the
     * image to translate only if the control key was pressed.
     *
     * @param dragMask New event mask.
     */
    public void setDragMask(final int dragMask) {
        _dragMask = dragMask;
    }

    /**
     * Returns true if the image may be scaled manually, false otherwise.
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
     * Reverts the affine transform which is used when display the image to the identity transform. This means:
     * <li>The scale factor will be reset to 1, scaling the image to it's original size</li>
     * <li>The translation in both directions will be set to 0, centering the image in the panel</li>
     * <li>The scale mode will be reset to manual scaling.</li>
     */
    public void setToIdentity() {
        _transX = 0;
        _transY = 0;
        _scaleAmount = 1;
        _scaleMode = SCALE_MANUALLY;
        repaint();
    }

    /**
     * Returns the current scale mode.
     *
     * @see JImagePanel#setScaleMode
     */
    public int getScaleMode() {
        return _scaleMode;
    }

    /**
     * Sets the scale mode property. The scale mode determines in which way the JImagePanel will try to fit
     * the image to the panels size:
     * <li>{@link JImagePanel#SCALE_MANUALLY} scales the image according to the shared's mouse wheel interaction</li>
     * <li>{@link JImagePanel#SCALE_HORIZONTAL} attempts to fit the image horizontally, regardless of it's height</li>
     * <li>{@link JImagePanel#SCALE_VERTICAL} attempts to fit the image vertically, regardless of it's width.
     * <li>{@link JImagePanel#SCALE_BOTH} tries to find the best fit for the image in both dimensions</li>
     * <p/>
     * Note that as long as scaling is allowed, the shared will still be able to change the scale factor using the
     * mouse wheel. The scale mode will then be reset to SCALE_MANUALLY. If you want to 'lock' one of the three
     * automatic scale modes you will have to invoke {@link JImagePanel#setScalable(boolean)} with a 'false'
     * argument.
     *
     * @param scaleMode new scale mode.
     */
    public void setScaleMode(final int scaleMode) {
        _scaleMode = scaleMode;
        repaint();
    }

    /**
     * Returns the current scale factor.
     */
    public double getScaleAmount() {
        return _scaleAmount;
    }

    /**
     * Set's the scale factor to be used by this image panel.
     *
     * @param scaleAmount new scale factor
     */
    public void setScaleAmount(final double scaleAmount) {
        _scaleAmount = scaleAmount;
    }

    /**
     * Returns the translation in x-direction.
     */
    public double getTransX() {
        return _transX;
    }

    /**
     * Sets the translation in x-direction.
     */
    public void setTransX(final double transX) {
        _transX = transX;
    }

    /**
     * Returns the translation in y-direction.
     */
    public double getTransY() {
        return _transY;
    }

    /**
     * Sets the translation in y-direction.
     */
    public void setTransY(final double transY) {
        _transY = transY;
    }

    public Dimension getPreferredScrollableViewportSize() {
        return _image == null ? new Dimension(20, 20) : new Dimension(_imageWidth, _imageHeight);
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    public boolean getScrollableTracksViewportHeight() {
        return true;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }
}
