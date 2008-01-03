package net.sarcommand.swingextensions.selectiontree;

import javax.swing.*;
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
 * For each item in the tree, a three-state checkbox will be displayed, indicating whether
 * <li>The item and all child items are selected</li>
 * <li>Some of the child items are selected and some are not</li>
 * <li>Neither the item nor any child items are selected</li>
 * <p/>
 * You can use a JSelectionTree just as you would use a JTree. The only difference lies in the fact that you
 * supply instances of the SelectionTreeNode interface as nodes. For most cases, you should be fine using the
 * DefaultSelectionTreeNode implementation, which mimics the DefaultMutableTreeNode class.
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
 *
 * @see SelectionTreeNode
 * @see DefaultSelectionTreeNode
 */
public class JSelectionTree extends JTree {
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
    }

    /**
     * Invoked whenever the user clicks a node in order to adapt the selection state.
     *
     * @param path TreePath leading to the node being clicked.
     */
    protected void nodeSelected(final TreePath path) {
        final SelectionTreeNode node = (SelectionTreeNode) path.getLastPathComponent();
        final SelectionTreeNode.State state = node.getState() == null ? SelectionTreeNode.State.NONE_SELECTED :
                node.getState();
        final SelectionTreeNode.State newState = state == SelectionTreeNode.State.ALL_SELECTED ?
                SelectionTreeNode.State.NONE_SELECTED : SelectionTreeNode.State.ALL_SELECTED;
        final TreeModel mdl = getModel();

        /* Update the children */
        setChildrenRecursively(node, mdl, newState);

        final TreePath parentPath = path.getParentPath();
        if (parentPath != null) {
            final Object[] components = parentPath.getPath();
            for (int i = components.length - 1; i >= 0; i--)
                updateNodeState(mdl, components[i]);
        }
        repaint();
    }

    /**
     * Updates the state of the specified node by looking at the child nodes.
     * <li>If all child nodes are selected, the given node's state will change to ALL_SELECTED</li>
     * <li>If only some child nodes are selected, the given node's state will change to SOME_SELECTED</li>
     * <li>If none child nodes are selected, the given node's state will change to NONE_SELECTED</li>
     *
     * @param model TreeModel used to look up the child nodes.
     * @param node  Node being updated.
     */
    protected void updateNodeState(final TreeModel model, final Object node) {
        if (!(node instanceof SelectionTreeNode))
            return;

        SelectionTreeNode.State state = null;
        boolean stateForAll = true;

        final int count = model.getChildCount(node);
        for (int i = 0; i < count; i++) {
            SelectionTreeNode.State childState = getState(model, model.getChild(node, i));
            if (state == null)
                state = childState;
            else if (childState != null && childState != state) {
                stateForAll = false;
                break;
            }
        }
        if (state == null)
            throw new RuntimeException("Could not determine the state for node " + node);

        ((SelectionTreeNode) node).setState(stateForAll ? state : SelectionTreeNode.State.SOME_SELECTED);
    }

    /**
     * Returns the current state of the given tree node.
     *
     * @param model TreeModel used to determine the node's children and their state.
     * @param node  Node being queried.
     * @return State of the given node.
     */
    protected SelectionTreeNode.State getState(final TreeModel model, final Object node) {
        if (node instanceof SelectionTreeNode) {
            final SelectionTreeNode.State state = ((SelectionTreeNode) node).getState();
            return state == null ? SelectionTreeNode.State.NONE_SELECTED : state;
        }

        SelectionTreeNode.State state = null;
        final int count = model.getChildCount(node);
        for (int i = 0; i < count; i++) {
            SelectionTreeNode.State childState = getState(model, node);
            if (state == null)
                state = childState;
            else if (childState != null && childState != state)
                return SelectionTreeNode.State.SOME_SELECTED;
        }
        return state;
    }

    /**
     * Recursively sets the state the given node's chidren. This method is invoked when a non-leaf node in the
     * tree is (de-)selected.
     *
     * @param node  Node which's chilren will be updated.
     * @param mdl   Model being used for looking up the node's children.
     * @param state State to set on each child node.
     */
    protected void setChildrenRecursively(final Object node, final TreeModel mdl,
                                          final SelectionTreeNode.State state) {
        final int count = mdl.getChildCount(node);
        for (int i = 0; i < count; i++)
            setChildrenRecursively(mdl.getChild(node, i), mdl, state);
        if (node instanceof SelectionTreeNode)
            ((SelectionTreeNode) node).setState(state);
    }
}
