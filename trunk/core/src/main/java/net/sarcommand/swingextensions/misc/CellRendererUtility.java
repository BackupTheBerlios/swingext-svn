package net.sarcommand.swingextensions.misc;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * A small utility class which can be used to mimic the DefaultXXXRenderer behaviour for custom renderer
 * implementations. Basically, it offers methods for tables, lists and treetables, which expose the same signature as
 * the according renderer interface, with the addition of a JComponent. The specified component will be configured in
 * the same way as the default renderer implementation regardings fonts, borders and such.
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
public class CellRendererUtility {
    public CellRendererUtility() {
    }

    /**
     * Adapts the given component to the specified table instance, making it look just as a DefaultTableCellRenderer
     * would.
     *
     * @param renderer   Your renderer component.
     * @param table      Table this renderer is used by.
     * @param isSelected see TableCellRenderer.
     * @param hasFocus   see TableCellRenderer.
     * @param row        see TableCellRenderer.
     * @param column     see TableCellRenderer.
     */
    public static void adaptToTable(final JComponent renderer, final JTable table, boolean isSelected,
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
        } else
            renderer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

//    /**
//     * Adapts the given component to the specified treetable instance, making it look just as a
//     * DefaultTreeTableCellRenderer would.
//     *
//     * @param renderer   Your renderer component.
//     * @param table      Table this renderer is used by.
//     * @param isSelected see TreeTableCellRenderer.
//     * @param hasFocus   see TreeTableCellRenderer.
//     * @param row        see TreeTableCellRenderer.
//     * @param column     see TreeTableCellRenderer.
//     */
//    public static void adaptToTreeTable(final JComponent renderer, final JTreeTable table, boolean isSelected,
//                                        final boolean hasFocus, final int row, final int column) {
//        if (isSelected) {
//            renderer.setForeground(table.getSelectionForeground());
//            renderer.setBackground(table.getSelectionBackground());
//        } else {
//            renderer.setForeground(table.getForeground());
//            renderer.setBackground(table.getBackground());
//        }
//
//        renderer.setFont(table.getFont());
//
//        if (hasFocus) {
//            Border border = null;
//            if (isSelected) {
//                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
//            }
//            if (border == null) {
//                border = UIManager.getBorder("Table.focusCellHighlightBorder");
//            }
//            renderer.setBorder(border);
//
//            if (!isSelected && table.isCellEditable(row, column)) {
//                Color col;
//                col = UIManager.getColor("Table.focusCellForeground");
//                if (col != null) {
//                    renderer.setForeground(col);
//                }
//                col = UIManager.getColor("Table.focusCellBackground");
//                if (col != null) {
//                    renderer.setBackground(col);
//                }
//            }
//        } else {
//            renderer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
//        }
//    }

    /**
     * Adapts the given component to the specified table instance, making it look just as a DefaultTableCellRenderer
     * would.
     *
     * @param renderer     Your renderer component.
     * @param list         The list the renderer is being used by.
     * @param value        see ListCellRenderer.
     * @param index        see ListCellRenderer.
     * @param isSelected   see ListCellRenderer.
     * @param cellHasFocus see ListCellRenderer.
     */
    public static void adaptToList(final JComponent renderer, JList list, Object value, int index, boolean isSelected,
                                   boolean cellHasFocus) {

        renderer.setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = UIManager.getColor("List.dropCellBackground");
            fg = UIManager.getColor("List.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            renderer.setBackground(bg == null ? list.getSelectionBackground() : bg);
            renderer.setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            renderer.setBackground(list.getBackground());
            renderer.setForeground(list.getForeground());
        }

        renderer.setEnabled(list.isEnabled());
        renderer.setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("List.focusCellHighlightBorder");
            }
        } else {
            border = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        }

        renderer.setBorder(border);
    }
}
