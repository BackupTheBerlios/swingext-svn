package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class RowSorterStub extends RowSorter<TableModel> {
    private JTable _table;
    private List<SortKey> _sortKeys;

    public RowSorterStub(final JTable table) {
        _table = table;
        _sortKeys = new LinkedList<SortKey>();
    }

    public void allRowsChanged() {

    }

    public int convertRowIndexToModel(int index) {
        return index;
    }

    public int convertRowIndexToView(int index) {
        return index;
    }

    public TableModel getModel() {
        return _table.getModel();
    }

    public int getModelRowCount() {
        return _table.getModel().getRowCount();
    }

    public List<SortKey> getSortKeys() {
        return _sortKeys;
    }

    public int getViewRowCount() {
        return _table.getModel().getRowCount();
    }

    public void modelStructureChanged() {

    }

    public void rowsDeleted(int firstRow, int endRow) {

    }

    public void rowsInserted(int firstRow, int endRow) {

    }

    public void rowsUpdated(int firstRow, int endRow) {

    }

    public void rowsUpdated(int firstRow, int endRow, int column) {

    }

    public void setSortKeys(List list) {
        _sortKeys = list;
    }

    /**
     * Reverses the sort order from ascending to descending (or
     * descending to ascending) if the specified column is already the
     * primary sorted column; otherwise, makes the specified column
     * the primary sorted column, with an ascending sort order.  If
     * the specified column is not sortable, this method has no
     * effect.
     *
     * @param column index of the column to make the primary sorted column,
     *               in terms of the underlying model
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void toggleSortOrder(int column) {
        List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
        SortKey sortKey;
        int sortIndex;
        for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
            if (keys.get(sortIndex).getColumn() == column) {
                break;
            }
        }
        if (sortIndex == -1) {
            // Key doesn't exist
            sortKey = new SortKey(column, SortOrder.ASCENDING);
            keys.add(0, sortKey);
        } else if (sortIndex == 0) {
            // It's the primary sorting key, toggle it
            keys.set(0, toggle(keys.get(0)));
        } else {
            // It's not the first, but was sorted on, remove old
            // entry, insert as first with ascending.
            keys.remove(sortIndex);
            keys.add(0, new SortKey(column, SortOrder.ASCENDING));
        }
        setSortKeys(keys);
        fireSortOrderChanged();
    }

    private SortKey toggle(SortKey key) {
        if (key.getSortOrder() == SortOrder.ASCENDING)
            return new SortKey(key.getColumn(), SortOrder.DESCENDING);
        return new SortKey(key.getColumn(), SortOrder.ASCENDING);
    }
}
