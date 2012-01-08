package net.sarcommand.swingextensions.container;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Container-related utilities.
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
public class ContainerUtilities {
    public static <T> Collection<T> getChildrenOfType(final Container container,
                                                      final Class<T> type) {
        return getChildrenOfType(container, type, true);
    }

    /**
     * Lists all child components for the given container that match a certain type. For example, you can use this
     * method to find all instances of JButton nested within a given component. Note that the type parameter does not
     * have to extend JComponent, which means you can also use this method to search for implementations of a certain
     * interface.
     *
     * @param container           The container to search.
     * @param type                The type of the component to look for.
     * @param traverseRecursively Whether or not to descend into the container's child components while searching.
     * @param <T>                 The inferred component type.
     * @return A list of all the components nested in the container.
     */
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
