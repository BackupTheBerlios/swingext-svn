package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.event.EventSupport;

/**
 * An autowiring Condition resolving to a boolean state. <hr/> Copyright 2006-2010 Torsten Heup
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
    private EventSupport<ConditionListener> _listeners;

    public BooleanCondition() {
        _listeners = EventSupport.create(ConditionListener.class);
    }

    public void addConditionListener(final ConditionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _listeners.addListener(listener);
    }

    public void removeConditionListener(final ConditionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");

        _listeners.removeListener(listener);
    }

    protected void fireConditionUpdated() {
        _listeners.delegate().conditionUpdated(this);
    }

    public abstract Boolean getState();
}
