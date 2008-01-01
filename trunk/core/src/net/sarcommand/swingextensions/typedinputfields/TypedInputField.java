package net.sarcommand.swingextensions.typedinputfields;

/**
 * Common interface for input fields which restrict data to a certain type. For instance, the IntegerInputField class
 * will only allow you entering valid integer numbers and reject other input, such as character data or other illegal
 * tokens. The way an input field will react if the user tries to enter illegal tokens is handled by a callback object,
 * an instance of TypedInputFieldEditCallback.
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
