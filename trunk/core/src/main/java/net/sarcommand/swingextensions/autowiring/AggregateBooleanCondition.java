package net.sarcommand.swingextensions.autowiring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A BooleanCondition that shadows the state of an arbitrary number of nested conditions.
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
public class AggregateBooleanCondition extends BooleanCondition {
    private ArrayList<BooleanCondition> _conditions;
    private ArrayList<BooleanCondition> _negatedConditions;

    private ConditionListener _nestedConditionListener;

    private Boolean _state;

    public AggregateBooleanCondition() {
        _conditions = new ArrayList<BooleanCondition>(4);
        _negatedConditions = new ArrayList<BooleanCondition>(4);
        _nestedConditionListener = new ConditionListener() {
            public void conditionUpdated(final Condition booleanCondition) {
                update();
            }
        };
    }

    public void addConditions(final BooleanCondition... condition) {
        addConditions(Arrays.asList(condition));
    }

    public void addConditions(final Collection<BooleanCondition> condition) {
        _conditions.addAll(condition);
        for (BooleanCondition c : condition)
            c.addConditionListener(_nestedConditionListener);
        update();
    }

    public void addNegatedConditions(final BooleanCondition... condition) {
        addNegatedConditions(Arrays.asList(condition));
    }

    public void addNegatedConditions(final Collection<BooleanCondition> condition) {
        _negatedConditions.addAll(condition);
        for (BooleanCondition c : condition)
            c.addConditionListener(_nestedConditionListener);
        update();
    }

    protected void update() {
        final Boolean oldState = _state;
        _state = getNestedState();
        if (oldState != _state)
            fireConditionUpdated();
    }

    protected boolean getNestedState() {
        for (BooleanCondition c : _conditions)
            if (c.getState() == null || !c.getState())
                return false;
        for (BooleanCondition c : _negatedConditions)
            if (c.getState() == null || c.getState())
                return false;
        return true;
    }

    @Override
    public Boolean getState() {
        return _state;
    }
}
