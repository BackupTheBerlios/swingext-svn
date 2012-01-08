package net.sarcommand.swingextensions.recentitems;

import net.sarcommand.swingextensions.formatters.FileFormat;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.prefs.Preferences;

/**
 * This class implements a list of recent files. Whenever you open or save a file, invoke addToRecentList(File). The
 * list will persist between sessions and ensure that the file last accessed is at the first position. If a file becomes
 * unavailable between sessions, it will automatically be removed from the list.
 * <p/>
 * You can automatically create a suitable JMenu for the recent list using the RecentItemsMenu class.
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
public class RecentFilesList extends RecentItemsList<File> {
    public RecentFilesList() {
        super();
    }

    public RecentFilesList(final int maximumLength) {
        super(maximumLength);
    }

    public RecentFilesList(final int maximumLength, final Preferences preferences) {
        super(maximumLength, preferences, new FileFormat());
    }

    public RecentFilesList(final Preferences preferences) {
        super(preferences, new FileFormat());
    }

    protected void preferencesUpdated() {
        super.preferencesUpdated();
        if (_internalUpdateFlag)
            return;
        final LinkedList<File> copy;
        synchronized (this) {
            copy = new LinkedList<File>(_recentItems);

            for (Iterator<File> iter = _recentItems.iterator(); iter.hasNext(); )
                if (!iter.next().exists())
                    iter.remove();
        }

        recentListUpdated(copy);
    }
}