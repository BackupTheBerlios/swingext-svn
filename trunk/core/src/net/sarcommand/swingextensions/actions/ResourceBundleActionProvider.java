package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.applicationsupport.ImageCache;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ResourceBundleActionProvider implements ActionProvider {
    private ResourceBundle _resourceBundle;
    private String _pathKey;
    private static final String ENABLED_KEY = "Enabled";

    public ResourceBundleActionProvider(final ResourceBundle bundle) {
        this(bundle, "");
    }

    public ResourceBundleActionProvider(final ResourceBundle bundle, final String pathKey) {
        if (bundle == null)
            throw new IllegalArgumentException("Parameter 'bundle' must not be null!");
        if (pathKey == null)
            throw new IllegalArgumentException("Parameter '_pathKey' must not be null!");
        _pathKey = pathKey;
        _resourceBundle = bundle;
    }

    public ReflectedAction createReflectedAction(final Object identifier, final Object target, final String methodName) {
        final ReflectedAction action = new ReflectedAction(identifier, target, methodName);
        mapAll(identifier, action);

        return action;
    }

    public ManagedAction createAction(final Object identifier) {
        final ManagedAction action = new ManagedAction(identifier);
        mapAll(identifier, action);

        return action;
    }

    private void mapAll(Object identifier, ManagedAction action) {
        final String prefix = identifier.toString();

        map(action, prefix, Action.ACTION_COMMAND_KEY);
        map(action, prefix, Action.DISPLAYED_MNEMONIC_INDEX_KEY);
        map(action, prefix, Action.LARGE_ICON_KEY);
        map(action, prefix, Action.LONG_DESCRIPTION);
        map(action, prefix, Action.MNEMONIC_KEY);
        map(action, prefix, Action.NAME);
        map(action, prefix, Action.SELECTED_KEY);
        map(action, prefix, Action.SHORT_DESCRIPTION);
        map(action, prefix, Action.SMALL_ICON);
        map(action, prefix, ManagedAction.GROUP_KEY);

        String resourceKey;

        resourceKey = prefix + _pathKey + ENABLED_KEY;
        if (_resourceBundle.containsKey(resourceKey))
            action.setEnabled(!_resourceBundle.getString(resourceKey).equalsIgnoreCase("false"));

        resourceKey = prefix + _pathKey + Action.ACCELERATOR_KEY;
        if (_resourceBundle.containsKey(resourceKey)) {
            final String accelerator = _resourceBundle.getString(resourceKey);
            if (accelerator.startsWith("menu"))
                action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                        KeyStroke.getKeyStroke(accelerator.substring(4)).getKeyCode(),
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            else
                action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
        }

        if (action.getValue(Action.NAME) == null)
            action.putValue(Action.NAME, prefix);
    }

    protected void map(final Action action, final String resourceKey, final String property) {
        final String key = resourceKey + _pathKey + property;
        if (_resourceBundle.containsKey(key))
            if (property.equals(Action.SMALL_ICON) || property.equals(Action.LARGE_ICON_KEY)) {
                action.putValue(property, ImageCache.loadIcon(_resourceBundle.getString(key)));
            } else
                action.putValue(property, _resourceBundle.getString(key));
    }
}
