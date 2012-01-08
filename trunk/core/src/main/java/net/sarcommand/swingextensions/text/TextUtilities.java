package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.binding.UpdatePolicy;
import net.sarcommand.swingextensions.utilities.SearchTask;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * This class encapsulates utility methods for dealing with JTextComponents.
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
public class TextUtilities {
    public static void turnIntoSearchField(final JTextField textField) {
        turnIntoSearchField(textField, null);
    }

    public static void turnIntoSearchField(final JTextField textField, final SearchTask searchTask) {
        final Object clientProperty = textField.getClientProperty(TextVariation.CLIENT_PROPERTY);
        if (clientProperty != null)
            ((TextVariation) clientProperty).detach();
        final SearchFieldVariation variation = new SearchFieldVariation(textField, searchTask);
        textField.putClientProperty(TextVariation.CLIENT_PROPERTY, variation);
    }


    public static TextComponentTextBinding bindTextProperty(final JTextComponent comp, final Object targetBean,
                                                            final String keypath, final UpdatePolicy updatePolicy) {
        return new TextComponentTextBinding(comp, targetBean, keypath, updatePolicy);
    }

    public static PasswordFieldBinding bindPasswordProperty(final JPasswordField comp, final Object targetBean,
                                                            final String keypath, final UpdatePolicy updatePolicy) {
        return new PasswordFieldBinding(comp, targetBean, keypath, updatePolicy);
    }

    public static TextVariation turnIntoMultilineLabel(final JTextPane textPane) {
        return new MultilineLabelVariation(textPane);
    }

    private TextUtilities() {
    }
}
