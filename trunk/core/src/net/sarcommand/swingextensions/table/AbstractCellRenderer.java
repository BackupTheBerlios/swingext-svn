package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.treetable.JTreeTable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This class can be used as the super class for CellRenderer implementations you write. It offers a set of utility
 * methods named adaptToXXX, where XXX is the component you are writing a renderer for. Calling this utility method
 * will adapt the renderer's color scheme (foreground, background, selection colors etc.) and border to the target
 * component's presets.
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
public abstract class AbstractCellRenderer extends JLabel {
    public AbstractCellRenderer() {
        super();
        setOpaque(true);
    }

    protected void adaptToTable(final JComponent renderer, final JTable table, boolean isSelected,
                                final boolean hasFocus, final int row, final int column) {
        Color fg = null;
        Color bg = null;

        JTable.DropLocation dropLocation = table.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column) {

            fg = UIManager.getColor("Table.dropCellForeground");
            bg = UIManager.getColor("Table.dropCellBackground");

            isSelected = true;
        }

        if (isSelected) {
            renderer.setForeground(fg == null ? table.getSelectionForeground()
                    : fg);
            renderer.setBackground(bg == null ? table.getSelectionBackground()
                    : bg);
        } else {
            renderer.setForeground(table.getForeground());
            renderer.setBackground(table.getBackground());
        }

        renderer.setFont(table.getFont());

        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
            renderer.setBorder(border);

            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = UIManager.getColor("Table.focusCellForeground");
                if (col != null) {
                    renderer.setForeground(col);
                }
                col = UIManager.getColor("Table.focusCellBackground");
                if (col != null) {
                    renderer.setBackground(col);
                }
            }
        } else {
            renderer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
    }

    protected void adaptToTreeTable(final JComponent renderer, final JTreeTable table, boolean isSelected,
                                    final boolean hasFocus, final int row, final int column) {
        if (isSelected) {
            renderer.setForeground(table.getSelectionForeground());
            renderer.setBackground(table.getSelectionBackground());
        } else {
            renderer.setForeground(table.getForeground());
            renderer.setBackground(table.getBackground());
        }

        renderer.setFont(table.getFont());

        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
            renderer.setBorder(border);

            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = UIManager.getColor("Table.focusCellForeground");
                if (col != null) {
                    renderer.setForeground(col);
                }
                col = UIManager.getColor("Table.focusCellBackground");
                if (col != null) {
                    renderer.setBackground(col);
                }
            }
        } else {
            renderer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
    }
}
