package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: heup
 * Date: Jul 25, 2006
 * Time: 12:29:37 PM
 */
public class JTreeTable extends JComponent implements Scrollable {
    public static final String TREE_CELL_RENDERER_PROPERTY = "treeCellRendererProperty";
    public static final String MODEL_PROPERTY = "modelProperty";
    public static final String ROW_HEIGHT_PROPERTY = "rowHeightProperty";

    protected TreeTableModel _mdl;
    protected TreeTableModelAdapter _mdlAdapter;
//    protected TreeTableColumnModel _columnModel;

    protected JTree _nestedTree;
    protected JTable _nestedTable;
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

        final TreeTableModel oldModel = getModel();
        _mdl = mdl;

        modelUpdated();
        firePropertyChange(MODEL_PROPERTY, oldModel, mdl);
    }

    public TreeTableModel getModel() {
        return _mdl;
    }

    public void setRowHeight(final int rowHeight) {
        if (rowHeight <= 0)
            throw new IllegalArgumentException("Illegal row height: Required to be > 0");

        final int oldRowHeight = _nestedTree.getRowHeight();
        _nestedTable.setRowHeight(rowHeight);
        _nestedTree.setRowHeight(rowHeight);

        firePropertyChange(ROW_HEIGHT_PROPERTY, oldRowHeight, rowHeight);
    }

    public int getRowHeight() {
        return _nestedTree.getRowHeight();
    }

    public void setTreeCellRenderer(final TreeCellRenderer renderer) {
        if (renderer == null)
            throw new IllegalArgumentException("Parameter 'renderer' must not be null!");
        TreeCellRenderer previousRenderer = _nestedTree.getCellRenderer();
        if (previousRenderer instanceof TreeCellRendererWrapper)
            previousRenderer = ((TreeCellRendererWrapper) previousRenderer).getDelegate();
        _nestedTree.setCellRenderer(new TreeCellRendererWrapper(renderer, _nestedTable));
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

        _nestedTable.setDefaultRenderer(JTree.class, new DefaultTreeTableCellRenderer());
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

    public JTableHeader getTableHeader() {
        return _nestedTable.getTableHeader();
    }

    private static class TreeCellRendererWrapper implements TreeCellRenderer {
        private TreeCellRenderer _delegate;
        private JTable _table;

        public TreeCellRendererWrapper() {
        }

        public TreeCellRendererWrapper(TreeCellRenderer delegate, final JTable table) {
            _delegate = delegate;
            _table = table;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (_delegate == null)
                return null;

            final Component c = _delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

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

    /* Delegate methods */

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

    public void setTableRenderer(final Class valueClass, final TableCellRenderer renderer) {
        _nestedTable.setDefaultRenderer(valueClass, renderer);
    }

    public TableCellRenderer getTableRenderer(final Class valueClass) {
        return _nestedTable.getDefaultRenderer(valueClass);
    }

    public void setTableEditor(final Class valueClass, final TableCellEditor editor) {
        _nestedTable.setDefaultEditor(valueClass, editor);
    }

    public TableCellEditor getTableEditor(final Class valueClass) {
        return _nestedTable.getDefaultEditor(valueClass);
    }

    public void setToggleClickCount(final int clickCount) {
        _nestedTree.setToggleClickCount(clickCount);
    }

    public int getToggleClickCount() {
        return _nestedTree.getToggleClickCount();
    }
}
