package net.sarcommand.swingextensions.recentitems;

import net.sarcommand.swingextensions.event.EventSupport;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;
import net.sarcommand.swingextensions.menuitemfactory.FileMenuItemFactory;
import net.sarcommand.swingextensions.menuitemfactory.MenuItemFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;
import java.util.List;

/**
 * This class creates a JMenu instance which will be synchronized with a given RecentItemsList or one of its subclasses.
 * The menu will automatically update whenever the list changes, and will propagate user selections made using swing's
 * action mechanism. You may choose to install ActionListener instances, or use the Action interface in order to be
 * notified. The selected value can either be obtained using the getLastSelection() method or by examining the
 * ActionEvent's actionCommand property: The string representation of the selected value will be stored here, formatted
 * by the Formatter instance used by the enclosed RecentItemsList.
 * <p/>
 * By default, the generated JMenuItems will simply contain the string representation of the given values. If you want
 * to customize the way the JMenuItems are created (for instance by adding an Icon), you can set a MenuItemFactory
 * instance.
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
public class RecentListMenu extends JMenu {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(RecentListMenu.class);

    /**
     * This property is used to store the value represented by a JMenuItem in the item's client properties.
     */
    public static final String ITEM_PROPERTY = RecentFilesList.class.getName() + ".item";

    /**
     * The recent items list rendered by this menu instance.
     */
    protected RecentItemsList _recentItems;

    /**
     * An optional MenuItemFactory instance used to create the JMenuItems in this menu.
     */
    protected MenuItemFactory _menuItemFactory;

    /**
     * Listener instance used to observe the set RecentItemsList.
     */
    protected PropertyChangeListener _recentListListener;

    /**
     * The action which should be invoked when an item on this list is selected.
     */
    protected Action _action;

    /**
     * Support class holding the list of registrered ActionListeners.
     */
    protected EventSupport<ActionListener> _actionSupport;

    /**
     * ActionListener installed on each menu item.
     */
    protected ActionListener _actionListener;

    /**
     * Creates a new RecentListMenu. This constructor is intended to be used by configuration frameworks like spring. Be
     * sure to invoke setRecentItems(RecentItemsList) before using this instance.
     */
    public RecentListMenu() {
        initialize(null, null);
    }

    /**
     * Creates a new RecentListMenu backed by the specified RecentItemsList.
     *
     * @param list RecentItemsList instance this menu will work on.
     */
    public RecentListMenu(final RecentItemsList list) {
        initialize(list, null);
    }

    /**
     * Creates a new RecentListMenu backed by the specified RecentItemsList, using the given factory to create items.
     *
     * @param list    RecentItemsList instance this menu will work on.
     * @param factory MenuItemFactory instance used to create the JMenuItems.
     */
    public RecentListMenu(final RecentItemsList list, final MenuItemFactory factory) {
        initialize(list, factory);
        setRecentItems(list);
        setMenuItemFactory(factory);
    }

    /**
     * Returns the RecentItemsList used by this menu.
     *
     * @return the RecentItemsList used by this menu.
     */
    public RecentItemsList getRecentItems() {
        return _recentItems;
    }

    /**
     * Sets the RecentItemsList used by this menu.
     *
     * @param recentItems the RecentItemsList used by this menu.
     */
    public void setRecentItems(final RecentItemsList recentItems) {
        if (_recentItems != null)
            _recentItems.removePropertyChangeListener(RecentItemsList.PROPERTY_RECENT_LIST, _recentListListener);

        _recentItems = recentItems;

        if (_recentItems != null)
            _recentItems.addPropertyChangeListener(RecentItemsList.PROPERTY_RECENT_LIST, _recentListListener);

        rebuildMenu();
    }

    /**
     * Returns the MenuItemFactory used by this menu, if there is one.
     *
     * @return the MenuItemFactory used by this menu, if there is one.
     */
    public MenuItemFactory getMenuItemFactory() {
        return _menuItemFactory;
    }

    /**
     * Sets the MenuItemFactory used by this menu.
     *
     * @param menuItemFactory the MenuItemFactory used by this menu.
     */
    public void setMenuItemFactory(final MenuItemFactory menuItemFactory) {
        _menuItemFactory = menuItemFactory;
        rebuildMenu();
    }

    /**
     * Returns the Action which will be invoked when an item from the menu is selected.
     *
     * @return the Action which will be invoked when an item from the menu is selected.
     */
    public Action getAction() {
        return _action;
    }

    /**
     * Sets the Action which will be invoked when an item from the menu is selected.
     *
     * @param action the Action which will be invoked when an item from the menu is selected.
     */
    public void setAction(final Action action) {
        _action = action;
        setText((String) action.getValue(Action.NAME));
        setToolTipText((String) action.getValue(Action.SHORT_DESCRIPTION));
        rebuildMenu();
    }

    /**
     * Adds an ActionListener which will be notified when an item from the menu is selected.
     *
     * @param listener an ActionListener which will be notified when an item from the menu is selected.
     */
    public void addActionListener(final ActionListener listener) {
        _actionSupport.addListener(listener);
        rebuildMenu();
    }

    /**
     * Removes a previously installed ActionListener.
     *
     * @param listener a previously installed ActionListener.
     */
    public void removeActionListener(final ActionListener listener) {
        _actionSupport.removeListener(listener);
        rebuildMenu();
    }

    /**
     * Invoked when the list of recent items or the MenuItemFactory have changed. This method will clear the existing
     * menu items and create them from scratch.
     */
    protected void rebuildMenu() {
        removeAll();
        final List list = _recentItems.getRecentItems();
        final Format formatter = _recentItems.getFormat();
        final MenuItemFactory factory = getMenuItemFactory();
        for (Object o : list) {
            final JMenuItem item;
            if (factory != null)
                item = factory.createItem(o);
            else if (formatter != null)
                item = new JMenuItem(formatter.format(o));
            else
                item = new JMenuItem(o.toString());
            item.putClientProperty(ITEM_PROPERTY, o);
            item.addActionListener(_actionListener);
            add(item);
        }
    }

    /**
     * Internal initializer method for subclasser's conveniance.
     *
     * @param recentItems the list of recent items to use.
     * @param factory     the MenuItemFactory to use.
     */
    protected void initialize(final RecentItemsList recentItems, final MenuItemFactory factory) {
        _recentListListener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                rebuildMenu();
            }
        };

        _actionSupport = EventSupport.create(ActionListener.class);

        _actionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                __log.debug("Recent item selected: " + e);
                final JMenuItem item = (JMenuItem) e.getSource();
                final Object value = item.getClientProperty(ITEM_PROPERTY);
                final Format formatter = _recentItems.getFormat();
                final String stringRepresentation = formatter == null ? value.toString() :
                        formatter.format(value);
                final ActionEvent event = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED,
                        stringRepresentation);

                if (_action != null)
                    _action.actionPerformed(event);
                _actionSupport.delegate().actionPerformed(event);
            }
        };

        setRecentItems(recentItems);
        setMenuItemFactory(factory != null ? factory : getDefaultMenuItemFactory(_recentItems));
    }

    /**
     * Returns the default MenuItemFactory instance for the given recent list type.
     *
     * @param list the recent items list displayed by the menu.
     * @return the default MenuItemFactory instance for the given recent list type.
     */
    protected MenuItemFactory getDefaultMenuItemFactory(final RecentItemsList list) {
        if (list instanceof RecentFilesList)
            return new FileMenuItemFactory();
        return null;
    }
}
