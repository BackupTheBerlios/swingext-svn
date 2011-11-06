package net.sarcommand.swingextensions.typedinputfields;

/**
 * A TypedInputField implementation which will only allow entering whole integer numbers.
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
public class IntegerInputField extends AbstractTypedTextField<Integer> {
    public static final String PATTERN_INTEGER = "-?[0-9]+";

    public IntegerInputField() {
        super();
        setDocument(new RegexpConstrainedDocument(this, PATTERN_INTEGER));
    }

    /**
     * Returns the current value of this component, or null if no legal value has been entered.
     *
     * @return This component's current value
     */
    public Integer getValue() {
        final String text = getText();
        if (text.length() == 0)
            return null;

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Sets the value for this component.
     *
     * @param value new component value.
     */
    public void setValue(final Integer value) {
        setText("" + value);
    }
}
