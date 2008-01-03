package net.sarcommand.swingextensions.selectiontree;

/**
 * Common interface for nodes within a JSelectionTree. Basically, each of those node has a state property indicating
 * whether
 * <li>the item and all child items are selected</li>
 * <li>some of the child items are selected and some are not</li>
 * <li>or neither the item nor any child items are selected.</li>
 * <p/>
 * A conveniance implementation is given by the DefaultSelectionTreeNode class.
 */
public interface SelectionTreeNode {
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
}
