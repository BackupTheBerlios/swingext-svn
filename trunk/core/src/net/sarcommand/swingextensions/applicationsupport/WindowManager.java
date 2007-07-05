package net.sarcommand.swingextensions.applicationsupport;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class WindowManager {
    public static final String DEFAULT_WINDOW_GROUP = "defaultWindowGroup";

    private HashMap<String, WindowGroup> _windowGroups;

    public WindowManager() {
        _windowGroups = new HashMap<String, WindowGroup>(2);
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
