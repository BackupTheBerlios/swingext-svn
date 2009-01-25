package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * todo [heup] add docs
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
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
public class TreeTableViewportRenderer extends JViewport implements TableCellRenderer {

    /**
     * Returns the component used for drawing the cell.  This method is
     * used to configure the renderer appropriately before drawing.
     *
     * @param table      the <code>JTable</code> that is asking the
     *                   renderer to draw; can be <code>null</code>
     * @param value      the value of the cell to be rendered.  It is
     *                   up to the specific renderer to interpret
     *                   and draw the value.  For example, if
     *                   <code>value</code>
     *                   is the string "true", it could be rendered as a
     *                   string or it could be rendered as a check
     *                   box that is checked.  <code>null</code> is a
     *                   valid value
     * @param isSelected true if the cell is to be rendered with the
     *                   selection highlighted; otherwise false
     * @param hasFocus   if true, render cell appropriately.  For
     *                   example, put a special border on the cell, if
     *                   the cell can be edited, render in the color used
     *                   to indicate editing
     * @param row        the row index of the cell being drawn.  When
     *                   drawing the header, the value of
     *                   <code>row</code> is -1
     * @param column     the column index of the cell being drawn
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final JTree tree = (JTree) value;

        final Rectangle rectangle = tree.getRowBounds(row);
        if (rectangle == null)
            return null;
        tree.repaint();    //??
        setView(tree);
        setViewPosition(new Point(tree.getX(), rectangle.y));
        setViewSize(new Dimension(tree.getWidth(), rectangle.height));
        return this;
    }
}
