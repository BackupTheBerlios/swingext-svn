package net.sarcommand.swingextensions.selectiontree;

import javax.swing.tree.TreeNode;

/**
 * Common interface for nodes within a JSelectionTree. Basically, each of those node has a state property indicating
 * whether <li>the item and all child items are selected</li> <li>some of the child items are selected and some are
 * not</li> <li>or neither the item nor any child items are selected.</li>
 * <p/>
 * A conveniance implementation is given by the DefaultSelectionTreeNode class.
 * <p/>
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
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
