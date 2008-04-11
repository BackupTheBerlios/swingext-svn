package net.sarcommand.swingextensions.typedinputfields;


import net.sarcommand.swingextensions.formatters.*;
import net.sarcommand.swingextensions.misc.*;


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
public class DoubleInputField extends AbstractTypedTextField<Double> implements FormatterSupporter {
    public static final String PATTERN_DOUBLE = "-?[0-9]+(\\.[0-9]*)?([eE]-?[0-9]+)?";
    private Formatter _format;

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
            return null;
        }
    }

    public void setValue(final Double value) {
        setText(_format == null ? "" + value : _format.convertToString(value));
    }

    public Formatter getFormatter() {
        return _format;
    }

    public void setFormatter(final Formatter format) {
        _format = format;
        setValue(getValue());
    }
}
