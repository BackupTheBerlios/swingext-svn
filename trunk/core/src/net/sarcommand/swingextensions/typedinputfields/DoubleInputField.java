package net.sarcommand.swingextensions.typedinputfields;


import net.sarcommand.swingextensions.formatters.FormatSupporter;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import java.text.Format;


/**
 * A TypedInputField implementation which only allows valid Double numbers.
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
public class DoubleInputField extends AbstractTypedTextField<Double> implements FormatSupporter {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(DoubleInputField.class);

    public static final String PATTERN_DOUBLE = "-?[0-9]+(\\.[0-9]*)?([eE]-?[0-9]+)?";
    private Format _format;

    public DoubleInputField() {
        super();
        setDocument(new RegexpConstrainedDocument(this, PATTERN_DOUBLE));
    }

    public Double getValue() {
        final String text = getText();
        if (text.length() == 0)
            return null;
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            __log.warn("Could not parse user input " + text, e);
            return null;
        }
    }

    public void setValue(final Double value) {
        final String s;
        if (_format != null && value != null) {
            try {
                s = _format.format(value);
            } catch (Exception e) {
                throw new RuntimeException("Could not format value '" + value + "'", e);
            }
        } else
            s = "";
        setText(s);
    }

    public Format getFormat() {
        return _format;
    }

    public void setFormat(final Format format) {
        _format = format;
        setValue(getValue());
    }
}
