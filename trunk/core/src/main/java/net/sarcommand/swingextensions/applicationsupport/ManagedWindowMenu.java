package net.sarcommand.swingextensions.applicationsupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Creates a JMenu which will hold an item for every element in a given WindowGroup. The menu items will automatically
 * bring a window to front when it is clicked. The menu will update itself when items are added to or removed from the
 * group automatically. You can add additional items to the menu, which will be preserved during updates.
 * <p/>
 * <hr/> Copyright 2006-2009 Torsten Heup
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
            final String windowTitle = g.getWindowID(window);
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
            for (String w : group.getWindowIDs()) {
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
