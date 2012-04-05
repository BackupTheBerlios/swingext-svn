package net.sarcommand.swingextensions.autowiring;

import javax.swing.*;

/**
 * An autowiring action that will enable or disable a component depending on the state of a set of BooleanConditions.
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
public class EnableComponentAction implements BooleanConditionedAction {
    private AggregateBooleanCondition _condition;
    private JComponent _component;

    /**
     * Instanciates an action that enables or disables the target component based on the attached boolean conditions.
     *
     * @param component The component to enable / disable.
     */
    public EnableComponentAction(final JComponent component) {
        if (component == null)
            throw new IllegalArgumentException("Parameter 'component' must not be null!");

        _component = component;
        _condition = new AggregateBooleanCondition();
        _condition.addConditionListener(new ConditionListener() {
            public void conditionUpdated(final Condition booleanCondition) {
                if (_component != null)
                    _component.setEnabled(_condition.getState());
            }
        });
    }

    /**
     * Triggers this action whenever all of the given conditions are fulfilled. Multiple invocations of this method will
     * cause the conditions to be joined.
     *
     * @param conditions The conditions to be fulfilled for this action to trigger.
     * @return An instance of AggregateBooleanCondition to use for method chaining.
     */
    public AggregateBooleanCondition whenever(final BooleanCondition... conditions) {
        _condition.addConditions(conditions);
        return _condition;
    }

    /**
     * Triggers this action whenever none of the given conditions are fulfilled. Multiple invocations of this method
     * will cause the conditions to be joined.
     *
     * @param conditions The conditions not to be fulfilled for this action to trigger.
     * @return An instance of AggregateBooleanCondition to use for method chaining.
     */
    public AggregateBooleanCondition unless(final BooleanCondition... conditions) {
        _condition.addConditions(new AggregateBooleanCondition(AggregateBooleanCondition.Operator.and, true, conditions));
        return _condition;
    }
}
