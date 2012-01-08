package net.sarcommand.swingextensions.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * This class holds utility methods which may be useful when dealing with JTables.
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
public class TableUtil {

    /**
     * This method will iterate over the given table's columns and attempt to find the optimal width for each. You might
     * want to do so after displaying a table for the first time or after updating the underlying model to ensure that
     * as much information as possible is shown.
     *
     * @param table The JTable to adapt, non-null.
     */
    public static void setPreferredColumnWidths(final JTable table) {
        setPreferredColumnWidths(table, true);
    }

    /**
     * This method will iterate over the given table's columns and attempt to find the optimal width for each. You might
     * want to do so after displaying a table for the first time or after updating the underlying model to ensure that
     * as much information as possible is shown.
     *
     * @param table         The JTable to adapt, non-null.
     * @param includeHeader whether or not the header should be included.
     */
    public static void setPreferredColumnWidths(final JTable table, final float[] columnWeigths,
                                                final boolean includeHeader) {
        if (table == null)
            throw new IllegalArgumentException("Parameter 'table' must not be null!");
        if (columnWeigths == null)
            throw new IllegalArgumentException("Parameter 'columnWeigths' must not be null!");


        final int columnCount = table.getColumnCount();
        final int rowCount = table.getRowCount();

        if (columnWeigths != null && columnWeigths.length != columnCount)
            throw new IllegalArgumentException("Illegal number of column weights, has to be equal to column count.");


        final TableColumnModel columnModel = table.getColumnModel();

        float totalWeight = 0;
        for (float weight : columnWeigths)
            totalWeight += weight;
        if (totalWeight == 0)
            totalWeight = 1;

        for (int col = 0; col < columnCount; col++) {
            int max = 0;
            if (includeHeader && table.getTableHeader() != null) {
                final TableColumn column = columnModel.getColumn(col);
                final TableCellRenderer columnRenderer = column.getCellRenderer();
                final TableCellRenderer renderer = columnRenderer != null ? columnRenderer :
                        table.getTableHeader().getDefaultRenderer();
                final int width = renderer.getTableCellRendererComponent(table,
                        column.getHeaderValue(), false, table.hasFocus(), -1, col).getMinimumSize().width;
                if (width > max)
                    max = width;
            }
            for (int row = 0; row < rowCount; row++) {
                final int width = table.getCellRenderer(row, col).getTableCellRendererComponent(table,
                        table.getValueAt(row, col), table.isCellSelected(row, col), table.hasFocus(),
                        row, col).getMinimumSize().width;
                if (width > max)
                    max = width;
            }
            final TableColumn column = columnModel.getColumn(col);
            column.setMinWidth(max);
            column.setPreferredWidth(Math.round(Integer.MAX_VALUE * (columnWeigths[col] / totalWeight)));
        }
    }

    /**
     * This method will iterate over the given table's columns and attempt to find the optimal width for each. You might
     * want to do so after displaying a table for the first time or after updating the underlying model to ensure that
     * as much information as possible is shown.
     *
     * @param table         The JTable to adapt, non-null.
     * @param includeHeader whether or not the header should be included.
     */
    public static void setPreferredColumnWidths(final JTable table, final boolean includeHeader) {
        if (table == null)
            throw new IllegalArgumentException("Parameter 'table' must not be null!");

        final int rowCount = table.getRowCount();
        final int columnCount = table.getColumnCount();

        final TableColumnModel columnModel = table.getColumnModel();

        for (int col = 0; col < columnCount; col++) {
            int max = 0;
            if (includeHeader && table.getTableHeader() != null) {
                final TableColumn column = columnModel.getColumn(col);
                final TableCellRenderer columnRenderer = column.getCellRenderer();
                final TableCellRenderer renderer = columnRenderer != null ? columnRenderer :
                        table.getTableHeader().getDefaultRenderer();
                final int width = renderer.getTableCellRendererComponent(table,
                        column.getHeaderValue(), false, table.hasFocus(), -1, col).getPreferredSize().width;
                if (width > max)
                    max = width;
            }
            for (int row = 0; row < rowCount; row++) {
                final int width = table.getCellRenderer(row, col).getTableCellRendererComponent(table,
                        table.getValueAt(row, col), table.isCellSelected(row, col), table.hasFocus(),
                        row, col).getPreferredSize().width;
                if (width > max)
                    max = width;
            }
            columnModel.getColumn(col).setPreferredWidth(max);
        }
    }

    /**
     * This method will iterate over the given table's rows and attempt to find the optimal height for each. You might
     * want to invoke this method whenever the table's model has been updated or the column margin's change. You can
     * obtain a utility class which does just that for you using the RowHeightAdapter class.
     *
     * @param table Table to adapt, non-null.
     */
    public static void setPreferredRowHeights(final JTable table) {
        if (table == null)
            throw new IllegalArgumentException("Parameter 'table' must not be null!");

        final int rowCount = table.getRowCount();
        final int columnCount = table.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            int max = 0;
            for (int col = 0; col < columnCount; col++) {
                final Component renderer = table.getCellRenderer(row, col).getTableCellRendererComponent(table,
                        table.getValueAt(row, col), table.isCellSelected(row, col), table.hasFocus(),
                        row, col);
                final Rectangle rect = table.getCellRect(row, col, true);
                renderer.setSize(rect.width, rect.height);
                final int height = renderer.getPreferredSize().height;
                if (height > max)
                    max = height;
            }
            table.setRowHeight(row, max <= 0 ? table.getRowHeight() : max);
        }
    }

    /**
     * This is a purely static class, so it can't be instanciated.
     */
    private TableUtil() {
    }
}
