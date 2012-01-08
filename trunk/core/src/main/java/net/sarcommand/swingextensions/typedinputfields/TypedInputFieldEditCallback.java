package net.sarcommand.swingextensions.typedinputfields;

/**
 * This interface defines callback objects for use with the TypedInputField class. TypedInputFields are components which
 * restrict user input to a certain data type, such as Integers or email addresses. They use instances of
 * TypedInputFieldEditCallback to signal whether the currently entered value is legal or illegal with regards to their
 * type or whether it may become valid with futher input. For instance, in case of a DoubleInputField the value "123"
 * would be legal, "123.2e" would be incomplete and the input of character data would be rejected at all.
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
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
public interface TypedInputFieldEditCallback {
    /**
     * Notifies the callback that the user tried to enter an illegal token.
     *
     * @param source TypedInputField generating this event.
     */
    public void inputIllegal(final TypedInputField source);

    /**
     * Notifies the callback that the current input is now valid.
     *
     * @param source TypedInputField generating this event.
     */
    public void inputLegal(final TypedInputField source);

    /**
     * Notifies the callback that the current input is now incomplete.
     *
     * @param source TypedInputField generating this event.
     */
    public void inputIncomplete(final TypedInputField source);
}
