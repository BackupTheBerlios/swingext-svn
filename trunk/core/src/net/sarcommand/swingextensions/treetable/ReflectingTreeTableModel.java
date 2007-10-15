package net.sarcommand.swingextensions.treetable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 */
public class ReflectingTreeTableModel extends AbstractTreeTableModel {
    private LinkedHashMap<String, ReflectingTreeTableColumn> _columns;

    /**
     * Creates a tree in which any node can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     * @see #DefaultTreeModel(javax.swing.tree.TreeNode,boolean)
     */
    public ReflectingTreeTableModel(final TreeNode root) {
        super(root);
        _columns = new LinkedHashMap<String, ReflectingTreeTableColumn>(4);
    }

    /**
     * Creates a tree specifying whether any node can have children,
     * or whether only certain nodes can have children.
     *
     * @param root               a TreeNode object that is the root of the tree
     * @param asksAllowsChildren a boolean, false if any node can
     *                           have children, true if each node is asked to see if
     *                           it can have children
     * @see #asksAllowsChildren
     */
    public ReflectingTreeTableModel(final TreeNode root, final boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        _columns = new LinkedHashMap<String, ReflectingTreeTableColumn>(4);
    }


    public int getColumnCount() {
        return _columns.size();
    }

    public String getColumnLabel(final int columnIndex) {
        if (columnIndex >= _columns.size())
            throw new IllegalArgumentException("Invalid column index: " + columnIndex + " (this model only has " + _columns.size() + " columns)");
        final String[] labels = _columns.keySet().toArray(new String[0]);
        return labels[columnIndex];
    }

    public Object getValueAt(final TreePath path, final int columnIndex) {
        if (columnIndex >= _columns.size())
            throw new IllegalArgumentException("Invalid column index: " + columnIndex + " (this model only has " + _columns.size() + " columns)");

        final ReflectingTreeTableColumn col = _columns.get(getColumnLabel(columnIndex));
        Object value = path.getLastPathComponent();
        if (value instanceof DefaultMutableTreeNode)
            value = ((DefaultMutableTreeNode) value).getUserObject();

        final Method getter = getGetterForClass(value.getClass(), col);
        if (getter == null)
            return null;
        try {
            return getter.invoke(value);
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke method " + getter);
        }
    }

    public void registerColumn(final String label, final Class clazz, final String fieldName) {

    }

    public void registerColumn(final String label, final Class clazz, final Method getter) {
        registerColumn(label, clazz, getter, null);
    }

    public void registerColumn(final String label, final Class clazz, final Method getter, final Method setter) {
        if (!_columns.containsKey(label))
            _columns.put(label, new ReflectingTreeTableColumn(label));
        final ReflectingTreeTableColumn column = _columns.get(label);
        column.setGetter(clazz, getter);
        column.setSetter(clazz, setter);
    }

    protected Method getGetterForClass(Class clazz, final ReflectingTreeTableColumn column) {
        Method m;

        while (true) {
            m = column.getGetter(clazz);
            if (m != null)
                break;
            clazz = clazz.getSuperclass();
            if (clazz == null)
                break;
        }

        if (m != null)
            return m;

        return null;
    }
}

