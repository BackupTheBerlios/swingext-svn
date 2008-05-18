package net.sarcommand.swingextensions.text;

import javax.swing.*;

/**
 * This class encapsulates utility methods for dealing with JTextComponents.
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
public class TextUtilities {
    /**
     * This call will turn the given text field into a search field:
     * <p/>
     * <li>It will be supplied with an appropriate icon</li> <li>If text is entered, a clear button will appear</li>
     * <li>The user can clear the field by pressing escape</li>
     * <p/>
     * The text field will be transformed with the help of the SearchFieldUI class. On Mac OSX, or rather when using the
     * Aqua LookAndFeel, a better-looking, more native-like implementation is available by setting a client property.
     * This method will revert to this implementation when the aqua Lookandfeel is detected.
     *
     * @param textField Text field to transform.
     */
    public static void turnIntoSearchField(final JTextField textField) {
        if (UIManager.getLookAndFeel().getName().toLowerCase().contains("aqua"))
            textField.putClientProperty("JTextField.variant", "search");
        else {

        }
    }

    private TextUtilities() {
    }
}
