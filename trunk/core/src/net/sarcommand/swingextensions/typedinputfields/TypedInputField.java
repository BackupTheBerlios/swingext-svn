package net.sarcommand.swingextensions.typedinputfields;

/**
 * Common interface for input fields which restrict data to a certain type. For instance, the IntegerInputField class
 * will only allow you entering valid integer numbers and reject other input, such as character data or other illegal
 * tokens. The way an input field will react if the user tries to enter illegal tokens is handled by a callback object,
 * an instance of TypedInputFieldEditCallback.
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
 *
 * @see net.sarcommand.swingextensions.typedinputfields.TypedInputFieldEditCallback
 */
public interface TypedInputField<E> {
    /**
     * Returns the current value of this component, or null if no legal value has been entered.
     *
     * @return This component's current value
     */
    public E getValue();

    /**
     * Sets the value for this component.
     *
     * @param value new component value.
     */
    public void setValue(final E value);

    /**
     * Sets the feedback object for this component. Whenever the input field's value changes, the feedback object
     * will be notified according to whether the new value is legal, illegal or possibly incomplete.
     *
     * @param callbackObject Feedback object to use.
     */
    public void setInputFieldEditFeedback(final TypedInputFieldEditCallback callbackObject);

    /**
     * Returns the currently used input feedback object.
     *
     * @return The currently used input feedback object.
     * @see TypedInputField#setInputFieldEditFeedback(TypedInputFieldEditCallback)
     */
    public TypedInputFieldEditCallback getInputFieldEditFeedback();
}
