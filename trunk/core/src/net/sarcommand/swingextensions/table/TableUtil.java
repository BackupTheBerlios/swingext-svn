package net.sarcommand.swingextensions.table;

import javax.swing.*;
import javax.swing.table.*;

/**
 * This class holds utility methods which may be useful when dealing with JTables.
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
public class TableUtil {
    /**
     * This method will iterate over the given table's columns and attempt to find the optimal width for each. You
     * might want to do so after displaying a table for the first time or after updating the underlying model to
     * ensure that as much information as possible is shown.
     *
     * @param table The JTable to adapt, non-null.
     */
    public static void setPreferredColumnSizes(final JTable table) {
        if (table == null)
            throw new IllegalArgumentException("Parameter 'table' must not be null!");

        final int rowCount = table.getRowCount();
        final int columnCount = table.getColumnCount();

        final TableColumnModel columnModel = table.getColumnModel();
        for (int col = 0; col < columnCount; col++) {
            int max = 0;
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
     * This is a purely static class, so it can't be instanciated.
     */
    private TableUtil() {
    }
}
