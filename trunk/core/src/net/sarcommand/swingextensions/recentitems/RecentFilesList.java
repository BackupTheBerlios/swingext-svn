package net.sarcommand.swingextensions.recentitems;

import net.sarcommand.swingextensions.formatters.*;

import java.io.*;
import java.util.*;
import java.util.prefs.*;

/**
 * This class implements a list of recent files. Whenever you open or save a file, invoke addToRecentList(File). The
 * list will persist between sessions and ensure that the file last accessed is at the first position. If a file
 * becomes unavailable between sessions, it will automatically be removed from the list.
 * <p/>
 * You can automatically create a suitable JMenu for the recent list using the RecentItemsMenu class.
 */
public class RecentFilesList extends RecentItemsList<File> {
    public RecentFilesList() {
        super();
    }

    public RecentFilesList(final int maximumLength) {
        super(maximumLength);
    }

    public RecentFilesList(final int maximumLength, final Preferences preferences) {
        super(maximumLength, preferences, new FileFormatter());
    }

    public RecentFilesList(final Preferences preferences) {
        super(preferences, new FileFormatter());
    }

    protected void preferencesUpdated() {
        super.preferencesUpdated();
        if (_internalUpdateFlag)
            return;
        final LinkedList<File> copy;
        synchronized (_recentItems) {
            copy = new LinkedList<File>(_recentItems);

            for (Iterator<File> iter = _recentItems.iterator(); iter.hasNext();)
                if (!iter.next().exists())
                    iter.remove();
        }

        recentListUpdated(copy);
    }
}