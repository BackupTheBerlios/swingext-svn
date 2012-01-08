package net.sarcommand.swingextensions.scrollpane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

/**
 * Implements a satellite view for a given JViewPort (or its enclosing JScrollPane). A satellite view is basically a
 * small 'thumbnail' of the viewports contents, highlighting the current view rectangle. One can use the satellite view
 * to navigate the JViewPort's current view rect using the mouse.
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
 */
public class SatelliteView extends JComponent {
    protected JViewport _peer;
    protected ChangeListener _peerChangeListener;
    protected Paint _viewRectPaint;

    protected Point _lastDrag;

    protected BufferedImage _backBuffer;
    private volatile boolean _contentsUpdated;
    private MouseInputAdapter _mouseInputAdapter;
    private Rectangle _viewHighlightRectangle;

    public SatelliteView() {
        initialize();
    }

    public SatelliteView(final JScrollPane scrollPane) {
        initialize();
        setPeer(scrollPane.getViewport());
    }

    public void setPeer(final JScrollPane scrollPane) {
        setPeer(scrollPane.getViewport());
    }

    public void setPeer(final JViewport viewport) {
        if (_peer != null)
            _peer.removeChangeListener(_peerChangeListener);

        _peer = viewport;
        _peer.addChangeListener(_peerChangeListener);
        contentsUpdated();
    }

    protected void initialize() {
        _peerChangeListener = new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                repaint();
            }
        };

        _mouseInputAdapter = new MouseInputAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                processClick(e.getPoint());
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                final Point p = e.getPoint();
                if (_lastDrag == null)
                    processClick(e.getPoint());
                else
                    processDrag(_lastDrag, p);
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                _lastDrag = null;
            }
        };
        addMouseListener(_mouseInputAdapter);
        addMouseMotionListener(_mouseInputAdapter);

        _viewRectPaint = Color.BLACK;
    }

    protected void processClick(final Point p) {
        _lastDrag = p;
    }

    protected void processDrag(final Point from, final Point to) {
        final Dimension viewSize = _peer.getViewSize();
        final Dimension visibleRect = _peer.getExtentSize();

        final double viewWidth = viewSize.getWidth();
        final double viewHeight = viewSize.getHeight();

        final int width = getWidth();
        final int height = getHeight();

        final double scale = Math.min(width / viewWidth, height / viewHeight);
        final double scaledViewWidth = viewSize.getWidth() * scale;
        final double scaledViewHeight = viewSize.getHeight() * scale;

        final int viewX = (int) Math.round((width - scaledViewWidth) / 2);
        final int viewY = (int) Math.round((height - scaledViewHeight) / 2);

        final double distanceX = ((to.x - from.x) / scaledViewWidth) * viewWidth;
        final double distanceY = ((to.y - from.y) / scaledViewHeight) * viewHeight;

        final Point position = _peer.getViewPosition();

        final Point newPosition = new Point();
        newPosition.x = Math.min(Math.max(0, (int) (position.x + distanceX)),
                (int) (viewSize.getWidth() - visibleRect.getWidth()));
        newPosition.y = Math.min(Math.max(0, (int) (position.y + distanceY)),
                (int) (viewSize.getHeight() - visibleRect.getHeight()));

        _peer.setViewPosition(newPosition);

        _lastDrag = to;
    }

    public Paint getViewRectPaint() {
        return _viewRectPaint;
    }

    public void setViewRectPaint(final Paint viewRectPaint) {
        _viewRectPaint = viewRectPaint;
    }

    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        _contentsUpdated = true;
        super.setBounds(x, y, width, height);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (_peer != null) {
            final JViewport peer = _peer;
            final Component view = peer.getView();
            final Graphics2D g2 = (Graphics2D) g;
            final int width = getWidth();
            final int height = getHeight();

            final Rectangle visibleRect = peer.getViewRect();

            final Dimension viewSize = peer.getViewSize();
            final double viewWidth = viewSize.getWidth();
            final double viewHeight = viewSize.getHeight();

            if (viewWidth <= 0 || viewHeight <= 0)
                return;

            final double scale = Math.min(width / viewWidth, height / viewHeight);
            final double scaledViewWidth = viewWidth * scale;
            final double scaledViewHeight = viewHeight * scale;

            final int viewX = (int) Math.round((width - scaledViewWidth) / 2);
            final int viewY = (int) Math.round((height - scaledViewHeight) / 2);

            if (_contentsUpdated || _backBuffer == null) {
                _backBuffer = getGraphicsConfiguration().createCompatibleImage((int) Math.ceil(scaledViewWidth),
                        (int) Math.ceil(scaledViewHeight));
                final Graphics2D d = _backBuffer.createGraphics();
                d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                d.scale(scale, scale);
                view.paint(d);
                d.scale(1 / scale, 1 / scale);
                d.dispose();
            }

            g2.drawImage(_backBuffer, viewX, viewY, null);

            final int x = Math.max(viewX, viewX + (int) Math.round(scaledViewWidth * (visibleRect.getX() / viewWidth)));
            final int y = Math.max(viewY,
                    viewY + (int) Math.round(scaledViewHeight * (visibleRect.getY() / viewHeight)));

            final int w = (int) Math.min(scaledViewWidth, ((visibleRect.getWidth() / viewWidth)) * scaledViewWidth);
            final int h = (int) Math.min(scaledViewHeight, ((visibleRect.getHeight() / viewHeight)) * scaledViewHeight);

            _viewHighlightRectangle = new Rectangle(x, y, w, h);

            g2.setPaint(new Color(200, 200, 200, 100));
            final Area area = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
            area.subtract(new Area(_viewHighlightRectangle));
            g2.fill(area);

            g2.setPaint(_viewRectPaint);
            g2.drawRect(x, y, w, h);

            _contentsUpdated = false;
        }
    }

    public void contentsUpdated() {
        _contentsUpdated = true;
        repaint();
    }
}
