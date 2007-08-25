package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.applicationsupport.ImageCache;
import net.sarcommand.swingextensions.utilities.KeyUtilities;

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

    public ResourceBundleActionProvider(final String bundleName) {
        this(ResourceBundle.getBundle(bundleName), "");
    }

    public ResourceBundleActionProvider(final String bundleName, final String pathKey) {
        this(ResourceBundle.getBundle(bundleName), pathKey);
    }

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
        final String prefix = identifier.toString() + _pathKey;
        final int length = prefix.length();

        for (String s : _resourceBundle.keySet()) {
            if (s.startsWith(prefix)) {
                final String key = s.substring(length);
                final String value = _resourceBundle.getString(s);
                if (key.equals(ENABLED_KEY))
                    action.setEnabled(!value.equalsIgnoreCase("false"));
                else if (key.equals(Action.ACCELERATOR_KEY)) {
                    String accelerator = value;
                    if (accelerator.startsWith("menu"))
                        accelerator = accelerator.replace("menu", KeyUtilities.getModifiersAsText(
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).trim());
                    action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
                } else if (key.equals(Action.SMALL_ICON) || key.equals(Action.LARGE_ICON_KEY)) {
                    action.putValue(key, ImageCache.loadIcon(value));
                } else {
                    action.putValue(key, value);
                }
            }
        }


        if (action.getValue(Action.NAME) == null)
            action.putValue(Action.NAME, identifier.toString());
    }
}
