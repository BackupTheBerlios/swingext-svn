package net.sarcommand.swingextensions.imagepanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

/**
 * Feb 22, 2010
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class JImagePanel2 extends JViewport {
    private ImagePanel _imagePanel;

    public JImagePanel2() {
        _imagePanel = new ImagePanel();
        setView(_imagePanel);

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
        _imagePanel.setImage(image);
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
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
    }

    protected static class ImagePanel extends JPanel {
        private BufferedImage _image;
        private double _scaleFactor;

        public ImagePanel() {
            _scaleFactor = 1.0d;
        }

        public void setScaleFactor(final double scaleFactor) {
            _scaleFactor = scaleFactor;
            contentUpdated();
        }

        public double getScaleFactor() {
            return _scaleFactor;
        }

        public void setImage(final BufferedImage image) {
            _image = image;
            contentUpdated();
        }

        private void contentUpdated() {
            if (_image != null) {
                final Dimension dim = new Dimension((int) (_image.getWidth() * _scaleFactor),
                        (int) (_image.getHeight() * _scaleFactor));
                setSize(dim);
                setMinimumSize(dim);
                setPreferredSize(dim);
                repaint();
            }
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
}
