package net.sarcommand.swingextensions.selectiontree;

import net.sarcommand.swingextensions.event.EventSupport;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

/**
 * Default implementation of the SelectionTreeNode interface. This implementation extends the DefaultMutableTreeNode and
 * adds two new features: <li>You can easily create new child nodes using the addChild(Object) method</li> <li>You can
 * add ActionListeners to the node in order to be notified when it has been (de-)selected.
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
 */
public class DefaultSelectionTreeNode extends DefaultMutableTreeNode implements SelectionTreeNode {
    private State _state;
    private EventSupport<SelectionTreeListener> _listenerSupport;
    private SelectionTreeListener _selectionListener;

    public DefaultSelectionTreeNode(final Object userObject) {
        super(userObject);
        initialize();
    }

    protected void initialize() {
        _state = State.NONE_SELECTED;
        _listenerSupport = EventSupport.create(SelectionTreeListener.class);
        _selectionListener = new SelectionTreeListener() {
            public void stateChanged(final SelectionTreeNode node, final State state) {
                updateState();
            }
        };
    }

    public void updateState() {
        final Enumeration enumeration = children();
        final int itemCount = getChildCount();

        /* If there are no children, use the set state */
        if (itemCount == 0)
            return;

        int selectedItems = 0;
        boolean containsPartiallySelectedNodes = false;
        while (enumeration.hasMoreElements()) {
            final Object o = enumeration.nextElement();
            if (o instanceof SelectionTreeNode) {
                final State state = ((SelectionTreeNode) o).getState();
                if (state == State.ALL_SELECTED)
                    selectedItems++;
                else if (state == State.SOME_SELECTED) {
                    selectedItems++;
                    containsPartiallySelectedNodes = true;
                }
            }
        }
        final State previousState = _state;

        if (selectedItems == 0)
            _state = State.NONE_SELECTED;
        else if (selectedItems == itemCount && !containsPartiallySelectedNodes)
            _state = State.ALL_SELECTED;
        else
            _state = State.SOME_SELECTED;

        if (_state != previousState)
            _listenerSupport.delegate().stateChanged(this, _state);
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
        if (state != State.ALL_SELECTED && state != State.NONE_SELECTED)
            throw new IllegalArgumentException("Can't set state to " + state);
        _state = state;
        if (getChildCount() > 0) {
            final Enumeration children = children();
            while (children.hasMoreElements()) {
                final Object o = children.nextElement();
                if (o instanceof SelectionTreeNode)
                    ((SelectionTreeNode) o).setState(state);
            }
        }
        _state = state;
        _listenerSupport.delegate().stateChanged(this, state);
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

    public void insert(final MutableTreeNode newChild, final int childIndex) {
        super.insert(newChild, childIndex);
        if (newChild instanceof SelectionTreeNode)
            ((SelectionTreeNode) newChild).addSelectionTreeListener(_selectionListener);
        updateState();
    }

    public void remove(final int childIndex) {
        final TreeNode child = getChildAt(childIndex);
        super.remove(childIndex);
        if (child instanceof SelectionTreeNode)
            ((SelectionTreeNode) child).removeSelectionTreeListener(_selectionListener);
    }

    public void addSelectionTreeListener(final SelectionTreeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _listenerSupport.addListener(listener);
    }

    public void removeSelectionTreeListener(final SelectionTreeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _listenerSupport.removeListener(listener);
    }
}
