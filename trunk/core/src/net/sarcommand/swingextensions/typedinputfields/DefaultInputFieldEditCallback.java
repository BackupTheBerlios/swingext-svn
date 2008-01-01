package net.sarcommand.swingextensions.typedinputfields;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Default implementation for the TypedInputFieldEditCallback interface. This implementation will set a red border on
 * the input field if the current input data is incomplete, and perform Toolkit.getToolkit().beep if illegal input
 * has been made.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
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
public class DefaultInputFieldEditCallback implements TypedInputFieldEditCallback {
    protected JComponent _component;
    protected Border _defaultBorder;
    protected Border _errorBorder;

    public DefaultInputFieldEditCallback(final JComponent target) {
        _component = target;
        _defaultBorder = _component.getBorder();
        _errorBorder = BorderFactory.createLineBorder(Color.RED);
    }

    public void inputIllegal(final TypedInputField source) {
        if (source == _component)
            Toolkit.getDefaultToolkit().beep();
    }

    public void inputIncomplete(final TypedInputField source) {
        if (source == _component)
            _component.setBorder(_errorBorder);

    }

    public void inputLegal(final TypedInputField source) {
        if (source == _component)
            _component.setBorder(_defaultBorder);
    }
}
