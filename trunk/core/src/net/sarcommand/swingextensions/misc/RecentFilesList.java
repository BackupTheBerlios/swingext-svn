package net.sarcommand.swingextensions.misc;

import net.sarcommand.swingextensions.event.EventSupport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * This class implements a small utility which allows you to manage a list of recent files for your application. The
 * concept of the recent list is pretty simple - whenever you access a file, programatically invoke addToRecentList().
 * The recent list will automatically keep track of the last files you accessed in order and store them to the user
 * preferences.
 * <p/>
 * You can invoke createMenu to automatically create a JMenu which will be updated whenever the list changes. Multiple
 * menu instances can be created for one list.
 * <p/>
 * In order to be notified when the user selects a file from the list, you can set a default action (making use of
 * the ActionManager concept) or install an ActionListener on this class. In both cases, the action command of the
 * event fired will contain the full path of the file selected.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class RecentFilesList {
    public static final String PROPERTY_RECENT_LIST = "recentFileList";
    public static final String PROPERTY_LENGTH = "length";
    public static final String PROPERTY_PREFERENCE_KEY = "preferenceKey";
    public static final String PROPERTY_PREFERENCES = "preferences";

    public static final String CLIENT_PROPERTY_FILE = "file";

    public static final int DEFAULT_LENGTH = 5;

    protected Preferences _preferences;
    protected String _preferenceKey;

    protected int _length;

    protected PropertyChangeSupport _pcs;

    protected PreferenceChangeListener _preferenceListener;
    protected LinkedList<File> _recentFiles;
    protected LinkedList<WeakReference<JMenu>> _menus;

    protected Action _action;
    protected ActionListener _actionListener;
    protected EventSupport<ActionListener> _actionSupport;

    protected volatile boolean _ownUpdateFlag;

    /**
     * Creates a new recent file list instance.
     */
    public RecentFilesList() {
        initialize(DEFAULT_LENGTH, null, null);
    }

    /**
     * Creates a new recent file list instance.
     *
     * @param length Maximum number of items stored in this list.
     */
    public RecentFilesList(final int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Illegal length, supposed to be >= 0 but was " + length);
        initialize(length, null, null);
    }

    /**
     * Creates a new recent file list instance.
     *
     * @param length  Maximum number of items stored in this list.
     * @param prefs   Preferences to use for storing this list, non-null.
     * @param prefKey Preference key to use for storing this list, non-null.
     */
    public RecentFilesList(final int length, final Preferences prefs, final String prefKey) {
        if (length <= 0)
            throw new IllegalArgumentException("Illegal length, supposed to be >= 0 but was " + length);
        if (prefs == null)
            throw new IllegalArgumentException("Parameter 'prefs' must not be null!");
        if (prefKey == null)
            throw new IllegalArgumentException("Parameter 'prefKey' must not be null!");

        initialize(length, prefs, prefKey);
    }

    /**
     * Creates a new recent file list instance.
     *
     * @param prefs   Preferences to use for storing this list, non-null.
     * @param prefKey Preference key to use for storing this list, non-null.
     */
    public RecentFilesList(final Preferences prefs, final String prefKey) {
        if (prefs == null)
            throw new IllegalArgumentException("Parameter 'prefs' must not be null!");
        if (prefKey == null)
            throw new IllegalArgumentException("Parameter 'prefKey' must not be null!");

        initialize(DEFAULT_LENGTH, prefs, prefKey);
    }

    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        _pcs.addPropertyChangeListener(listener);
    }

    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        _pcs.removePropertyChangeListener(listener);
    }

    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String,java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        _pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String,java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        _pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Initialized the list.
     *
     * @param length        Maximum item count for this list.
     * @param preferences   Preferences instance used to store the recent files.
     * @param preferenceKey Preferences key used to store the recent files.
     */
    protected void initialize(final int length, final Preferences preferences, final String preferenceKey) {
        _pcs = new PropertyChangeSupport(this);
        _actionSupport = EventSupport.create(ActionListener.class);
        _preferenceListener = new PreferenceChangeListener() {
            public void preferenceChange(final PreferenceChangeEvent evt) {
                if (!_ownUpdateFlag && _preferenceKey != null && evt.getKey().equals(_preferenceKey))
                    preferencesUpdated();
            }
        };
        _menus = new LinkedList<WeakReference<JMenu>>();
        _recentFiles = new LinkedList<File>();
        _actionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JMenuItem item = (JMenuItem) e.getSource();
                final File file = (File) item.getClientProperty(CLIENT_PROPERTY_FILE);
                final ActionEvent event = new ActionEvent(item, ActionEvent.ACTION_PERFORMED, file.getAbsolutePath());
                if (_action != null)
                    _action.actionPerformed(event);
                _actionSupport.delegate().actionPerformed(event);
            }
        };

        setLength(length);
        setPreferences(preferences);
        setPreferenceKey(preferenceKey);
    }

    /**
     * Returns the preferences key currently being used to store this list.
     *
     * @return the preferences key currently being used to store this list.
     */
    public String getPreferenceKey() {
        return _preferenceKey;
    }

    /**
     * Sets the preference key to use for storing this list (as long as the preferences property has been set as well).
     *
     * @param preferenceKey preference key to use for storing this list, non-null.
     */
    public void setPreferenceKey(final String preferenceKey) {
        if (preferenceKey == null)
            throw new IllegalArgumentException("Parameter 'preferenceKey' must not be null!");

        final String oldKey = _preferenceKey;
        _preferenceKey = preferenceKey;
        _pcs.firePropertyChange(PROPERTY_PREFERENCE_KEY, oldKey, preferenceKey);
        preferencesUpdated();
    }

    /**
     * Returns the preferences instance currently used by this list.
     *
     * @return the preferences instance currently used by this list.
     */
    public Preferences getPreferences() {
        return _preferences;
    }

    /**
     * Sets the preferences instance which should be used for backing this list (as long as a preferences key is
     * passed as well).
     * <p/>
     * Will fire a PROPERTY_PREFERENCES change event.
     *
     * @param preferences Preferences instance to use, non-null.
     */
    public void setPreferences(final Preferences preferences) {
        if (preferences == null)
            throw new IllegalArgumentException("Parameter 'preferences' must not be null!");

        if (_preferences != null)
            _preferences.removePreferenceChangeListener(_preferenceListener);

        final Preferences oldPrefs = _preferences;
        _preferences = preferences;

        preferences.addPreferenceChangeListener(_preferenceListener);
        _pcs.firePropertyChange(PROPERTY_PREFERENCES, oldPrefs, preferences);
        preferencesUpdated();
    }

    /**
     * Returns the maximum number of items for this list.
     *
     * @return the maximum number of items for this list.
     */
    public int getLength() {
        return _length;
    }

    /**
     * Sets the maximum length (maximum number of entries) for this list.
     * <p/>
     * Will fire a PROPERTY_LENGTH change event.
     *
     * @param length New maximum length for this list.
     */
    public void setLength(final int length) {
        final int oldLength = _length;
        _length = length;
        _pcs.firePropertyChange(PROPERTY_LENGTH, oldLength, length);
    }

    /**
     * Creates a new JMenu representing this list. The menu will have no name or icon or anything, you will have to
     * set those manually. However, the menu will automatically be updated when the list of recent files changes
     * in any way. Menus created by this method will be weakly referenced, so whenever you dispose of a menu it will
     * be garbage collected without further effort from your side.
     *
     * @return a new JMenu representing this list
     */
    public JMenu createMenu() {
        final JMenu menu = new JMenu();
        _menus.add(new WeakReference<JMenu>(menu));
        updateMenu(menu);
        return menu;
    }

    /**
     * Returns the list of recent files, ordered by their access time. The returned list is not modifiable.
     *
     * @return List of recent files, unmodifiable.
     */
    public List<File> getRecentFiles() {
        return Collections.unmodifiableList(_recentFiles);
    }

    /**
     * Invoked when the preferences have been updated. Will update the list and created menu instances accordingly.
     */
    protected void preferencesUpdated() {
        final LinkedList<File> copy = new LinkedList<File>(_recentFiles);
        _recentFiles.clear();
        if (_preferenceKey != null && _preferences != null) {
            final String value = _preferences.get(_preferenceKey, null);
            if (value != null) {
                final String[] items = value.split("\\|");
                final ArrayList<File> files = new ArrayList<File>(items.length);
                for (String s : items) {
                    final File file = new File(s);
                    if (file.exists())
                        files.add(file);
                }

                for (int i = 0; i < Math.min(files.size(), _length); i++)
                    _recentFiles.add(files.get(i));
            }
        }

        updateMenus();

        _pcs.firePropertyChange(PROPERTY_RECENT_LIST, copy, _recentFiles);
    }

    /**
     * Invokes updateMenu for all menu instances created for this list. This method will prune the list of menus,
     * removing any references to menus which have been garbage collected.
     */
    protected void updateMenus() {
        for (Iterator<WeakReference<JMenu>> iter = _menus.iterator(); iter.hasNext();) {
            final WeakReference<JMenu> ref = iter.next();
            if (ref.get() == null) {
                iter.remove();
                continue;
            }

            updateMenu(ref.get());
        }
    }

    /**
     * Invoked when a JMenu instance representing this list needs to be updated.
     *
     * @param menu menu which needs to be updated.
     */
    protected void updateMenu(final JMenu menu) {
        menu.removeAll();
        for (File f : _recentFiles) {
            final JMenuItem item = new JMenuItem(f.getName());
            item.putClientProperty(CLIENT_PROPERTY_FILE, f);
            item.addActionListener(_actionListener);
            item.setToolTipText(f.getAbsolutePath());
            menu.add(item);
        }
    }

    /**
     * Adds a file to the recent list. If the list already contained the file, it will move to the top position.
     * Otherwise, it will be added at position 0 and the list will be truncated if its new length exceeds the
     * length property.
     *
     * @param file File to add to the recent list.
     */
    public void addToRecentList(final File file) {
        final LinkedList<File> copy = new LinkedList<File>(_recentFiles);

        if (_recentFiles.contains(file)) {
            _recentFiles.remove(file);
            _recentFiles.add(0, file);
        } else {
            _recentFiles.add(0, file);
            if (_recentFiles.size() > _length)
                _recentFiles.removeLast();
        }

        recentListUpdated(copy);
    }

    /**
     * Invoked when the list of recent files has changed due to an add or remove. This method will update the
     * preferences (if applicable) and fire a PROPERTY_RECENT_LIST change event.
     *
     * @param previousList The previous list of recent files.
     */
    protected void recentListUpdated(final LinkedList<File> previousList) {
        updateMenus();

        if (_preferenceKey != null && _preferences != null) {
            final StringBuilder builder = new StringBuilder();
            for (Iterator<File> iter = _recentFiles.iterator(); iter.hasNext();) {
                final File f = iter.next();
                builder.append(f.getAbsolutePath());
                if (iter.hasNext())
                    builder.append('|');
            }
            _ownUpdateFlag = true;
            _preferences.put(_preferenceKey, builder.toString());
            _ownUpdateFlag = false;
        }

        _pcs.firePropertyChange(PROPERTY_RECENT_LIST, new LinkedList<File>(previousList),
                new LinkedList<File>(_recentFiles));
    }

    /**
     * Removes a file entry from the recent list.
     *
     * @param file file to remove.
     */
    public void removeFromRecentList(final File file) {
        final LinkedList<File> copy = new LinkedList<File>(_recentFiles);
        _recentFiles.remove(file);
        recentListUpdated(copy);
    }

    /**
     * Sets the action which should be triggered whenever the user selects a file from one of the generated recent
     * menus. The action command of the event being fired will contain the full path of the selected file.
     *
     * @param action action which should be triggered whenever the user selects a file.
     */
    public void setAction(final Action action) {
        _action = action;
    }

    /**
     * Adds an action listener to this list. The listener will be triggered when the user selected a file from one of
     * the generated JMenu instances. The action command of the event being fired will contain the full path of the
     * selected file.
     *
     * @param listener Listener to install.
     */
    public void addActionListener(final ActionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _actionSupport.addListener(listener);
    }

    /**
     * Removes a previously installed action listener.
     *
     * @param listener Listener to remove.
     */
    public void removeActionListener(final ActionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _actionSupport.removeListener(listener);
    }
}
