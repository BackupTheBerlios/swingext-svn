package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.event.DocumentAdapter;
import net.sarcommand.swingextensions.event.DocumentListenerGlue;
import net.sarcommand.swingextensions.typedinputfields.TypedTextField;

import javax.swing.event.DocumentEvent;

/**
 * Condition testing that a given TypedTextField instance has a non-null value.
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
public class HasValueCondition extends BooleanCondition {
    /**
     * Instance to monitor.
     */
    protected TypedTextField _inputField;

    /**
     * Instanciates a condition that will indicate whether the given typed input field has a non-null value.
     *
     * @param inputField instance to monitor.
     */
    public HasValueCondition(final TypedTextField inputField) {
        _inputField = inputField;
        new DocumentListenerGlue(_inputField, new DocumentAdapter() {
            @Override
            public void documentChanged(final DocumentEvent e) {
                fireConditionUpdated();
            }
        });
    }

    @Override
    public Boolean getState() {
        return _inputField.getValue() != null;
    }
}
