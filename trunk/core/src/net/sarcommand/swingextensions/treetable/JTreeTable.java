package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: heup
 * Date: Jul 25, 2006
 * Time: 12:29:37 PM
 */
public class JTreeTable extends TreeTableDelegate implements Scrollable {
    public static final String TREE_CELL_RENDERER_PROPERTY = "treeCellRendererProperty";
    public static final String MODEL_PROPERTY = "modelProperty";
    public static final String ROW_HEIGHT_PROPERTY = "rowHeightProperty";

    protected TreeTableModel _mdl;
    protected TreeTableModelAdapter _mdlAdapter;

    protected TreeModelListener _modelListener;
    protected TreeExpansionListener _expansionListener;
    protected MouseAdapter _mouseAdapter;

    public JTreeTable() {
        initialize();
    }

    public JTreeTable(final TreeTableModel mdl) {
        this();
        setModel(mdl);
    }

    public void setModel(final TreeTableModel mdl) {
        if (mdl == null)
            throw new IllegalArgumentException("Parameter 'mdl' must not be null!");

        if (_mdl != null)
            _mdl.removeTreeModelListener(_modelListener);

        final TreeTableModel oldModel = getModel();
        _mdl = mdl;
        _mdl.addTreeModelListener(_modelListener);
        modelUpdated();
        firePropertyChange(MODEL_PROPERTY, oldModel, mdl);
    }

    public TreeTableModel getModel() {
        return _mdl;
    }

    public void setTreeCellRenderer(final TreeCellRenderer renderer) {
        if (renderer == null)
            throw new IllegalArgumentException("Parameter 'renderer' must not be null!");
        TreeCellRenderer previousRenderer = _nestedTree.getCellRenderer();
        if (previousRenderer instanceof TreeViewRendererWrapper)
            previousRenderer = ((TreeViewRendererWrapper) previousRenderer).getDelegate();
        _nestedTree.setCellRenderer(new TreeViewRendererWrapper(renderer, _nestedTable));
        firePropertyChange(TREE_CELL_RENDERER_PROPERTY, previousRenderer, renderer);
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initLayout() {
        setLayout(new GridLayout(1, 1));
        add(_nestedTable, BorderLayout.CENTER);
    }

    protected void modelUpdated() {
        _nestedTree.setModel(_mdl);
        _mdlAdapter = new TreeTableModelAdapter(_nestedTree, _mdl);
        _nestedTable.setModel(_mdlAdapter);
    }

    protected void initComponents() {
        _nestedTree = new JTree(_mdl);

        _mdlAdapter = new TreeTableModelAdapter(_nestedTree, _mdl);
        _nestedTable = new JTable();

        _nestedTable.setDefaultRenderer(JTree.class, new TreeTableViewportRenderer());
//        _nestedTable.setColumnModel(new TreeTableColumnModel(new TreeTableRootColumn(_nestedTree)));
        _nestedTable.setModel(_mdlAdapter);

        _nestedTable.setRowHeight(_nestedTree.getRowHeight());
        setTreeCellRenderer(new DefaultTreeCellRenderer());
    }


    protected void setupEventHandlers() {
        _mouseAdapter = new MouseAdapter() {
            /**
             * Invoked when a mouse button has been pressed on a component.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                final int columnWidth = _nestedTable.getColumnModel().getColumn(0).getWidth();
                if (e.getX() < columnWidth)
                    forwardMouseEvent(e);
            }

            /**
             * Invoked when a mouse button has been released on a component.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                final int columnWidth = _nestedTable.getColumnModel().getColumn(0).getWidth();
                if (e.getX() < columnWidth)
                    forwardMouseEvent(e);
            }

            /**
             * Invoked when the mouse has been clicked on a component.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                final int columnWidth = _nestedTable.getColumnModel().getColumn(0).getWidth();
                if (e.getX() < columnWidth)
                    forwardMouseEvent(e);
            }
        };

        _expansionListener = new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                _mdlAdapter.fireTableDataChanged();
            }

            public void treeCollapsed(TreeExpansionEvent event) {
                _mdlAdapter.fireTableDataChanged();
            }
        };

        _nestedTable.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            public void columnAdded(final TableColumnModelEvent e) {

            }

            public void columnRemoved(final TableColumnModelEvent e) {

            }

            public void columnMoved(final TableColumnModelEvent e) {

            }

            public void columnMarginChanged(final ChangeEvent e) {
                ((AbstractTreeTableModel) _mdl).fireTreeDataChanged();
            }

            public void columnSelectionChanged(final ListSelectionEvent e) {

            }
        });

        _modelListener = new TreeModelListener() {
            public void treeNodesChanged(final TreeModelEvent e) {
                _mdlAdapter.fireTableDataChanged();
            }

            public void treeNodesInserted(final TreeModelEvent e) {
                _mdlAdapter.fireTableDataChanged();
            }

            public void treeNodesRemoved(final TreeModelEvent e) {
                _mdlAdapter.fireTableDataChanged();
            }

            public void treeStructureChanged(final TreeModelEvent e) {
                _mdlAdapter.fireTableDataChanged();
            }
        };
        _nestedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                _nestedTree.setSelectionRows(_nestedTable.getSelectedRows());
            }
        });

        _nestedTable.addMouseListener(_mouseAdapter);
        _nestedTree.addTreeExpansionListener(_expansionListener);
    }

    protected void forwardMouseEvent(final MouseEvent e) {
        final MouseEvent newEvent = new MouseEvent(_nestedTree, e.getID(), e.getWhen(), e.getModifiers(), e.getX(),
                e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
        _nestedTree.dispatchEvent(newEvent);
    }

    public TableColumnModel getTreeTableColumnModel() {
        return _nestedTable.getColumnModel();
    }

    public void addNotify() {
        super.addNotify();
        final Container container = getParent();
        if (container instanceof JViewport && container.getParent() instanceof JScrollPane) {
            final JScrollPane ancestor = (JScrollPane) container.getParent();
            ancestor.setColumnHeaderView(getTableHeader());
        }
    }

    public void setTreeTableRenderer(final Class valueClass, final TreeTableCellRenderer renderer) {
        _nestedTable.setDefaultRenderer(valueClass, new TreeTableCellRendererWrapper(this, renderer));
    }

    public TreeTableCellRenderer getTreeTableRenderer(final Class valueClass) {
        return ((TreeTableCellRendererWrapper) _nestedTable.getDefaultRenderer(valueClass)).getRenderer();
    }

    public void setTreeTableEditor(final Class valueClass, final TreeTableCellEditor editor) {
        _nestedTable.setDefaultEditor(valueClass, new TreeTableCellEditorWrapper(this, editor));
    }

    public TreeTableCellEditor getTableEditor(final Class valueClass) {
        final TableCellEditor cellEditor = _nestedTable.getDefaultEditor(valueClass);
        return cellEditor != null && cellEditor instanceof TreeTableCellEditorWrapper ?
                ((TreeTableCellEditorWrapper) cellEditor).getEditor() : null;
    }

    public TreeTableCellEditor getCellEditor() {
        final TableCellEditor cellEditor = _nestedTable.getCellEditor();
        return cellEditor != null && cellEditor instanceof TreeTableCellEditorWrapper ?
                ((TreeTableCellEditorWrapper) cellEditor).getEditor() : null;
    }

    public TableCellEditor getCellEditor(final int row, final int column) {
        return _nestedTable.getCellEditor(row, column);
    }

    public static class TreeTableCellRendererWrapper implements TableCellRenderer {
        private JTreeTable _treeTable;
        private TreeTableCellRenderer _renderer;

        public TreeTableCellRendererWrapper(final JTreeTable treeTable, final TreeTableCellRenderer renderer) {
            _treeTable = treeTable;
            _renderer = renderer;
        }

        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                       final boolean hasFocus, final int row, final int column) {
            return _renderer.getTreeTableCellRendererComponent(_treeTable, value, isSelected, hasFocus,
                    _treeTable.getPathForRow(row), row, column);
        }

        public TreeTableCellRenderer getRenderer() {
            return _renderer;
        }
    }

    public static class TreeTableCellEditorWrapper implements TableCellEditor {
        private JTreeTable _treeTable;
        private TreeTableCellEditor _editor;

        public TreeTableCellEditorWrapper(final JTreeTable treeTable, final TreeTableCellEditor editor) {
            _treeTable = treeTable;
            _editor = editor;
        }

        public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
                                                     final int row, final int column) {
            return _editor.getTreeTableCellEditorComponent(_treeTable, value, isSelected, _treeTable.getPathForRow(row),
                    row, column);
        }

        public void addCellEditorListener(final CellEditorListener l) {
            _editor.addCellEditorListener(l);
        }

        public void cancelCellEditing() {
            _editor.cancelCellEditing();
        }

        public Object getCellEditorValue() {
            return _editor.getCellEditorValue();
        }

        public boolean isCellEditable(final EventObject anEvent) {
            return _editor.isCellEditable(anEvent);
        }

        public void removeCellEditorListener(final CellEditorListener l) {
            _editor.removeCellEditorListener(l);
        }

        public boolean shouldSelectCell(final EventObject anEvent) {
            return _editor.shouldSelectCell(anEvent);
        }

        public boolean stopCellEditing() {
            return _editor.stopCellEditing();
        }

        public TreeTableCellEditor getEditor() {
            return _editor;
        }
    }

    private static class TreeViewRendererWrapper implements TreeCellRenderer {
        private TreeCellRenderer _delegate;
        private JTable _table;

        public TreeViewRendererWrapper() {
        }

        public TreeViewRendererWrapper(TreeCellRenderer delegate, final JTable table) {
            _delegate = delegate;
            _table = table;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            if (_delegate == null)
                return null;

            final Component c = _delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf,
                    row, hasFocus);

            if (_table.getColumnCount() > 0) {
                final TableColumn col = _table.getColumn(_table.getColumnName(_table.convertColumnIndexToView(0)));
                if (col != null) {
                    c.setPreferredSize(new Dimension(col.getWidth(), c.getPreferredSize().height));
                }
            }
            return c;
        }

        public TreeCellRenderer getDelegate() {
            return _delegate;
        }

        public void setDelegate(TreeCellRenderer delegate) {
            _delegate = delegate;
        }
    }
}
