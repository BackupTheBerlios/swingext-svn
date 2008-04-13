package net.sarcommand.swingextensions.recentitems;

import net.sarcommand.swingextensions.formatters.Formatter;
import net.sarcommand.swingextensions.internal.*;

import java.beans.*;
import java.text.*;
import java.util.*;
import java.util.prefs.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class RecentItemsList<T> {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(RecentItemsList.class);

    public static final int DEFAULT_LENGTH = 5;

    /**
     * Key for the PropertyChangeEvent fired when the list of recent items is updated.
     */
    public static final String PROPERTY_RECENT_LIST = "recentItemsList";

    /**
     * Key for the PropertyChangeEvent fired when the maximum lenth of the list is changed.
     */
    public static final String PROPERTY_MAXIMUM_LENGTH = "maximumLength";

    /**
     * Key for the PropertyChangeEvent fired when the preferences instance used is changed.
     */
    public static final String PROPERTY_PREFERENCES = "preferences";

    /**
     * The list of recent items.
     */
    protected LinkedList<T> _recentItems;

    /**
     * The maximum length of this list, that is, the maximum number of items being stored. Defaults to DEFAUL_LENGTH.
     *
     * @see RecentItemsList#DEFAULT_LENGTH
     */
    protected int _maximumLength;

    /**
     * The formatter instance being used to convert the values in this list to string representations. Those are
     * required to store those values in the preferences.
     */
    protected Formatter<T> _formatter;

    /**
     * The preferences instance being used to store the items on this list between sessions.
     */
    protected Preferences _preferences;

    /**
     * This flag is used when internal updates occur. Changes to the preferences will be ignored when this flag is set.
     */
    protected volatile boolean _internalUpdateFlag;

    /**
     * PropertyChangeSupport used for this class.
     */
    protected PropertyChangeSupport _changeSupport;

    /**
     * Listener instance used to monitor the preferences for changes.
     */
    protected PreferenceChangeListener _preferenceListener;

    /**
     * Creates a new RecentItemsList with the default maximum length and no preferences.
     */
    public RecentItemsList() {
        initialize(DEFAULT_LENGTH, null, null);
    }

    /**
     * Creates a new RecentItemsList with the given maximum length and no preferences.
     *
     * @param maximumLength Maximum number of items on this list.
     */
    public RecentItemsList(final int maximumLength) {
        initialize(maximumLength, null, null);
    }

    /**
     * Creates a new RecentItemsList with the default maximum length, using the given preferences.
     *
     * @param preferences Preferences instance to store the values on this list on.
     * @param formatter   Formatter instance used to store values in the preferences.
     */
    public RecentItemsList(final Preferences preferences, final Formatter formatter) {
        initialize(DEFAULT_LENGTH, preferences, formatter);
    }

    /**
     * Creates a new RecentItemsList with the specified maximum length, using the given preferences.
     *
     * @param maximumLength Maximum number of items on this list.
     * @param preferences   Preferences instance to store the values on this list on.
     * @param formatter     Formatter instance used to store values in the preferences.
     */
    public RecentItemsList(final int maximumLength, final Preferences preferences, final Formatter formatter) {
        initialize(maximumLength, preferences, formatter);
    }

    /**
     * Internal initializer method. You should only access this method when subclassing RecentItemsList.
     *
     * @param length      maximumLenth of the list.
     * @param preferences Preferences instance in which the values in this list are stored.
     * @param formatter   Formatter instance to use for storing values in the preferences.
     */
    protected void initialize(final int length, final Preferences preferences, final Formatter formatter) {
        if (length <= 0)
            throw new IllegalArgumentException("Illegal maximum list length: Has to be > 0, was " + length);
        if ((preferences == null) != (formatter == null))
            throw new IllegalArgumentException("If you set a preference node, you have to set a formatter instance" +
                    " as well. Preferences: " + preferences + " Formatter: " + formatter);

        _preferenceListener = new PreferenceChangeListener() {
            public void preferenceChange(final PreferenceChangeEvent evt) {
                preferencesUpdated();
            }
        };

        _changeSupport = new PropertyChangeSupport(this);
        _recentItems = new LinkedList<T>();
        setMaximumLength(length);
        setPreferences(preferences);
        setFormatter(formatter);
    }

    /**
     * Returns the list of recent items on this list. The returned collections is not modifiable.
     *
     * @return the list of recent items on this list (unmodifiable).
     */
    public List<T> getRecentItems() {
        synchronized (_recentItems) {
            return Collections.unmodifiableList(new ArrayList<T>(_recentItems));
        }
    }

    /**
     * Removes an item entry from the recent list.
     *
     * @param item the item to remove.
     */
    public void removeFromRecentList(final T item) {
        if (item == null)
            throw new IllegalArgumentException("Parameter 'item' must not be null!");

        __log.debug(this + "removing item " + item);
        final LinkedList<T> copy;
        synchronized (_recentItems) {
            copy = new LinkedList<T>(_recentItems);
            _recentItems.remove(item);
        }
        recentListUpdated(copy);
    }

    /**
     * Adds an item to the recent list. If the list already contained the item, it will move to the top position.
     * Otherwise, it will be added at position 0 and the list will be truncated if its new length exceeds the
     * length property.
     *
     * @param item item to add to the recent list.
     */
    public void addToRecentList(final T item) {
        final LinkedList<T> copy;
        synchronized (_recentItems) {
            copy = new LinkedList<T>(_recentItems);

            if (_recentItems.contains(item)) {
                _recentItems.remove(item);
                _recentItems.add(0, item);
            } else {
                _recentItems.add(0, item);
                if (_recentItems.size() > _maximumLength)
                    _recentItems.removeLast();
            }
        }

        recentListUpdated(copy);
    }

    /**
     * Invoked when the list of recent items has changed due to an add or remove. This method will update the
     * preferences (if applicable) and fire a PROPERTY_RECENT_LIST change event.
     *
     * @param previousList The previous list of recent items.
     */
    protected void recentListUpdated(final LinkedList<T> previousList) {
        if (_internalUpdateFlag)
            return;
        synchronized (_recentItems) {
            final Preferences node = getPreferenceNode();
            if (node != null) {
                _internalUpdateFlag = true;
                try {
                    clearNode(node);
                    int index = 0;
                    for (T item : _recentItems)
                        node.put("" + index++, _formatter.convertToString(item));
                    try {
                        node.flush();
                    } catch (BackingStoreException e) {
                        throw new RuntimeException("Exception while committing changes to node " + node.absolutePath(), e);
                    }
                } finally {
                    _internalUpdateFlag = false;
                }
            }
            _changeSupport.firePropertyChange(PROPERTY_RECENT_LIST, new LinkedList<T>(previousList),
                    new LinkedList<T>(_recentItems));
        }
    }

    /**
     * Utility method which will clear the given preferences node, removing all keys.
     *
     * @param node node to clear.
     */
    protected void clearNode(final Preferences node) {
        try {
            node.clear();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Could not clear the node for update", e);
        }
    }

    /**
     * Invoked when the preferences have been updated. Will update the list of recent files.
     */
    protected void preferencesUpdated() {
        if (_internalUpdateFlag)
            return;
        __log.debug("Preferences have been updated, will adapt recent list");
        final LinkedList<T> copy;
        synchronized (_recentItems) {
            copy = new LinkedList<T>(_recentItems);
            _recentItems.clear();

            final Preferences node = getPreferenceNode();
            if (node != null) {
                final String[] keys;

                /* Get the keys stored in the preferences */
                try {
                    keys = node.keys();
                } catch (BackingStoreException e) {
                    throw new RuntimeException("Could not access the defined preferences @" + node.absolutePath(), e);
                }

                final HashMap<Integer, T> items = new HashMap<Integer, T>(getMaximumLength());
                for (String key : keys) {
                    final int index;
                    try {
                        index = Integer.parseInt(key);
                    } catch (NumberFormatException e) {
                        __log.warn("Found a broken entry in preference node " + node.absolutePath() + " for key " + key);
                        node.remove(key);
                        continue;
                    }

                    final String valueRepresentation = node.get(key, null);

                    if (valueRepresentation == null) {
                        __log.warn("Found a broken entry in preference node " + node.absolutePath() + " for key " + key);
                        node.remove(key);
                        continue;
                    }

                    final T value;
                    try {
                        value = _formatter.convertToValue(valueRepresentation);
                    } catch (ParseException e) {
                        throw new RuntimeException("The given formatter " + _formatter + " could not decipher the" +
                                " string representation " + valueRepresentation);
                    }

                    if (value == null) {
                        __log.warn("Found a broken entry in preference node " + node.absolutePath() + " for key " + key);
                        continue;
                    }

                    if (!items.containsValue(value))
                        items.put(index, value);
                }

                final LinkedList<Integer> keyList = new LinkedList<Integer>(items.keySet());
                Collections.sort(keyList);
                for (Integer i : keyList) {
                    _recentItems.add(items.get(i));
                    if (_recentItems.size() >= getMaximumLength())
                        break;
                }

                try {
                    node.flush();
                } catch (BackingStoreException e) {
                    throw new RuntimeException("Could not store the changes " +
                            "made to preference node " + node.absolutePath(), e);
                }
            } else
                __log.error("Could not obtain the preferences node, most likely the preferences have not been" +
                        " configured correctly");
        }

        _internalUpdateFlag = true;
        try {
            _changeSupport.firePropertyChange(PROPERTY_RECENT_LIST, copy, _recentItems);
        } finally {
            _internalUpdateFlag = false;
        }
    }

    /**
     * Utility method which will return the preferences node used to store data, if one has been configured. The node
     * will be sync()ed before it is returned, so theoretically it is assured that all values are up to date.
     *
     * @return returns the configured preference node, or null if there is none.
     */
    protected Preferences getPreferenceNode() {
        if ((_preferences == null) != (_formatter == null)) {
            __log.warn("A preference node has been specified, but no formatter was given. Preferences can" +
                    " not be used.");
            return null;
        }
        if (_preferences != null) {
            __log.trace("Obtaining preferences from " + _preferences.absolutePath());
            try {
                _preferences.sync();
            } catch (BackingStoreException e) {
                throw new RuntimeException("Could not access prefernce node " + _preferences.absolutePath(), e);
            }
            return _preferences;
        }
        return null;
    }

    /**
     * Returns the maximum length of this list, that is, the maximum number of recent entries being saved.
     *
     * @return the maximum length of this list
     */
    public int getMaximumLength() {
        return _maximumLength;
    }

    /**
     * Sets the maximum length of this list, that is, the maximum number of recent entries being saved.
     *
     * @param maximumLength the maximum length of this list
     */
    public void setMaximumLength(final int maximumLength) {
        _maximumLength = maximumLength;
    }

    /**
     * Returns the preferences instance used to store the items on this list.
     *
     * @return the preferences instance used to store the items on this list.
     */
    public Preferences getPreferences() {
        return _preferences;
    }

    /**
     * Sets the preferences instance used to store the items on this list.
     *
     * @param preferences the preferences instance used to store the items on this list.
     */
    public void setPreferences(final Preferences preferences) {
        if (_preferences != null)
            _preferences.removePreferenceChangeListener(_preferenceListener);
        _preferences = preferences;
        if (_preferences != null)
            _preferences.addPreferenceChangeListener(_preferenceListener);
        if (_formatter != null && _preferences != null)
            preferencesUpdated();
    }

    /**
     * Returns the Formatter instance used to store the items on this list on the preferences.
     *
     * @return the Formatter instance used to store the items on this list on the preferences.
     */
    public Formatter<T> getFormatter() {
        return _formatter;
    }

    /**
     * Sets the Formatter instance used to store the items on this list on the preferences.
     *
     * @param formatter the Formatter instance used to store the items on this list on the preferences.
     */
    public void setFormatter(final Formatter<T> formatter) {
        _formatter = formatter;
        if (_formatter != null && _preferences != null)
            preferencesUpdated();
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(listener);
    }

    public boolean hasListeners(final String propertyName) {
        return _changeSupport.hasListeners(propertyName);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
