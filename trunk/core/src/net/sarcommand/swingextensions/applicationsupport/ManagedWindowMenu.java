package net.sarcommand.swingextensions.applicationsupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ManagedWindowMenu extends JMenu {
    private final Collection<WindowGroup> _windowGroups;
    private final LinkedList<Item> _items;
    private final ActionListener _actionListener;

    public ManagedWindowMenu(final Collection<WindowGroup> windowGroups) {
        _windowGroups = windowGroups;
        _items = new LinkedList<Item>();
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
