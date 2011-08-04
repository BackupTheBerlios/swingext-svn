package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

public class JTreeTable extends JTable {
    protected TreeTableTreeView _tree;
    private final TreeTableModel _model;


    public JTreeTable(final TreeTableModel model) {
        _model = model;
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _tree = new TreeTableTreeView(this, _model);
        setModel(new TreeTableModelAdapter(this, _model));

        setDefaultRenderer(TreeTableModel.class, new TableCellRenderer() {
            public Component getTableCellRendererComponent(final JTable jTable, final Object o, final boolean b,
                                                           final boolean b1, final int row, final int column) {
                _tree.select(row, b);
                return _tree;
            }
        });

        _tree.setRowHeight(getRowHeight());
    }

    protected void initLayout() {

    }

    protected void setupEventHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(final MouseEvent mouseEvent) {
                final MouseEvent event = convertMouseEventForTree(mouseEvent);
                if (event != null) {
                    _tree.dispatchEvent(event);
                    revalidate();
                    repaint();
                }
            }

            @Override public void mousePressed(final MouseEvent mouseEvent) {
                final MouseEvent event = convertMouseEventForTree(mouseEvent);
                if (event != null) {
                    _tree.dispatchEvent(event);
                    revalidate();
                    repaint();
                }

            }

            @Override public void mouseReleased(final MouseEvent mouseEvent) {
                final MouseEvent event = convertMouseEventForTree(mouseEvent);
                if (event != null) {
                    _tree.dispatchEvent(event);
                    revalidate();
                    repaint();
                }
            }
        });

        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent listSelectionEvent) {
                _tree.setSelectionRows(getSelectedRows());
                repaint();
            }
        });
    }

    private MouseEvent convertMouseEventForTree(final MouseEvent event) {
        final int columnIndexAtX = getColumnModel().getColumnIndexAtX(event.getX());
        if (columnIndexAtX < 0)
            return null;

        final int modelIndex = convertColumnIndexToModel(columnIndexAtX);
        if (modelIndex != 0)
            return null;

        final Rectangle rect = getCellRect(0, columnIndexAtX, true);
        return new MouseEvent(event.getComponent(), event.getID(), event.getWhen(), event.getModifiers(),
                (int) (event.getX() - rect.getX()), event.getY(), event.getClickCount(), event.isPopupTrigger());
    }

    public void setRowHeight(final int rowHeight) {
        super.setRowHeight(rowHeight);
        if (_tree != null)
            _tree.setRowHeight(rowHeight);
    }

    protected int getTreeRowCount() {
        return _tree.getRowCount();
    }

    protected Object getTreeComponentForViewRow(final int viewRow) {
        final TreePath pathForRow = _tree.getPathForRow(viewRow);
        return pathForRow.getLastPathComponent();
    }
}
