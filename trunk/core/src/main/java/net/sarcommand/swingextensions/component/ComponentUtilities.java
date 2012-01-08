package net.sarcommand.swingextensions.component;

import javax.swing.*;

/**
 * A utility for binding the state of a component to the property of a java bean.
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
public class ComponentUtilities {
    /**
     * Creates a binding which will enable the target component whenever the described bean value is not null.
     *
     * @param component             Component to enable / disable
     * @param targetBean            The bean to monitor
     * @param targetProperty        The bean property to monitor
     * @param enabledIfValuePresent Whether the component will be enabled or disabled when the monitored property is !=
     *                              null
     * @return The created binding.
     */
    public static EnabledPropertyToValuePresentBinding bindEnabledPropertyToValuePresent(final JComponent component,
                                                                                         final Object targetBean,
                                                                                         final String targetProperty,
                                                                                         final boolean enabledIfValuePresent) {
        return new EnabledPropertyToValuePresentBinding(component, targetBean, targetProperty, enabledIfValuePresent);
    }

    /**
     * Binds the enabled state of the target component to a selection being present in the given JList.
     *
     * @param component             Component to enable / disable
     * @param list                  JList instance to monitor
     * @param enabledIfValuePresent Whether the component will be enabled or disabled when a selection is present.
     * @return The created binding.
     */
    public static EnabledPropertyToSelectionPresentBinding bindEnabledPropertyToSelectionPresent(
            final JComponent component, final JList list, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToSelectionPresentBinding(component, list, enabledIfValuePresent);
    }

    /**
     * Binds the enabled state of the target component to a selection being present in the given JTable.
     *
     * @param component             Component to enable / disable
     * @param table                 JTable instance to monitor
     * @param enabledIfValuePresent Whether the component will be enabled or disabled when a selection is present.
     * @return The created binding.
     */
    public static EnabledPropertyToSelectionPresentBinding bindEnabledPropertyToSelectionPresent(
            final JComponent component, final JTable table, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToSelectionPresentBinding(component, table, enabledIfValuePresent);
    }

    /**
     * Binds the enabled state of the target component to a selection being present in the given JTree.
     *
     * @param component             Component to enable / disable
     * @param tree                  JTree instance to monitor
     * @param enabledIfValuePresent Whether the component will be enabled or disabled when a selection is present.
     * @return The created binding.
     */
    public static EnabledPropertyToSelectionPresentBinding bindEnabledPropertyToSelectionPresent(
            final JComponent component, final JTree tree, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToSelectionPresentBinding(component, tree, enabledIfValuePresent);
    }
}
