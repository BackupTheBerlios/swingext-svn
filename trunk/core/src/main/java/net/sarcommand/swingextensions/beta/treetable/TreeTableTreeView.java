package net.sarcommand.swingextensions.beta.treetable;

import javax.swing.*;
import java.awt.*;

/**
 * BETA
 * <p/>
 * 8/4/11
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */

/*
 * Copyright 2005-2011 Torsten Heup
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

public class TreeTableTreeView extends JTree {
    protected int _visibleRow;
    protected boolean _selected;
    private final JTreeTable _parent;

    public TreeTableTreeView(final JTreeTable parent, final TreeTableModel model) {
        _parent = parent;
        setModel(model);
    }

    @Override
    public void paint(final Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.translate(0, -_visibleRow * getRowHeight());
        super.paint(graphics);
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
    }

    public void select(final int visibleRow, final boolean selected) {
        _visibleRow = visibleRow;
        _selected = selected;
        setBackground(selected ? _parent.getSelectionBackground() : _parent.getBackground());
    }
}
