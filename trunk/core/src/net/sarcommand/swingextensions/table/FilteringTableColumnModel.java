package net.sarcommand.swingextensions.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A column model implementation which allows you to show and hide columns in the JTable. You can use the
 * FilteringTableModelPopup class as a conveniance to obtain a context menu:
 * <p/>
 * <code>
 * final JTable table = new JTable();
 * final FilteringTableColumnModel model = new FilteringTableColumnModel();
 * table.setColumnModel(model);
 * table.getTableHeader().setComponentPopup(new FilteringTableColumnModelPopup(model));
 * </code>
 * <p/>
 * <p/>
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
 *
 * @see FilteringTableColumnModelPopup
 */
public class FilteringTableColumnModel extends DefaultTableColumnModel {
    private HashMap<Object, TableColumn> _hiddenColumnMapping;
    private HashMap<TableColumn, TableColumn> _predecessorMapping;

    public FilteringTableColumnModel() {
        initialize();
    }

    protected void initialize() {
        _hiddenColumnMapping = new HashMap<Object, TableColumn>(4);
        _predecessorMapping = new HashMap<TableColumn, TableColumn>(4);
    }

    /**
     * Determines whether a given column should be visible or hidden.
     *
     * @param identifier Identifier of the affected column.
     * @param visible    Visibility flag.
     */
    public void setColumnVisible(final String identifier, final boolean visible) {
        if (visible) {
            final TableColumn tableColumn = _hiddenColumnMapping.get(identifier);
            addColumn(tableColumn);
            moveColumn(getColumnIndex(identifier), getIndexForColumn(tableColumn));
            _hiddenColumnMapping.remove(identifier);
            _predecessorMapping.remove(tableColumn);
        } else {
            final TableColumn tableColumn = getColumn(getColumnIndex(identifier));
            _hiddenColumnMapping.put(identifier, tableColumn);
            final int index = getColumnIndex(identifier);
            if (index != 0)
                _predecessorMapping.put(tableColumn, getColumn(index - 1));
            removeColumn(tableColumn);
        }
    }

    /**
     * Returns whether the given column is currently visible.
     *
     * @param identifier Identifier of the column being queried.
     * @return whether the given column is currently visible.
     */
    public boolean isColumnVisible(final Object identifier) {
        return !_hiddenColumnMapping.containsKey(identifier);
    }

    public Collection<Object> getAllColumnIdentfiers() {
        final Enumeration<TableColumn> enumeration = getColumns();
        final LinkedList<Object> result = new LinkedList<Object>();

        while (enumeration.hasMoreElements())
            result.add(enumeration.nextElement().getIdentifier());
        result.addAll(_hiddenColumnMapping.keySet());
        return result;
    }

    protected int getIndexForColumn(final TableColumn tableColumn) {
        TableColumn runner = tableColumn;
        while (runner != null && _hiddenColumnMapping.containsKey(runner.getIdentifier()))
            runner = _predecessorMapping.get(runner);
        return runner == null ? 0 : getColumnIndex(runner.getIdentifier()) + 1;
    }
}
