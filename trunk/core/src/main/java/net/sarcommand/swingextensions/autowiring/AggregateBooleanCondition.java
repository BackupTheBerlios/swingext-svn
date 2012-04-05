package net.sarcommand.swingextensions.autowiring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A BooleanCondition that shadows the state of an arbitrary number of nested conditions.
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
public class AggregateBooleanCondition extends BooleanCondition {
    /**
     * Represents the boolean operator connecting the nested conditions.
     */
    public static enum Operator {
        and, or, xor
    }

    /**
     * The operator for this condition.
     */
    protected Operator _operator;

    /**
     * Indicates whether this condition is negated.
     */
    protected boolean _negated;

    /**
     * The nested conditions.
     */
    protected ArrayList<BooleanCondition> _conditions;

    /**
     * The listener installed on all nested conditions.
     */
    protected ConditionListener _nestedConditionListener;

    /**
     * The cached state of this condition, as returned by getNestedCondition.
     */
    protected Boolean _state;

    /**
     * Creates a new AggregateBooleanCondition that connects all nested conditions with an 'and' operator.
     */
    public AggregateBooleanCondition() {
        this(Operator.and, false);
    }

    /**
     * Creates a new AggregateBooleanCondition that connects all nested conditions with an 'and' operator.
     *
     * @param negated Whether this condition should be negated.
     */
    public AggregateBooleanCondition(final boolean negated) {
        this(Operator.and, negated);
    }

    /**
     * Creates a new AggregateBooleanCondition that connects all nested conditions with an the given operator.
     *
     * @param operator operator for this condition.
     */
    public AggregateBooleanCondition(final Operator operator) {
        this(operator, false);
    }

    /**
     * Creates a new AggregateBooleanCondition that connects all nested conditions with an the given operator.
     *
     * @param operator operator for this condition.
     * @param negated  Whether this condition should be negated.
     */
    public AggregateBooleanCondition(final Operator operator, final boolean negated) {
        _operator = operator;
        _negated = negated;
        _conditions = new ArrayList<BooleanCondition>(4);
        _nestedConditionListener = new ConditionListener() {
            public void conditionUpdated(final Condition booleanCondition) {
                update();
            }
        };
    }

    /**
     * Creates a new condition with the given operator and nested conditions.
     *
     * @param operator   The operator for this condition.
     * @param conditions The list of nested conditions (may be empty).
     */
    public AggregateBooleanCondition(final Operator operator, final BooleanCondition... conditions) {
        this(operator);
        addConditions(conditions);
    }

    /**
     * Creates a new condition with the given operator and nested conditions.
     *
     * @param operator   The operator for this condition.
     * @param negated    Whether this condition should be negated.
     * @param conditions The list of nested conditions (may be empty).
     */
    public AggregateBooleanCondition(final Operator operator, final boolean negated,
                                     final BooleanCondition... conditions) {
        this(operator, negated);
        addConditions(conditions);
    }

    /**
     * Adds the given nested conditions.
     *
     * @param conditions conditions to add.
     */
    public void addConditions(final BooleanCondition... conditions) {
        addConditions(Arrays.asList(conditions));
    }

    /**
     * Adds the given nested conditions.
     *
     * @param conditions conditions to add.
     */
    public void addConditions(final Collection<BooleanCondition> conditions) {
        if (conditions == null)
            throw new IllegalArgumentException("Parameter 'conditions' must not be null!");

        _conditions.addAll(conditions);
        for (BooleanCondition c : conditions)
            c.addConditionListener(_nestedConditionListener);
        update();
    }

    /**
     * Invoked when one of the nest conditions is updated.
     */
    protected void update() {
        final Boolean oldState = _state;
        _state = _negated ? !getNestedState() : getNestedState();
        if (oldState != _state)
            fireConditionUpdated();
    }

    /**
     * Computes the new state for this condition based on the list of nested conditions.
     *
     * @return this condition's new state.
     */
    protected boolean getNestedState() {
        switch (_operator) {
            case and:
                for (BooleanCondition c : _conditions)
                    if (c.getState() == null || !c.getState())
                        return false;
                return true;

            case or:
                for (BooleanCondition c : _conditions)
                    if (c.getState() != null && c.getState())
                        return true;
                return false;
            case xor:
                boolean hit = false;
                for (BooleanCondition c : _conditions) {
                    if (c.getState() != null && c.getState()) {
                        if (hit)
                            return false;
                        else
                            hit = true;
                    }
                }
                return hit;
            default:
                return false;
        }
    }

    @Override
    public Boolean getState() {
        return _state;
    }
}
