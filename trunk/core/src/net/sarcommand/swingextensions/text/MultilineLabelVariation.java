package net.sarcommand.swingextensions.text;

import javax.swing.*;

/**
 * ComponentVariation turning a JTextPane into a multiline text label.
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
public class MultilineLabelVariation implements TextVariation<JTextPane> {
    private final JTextPane _textPane;

    public MultilineLabelVariation(final JTextPane textPane) {
        _textPane = textPane;
        _textPane.setEditable(false);
        _textPane.setOpaque(false);
    }

    public void detach() {
        _textPane.setEditable(true);
        _textPane.setOpaque(true);
    }

    public JTextPane getAlteredComponent() {
        return _textPane;
    }
}
