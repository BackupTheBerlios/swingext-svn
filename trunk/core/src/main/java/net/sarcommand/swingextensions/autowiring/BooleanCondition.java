package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.event.EventSupport;

/**
 * An autowiring Condition resolving to a boolean state. You can add ConditionListener instances to monitor the
 * condition, and freely invoke getState to retrieve the last known state of the condition.
 * <p/>
 * <em>Note:</em>Conditions are not designed to be threadsafe. Make sure that only one thread ever causes modifications
 * for a condition instance, or add suitable means of synchronization.
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
public abstract class BooleanCondition implements Condition<Boolean> {
    /**
     * The event listeners installed on this condition.
     */
    protected EventSupport<ConditionListener> _listeners;

    public BooleanCondition() {
        _listeners = EventSupport.create(ConditionListener.class);
    }

    /**
     * Adds a listener to be notified whenever this condition changes state.
     *
     * @param listener Listener instance to add, non-null.
     */
    public void addConditionListener(final ConditionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _listeners.addListener(listener);
    }

    /**
     * Removes a previously installed ConditionListener instance. Attempting to remove a listener that was not
     * previously installed will to nothing, however, passing a null parameter value will cause an exception.
     *
     * @param listener instance to be removed, non-null.
     */
    public void removeConditionListener(final ConditionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _listeners.removeListener(listener);
    }

    /**
     * Notifies all installed listeners that this condition has been updated.
     */
    protected void fireConditionUpdated() {
        _listeners.delegate().conditionUpdated(this);
    }

    /**
     * Returns the last state of this condition.
     *
     * @return The state of this condition.
     */
    public abstract Boolean getState();

    /**
     * Creates a new BooleanCondition that connects the given condition with this one using an and-operator. This method
     * will return a new instance.
     *
     * @param other The condition to connect with this one.
     * @return The resulting condition.
     */
    public AggregateBooleanCondition and(final BooleanCondition other) {
        return new AggregateBooleanCondition(AggregateBooleanCondition.Operator.and, this, other);
    }

    /**
     * Creates a new BooleanCondition that connects the given condition with this one using an or-operator. This method
     * will return a new instance.
     *
     * @param other The condition to connect with this one.
     * @return The resulting condition.
     */
    public AggregateBooleanCondition or(final BooleanCondition other) {
        return new AggregateBooleanCondition(AggregateBooleanCondition.Operator.or, this, other);
    }

    /**
     * Creates a new BooleanCondition that connects the given condition with this one using an xor-operator. This method
     * will return a new instance.
     *
     * @param other The condition to connect with this one.
     * @return The resulting condition.
     */
    public AggregateBooleanCondition xor(final BooleanCondition other) {
        return new AggregateBooleanCondition(AggregateBooleanCondition.Operator.xor, this, other);
    }
}
