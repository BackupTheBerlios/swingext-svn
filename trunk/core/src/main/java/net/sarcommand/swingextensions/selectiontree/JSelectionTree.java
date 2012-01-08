package net.sarcommand.swingextensions.selectiontree;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Vector;

/**
 * The JSelectionTree class lets a user select items from a hierarchical structure. This control is well known from
 * installer components, which will usually offer something like this:
 * <p/>
 * <pre>
 * + Install everything
 * ++ Install package A
 * +++ Install subpackage AA
 * +++ Install subpackage AB
 * ++ Install package B
 * +++ Install subpackage BA
 * +++ Install subpackage BB
 * +++ Install subpackage BC
 * </pre>
 * <p/>
 * The (de-)selection of an item in the hierarchy will automatically cause all child items to switch to the same state.
 * When selecting to install package A, the subpackages AA and AB will be selected as well.
 * <p/>
 * For each item in the tree, a three-state checkbox will be displayed, indicating whether <li>The item and all child
 * items are selected</li> <li>Some of the child items are selected and some are not</li> <li>Neither the item nor any
 * child items are selected</li>
 * <p/>
 * You can use a JSelectionTree just as you would use a JTree. The only difference lies in the fact that you supply
 * instances of the SelectionTreeNode interface as nodes. For most cases, you should be fine using the
 * DefaultSelectionTreeNode implementation, which mimics the DefaultMutableTreeNode class.
 * <p/>
 * If you interfere with the tree's selecetion management programatically, invoke updateSelections() afterwards to
 * ensure that all nodes are in a consistent state.
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @see SelectionTreeNode
 * @see DefaultSelectionTreeNode
 */
public class JSelectionTree extends JTree {
    private TreeModelListener _modelListener;

    public JSelectionTree() {
        initialize();
    }

    public JSelectionTree(final TreeModel newModel) {
        super(newModel);
        initialize();
    }

    public JSelectionTree(final TreeNode root) {
        super(root);
        initialize();
    }

    public JSelectionTree(final TreeNode root, final boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        initialize();
    }

    public JSelectionTree(final Hashtable<?, ?> value) {
        super(value);
        initialize();
    }

    public JSelectionTree(final Object[] value) {
        super(value);
        initialize();
    }

    public JSelectionTree(final Vector<?> value) {
        super(value);
        initialize();
    }

    /**
     * Sets up the tree instance by installing appropriate renderers and event listeners.
     */
    protected void initialize() {
        setCellRenderer(new DefaultSelectionTreeCellRenderer());
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final TreePath path = getPathForLocation(e.getX(), e.getY());
                    if (path != null && path.getPathCount() > 0) {
                        if (path.getLastPathComponent() instanceof SelectionTreeNode)
                            nodeSelected(path);
                    }
                }
            }
        });
        _modelListener = new TreeModelListener() {
            public void treeNodesChanged(final TreeModelEvent e) {
                updateSelections();
            }

            public void treeNodesInserted(final TreeModelEvent e) {
                updateSelections();
            }

            public void treeNodesRemoved(final TreeModelEvent e) {
                updateSelections();
            }

            public void treeStructureChanged(final TreeModelEvent e) {
                updateSelections();
            }
        };

        if (getModel() != null)
            getModel().addTreeModelListener(_modelListener);
    }

    /**
     * Sets the tree model used by this instance. This implementation will simply pass the model to the super class and
     * update required listener references.
     *
     * @param newModel The new model used by this instance.
     */
    public void setModel(final TreeModel newModel) {
        if (getModel() != null)
            getModel().removeTreeModelListener(_modelListener);
        super.setModel(newModel);
        if (getModel() != null)
            getModel().addTreeModelListener(_modelListener);
        updateSelections();
    }

    /**
     * If you programatically change the selection state of nodes, invoke this method to ensure that the tree is still
     * in a consistent state. It will be invoked automatically whenever the TreeModel signals a change. Making
     * unncessesary calls to this method will not be harmful in any way, except that it wastes performance.
     */
    public void updateSelections() {
        final Object root = getModel().getRoot();
        if (root instanceof SelectionTreeNode)
            ((SelectionTreeNode) root).updateState();
    }

    /**
     * Invoked whenever the user clicks a node in order to adapt the selection state. This method will calculate the new
     * state for the selected node and invoke the according setState method. Propagating the change to parent and child
     * nodes is left to the node implementation.
     *
     * @param path TreePath leading to the node being clicked.
     */
    protected void nodeSelected(final TreePath path) {
        final SelectionTreeNode node = (SelectionTreeNode) path.getLastPathComponent();
        final SelectionTreeNode.State state = node.getState();
        final SelectionTreeNode.State newState = state == SelectionTreeNode.State.ALL_SELECTED ?
                SelectionTreeNode.State.NONE_SELECTED : SelectionTreeNode.State.ALL_SELECTED;
        node.setState(newState);
        repaint();
    }
}
