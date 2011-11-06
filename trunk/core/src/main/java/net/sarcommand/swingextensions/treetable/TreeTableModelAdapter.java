package net.sarcommand.swingextensions.treetable;

import net.sarcommand.swingextensions.event.TreeModelAdapter;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.table.AbstractTableModel;

/**
 * 8/4/11
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */

/*
 * Copyright 2005-2011 Torsten Heup
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

public class TreeTableModelAdapter extends AbstractTableModel {
    private final JTreeTable _treeTable;
    private final TreeTableModel _model;

    public TreeTableModelAdapter(final JTreeTable treeTable, final TreeTableModel model) {
        _treeTable = treeTable;
        _model = model;
        _model.addTreeModelListener(new TreeModelAdapter() {
            @Override protected void treeChanged(final TreeModelEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        fireTableDataChanged();
                    }
                });
            }
        });
    }

    public int getColumnCount() {
        return _model.getColumnCount() + 1;
    }

    public int getRowCount() {
        return _treeTable.getTreeRowCount();
    }

    @Override public Class<?> getColumnClass(final int i) {
        return i == 0 ? TreeTableModel.class : _model.getColumnClass(i - 1);
    }

    @Override public String getColumnName(final int i) {
        return i == 0 ? "" : _model.getColumnName(i - 1);
    }

    public Object getValueAt(final int row, final int col) {
        if (col == 0)
            return null;

        final Object object = getObjectForRow(row);
        return _model.getValueAt(object, col - 1);
    }

    private Object getObjectForRow(final int row) {
        final int viewRow = _treeTable.convertRowIndexToView(row);
        return _treeTable.getTreeComponentForViewRow(viewRow);
    }

    @Override public boolean isCellEditable(final int row, final int column) {
        return column == 0 ? false : _model.isCellEditable(row, column - 1);
    }

    @Override public void setValueAt(final Object o, final int row, final int column) {
        if (column == 0)
            return;
        final Object treeNode = getObjectForRow(row);
        _model.setValueAt(treeNode, o, column - 1);
    }
}
