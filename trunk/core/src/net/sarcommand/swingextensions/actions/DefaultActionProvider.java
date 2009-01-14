package net.sarcommand.swingextensions.actions;

import javax.swing.*;


/**
 * Default action provider used by the ActionManager. This instance will return null for all action identifiers, it
 * merely works as a placeholder.
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
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
public class DefaultActionProvider implements ActionProvider {
    /**
     * @see net.sarcommand.swingextensions.actions.ActionProvider#createManagedAction(Object)
     */
    public ManagedAction createManagedAction(final Object identifier) {
        final ManagedAction action = new ManagedAction(identifier);
        action.putValue(Action.NAME, identifier.toString());
        return action;
    }

    /**
     * @see net.sarcommand.swingextensions.actions.ActionProvider#createReflectedAction(Object, Object, String)
     */
    public ReflectedAction createReflectedAction(final Object identifier, final Object target,
                                                 final String methodName) {
        final ReflectedAction action = new ReflectedAction(identifier, target, methodName);
        action.putValue(Action.NAME, identifier.toString());
        return action;
    }
}
