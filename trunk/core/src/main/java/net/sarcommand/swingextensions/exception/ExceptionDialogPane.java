package net.sarcommand.swingextensions.exception;

import javax.swing.*;
import java.awt.*;

/**
 * Used internally by the ExceptionDialog class. <hr/> Copyright 2006-2012 Torsten Heup
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
public class ExceptionDialogPane extends JComponent {
    public static final String UI_KEY = "ExceptionDialogPane.UI";
    private ExceptionDialog _dialog;

    protected ExceptionDialogPane(final ExceptionDialog parent) {
        _dialog = parent;
        UIManager.getUI(this).installUI(this);
    }

    public String getUIClassID() {
        if (!UIManager.getDefaults().containsKey(UI_KEY))
            UIManager.getDefaults().put(UI_KEY, BasicExceptionDialogPaneUI.class.getName());
        return UI_KEY;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public ExceptionDialog getDialog() {
        return _dialog;
    }
}
