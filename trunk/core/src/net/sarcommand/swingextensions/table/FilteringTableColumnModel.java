package net.sarcommand.swingextensions.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.util.*;
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
    private LinkedList<Object> _originalColumnOrder;
    private HashSet<Object> _nonHideables;

    private Preferences _preferences;
    private String _preferenceKey;

    public FilteringTableColumnModel() {
        initialize();
    }

    public FilteringTableColumnModel(final Preferences preferences, final String preferenceKey) {
        initialize();
        setPreferences(preferences);
        setPreferenceKey(preferenceKey);
    }

    protected void initialize() {
        _hiddenColumnMapping = new HashMap<Object, TableColumn>(4);
        _originalColumnOrder = new LinkedList<Object>();
        _nonHideables = new HashSet<Object>(4);
    }

    public void setHideable(final Object identifier, final boolean hideable) {
        if (!hideable)
            _nonHideables.add(identifier);
        else
            _nonHideables.remove(identifier);
    }

    public boolean isHideable(final Object identifier) {
        return !_nonHideables.contains(identifier);
    }

    /**
     * Determines whether a given column should be visible or hidden.
     *
     * @param identifier Identifier of the affected column.
     * @param visible    Visibility flag.
     */
    public void setColumnVisible(final Object identifier, final boolean visible) {
        if (!visible && _nonHideables.contains(identifier))
            return;
        if (visible) {
            final TableColumn tableColumn = _hiddenColumnMapping.get(identifier);
            super.addColumn(tableColumn);
            super.moveColumn(getColumnIndex(identifier), getIndexForHiddenColumn(tableColumn));
            _hiddenColumnMapping.remove(identifier);
        } else {
            final TableColumn tableColumn = getColumn(getColumnIndex(identifier));
            _hiddenColumnMapping.put(identifier, tableColumn);
            super.removeColumn(tableColumn);
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

    public void removeColumn(final TableColumn column) {
        super.removeColumn(column);
        _originalColumnOrder.remove(column.getIdentifier());
        if (_hiddenColumnMapping.containsKey(column))
            _hiddenColumnMapping.remove(column);
    }

    public void addColumn(final TableColumn aColumn) {
        super.addColumn(aColumn);
        if (_originalColumnOrder.contains(aColumn.getIdentifier()))
            _originalColumnOrder.remove(aColumn.getIdentifier());
        _originalColumnOrder.add(aColumn.getIdentifier());

        if (_preferences != null && _preferenceKey != null) {
            final Object identifier = aColumn.getIdentifier();
            final boolean visible = _preferences.getBoolean(_preferenceKey + '.' + identifier, true);
            if (!visible)
                setColumnVisible(identifier, false);
        }
    }

    public void moveColumn(final int columnIndex, final int newIndex) {
        super.moveColumn(columnIndex, newIndex);
        _originalColumnOrder.add(newIndex, _originalColumnOrder.remove(columnIndex));
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
        return Collections.unmodifiableList(_originalColumnOrder);
    }

    /**
     * Returns the index at which a hidden column will be put when made visible again.
     *
     * @param tableColumn TableColumn being queried.
     * @return Index of the column if it were to be made visible.
     */
    protected int getIndexForHiddenColumn(final TableColumn tableColumn) {
        Object predecessor = null;
        for (int i = _originalColumnOrder.indexOf(tableColumn.getIdentifier()) - 1; i >= 0 && predecessor == null; i--)
        {
            final Object identifier = _originalColumnOrder.get(i);
            if (!_hiddenColumnMapping.containsKey(identifier))
                predecessor = identifier;
        }
        return predecessor == null ? 0 : (super.getColumnIndex(predecessor) + 1);
    }
}
