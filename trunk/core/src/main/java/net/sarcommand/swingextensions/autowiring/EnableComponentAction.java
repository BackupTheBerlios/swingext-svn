package net.sarcommand.swingextensions.autowiring;

import javax.swing.*;
import java.util.Collection;

/**
 * An autowiring action that will enable or disable a component depending on the state of a set of BooleanConditions.
 * <p/>
 * <hr/> Copyright 2006-2010 Torsten Heup
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
public class EnableComponentAction implements AutowiringAction {
    private AggregateBooleanCondition _condition;
    private JComponent _component;

    protected EnableComponentAction() {
        _condition = new AggregateBooleanCondition();
        _condition.addConditionListener(new ConditionListener() {
            public void conditionUpdated(final Condition booleanCondition) {
                if (_component != null)
                    _component.setEnabled(_condition.getState());
            }
        });
    }

    public EnableComponentAction(final JComponent component, final BooleanCondition... conditions) {
        this();
        if (component == null)
            throw new IllegalArgumentException("Parameter 'component' must not be null!");

        _component = component;
        _condition.addConditions(conditions);
    }

    public EnableComponentAction(final JComponent component, final Collection<BooleanCondition> conditions,
                                 final Collection<BooleanCondition> negatedConditions) {
        this();
        if (component == null)
            throw new IllegalArgumentException("Parameter 'component' must not be null!");

        _component = component;
        _condition.addConditions(conditions);
        _condition.addNegatedConditions(negatedConditions);
    }

    public EnableComponentAction whenever(final BooleanCondition... conditions) {
        _condition.addConditions(conditions);
        return this;
    }
}
