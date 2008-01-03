package net.sarcommand.swingextensions.selectiontree;

import net.sarcommand.swingextensions.event.EventSupport;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Default implementation of the SelectionTreeNode interface. This implementation extends the DefaultMutableTreeNode
 * and adds two new features:
 * <li>You can easily create new child nodes using the addChild(Object) method</li>
 * <li>You can add ActionListeners to the node in order to be notified when it has been (de-)selected.
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
public class DefaultSelectionTreeNode extends DefaultMutableTreeNode implements SelectionTreeNode {
    private State _state;
    private EventSupport<ActionListener> _actionListeners;

    public DefaultSelectionTreeNode(final Object userObject) {
        super(userObject);
        _actionListeners = EventSupport.create(ActionListener.class);
    }

    public State getState() {
        return _state;
    }

    /**
     * Sets the node's new state. Invoking this method will cause an ActionEvent to be posted to all registered
     * listeners.
     *
     * @param state New state for the node.
     */
    public void setState(final State state) {
        _state = state;
        _actionListeners.delegate().actionPerformed(new ActionEvent(getUserObject(), ActionEvent.ACTION_PERFORMED,
                state.toString()));
    }

    /**
     * Adds a new DefaultSelectionTreeNode for the given user object and adds it as a child to this node.
     *
     * @param userObject User object for the new child.
     * @return The created node instance.
     */
    public DefaultSelectionTreeNode addChild(final Object userObject) {
        final DefaultSelectionTreeNode node = new DefaultSelectionTreeNode(userObject);
        super.add(node);
        return node;
    }

    /**
     * Allows adding an ActionListener, which will be notified when the node is (de-) selected.
     *
     * @param listener ActionListener which will be notified when the node is (de-) selected.
     */
    public void addActionListener(final ActionListener listener) {
        _actionListeners.addListener(listener);
    }

    /**
     * Removes a previously installed ActionListener from this node.
     *
     * @param listener a previously installed ActionListener.
     */
    public void removeActionListener(final ActionListener listener) {
        _actionListeners.removeListener(listener);
    }
}
