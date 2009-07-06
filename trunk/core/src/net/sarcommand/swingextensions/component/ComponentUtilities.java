package net.sarcommand.swingextensions.component;

import javax.swing.*;

/**
 */
public class ComponentUtilities {
    public static EnabledPropertyToValuePresentBinding bindEnabledPropertyToValuePresent(final JComponent component, final Object targetBean,
                                                                                         final String targetProperty, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToValuePresentBinding(component, targetBean, targetProperty, enabledIfValuePresent);
    }

    public static EnabledPropertyToSelectionPresentBinding bindEnabledPropertyToSelectionPresent(final JComponent component, final JList list, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToSelectionPresentBinding(component, list, enabledIfValuePresent);
    }

    public static EnabledPropertyToSelectionPresentBinding bindEnabledPropertyToSelectionPresent(final JComponent component, final JTable table, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToSelectionPresentBinding(component, table, enabledIfValuePresent);
    }

    public static EnabledPropertyToSelectionPresentBinding bindEnabledPropertyToSelectionPresent(final JComponent component, final JTree tree, final boolean enabledIfValuePresent) {
        return new EnabledPropertyToSelectionPresentBinding(component, tree, enabledIfValuePresent);
    }
}
