package net.sarcommand.swingextensions.container;

import net.sarcommand.swingextensions.event.EventSupport;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * A container which will frame its subcomponents with a border including a JCheckBox. This class is meant to be
 * used for grouping components which are connected with a boolean state, for instance, a set of parameters which
 * should only be set if an option is selected
 * <p/>
 * Basically, this container works like an empty border with an embedded checkbox. Since borders can't actively
 * react to events (thus the check box could not be selected), it was necessary to subclass JPanel rather than
 * creating a Border implementation.
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
public class CheckBoxContainer extends JPanel implements ItemSelectable {
    /**
     * The JCheckBox instance internally used for rendering.
     */
    protected JCheckBox _checkBox;

    /**
     * The titled border
     */
    protected TitledBorder _border;

    /**
     * Event support for item listeners.
     */
    protected EventSupport<ItemListener> _itemListeners;

    /**
     * Event support for action listeners.
     */
    protected EventSupport<ActionListener> _actionListeners;

    /**
     * Creates a new CheckBoxContainer with an unlabeled JCheckBox.
     */
    public CheckBoxContainer() {
        initialize();
    }

    /**
     * Creates a new CheckBoxContainer and sets the given action on the embedded JCheckBox.
     *
     * @param action action to set on the embedded JCheckBox.
     */
    public CheckBoxContainer(final Action action) {
        initialize();
        setAction(action);
    }

    /**
     * Creates a new CheckBoxContainer using a JCheckBox labeled with the given String.
     *
     * @param text text to use as label for the JCheckBox.
     */
    public CheckBoxContainer(final String text) {
        initialize();
        setCheckboxText(text);
    }

    /**
     * Returns the insets for child components after reserving space for the JCheckBox and border.
     *
     * @return the insets for child components after reserving space for the JCheckBox and border.
     */
    public Insets getInsets() {
        final Insets insets = super.getInsets();
        return new Insets(insets.top + _checkBox.getPreferredSize().height, insets.left + 5,
                insets.bottom + 5, insets.right + 5);
    }

    /**
     * Sets the text used as label for the embedded JCheckBox.
     *
     * @param text the text used as label for the embedded JCheckBox.
     */
    public void setCheckboxText(final String text) {
        _checkBox.setAction(null);
        _checkBox.setText(text);
        repaint(getCheckboxBounds());
    }

    /**
     * Sets the action property of the embedded JCheckBox.
     *
     * @param action the action property of the embedded JCheckBox.
     */
    public void setAction(final Action action) {
        _checkBox.setAction(action);
        repaint(getCheckboxBounds());
    }

    /**
     * Adds an ActionListener which will be notified in the embedded JCheckBox is clicked.
     *
     * @param listener an ActionListener which will be notified in the embedded JCheckBox is clicked, non-null.
     */
    public void addActionListener(final ActionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _actionListeners.addListener(listener);
    }

    /**
     * Removes a previously installed ActionListener.
     *
     * @param listener listener to remove, non-null.
     */
    public void removeActionListener(final ActionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _actionListeners.removeListener(listener);
    }

    /**
     * Adds an ItemListener which will be notified in the embedded JCheckBox's selection state changes.
     *
     * @param listener an ItemListener which will be notified in the embedded JCheckBox's selection state changes.
     */
    public void addItemListener(final ItemListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _itemListeners.addListener(listener);
    }

    /**
     * Removes a previously installed ItemListener.
     *
     * @param listener listener to remove, non-null.
     */
    public void removeItemListener(final ItemListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _itemListeners.removeListener(listener);
    }

    /**
     * Returns whether the embedded JCheckBox is selected.
     *
     * @return whether the embedded JCheckBox is selected.
     */
    public boolean isSelected() {
        return _checkBox.isSelected();
    }

    /**
     * @see java.awt.ItemSelectable#getSelectedObjects()
     */
    public Object[] getSelectedObjects() {
        return isSelected() ? null : new Object[]{_checkBox.getText()};
    }

    /**
     * Initializer method called by all constructors.
     */
    protected void initialize() {
        _checkBox = new JCheckBox();
        _checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                fireSelectionEvents(e);
            }
        });

        _border = BorderFactory.createTitledBorder("");

        _itemListeners = EventSupport.create(ItemListener.class);
        _actionListeners = EventSupport.create(ActionListener.class);

        final MouseListener mouseListener = new MouseListener() {
            public void mouseClicked(final MouseEvent e) {
                delegateMouseEvent(e);
            }

            public void mouseEntered(final MouseEvent e) {
                delegateMouseEvent(e);
            }

            public void mouseExited(final MouseEvent e) {
                delegateMouseEvent(e);
            }

            public void mousePressed(final MouseEvent e) {
                delegateMouseEvent(e);
            }

            public void mouseReleased(final MouseEvent e) {
                delegateMouseEvent(e);
            }
        };

        addMouseListener(mouseListener);
    }

    /**
     * Invoked when the embedded JCheckBox is clicked. Will fire according ActionEvents and ItemEvents to all
     * registered listeners.
     *
     * @param e The original ActionEvent fired by the JCheckBox.
     */
    protected void fireSelectionEvents(final ActionEvent e) {
        final ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "",
                e.getWhen(), e.getModifiers());
        _actionListeners.delegate().actionPerformed(actionEvent);

        final ItemEvent itemEvent = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, isSelected() ?
                ItemEvent.SELECTED : ItemEvent.DESELECTED);
        _itemListeners.delegate().itemStateChanged(itemEvent);
    }

    /**
     * Overridden to draw the JCheckBox and border components.
     *
     * @param g Graphics used for rendering.
     */
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Insets insets = super.getInsets();

        final Rectangle checkboxBounds = getCheckboxBounds();

        final int x = insets.left;
        final int y = insets.top + (int) Math.round(checkboxBounds.getHeight() / 2);
        _border.paintBorder(this, g, x, y, getWidth() - x - insets.right, getHeight() - y - insets.bottom);

        g.clearRect(checkboxBounds.x, checkboxBounds.y, checkboxBounds.width, checkboxBounds.height);
        g.translate(checkboxBounds.x, checkboxBounds.y);

        _checkBox.setBounds(0, 0, checkboxBounds.width, checkboxBounds.height);
        _checkBox.paint(g);
        g.translate(-checkboxBounds.x, -checkboxBounds.y);
    }

    /**
     * Returns the current bounds of the embedded JCheckBox.
     *
     * @return the current bounds of the embedded JCheckBox.
     */
    protected Rectangle getCheckboxBounds() {
        final Dimension size = _checkBox.getPreferredSize();
        final Insets insets = super.getInsets();
        final int x = insets.left + 12;
        final int y = insets.top;
        return new Rectangle(x, y, size.width, size.height);
    }

    /**
     * Used to delegate MouseEvents to the JCheckBox.
     *
     * @param e the original MouseEvent.
     */
    protected void delegateMouseEvent(final MouseEvent e) {
        final Rectangle checkboxBounds = getCheckboxBounds();

        if (checkboxBounds.contains(e.getPoint())) {
            final MouseEvent event = new MouseEvent(_checkBox, e.getID(), e.getWhen(), e.getModifiers(),
                    e.getX() - checkboxBounds.x, e.getY() - checkboxBounds.y,
                    e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            _checkBox.dispatchEvent(event);
            repaint(checkboxBounds);
        }
    }
}
