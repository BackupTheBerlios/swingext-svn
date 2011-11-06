package net.sarcommand.swingextensions.imagepanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Feb 22, 2010
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class JImagePanel2 extends JViewport {
    private ImagePanel _imagePanel;
    private JLayeredPane _layeredPane;
    private BufferedImage _image;

    private ArrayList<JComponent> _overlays;

    public JImagePanel2() {
        _overlays = new ArrayList<JComponent>();

        _imagePanel = new ImagePanel();

        _layeredPane = new JLayeredPane();
        _layeredPane.add(_imagePanel, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(final ComponentEvent e) {
                final Dimension newSize = _layeredPane.getSize();
                _imagePanel.setSize(newSize);
                for (JComponent overlay : _overlays)
                    overlay.setSize(newSize);
            }
        });

        setView(_layeredPane);

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(final MouseWheelEvent e) {
                final double increment = e.getUnitsToScroll() > 0 ? 1.05 : 0.95;
                final double newScale = _imagePanel.getScaleFactor() * increment;
                setScale(newScale);
            }
        });

        _imagePanel.setFocusable(true);
        _imagePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                double increment = 0;
                if (e.getKeyChar() == '+')
                    increment = 1.05;
                else if (e.getKeyChar() == '-')
                    increment = 0.95;
                else
                    return;
                final double newScale = _imagePanel.getScaleFactor() * increment;
                setScale(newScale);
            }
        });
    }

    public void setImage(final BufferedImage image) {
        _image = image;
        _imagePanel.setImage(image);
        contentUpdated();
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
    }

    private void contentUpdated() {
        if (_image != null) {
            final double scaleFactor = _imagePanel.getScaleFactor();
            final Dimension dim = new Dimension((int) (_image.getWidth() * scaleFactor),
                    (int) (_image.getHeight() * scaleFactor));
            _layeredPane.setSize(dim);
            _layeredPane.setMinimumSize(dim);
            _layeredPane.setPreferredSize(dim);
            repaint();
        }
    }

    public void setScale(final double scale) {
        final Rectangle viewRect = getVisibleRect();
        final Point viewPosition = getViewPosition();
        final Dimension size0 = getViewSize();

        final double halfWidth = viewRect.width / 2.;
        final double halfHeight = viewRect.height / 2.;

        final double relX = (viewPosition.x + halfWidth) / size0.getWidth();
        final double relY = (viewPosition.y + halfHeight) / size0.getHeight();

        _imagePanel.setScaleFactor(scale);
        validate();

        final Dimension size1 = getViewSize();
        setViewPosition(new Point((int) (Math.max(0, (relX * size1.width) - halfWidth)),
                (int) (Math.max(0, (relY * size1.getHeight()) - halfHeight))));

        contentUpdated();
    }

    public Point2D pixelToJava2D(final Point2D source) {
        return pixelToJava2D(source.getX(), source.getY(), null);
    }

    public Point2D pixelToJava2D(final Point2D source, Point2D dest) {
        return pixelToJava2D(source.getX(), source.getY(), dest);
    }

    public Point2D pixelToJava2D(final double sourceX, final double sourceY) {
        return pixelToJava2D(sourceX, sourceY, null);
    }

    public Point2D pixelToJava2D(final double sourceX, final double sourceY, Point2D dest) {
        if (dest == null)
            dest = new Point2D.Double();
        final double v = _imagePanel.getScaleFactor();
        dest.setLocation(sourceX * v, sourceY * v);
        return dest;
    }

    public Point2D java2DToPixel(final Point2D source) {
        return java2DToPixel(source, null);
    }

    public Point2D java2DToPixel(final Point2D source, Point2D dest) {
        return java2DToPixel(source.getX(), source.getY(), dest);
    }

    public Point2D java2DToPixel(final double sourceX, final double sourceY) {
        return java2DToPixel(sourceX, sourceY, null);
    }

    public Point2D java2DToPixel(final double sourceX, final double sourceY, Point2D dest) {
        if (dest == null)
            dest = new Point2D.Double();
        final double v = _imagePanel.getScaleFactor();
        dest.setLocation(sourceX / v, sourceY / v);
        return dest;
    }

    public Dimension getImageSize() {
        return new Dimension(_image.getWidth(), _image.getHeight());
    }

    public Dimension getScaledImageSize() {
        final double v = _imagePanel.getScaleFactor();
        return new Dimension((int) (_image.getWidth() * v), (int) (_image.getHeight() * v));
    }

    public void removeAllOverlays() {
        removeAllOverlays(JLayeredPane.PALETTE_LAYER);
    }

    protected static class ImagePanel extends JPanel {
        private BufferedImage _image;
        private double _scaleFactor;

        public ImagePanel() {
            _scaleFactor = 1.0d;
        }

        public void setScaleFactor(final double scaleFactor) {
            _scaleFactor = scaleFactor;
        }

        public double getScaleFactor() {
            return _scaleFactor;
        }

        public void setImage(final BufferedImage image) {
            _image = image;
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D) g;
            if (_image != null) {
                g2.scale(_scaleFactor, _scaleFactor);
                g2.drawImage(_image, 0, 0, null);
                g2.scale(1 / _scaleFactor, 1 / _scaleFactor);
            }
        }
    }

    public void addOverlay(final JComponent overlay) {
        addOverlay(overlay, JLayeredPane.PALETTE_LAYER);
    }

    public void addOverlay(final JComponent overlay, final Integer layer) {
        _overlays.add(overlay);
        _layeredPane.add(overlay, layer);
        overlay.setSize(_layeredPane.getSize());
    }

    public void removeOverlay(final JComponent overlay) {
        _overlays.remove(overlay);
        _layeredPane.remove(overlay);
    }

    public void removeAllOverlays(final Integer layer) {
        final Component[] componentsInLayer = _layeredPane.getComponentsInLayer(layer);
        for (Component c : componentsInLayer)
            _layeredPane.remove(c);
    }

    public BufferedImage getImage() {
        return _image;
    }
}
