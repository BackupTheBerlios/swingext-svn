package net.sarcommand.swingextensions.label;

import net.sarcommand.swingextensions.utilities.ComponentVariation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Adds a HTML-link like behaviour to a JLabel. The label will display a hand cursor and visually mimic a link. One can
 * install an ActionListener which will be triggered when the link is clicked.
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
public class LinkVariation implements ComponentVariation {
    private final JLabel _label;
    private MouseAdapter _mouseAdapter;
    private PropertyChangeListener _propertyChangeListener;

    public LinkVariation(final JLabel label, final String actionCommand, final ActionListener listener) {
        _label = label;

        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        _mouseAdapter = new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                listener.actionPerformed(new ActionEvent(label, ActionEvent.ACTION_PERFORMED, actionCommand, e.getWhen(),
                        e.getModifiers()));
            }
        };
        label.addMouseListener(_mouseAdapter);

        _propertyChangeListener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                textChanged();
            }
        };
        label.addPropertyChangeListener("text", _propertyChangeListener);

        textChanged();
    }

    public void detach() {
        _label.setCursor(Cursor.getDefaultCursor());
        _label.removeMouseListener(_mouseAdapter);
        _label.removePropertyChangeListener("text", _propertyChangeListener);
    }

    public Object getAlteredComponent() {
        return _label;
    }

    protected void textChanged() {
        final String text = _label.getText();
        if (text != null && !text.toLowerCase().contains("<html>"))
            _label.setText("<html></head><body><font color=\"blue\"><u>" + text + "</u></font></body></html>");
    }
}
