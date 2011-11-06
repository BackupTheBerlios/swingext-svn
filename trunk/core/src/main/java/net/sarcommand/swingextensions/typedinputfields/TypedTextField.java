package net.sarcommand.swingextensions.typedinputfields;

import java.beans.PropertyEditor;

/**
 * A generic, text based implementation of the TypedInputField interface which will restrict the entered content using
 * one or multiple regular expressions. For instance, instantiating a TypedTextField with the pattern "[a-z]*" would
 * make it accept all lowercase ascii characters, but deny uppercase, numbers and special characters.
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
public class TypedTextField<E> extends AbstractTypedTextField<E> implements TypedInputField<E> {
    public static final String PATTERN_EMAIL_RFC_2282 = "[a-zA-Z0-9!#$%&'\\*\\+\\.\\-/=\\?_\\´\\{|\\}~]+" +
            "@[a-zA-Z0-9_\\.]+[\\.][a-zA-Z0-9]{2,4}";

    private String[] _patternStrings;

    /**
     * Creates a new TypedInputField.
     *
     * @param editor   PropertyEditor used to convert the field's value from String to E and vice versa.
     * @param patterns A list of the patterns accepted by this field.
     */
    public TypedTextField(final PropertyEditor editor, final String... patterns) {
        _patternStrings = patterns;
        initialize();
        setEditor(editor);
    }

    /**
     * Returns the current value of this component, or null if no legal value has been entered.
     *
     * @return This component's current value
     */
    public E getValue() {
        final PropertyEditor editor = getEditor();
        return (E) editor.getValue();
    }

    /**
     * Sets the value for this component.
     *
     * @param value new component value.
     */
    public void setValue(final E value) {
        final PropertyEditor editor = getEditor();
        editor.setValue(value);
        setText(editor.getAsText());
    }

    protected void initialize() {
        setInputFieldEditFeedback(new DefaultInputFieldEditCallback(this));
        setDocument(new RegexpConstrainedDocument(this, _patternStrings));
    }
}
