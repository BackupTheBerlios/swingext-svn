package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.binding.Keypath;

/**
 * Autowiring action that will set a property of a target bean (or component) depending on whether or not a boolean
 * condition is met. You can instanciate the class by specifying the target bean, a keypath used to set the bean's
 * property, a value for when the condition returns true and a value for when it returns false. The type parameter for
 * this action should match the property's type.
 * <p/>
 * Example: Setting a label's icon: <code> new SetPropertyAction<Icon>(myLabel, new Keypath<Icon>("icon"), iconWhenTrue,
 * iconWhenFalse); </code>
 * <p/>
 * <em>Note:</em>The autowiring class offers a set of utility methods for changing components' properties, e.g.
 * setIcon(...), setForeground(...) etc. <hr/> Copyright 2006-2012 Torsten Heup
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
public class SetPropertyAction<T> implements BooleanConditionedAction {
    /**
     * The target bean, which's property will be set.
     */
    protected Object _target;

    /**
     * The keypath used to set the property value.
     */
    protected Keypath<T> _keypath;

    /**
     * The value to set when the attached conditions resolve to true.
     */
    protected T _propertyWhenTrue;

    /**
     * The value to set when the attached conditions resolve to false.
     */
    protected T _propertyWhenFalse;

    /**
     * The boolean condition to monitor.
     */
    protected AggregateBooleanCondition _condition;

    /**
     * Instanciates a new action that will change a target bean's property depending on a condition.
     *
     * @param target            The target bean, which's property will be set.
     * @param keypath           The keypath used to set the property value.
     * @param propertyWhenTrue  The value to set when the attached conditions resolve to true.
     * @param propertyWhenFalse The value to set when the attached conditions resolve to false.
     */
    public SetPropertyAction(final Object target, final Keypath<T> keypath, final T propertyWhenTrue,
                             final T propertyWhenFalse) {
        _target = target;
        _keypath = keypath;
        _propertyWhenTrue = propertyWhenTrue;
        _propertyWhenFalse = propertyWhenFalse;

        _condition = new AggregateBooleanCondition();
        _condition.addConditionListener(new ConditionListener() {
            @Override
            public void conditionUpdated(final Condition condition) {
                update();
            }
        });
        update();
    }

    @Override
    public AggregateBooleanCondition whenever(final BooleanCondition... conditions) {
        _condition.addConditions(conditions);
        return _condition;
    }

    @Override
    public AggregateBooleanCondition unless(final BooleanCondition... conditions) {
        _condition.addConditions(new AggregateBooleanCondition(AggregateBooleanCondition.Operator.and, true, conditions));
        return _condition;
    }

    /**
     * Triggered whenever the attached boolean condition updates.
     */
    protected void update() {
        final Boolean state = _condition.getState();
        if (state != null)
            _keypath.set(_target, state ? _propertyWhenTrue : _propertyWhenFalse);
    }
}
