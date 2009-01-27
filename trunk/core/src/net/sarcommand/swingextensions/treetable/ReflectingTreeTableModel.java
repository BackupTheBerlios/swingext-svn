package net.sarcommand.swingextensions.treetable;

import net.sarcommand.swingextensions.binding.Keypath;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;

/**
 * todo [heup] add docs <hr/> Copyright 2006-2009 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class ReflectingTreeTableModel extends AbstractTreeTableModel {
    private ArrayList<ReflectingTreeTableColumn> _columns;

    /**
     * Creates a tree in which any node can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     * @see #DefaultTreeModel(javax.swing.tree.TreeNode,boolean)
     */
    public ReflectingTreeTableModel(final TreeNode root) {
        super(root);
        _columns = new ArrayList<ReflectingTreeTableColumn>(8);
    }

    /**
     * Creates a tree specifying whether any node can have children, or whether only certain nodes can have children.
     *
     * @param root               a TreeNode object that is the root of the tree
     * @param asksAllowsChildren a boolean, false if any node can have children, true if each node is asked to see if it
     *                           can have children
     * @see #asksAllowsChildren
     */
    public ReflectingTreeTableModel(final TreeNode root, final boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        _columns = new ArrayList<ReflectingTreeTableColumn>(8);
    }


    public int getColumnCount() {
        return _columns.size();
    }

    public String getColumnLabel(final int columnIndex) {
        if (columnIndex >= _columns.size())
            throw new IllegalArgumentException("Invalid column index: " + columnIndex + " (this model only has " + _columns.size() + " columns)");
        return _columns.get(columnIndex).getLabel();
    }

    public Object getValueAt(final TreePath path, final int columnIndex) {
        if (columnIndex >= _columns.size())
            throw new IllegalArgumentException("Invalid column index: " + columnIndex + " (this model only has " + _columns.size() + " columns)");

        Object value = path.getLastPathComponent();
        if (value instanceof DefaultMutableTreeNode)
            value = ((DefaultMutableTreeNode) value).getUserObject();

        return _columns.get(columnIndex).getKeypath().get(value);
    }

    public void addColumn(final String label, final String keypath) {
        _columns.add(new ReflectingTreeTableColumn(label, new Keypath(keypath, true)));
    }
}

