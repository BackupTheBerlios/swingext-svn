package net.sarcommand.swingextensions.typedinputfields;

import javax.swing.*;
import java.beans.PropertyEditor;

/**
 * Abstract super class for JTextField-based implementation of the TypedInputField interface. This implementation
 * uses a property editor to convert between the textual representation of data and the actual data type.
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
public abstract class AbstractTypedTextField<E> extends JTextField implements TypedInputField<E> {
    protected TypedInputFieldEditCallback _callback;
    protected PropertyEditor _editor;

    public PropertyEditor getEditor() {
        return _editor;
    }

    public void setEditor(final PropertyEditor editor) {
        _editor = editor;
    }

    /**
     * Sets the feedback object for this component. Whenever the input field's value changes, the feedback object
     * will be notified according to whether the new value is legal, illegal or possibly incomplete.
     *
     * @param callbackObject Feedback object to use.
     */
    public void setInputFieldEditFeedback(final TypedInputFieldEditCallback callbackObject) {
        _callback = callbackObject;
    }

    /**
     * Returns the currently used input feedback object.
     *
     * @return The currently used input feedback object.
     * @see net.sarcommand.swingextensions.typedinputfields.TypedInputField#setInputFieldEditFeedback(net.sarcommand.swingextensions.typedinputfields.TypedInputFieldEditCallback)
     */
    public TypedInputFieldEditCallback getInputFieldEditFeedback() {
        if (_callback == null)
            _callback = new DefaultInputFieldEditCallback(this);
        return _callback;
    }

    /**
     * Returns the current value of this component, or null if no legal value has been entered.
     *
     * @return This component's current value
     */
    public abstract E getValue();

    /**
     * Sets the value for this component.
     *
     * @param value new component value.
     */
    public abstract void setValue(final E value);
}
