package net.sarcommand.swingextensions.resources;

import net.sarcommand.swingextensions.actions.ResourceBundleActionProvider;
import net.sarcommand.swingextensions.applicationsupport.ImageCache;

import javax.swing.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class SwingExtResources {
    private static ResourceBundle __internalBundle;
    private static ResourceBundle __userBundle;

    private static boolean __bundlesLoaded;

    private static ResourceBundleActionProvider __actionProvider;

    public static String getResource(final String key) {
        if (!__bundlesLoaded)
            loadBundles();

        String value = null;
        if (__userBundle != null) {
            try {
                value = __userBundle.getString(key);
            } catch (Exception e) {
                //that's allright
            }
        }
        return value == null ? __internalBundle.getString(key) : value;
    }

    public static Icon getIconResource(final String key) {
        final String resourceKey = getResource(key);
        if (resourceKey == null)
            return null;
        return ImageCache.loadIcon(resourceKey);
    }

    public static Action getActionResource(final String key, final Object target, final String methodName) {
        if (!__bundlesLoaded)
            loadBundles();
        return __actionProvider.createReflectedAction(key, target, methodName);
    }

    private static void loadBundles() {
        __internalBundle = ResourceBundle.getBundle("localization/swingExtInternalResources");
        __actionProvider = new ResourceBundleActionProvider(__internalBundle, ".");
        try {
            __userBundle = ResourceBundle.getBundle("swingExtResources");
        } catch (MissingResourceException e) {
            //that's allright
        }
        __bundlesLoaded = true;
    }

    private SwingExtResources() {

    }
}
