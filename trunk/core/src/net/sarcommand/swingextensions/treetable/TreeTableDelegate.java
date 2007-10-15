package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.EventObject;

/**
 */
public class TreeTableDelegate extends JComponent {
    protected JTable _nestedTable;
    protected JTree _nestedTree;

    public void setShowsRootHandles(final boolean newValue) {
        _nestedTree.setShowsRootHandles(newValue);
    }

    public boolean getShowsRootHandles() {
        return _nestedTree.getShowsRootHandles();
    }

    public boolean isRootVisible() {
        return _nestedTree.isRootVisible();
    }

    public void setRootVisible(final boolean rootVisible) {
        _nestedTree.setRootVisible(rootVisible);
    }

    public Dimension getPreferredScrollableViewportSize() {
        return _nestedTable.getPreferredScrollableViewportSize();
    }

    public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return _nestedTable.getScrollableBlockIncrement(visibleRect, orientation, direction);
    }

    public boolean getScrollableTracksViewportHeight() {
        return _nestedTable.getScrollableTracksViewportHeight();
    }

    public boolean getScrollableTracksViewportWidth() {
        return _nestedTable.getScrollableTracksViewportWidth();
    }

    public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return _nestedTable.getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    public void setToggleClickCount(final int clickCount) {
        _nestedTree.setToggleClickCount(clickCount);
    }

    public int getToggleClickCount() {
        return _nestedTree.getToggleClickCount();
    }

    public JTableHeader getTableHeader() {
        return _nestedTable.getTableHeader();
    }

    public int convertColumnIndexToModel(final int viewColumnIndex) {
        return _nestedTable.convertColumnIndexToModel(viewColumnIndex);
    }

    public int convertColumnIndexToView(final int modelColumnIndex) {
        return _nestedTable.convertColumnIndexToView(modelColumnIndex);
    }

    public int convertRowIndexToModel(final int viewRowIndex) {
        return _nestedTable.convertRowIndexToModel(viewRowIndex);
    }

    public int convertRowIndexToView(final int modelRowIndex) {
        return _nestedTable.convertRowIndexToView(modelRowIndex);
    }

    public boolean editCellAt(final int row, final int column) {
        return _nestedTable.editCellAt(row, column);
    }

    public boolean editCellAt(final int row, final int column, final EventObject e) {
        return _nestedTable.editCellAt(row, column, e);
    }

    public Rectangle getCellRect(final int row, final int column, final boolean includeSpacing) {
        return _nestedTable.getCellRect(row, column, includeSpacing);
    }

    public TableCellRenderer getCellRenderer(final int row, final int column) {
        return _nestedTable.getCellRenderer(row, column);
    }

    public int getEditingColumn() {
        return _nestedTable.getEditingColumn();
    }

    public int getEditingRow() {
        return _nestedTable.getEditingRow();
    }

    public Component getEditorComponent() {
        return _nestedTable.getEditorComponent();
    }

    public void changeSelection(final int rowIndex, final int columnIndex, final boolean toggle, final boolean extend) {
        _nestedTable.changeSelection(rowIndex, columnIndex, toggle, extend);
    }

    public int getSelectedColumn() {
        return _nestedTable.getSelectedColumn();
    }

    public int getSelectedRow() {
        return _nestedTable.getSelectedRow();
    }

    public int[] getSelectedRows() {
        return _nestedTable.getSelectedRows();
    }

    public int[] getSelectedColumns() {
        return _nestedTable.getSelectedColumns();
    }

    public int getSelectedColumnCount() {
        return _nestedTable.getSelectedColumnCount();
    }

    public int getSelectedRowCount() {
        return _nestedTable.getSelectedRowCount();
    }

    public TreePath getSelectionPath() {
        return _nestedTree.getSelectionPath();
    }

    public void setRowHeight(final int rowHeight) {
        if (rowHeight <= 0)
            throw new IllegalArgumentException("Illegal row height: Required to be > 0");

        final int oldRowHeight = _nestedTree.getRowHeight();
        _nestedTable.setRowHeight(rowHeight);
        _nestedTree.setRowHeight(rowHeight);

        firePropertyChange(JTreeTable.ROW_HEIGHT_PROPERTY, oldRowHeight, rowHeight);
    }

    public int getRowHeight() {
        return _nestedTree.getRowHeight();
    }

    public Rectangle getPathBounds(final TreePath path) {
        return _nestedTree.getPathBounds(path);
    }

    public TreePath getPathForRow(final int row) {
        return _nestedTree.getPathForRow(row);
    }

    public TreePath getPathForLocation(final int x, final int y) {
        return _nestedTree.getPathForLocation(x, y);
    }

    public boolean isCellEditable(final int row, final int column) {
        return _nestedTable.isCellEditable(row, column);
    }

    public Color getSelectionBackground() {
        return _nestedTable.getSelectionBackground();
    }

    public Color getSelectionForeground() {
        return _nestedTable.getSelectionForeground();
    }

    public void setSelectionForeground(final Color selectionForeground) {
        _nestedTable.setSelectionForeground(selectionForeground);
    }

    public void setSelectionBackground(final Color selectionBackground) {
        _nestedTable.setSelectionBackground(selectionBackground);
    }
}
