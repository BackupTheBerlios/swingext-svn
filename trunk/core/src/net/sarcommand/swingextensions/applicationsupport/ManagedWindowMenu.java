package net.sarcommand.swingextensions.applicationsupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ManagedWindowMenu extends JMenu {
    protected final Collection<WindowGroup> _windowGroups;
    protected final LinkedList<Item> _items;
    protected final ActionListener _actionListener;

    protected ArrayList<Component> _itemsBeforeWindows;
    protected ArrayList<Component> _itemsAfterWindows;

    public ManagedWindowMenu(final Collection<WindowGroup> windowGroups) {
        _windowGroups = windowGroups;
        _items = new LinkedList<Item>();
        _itemsAfterWindows = new ArrayList<Component>(2);
        _itemsBeforeWindows = new ArrayList<Component>(2);

        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String propertyName = evt.getPropertyName();
                if (propertyName.equals(WindowGroup.FOCUSED_WINDOW_PROPERTY))
                    focusChanged();
                else if (propertyName.equals(WindowGroup.WINDOW_COUNT_PROPERTY))
                    rebuildMenu();
            }
        };

        _actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Item item = (Item) e.getSource();
                final Window window = item.getGroup().getWindow(item.getText());
                window.setVisible(true);
                window.requestFocus();
            }
        };

        for (WindowGroup g : windowGroups)
            g.addPropertyChangeListener(listener);
    }

    public void addComponentBeforeWindowItems(final Component c) {
        _itemsBeforeWindows.add(c);
    }

    public void addComponentAfterWindowItems(final Component c) {
        _itemsAfterWindows.add(c);
    }

    protected void focusChanged() {
        for (WindowGroup g : _windowGroups) {
            final Window window = g.getCurrentFocusOwner();
            final String windowTitle = g.getWindowName(window);
            if (window != null) {
                for (Item t : _items) {
                    if (t.getText().equals(windowTitle) && t.getGroup().equals(g)) {
                        t.setSelected(true);
                        return;
                    }
                }
            }
        }
    }

    protected void rebuildMenu() {
        removeAll();
        for (Component c : _itemsBeforeWindows)
            add(c);
        _items.clear();
        ButtonGroup buttonGroup = new ButtonGroup();
        for (Iterator<WindowGroup> it = _windowGroups.iterator(); it.hasNext();) {
            final WindowGroup group = it.next();
            for (String w : group.getWindowNames()) {
                final Item item = new Item(w, group);
                add(item);
                item.addActionListener(_actionListener);
                buttonGroup.add(item);
                _items.add(item);
            }
            if (it.hasNext())
                add(new JSeparator());
        }
        for (Component c : _itemsAfterWindows)
            add(c);
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
