package net.sarcommand.swingextensions.label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * todo [heup] add docs
 * <p/>
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class LabelUtilities {
    public static final String LINK_LABEL_ADAPTER_PROPERTY = "swingExt.LabelUtilities.linkLabelAdapterProperty";

    public static void turnIntoLink(final JLabel label, final ActionListener listener) {
        final LinkLabelAdapter adapter = new LinkLabelAdapter(label, listener);
        label.putClientProperty(LINK_LABEL_ADAPTER_PROPERTY, adapter);
    }

    protected static class LinkLabelAdapter {
        private JLabel _label;

        public LinkLabelAdapter(final JLabel label, final ActionListener listener) {
            _label = label;
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new MouseAdapter() {
                public void mouseClicked(final MouseEvent e) {
                    listener.actionPerformed(new ActionEvent(label, ActionEvent.ACTION_PERFORMED, null, e.getWhen(),
                            e.getModifiers()));
                }
            });
            label.addPropertyChangeListener("text", new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent evt) {
                    textChanged();
                }
            });
            textChanged();
        }

        protected void textChanged() {
            final String text = _label.getText();
            if (text != null && !text.toLowerCase().contains("<html>"))
                _label.setText("<html></head><body><font color=\"blue\"><u>" + text + "</u></font></body></html>");
        }
    }
}
