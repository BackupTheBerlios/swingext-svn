package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

/**
 * Created by IntelliJ IDEA.
 * User: heup
 * Date: Jul 25, 2006
 * Time: 12:34:40 PM
 */
public class TreeTableModelAdapter implements TableModel {
    protected JTree _nestedTree;
    protected TreeTableModel _model;
    protected EventListenerList _eventListeners;

    public TreeTableModelAdapter(final JTree nestedTree, final TreeTableModel mdl) {
        _nestedTree = nestedTree;
        _model = mdl;
    }

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? JTree.class : _model.getColumnClass(columnIndex - 1);
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        return _model == null ? 0 : _model.getColumnCount() + 1;
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param columnIndex the index of the column
     * @return the name of the column
     */
    public String getColumnName(int columnIndex) {
        return columnIndex == 0 ? "" : _model.getColumnLabel(columnIndex - 1);
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return _nestedTree.getRowCount();
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return _nestedTree;

        final TreePath path = _nestedTree.getPathForRow(rowIndex);
        if (path == null)
            return null;

        return _model.getValueAt(path, columnIndex - 1);
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param rowIndex    the row whose value to be queried
     * @param columnIndex the column whose value to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        final TreePath path = _nestedTree.getPathForRow(rowIndex);
        return path != null && columnIndex != 0 && _model.isCellEditable(path, columnIndex);
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param aValue      the new value
     * @param rowIndex    the row whose value is to be changed
     * @param columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final TreePath path = _nestedTree.getPathForRow(rowIndex);
        if (path == null)
            return;
        _model.setValueAt(aValue, path, columnIndex - 1);
    }


    /**
     * Adds a listener to the list that is notified each time a change
     * to the data model occurs.
     *
     * @param l the TableModelListener
     */
    public void addTableModelListener(TableModelListener l) {
        if (_eventListeners == null)
            _eventListeners = new EventListenerList();
        _eventListeners.add(TableModelListener.class, l);
    }


    /**
     * Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     *
     * @param l the TableModelListener
     */
    public void removeTableModelListener(TableModelListener l) {
        if (_eventListeners == null)
            return;
        _eventListeners.remove(TableModelListener.class, l);
    }

    public void fireTableDataChanged() {
        if (_eventListeners == null)
            return;
        final TableModelListener[] modelListeners = _eventListeners.getListeners(TableModelListener.class);
        for (TableModelListener l : modelListeners)
            l.tableChanged(new TableModelEvent(this));
    }
}
