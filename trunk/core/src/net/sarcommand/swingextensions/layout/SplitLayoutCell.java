package net.sarcommand.swingextensions.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class realizes one cell inside a MultiCellSplitPane. It is only used internally and you should not have
 * to bother with it directly at any point.
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
public class SplitLayoutCell extends JComponent {
    private Component _component;

    private SplitLayoutCell _nextCell;

    private Size _layoutSize;
    private int _lastDrag;

    private boolean _horizontal;

    public SplitLayoutCell(final Component comp, final int orientation) {
        if (orientation != SwingConstants.HORIZONTAL && orientation != SwingConstants.VERTICAL)
            throw new IllegalArgumentException("Illegal orientation: " + orientation);

        _horizontal = orientation == SwingConstants.HORIZONTAL;
        _component = comp;
        final Dimension preferredSize = _component.getPreferredSize();
        _layoutSize = new Size(preferredSize.width, preferredSize.height);

        setLayout(new BorderLayout());
        add(_component, BorderLayout.CENTER);
    }

    public void setNextCell(final SplitLayoutCell cell) {
        _nextCell = cell;
        addDivider();
    }

    public boolean canModifyVariableSize(final int delta) {
        return ((_horizontal ? _layoutSize.width : _layoutSize.height)
                + delta) > (_horizontal ? getMinimumSize().width :
                getMinimumSize().height);
    }

    public void modifyVariableSize(final int delta) {
        if (_horizontal)
            _layoutSize.width += delta;
        else
            _layoutSize.height += delta;
    }

    protected void addDivider() {
        final JComponent divider = new JPanel();
        divider.setOpaque(false);
        final Dimension size = _horizontal ? new Dimension(2, 10) : new Dimension(10, 2);
        divider.setMinimumSize(size);
        divider.setPreferredSize(size);
        divider.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                _lastDrag = _horizontal ? e.getXOnScreen() : e.getYOnScreen();
            }
        });
        divider.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (_horizontal) {
                    final int xOnScreen = e.getXOnScreen();
                    int delta = xOnScreen - _lastDrag;
                    _lastDrag = xOnScreen;
                    if (canModifyVariableSize(delta) && _nextCell.canModifyVariableSize(-delta)) {
                        modifyVariableSize(delta);
                        _nextCell.modifyVariableSize(-delta);
                        ((JComponent) getParent()).revalidate();
                    }
                } else {
                    final int yOnScreen = e.getYOnScreen();
                    int delta = yOnScreen - _lastDrag;
                    _lastDrag = yOnScreen;
                    if (canModifyVariableSize(delta) && _nextCell.canModifyVariableSize(-delta)) {
                        modifyVariableSize(delta);
                        _nextCell.modifyVariableSize(-delta);
                        ((JComponent) getParent()).revalidate();
                    }
                }
            }
        });
        divider.setCursor(Cursor.getPredefinedCursor(_horizontal ? Cursor.W_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR));
        add(divider, _horizontal ? BorderLayout.EAST : BorderLayout.SOUTH);
    }

    public Size getLayoutSize() {
        return _layoutSize;
    }

    public void setLayoutSize(final Size d) {
        _layoutSize = d;
    }

    public String toString() {
        return "SplitLayoutCell[" + _component + "]";
    }
}
