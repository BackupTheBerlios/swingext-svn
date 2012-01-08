package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.applicationsupport.ImageCache;
import net.sarcommand.swingextensions.utilities.KeyUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * An ActionProvider instance using a resource bundle to configure Action instances. It will use the toString() method
 * on the Action's identifier and use it to locate the according properties. By default, it will look for the pattern
 * <code><actionIdentifier.toString()>.<actionProperty></code>. The 'actionProperty' element is the property of the
 * Action instance you want to set. <code>myAction.Name</code> would set the 'name' property for an Action identified by
 * the string 'myAction'. Apart from the obvious string properties (Name, ShortDescription etc) you can also set image
 * properties (SmallIcon, SwingLargeIconKey). If you do, the property value will be referred to the ImageCache, which
 * attempts to load an icon with the given name. Futhermore, you can set acclerator keys which will automatically be
 * converted to an appropriate KeyStroke instance.
 * <p/>
 * Side note: There is one special thing about accelerator keys: Since actions should behave consistently with the
 * platform, which rises the issue of the right menu key. On windows, you usually trigger the copy action with ctrl+c,
 * while you use command+c on the mac. To circumvent this issue, you can use the token 'menu' in your property files to
 * define the correct accelerator. The action provider will replace it with the correct platform-specific key for you.
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
public class ResourceBundleActionProvider implements ActionProvider {
    /**
     * The constant string being used as a key in the resource bundles for the Action's enabled state (Action does not
     * provide a proper key, but a boolean method).
     */
    public static final String ENABLED_KEY = "Enabled";

    /**
     * The resource bundle being used to obtain actions.
     */
    protected ResourceBundle _resourceBundle;

    /**
     * Creates a new action provider using the given bundle.
     *
     * @param bundleName The name of the ResourceBundle to use (will be passed to ResourceBundle.getBundle(String)
     */
    public ResourceBundleActionProvider(final String bundleName) {
        this(ResourceBundle.getBundle(bundleName));
    }

    /**
     * Creates a new action provider using the given bundle.
     *
     * @param bundle The resource bundle to use.
     */
    public ResourceBundleActionProvider(final ResourceBundle bundle) {
        if (bundle == null)
            throw new IllegalArgumentException("Parameter 'bundle' must not be null!");
        _resourceBundle = bundle;
    }

    /**
     * This method will map all properties found in the resource bundle for the action identifier to the given action
     * instance. Subclasses which provide their own action implementation may want to reuse this method.
     *
     * @param identifier Unique action identifier used to look up properties in the resource bundle.
     * @param action     The action to which the looked up properties will be mapped.
     */
    public void configurePropertiesForAction(Object identifier, Action action) {
        final String prefix = identifier.toString() + '.';
        final int length = prefix.length();

        for (String s : _resourceBundle.keySet()) {
            if (s.startsWith(prefix)) {
                final String key = s.substring(length);
                final String value = _resourceBundle.getString(s);
                if (key.equals(ENABLED_KEY))
                    action.setEnabled(!value.equalsIgnoreCase("false"));
                else if (key.equals(Action.ACCELERATOR_KEY)) {
                    String accelerator = value;
                    if (accelerator.contains("menu"))
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
