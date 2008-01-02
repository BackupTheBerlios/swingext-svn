package net.sarcommand.swingextensions.demo;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Base class for demo components.
 * <p/>
 * Copyright 2006 Torsten Heup
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
public abstract class DemoClass extends JComponent {
    public abstract String getDemoName();

    private static ResourceBundle __res = ResourceBundle.getBundle("demoDescriptions");

    public StyledDocument getDemoDescription() {
        return getRTFResource(getClass().getSimpleName() + ".description");
    }

    protected StyledDocument getRTFResource(final String resourceID) {
        final String key;
        try {
            key = __res.getString(resourceID);
        } catch (Exception e) {
            return createErrorDocument();
        }

        if (key == null)
            return createErrorDocument();

        final InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(key);
        if (resourceStream == null)
            return createErrorDocument();

        final BufferedInputStream inStream = new BufferedInputStream(resourceStream);
        final RTFEditorKit kit = new RTFEditorKit();
        final StyledDocument result = (StyledDocument) kit.createDefaultDocument();
        try {
            kit.read(inStream, result, 0);
            inStream.close();
        } catch (IOException e) {
            return createErrorDocument();
        } catch (BadLocationException e) {
            return createErrorDocument();
        }
        return result;
    }

    protected StyledDocument createErrorDocument() {
        final StyledDocument result = new DefaultStyledDocument();
        final SimpleAttributeSet red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.RED);
        try {
            result.insertString(0, "Error: Could not load demo description!", red);
        } catch (BadLocationException e) {
            //never happens
        }
        return result;
    }
}
