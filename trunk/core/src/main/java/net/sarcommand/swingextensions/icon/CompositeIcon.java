package net.sarcommand.swingextensions.icon;

import javax.swing.*;
import java.awt.*;

/**
 * This class implements an icon which is a composite of a number of other icons. The swing components all allow just
 * a single icon to be set (so do the default renderers). However, on some occasions, for instance when using icons to
 * visualize flags associated with a node in a JTree, you might want to have multiple icons. This class offers a simple
 * matrix which can be filled with your icons, which will then be painted as one.
 * <p/>
 * Note: This class does not use an image buffer but rather paints the icons directly. Any changes you make to one
 * of your icons after instanciating the CompositeIcon will therefore be taken into account.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
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
public class CompositeIcon implements Icon {
    /**
     * The icon matrix. Update width and height if you make any changes here.
     */
    protected Icon[][] _icons;

    /**
     * The composite icon's width.
     */
    protected int _width;

    /**
     * The composite icon's height.
     */
    protected int _height;

    /**
     * Creates a new composite icon placing all specified icons in a row.
     *
     * @param icons Icons to display.
     */
    public CompositeIcon(final Icon... icons) {
        this(1, icons.length, icons);
    }

    /**
     * Creates a new composite icon which displays a list of icons in a matrix.
     *
     * @param rows    Number of rows in the matrix.
     * @param columns Number of columns in the matrix.
     * @param icons   Icons to display.
     */
    public CompositeIcon(final int rows, final int columns, final Icon... icons) {
        if (icons.length == 0)
            throw new IllegalArgumentException("No icons specified");
        if (icons.length > rows * columns)
            throw new IllegalArgumentException("Too many icons specified, expected a maximum of " + rows * columns + " icons");
        _icons = new Icon[rows][columns];

        int row = 0, col = 0, colWidth = 0, maxWidth = 0, rowHeight = 0, maxHeight = 0;
        for (Icon i : icons) {
            _icons[row][col++] = i;
            rowHeight = Math.max(rowHeight, i.getIconHeight());
            colWidth += i.getIconWidth();
            if (col == columns) {
                row++;
                col = 0;
                maxHeight += rowHeight;
                maxWidth = Math.max(colWidth, maxWidth);
            }
        }
        _width = maxWidth;
        _height = maxHeight;
    }

    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return _height;
    }

    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return _width;
    }

    /**
     * Draw the icon at the specified location.  Icon implementations
     * may use the Component argument to get properties useful for
     * painting, e.g. the foreground or background color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        final int colWidth = _width / _icons[0].length;
        final int rowHeight = _height / _icons.length;

        for (int row = 0; row < _icons.length; row++) {
            for (int col = 0; col < _icons[row].length; col++) {
                final Icon icon = _icons[row][col];
                if (icon == null)
                    break;
                final int insetX = (colWidth - icon.getIconWidth()) / 2;
                final int insetY = (rowHeight - icon.getIconHeight()) / 2;

                icon.paintIcon(c, g, x + (col * colWidth) + insetX, y + (row * rowHeight) + insetY);
            }
        }
    }
}
