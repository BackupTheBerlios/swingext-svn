package net.sarcommand.swingextensions.internal;

import net.sarcommand.swingextensions.actions.ResourceBundleActionProvider;
import net.sarcommand.swingextensions.applicationsupport.ImageCache;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class is used by the swingext library to load icons and text resources.
 * <p/>
 * <b>This is a purely internal class. You not never have to deal with it directly</b>
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
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

    public static BufferedImage getImageResource(final String key) {
        final String resourceKey = getResource(key);
        if (resourceKey == null)
            return null;
        return ImageCache.loadImage(resourceKey);
    }

    public static Action getActionResource(final String key, final Object target, final String methodName) {
        if (!__bundlesLoaded)
            loadBundles();
        return __actionProvider.createReflectedAction(key, target, methodName);
    }

    private static void loadBundles() {
        __internalBundle = ResourceBundle.getBundle("localization/swingExtInternalResources");
        __actionProvider = new ResourceBundleActionProvider(__internalBundle);
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
