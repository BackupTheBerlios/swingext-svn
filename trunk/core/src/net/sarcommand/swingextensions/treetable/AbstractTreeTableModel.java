package net.sarcommand.swingextensions.treetable;

import net.sarcommand.swingextensions.event.EventSupport;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * An abstract conveniance implementation for the TreeTableModel interface.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public abstract class AbstractTreeTableModel extends DefaultTreeModel implements TreeTableModel {
    private EventSupport<TreeModelListener> _treeModelListeners;

    public AbstractTreeTableModel() {
        super(null);
        initialize();
    }

    protected AbstractTreeTableModel(TreeNode root) {
        super(root);
        initialize();
    }

    protected AbstractTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        initialize();
    }

    protected void initialize() {
        _treeModelListeners = EventSupport.create(TreeModelListener.class);
    }

    public void addTreeModelListener(TreeModelListener l) {
        _treeModelListeners.addListener(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        _treeModelListeners.removeListener(l);
    }

    public void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        _treeModelListeners.delegate().treeNodesChanged(new TreeModelEvent(source, path, childIndices, children));
    }

    public void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        _treeModelListeners.delegate().treeNodesInserted(new TreeModelEvent(source, path, childIndices, children));
    }

    public void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        _treeModelListeners.delegate().treeNodesRemoved(new TreeModelEvent(source, path, childIndices, children));
    }

    public void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        _treeModelListeners.delegate().treeStructureChanged(new TreeModelEvent(source, path, childIndices, children));
    }

    public void fireTreeDataChanged() {
        final Object root = getRoot();
        _treeModelListeners.delegate().treeStructureChanged(new TreeModelEvent(this, root == null ?
                null : new TreePath(root)));
    }

    public void setValueAt(final Object value, final TreePath path, final int columnIndex) {
    }

    public boolean isCellEditable(final TreePath path, final int columnIndex) {
        return false;
    }

    public Class getColumnClass(int columnIndex) {
        return Object.class;
    }
}
