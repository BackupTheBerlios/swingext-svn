package net.sarcommand.swingextensions.selectiontree;

import javax.swing.tree.TreeNode;

/**
 * Common interface for nodes within a JSelectionTree. Basically, each of those node has a state property indicating
 * whether <li>the item and all child items are selected</li> <li>some of the child items are selected and some are
 * not</li> <li>or neither the item nor any child items are selected.</li>
 * <p/>
 * A conveniance implementation is given by the DefaultSelectionTreeNode class.
 */
public interface SelectionTreeNode extends TreeNode {
    public static enum State {
        NONE_SELECTED, SOME_SELECTED, ALL_SELECTED
    }

    /**
     * Returns this node's current state.
     *
     * @return this node's current state.
     */
    public State getState();

    /**
     * Updates this node's current state.
     *
     * @param state new state for this node.
     */
    public void setState(final State state);

    /**
     * Instructs the node to update its state. The node will then scan its children to determine whether all, some or
     * none of them are selected. If the node does not have children, the value provided by the last call of setState
     * will be retained.
     */
    public void updateState();

    /**
     * Adds a selection listener to the node, which will be notified when the node's state changes.
     *
     * @param listener listener to add.
     * @see net.sarcommand.swingextensions.selectiontree.SelectionTreeListener
     */
    public void addSelectionTreeListener(final SelectionTreeListener listener);

    /**
     * Removes a selection listener from the node.
     *
     * @param listener listener to remove.
     * @see net.sarcommand.swingextensions.selectiontree.SelectionTreeListener
     */
    public void removeSelectionTreeListener(final SelectionTreeListener listener);
}
