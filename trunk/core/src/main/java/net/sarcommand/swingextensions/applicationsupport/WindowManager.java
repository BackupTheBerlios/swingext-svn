package net.sarcommand.swingextensions.applicationsupport;

import java.awt.*;
import java.util.*;

/**
 * todo Add javadoc <hr/> Copyright 2006-2012 Torsten Heup
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
public class WindowManager {
    public static final String DEFAULT_WINDOW_GROUP = "defaultWindowGroup";

    private Map<String, WindowGroup> _windowGroups;

    public WindowManager() {
        _windowGroups = Collections.synchronizedMap(new HashMap<String, WindowGroup>(2));
        _windowGroups.put(DEFAULT_WINDOW_GROUP, new WindowGroup());
    }

    public void addWindow(final String identifier, final Window window) {
        registerWindow(identifier, DEFAULT_WINDOW_GROUP, window);
    }

    public void registerWindow(final String identifier, final String windowGroup, final Window window) {
        WindowGroup set = _windowGroups.get(windowGroup);
        if (set == null) {
            set = new WindowGroup();
            _windowGroups.put(windowGroup, set);
        }
        set.addWindow(identifier, window);
    }

    public WindowGroup getWindowGroup(final String windowGroupID) {
        return _windowGroups.get(windowGroupID);
    }

    public WindowGroup getDefaultWindowGroup() {
        return _windowGroups.get(DEFAULT_WINDOW_GROUP);
    }

    public ManagedWindowMenu getWindowMenu() {
        return new ManagedWindowMenu(_windowGroups.values());
    }

    public ManagedWindowMenu getWindowMenu(final String... windowGroup) {
        final Collection<WindowGroup> groups = new LinkedList<WindowGroup>();
        for (String s : windowGroup) {
            final WindowGroup group = _windowGroups.get(s);
            if (group != null)
                groups.add(group);
        }
        return new ManagedWindowMenu(groups);
    }
}
