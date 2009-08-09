package net.sarcommand.swingextensions.label;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * todo [heup] add docs
 * <p/>
 * <hr/> Copyright 2006-2009 Torsten Heup
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
public class LabelUtilities {
    public static final String LINK_LABEL_VARIATION_PROPERTY = "swingExt.LabelUtilities.linkLabelVariationProperty";

    public static void turnIntoLink(final JLabel label, final String actionCommand, final ActionListener listener) {
        final LinkVariation adapter = new LinkVariation(label, actionCommand, listener);
        label.putClientProperty(LINK_LABEL_VARIATION_PROPERTY, adapter);
    }
}
