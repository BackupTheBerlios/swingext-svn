package net.sarcommand.swingextensions.container;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 */
public class ContainerUtilities {
    public static <T> Collection<T> getChildrenOfType(final Container container,
                                                      final Class<T> type) {
        return getChildrenOfType(container, type, true);
    }

    public static <T> Collection<T> getChildrenOfType(final Container container,
                                                      final Class<T> type,
                                                      final boolean traverseRecursively) {
        final ArrayList<T> result = new ArrayList<T>();
        final Component[] components = container.getComponents();
        for (Component c : components) {
            if (type.isAssignableFrom(c.getClass()))
                result.add((T) c);
            if (traverseRecursively && c instanceof Container)
                result.addAll(getChildrenOfType((Container) c, type, true));
        }
        return result;
    }
}
