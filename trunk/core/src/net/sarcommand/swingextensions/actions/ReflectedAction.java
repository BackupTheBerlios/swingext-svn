package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

/**
 * A ReflectedAction is an implementation of the ManagedAction concept which uses reflection to invoke a method when
 * the action is performed. It is created with a target object and the name of the method handling the action. By
 * convention, the method being notified has to meet one of the following two signatures:
 * <li>(Object, ActionEvent), in which case the action's identifier and the generated event will be passed as
 * parameters, or</li>
 * <li>() with no parameters, in which case the method will merely used as a notifier.</li>
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ReflectedAction extends ManagedAction {
    /**
     * The target object on which the declared method will be invoked.
     */
    protected Object _target;
    /**
     * The name of the method to invoke.
     */
    protected String _methodName;

    /**
     * Creates a new ReflectedAction instance.
     *
     * @param identifier The unique identifier of this action.
     * @param target     The target object, on which the specified method will be invoked when the action is performed.
     * @param methodName The name of the method to invoke when the action is performed.
     */
    public ReflectedAction(final Object identifier, final Object target, final String methodName) {
        super(identifier);
        if (target == null)
            throw new IllegalArgumentException("Parameter 'target' must not be null!");
        if (methodName == null)
            throw new IllegalArgumentException("Parameter 'methodName' must not be null!");
        if (!(SwingExtUtil.hasMethod(target, methodName) || SwingExtUtil.hasMethod(target, methodName, Object.class,
                ActionEvent.class)))
            throw new IllegalArgumentException("Method " + methodName + " could not be found on proxy target " + target);
        _target = target;
        _methodName = methodName;
    }

    public void actionPerformed(ActionEvent e) {
        Method method = SwingExtUtil.getMethod(_target, _methodName, Object.class, ActionEvent.class);
        if (method != null) {
            try {
                method.invoke(_target, getIdentifier(), e);
                return;
            } catch (Exception e1) {
                throw new RuntimeException("Could not access method " + _methodName + " on target " + _target, e1);
            }
        }
        method = SwingExtUtil.getMethod(_target, _methodName);
        if (method != null) {
            try {
                method.invoke(_target);
                return;
            } catch (Exception e1) {
                throw new RuntimeException("Could not access method " + _methodName + " on target " + _target, e1);
            }
        }

        throw new RuntimeException("Could not access method " + _methodName + " on target " + _target);
    }
}
