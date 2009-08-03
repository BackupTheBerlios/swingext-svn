package net.sarcommand.swingextensions.localization;

import net.sarcommand.swingextensions.applicationsupport.ImageCache;
import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * todo [heup] Add javadoc <hr/> Copyright 2006-2009 Torsten Heup
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
public class SwingResourceBundle extends ResourceBundle {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(SwingResourceBundle.class);

    private ResourceBundle _bundle;

    public SwingResourceBundle(final ResourceBundle bundle) {
        _bundle = bundle;
    }

    public SwingResourceBundle(final String bundleName) {
        _bundle = ResourceBundle.getBundle(bundleName);
    }

    public SwingResourceBundle(final String bundleName, final Locale locale) {
        _bundle = ResourceBundle.getBundle(bundleName, locale);
    }

    public void localizeComponent(final JComponent target, final String key) {
        for (String s : _bundle.keySet()) {
            if (s.startsWith(key)) {
                applyProperty(target, s);
            }
        }
    }

    public JLabel getLabel(final Object key) {
        final JLabel label = new JLabel();
        localizeComponent(label, key.toString());
        return label;
    }

    private void applyProperty(final JComponent target, final String property) {
        final int dotIndex = property.indexOf('.');
        if (dotIndex < 0)
            return;

        final String valueAsString = _bundle.getString(property);

        final String keypath = property.substring(dotIndex + 1);

        final Keypath k;
        final Class valueClass;
        try {
            k = new Keypath(keypath, true);
            valueClass = k.getValueClass(target);
        } catch (Exception e) {
            __log.warn("Failed to obtain a suitable keypath for property key " + property, e);
            return;
        }

        if (valueClass.equals(String.class))
            k.set(target, valueAsString);
        else {
            final PropertyEditor propertyEditor = PropertyEditorManager.findEditor(valueClass);

            if (propertyEditor != null) {
                try {
                    propertyEditor.setAsText(valueAsString);
                    return;
                } catch (IllegalArgumentException e) {
                    //will try to handle this with internal editors
                }
            }

            /* no editor was found, or the editor could not handle the request */
            if (valueClass.isAssignableFrom(BufferedImage.class)) {
                final BufferedImage img = ImageCache.loadImage(valueAsString,
                        ImageCache.ErrorPolicy.ON_ERROR_RETURN_NULL);
                if (img != null)
                    k.set(target, img);
            } else if (valueClass.isAssignableFrom(ImageIcon.class)) {
                final ImageIcon icon = ImageCache.loadIcon(valueAsString, ImageCache.ErrorPolicy.ON_ERROR_RETURN_NULL);
                if (icon != null)
                    k.set(target, icon);
            }
        }
    }

    protected Object handleGetObject(final String key) {
        return _bundle.getObject(key);
    }

    public Enumeration<String> getKeys() {
        return _bundle.getKeys();
    }

    public String getString(final Object key) {
        return _bundle.getString(key.toString());
    }
}
