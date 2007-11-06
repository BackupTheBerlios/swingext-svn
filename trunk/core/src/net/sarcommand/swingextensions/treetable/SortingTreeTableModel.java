package net.sarcommand.swingextensions.treetable;

import net.sarcommand.swingextensions.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class SortingTreeTableModel implements TreeTableModel {
    private TreeTableModel _delegate;
    private HashMap<Object, List<Object>> _sortedElements;
    private EventSupport<TreeModelListener> _treeModelListeners;

    private List<RowSorter.SortKey> _sortKeys;
    private TreeTableSorter _sorter;

    public SortingTreeTableModel(TreeTableModel delegate) {
        _delegate = delegate;
        _treeModelListeners = EventSupport.create(TreeModelListener.class);
    }

    public TreeTableSorter getSorter() {
        return _sorter;
    }

    public void setSorter(TreeTableSorter sorter) {
        _sorter = sorter;
    }

    public Class getColumnClass(int columnIndex) {
        return _delegate.getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return _delegate.getColumnCount();
    }

    public String getColumnLabel(int columnIndex) {
        return _delegate.getColumnLabel(columnIndex);
    }

    public Object getValueAt(TreePath path, int columnIndex) {
        return _delegate.getValueAt(path, columnIndex);
    }

    public boolean isCellEditable(TreePath path, int columnIndex) {
        return _delegate.isCellEditable(path, columnIndex);
    }

    public void setValueAt(Object value, TreePath path, int columnIndex) {
        _delegate.setValueAt(value, path, columnIndex);
    }

    public void sort(final List<RowSorter.SortKey> sortKeys) {
        _sortKeys = sortKeys;
        if (_sorter != null && sortKeys.size() > 0) {
            _sortedElements = new HashMap<Object, List<Object>>(32);
            sort(null, _delegate.getRoot());
        } else
            _sortedElements = null;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                _treeModelListeners.delegate().treeStructureChanged(new TreeModelEvent(this, new TreePath(_delegate.getRoot())));
            }
        });
    }

    public void sort(final TreePath path, final Object node) {
        final int count = _delegate.getChildCount(node);

        List<Object> children = new ArrayList<Object>(count);
        for (int i = 0; i < count; i++) {
            final Object o = _delegate.getChild(node, i);
            children.add(o);
            if (!_delegate.isLeaf(o))
                sort(path == null ? new TreePath(o) : path.pathByAddingChild(o), o);
        }

        children = _sorter.sort(path, _delegate, _sortKeys, children);
        _sortedElements.put(node, children);
    }

    public void addTreeModelListener(TreeModelListener l) {
        _treeModelListeners.addListener(l);
    }

    public Object getChild(Object parent, int index) {
        return _sortedElements == null ? _delegate.getChild(parent, index) : _sortedElements.get(parent).get(index);
    }

    public int getChildCount(Object parent) {
        return _delegate.getChildCount(parent);
    }

    public int getIndexOfChild(Object parent, Object child) {
        return _sortedElements == null ? _delegate.getIndexOfChild(parent, child) :
                _sortedElements.get(parent).indexOf(child);
    }

    public Object getRoot() {
        return _delegate.getRoot();
    }

    public boolean isLeaf(Object node) {
        return _delegate.isLeaf(node);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        _treeModelListeners.removeListener(l);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        _delegate.valueForPathChanged(path, newValue);
    }
}
