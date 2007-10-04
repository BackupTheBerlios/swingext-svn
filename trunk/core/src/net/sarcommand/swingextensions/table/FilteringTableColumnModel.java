package net.sarcommand.swingextensions.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.prefs.Preferences;

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
 * If you supply a preferences instance and key, the model will keep track of the visibility state of your columns
 * between sessions.
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

    private Preferences _preferences;
    private String _preferenceKey;

    public FilteringTableColumnModel() {
        initialize();
    }

    public FilteringTableColumnModel(final Preferences preferences, final String preferenceKey) {
        initialize();
        setPreferences(_preferences);
        setPreferenceKey(_preferenceKey);
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
    public void setColumnVisible(final Object identifier, final boolean visible) {
        if (visible) {
            final TableColumn tableColumn = _hiddenColumnMapping.get(identifier);
            super.addColumn(tableColumn);
            moveColumn(getColumnIndex(identifier), getIndexForHiddenColumn(tableColumn));
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
        if (_preferences != null && _preferenceKey != null) {
            final String key = _preferenceKey + '.' + identifier;
            _preferences.putBoolean(key, visible);
        }
    }

    public void setPreferences(final Preferences prefs) {
        _preferences = prefs;
        updateFromPreferences();
    }

    public Preferences getPreferences() {
        return _preferences;
    }

    public String getPreferenceKey() {
        return _preferenceKey;
    }

    public void setPreferenceKey(final String preferenceKey) {
        _preferenceKey = preferenceKey;
        updateFromPreferences();
    }

    public void addColumn(final TableColumn aColumn) {
        super.addColumn(aColumn);
        if (_preferences != null && _preferenceKey != null) {
            final Object identifier = aColumn.getIdentifier();
            final boolean visible = _preferences.getBoolean(_preferenceKey + '.' + identifier, true);
            if (!visible)
                setColumnVisible(identifier, false);
        }

    }

    /**
     * Updates the visibility state for all currently installed columns from the preferences, if applicable.
     */
    protected void updateFromPreferences() {
        if (_preferences != null && _preferenceKey != null) {
            final int columnCount = getColumnCount();
            final boolean[] visible = new boolean[columnCount];
            int visibleCount = 0;
            for (int i = 0; i < columnCount; i++) {
                final TableColumn tableColumn = getColumn(i);
                final Object identifier = tableColumn.getIdentifier();
                visible[i] = _preferences.getBoolean(_preferenceKey + '.' + identifier, true);
                if (visible[i])
                    visibleCount++;
            }
            for (int i = 0; i < visible.length; i++) {
                if (i == 0 && visibleCount == 0)
                    setColumnVisible(getColumn(i).getIdentifier(), true);
                else if (!visible[i])
                    setColumnVisible(getColumn(i).getIdentifier(), false);
            }
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

    /**
     * Returns an ordered collection of the column identifiers, regardless of whether or not they are visible.
     *
     * @return an ordered collection of the column identifiers, regardless of whether or not they are visible.
     */
    public Collection<Object> getAllColumnIdentfiers() {
        final Enumeration<TableColumn> enumeration = getColumns();
        final LinkedList<Object> result = new LinkedList<Object>();

        while (enumeration.hasMoreElements())
            result.add(enumeration.nextElement().getIdentifier());

        for (TableColumn col : _hiddenColumnMapping.values())
            result.add(getIndexForHiddenColumn(col), col.getIdentifier());
        return result;
    }

    /**
     * Returns the index at which a hidden column will be put when made visible again.
     *
     * @param tableColumn TableColumn being queried.
     * @return Index of the column if it were to be made visible.
     */
    protected int getIndexForHiddenColumn(final TableColumn tableColumn) {
        TableColumn runner = tableColumn;
        while (runner != null && _hiddenColumnMapping.containsKey(runner.getIdentifier()))
            runner = _predecessorMapping.get(runner);
        return runner == null ? 0 : getColumnIndex(runner.getIdentifier()) + 1;
    }
}
