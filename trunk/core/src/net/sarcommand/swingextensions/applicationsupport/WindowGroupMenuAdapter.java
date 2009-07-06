package net.sarcommand.swingextensions.applicationsupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

/**
 */
public class WindowGroupMenuAdapter {
    protected JMenu _menu;
    protected WindowGroup _group;
    protected int _addItemsAfterIndex;

    protected ActionListener _actionListener;
    protected LinkedList<Item> _installedWindowItems;

    public WindowGroupMenuAdapter(final JMenu menu, final int addItemsAfterIndex, final WindowGroup group) {
        initialize(menu, addItemsAfterIndex, group);

    }

    public JMenu getMenu() {
        return _menu;
    }

    public WindowGroup getGroup() {
        return _group;
    }

    protected void initialize(final JMenu menu, final int addItemsAfterIndex, final WindowGroup group) {
        if (menu == null)
            throw new IllegalArgumentException("Parameter 'menu' must not be null!");
        if (group == null)
            throw new IllegalArgumentException("Parameter 'group' must not be null!");

        _installedWindowItems = new LinkedList<Item>();
        _menu = menu;
        _group = group;
        _addItemsAfterIndex = addItemsAfterIndex;

        _actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Item item = (Item) e.getSource();
                final Window window = item.getGroup().getWindow(item.getText());
                window.setVisible(true);
                window.requestFocus();
            }
        };

        _group.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String propertyName = evt.getPropertyName();
                if (propertyName.equals(WindowGroup.FOCUSED_WINDOW_PROPERTY))
                    focusChanged();
                else if (propertyName.equals(WindowGroup.WINDOW_COUNT_PROPERTY))
                    rebuildMenu();
            }
        });
    }

    protected void focusChanged() {
        final Window window = _group.getCurrentFocusOwner();
        final String windowTitle = _group.getWindowID(window);
        if (window != null) {
            for (Item t : _installedWindowItems) {
                if (t.getText().equals(windowTitle)) {
                    t.setSelected(true);
                    return;
                }
            }
        }
    }

    protected void rebuildMenu() {
        for (Item i : _installedWindowItems)
            _menu.remove(i);
        _installedWindowItems.clear();

        ButtonGroup buttonGroup = new ButtonGroup();
        final WindowGroup group = _group;

        int index = _addItemsAfterIndex;
        for (String w : group.getWindowIDs()) {
            final Item item = new Item(w, group);

            _menu.add(item, index < 0 ? _menu.getItemCount() : index);
            if (index >= 0)
                index++;

            item.addActionListener(_actionListener);
            buttonGroup.add(item);
            _installedWindowItems.add(item);
        }
        focusChanged();
    }

    protected static class Item extends JCheckBoxMenuItem {
        private WindowGroup _group;

        public Item(final String label, final WindowGroup group) {
            super(label);
            _group = group;
        }

        public WindowGroup getGroup() {
            return _group;
        }
    }
}
