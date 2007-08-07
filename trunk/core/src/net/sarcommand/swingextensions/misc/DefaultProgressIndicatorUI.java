package net.sarcommand.swingextensions.misc;

import net.sarcommand.swingextensions.utilities.ColorUtilities;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Implements the most common ajax-like look for a progress indicator.
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
public class DefaultProgressIndicatorUI extends ProgressIndicatorUI {
    /**
     * The array of highlighting colors being used.
     */
    protected Color[] _colors;

    /**
     * The base color used for non-highlighted portions.
     */
    protected Color _baseColor;

    /**
     * Timer used to fire periodical update events.
     */
    protected Timer _timer;

    /**
     * The target indicator on which this UI was installed.
     */
    protected JProgressIndicator _target;

    /**
     * A counter variable (0-11) used to decide which branch should be highlighted. Will be updated after
     * 'updateDelay' miliseconds.
     */
    protected int _timerValue;

    public static ComponentUI createUI(JComponent c) {
        if (!(c instanceof JProgressIndicator))
            throw new IllegalArgumentException("Cannot create UI for class " + c.getClass().getName());
        return new DefaultProgressIndicatorUI();
    }

    public DefaultProgressIndicatorUI() {
        initialize();
    }

    public void initialize() {
        _baseColor = new Color(180, 180, 180);
        _colors = new Color[]{
                ColorUtilities.darker(_baseColor, 180),
                ColorUtilities.darker(_baseColor, 150),
                ColorUtilities.darker(_baseColor, 120),
                ColorUtilities.darker(_baseColor, 90),
                ColorUtilities.darker(_baseColor, 60),
                ColorUtilities.darker(_baseColor, 30),
        };
    }

    public void installUI(final JComponent c) {
        _target = (JProgressIndicator) c;
        if (_timer != null)
            stopProgress();
        _timer = new Timer(_target.getUpdateDelay(), new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                timerIncrement();
            }
        });
        _target.setMinimumSize(new Dimension(24, 24));
        _target.setPreferredSize(new Dimension(32, 32));
    }

    public void startProgress() {
        _timerValue = 0;
        _timer.restart();
        _target.repaint();
    }

    public void stopProgress() {
        _timer.stop();
        _target.repaint();
    }

    protected void timerIncrement() {
        _timerValue++;
        if (_timerValue > 11)
            _timerValue = 0;
        _target.repaint();
    }

    public void paint(final Graphics g, final JComponent c) {
        super.paint(g, c);
        final Graphics2D g2 = (Graphics2D) g.create();

        final Rectangle bounds = _target.getBounds();
        g2.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final Point2D center = new Point2D.Double(bounds.width / 2., bounds.height / 2.);
        final double innerInset = bounds.height * .15;
        final double outerInset = ((bounds.height - innerInset) / 2.) - 1;

        g2.setStroke(new BasicStroke(Math.max(bounds.height / 30f, 1.2f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0));
        final Line2D line = new Line2D.Double(0, innerInset, 0, outerInset);
        g2.translate(center.getX(), center.getY());

        final boolean running = _target.isIndicatingProgress();
        if (!running)
            g2.setColor(_baseColor);
        final AffineTransform transform = new AffineTransform();
        for (int i = 0; i < 12; i++) {
            if (running) {
                final int diff = (_timerValue < i ? _timerValue + 12 : _timerValue) - i;
                g2.setColor(diff < _colors.length ? _colors[diff] : _baseColor);
            }

            transform.setToTranslation(center.getX(), center.getY());
            transform.rotate(Math.toRadians(i * 30));
            g2.setTransform(transform);
            g2.draw(line);
        }
    }
}
