package net.sarcommand.swingextensions.actions;

import javax.swing.*;
import java.util.Collection;
import java.util.Map;

/**
 * This class can be used to keep track of the enabled state for the actions you use in your component. If you are
 * using fix states in your application, managing states should save you from the effort of manually disabling and
 * enabling components. As a simple example, consider an application used to gather data from an external source.
 * While you're collecting data, you want everyone except for the stop (and possibly pause) action to be disabled
 * so the user won't attempt to save files or anything like that. Once data acquisition has finished, you want all
 * your actions to revert to their original states. In this case, all you have to do is retrieve the current action
 * state before starting the data acquisiition and restore it afterwards. If you're using the ActionManager class,
 * you will never have to instanciate this class directly. Instead, you can use ActionManager's utility methods.
 * <p/>
 * <hr/>
 * Copyright 2006 Torsten Heup
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
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public interface ActionState {
    /**
     * Returns whether a given action is enabled in this state.
     *
     * @param action Action being queried.
     * @return whether the action is enabled in this state.
     */
    public boolean isEnabled(final Action action);

    /**
     * Set whether the given action is enabled in this state.
     *
     * @param action  Action to toggle.
     * @param enabled Whether or not the action is enabled.
     */
    public void setEnabled(final Action action, final boolean enabled);

    /**
     * Performs a bulk setEnabled operation on the given set of actions. This is a conveniance method which does
     * exactly the same as if you invoked setEnabled for every action in the collection.
     *
     * @param actions Actions being toggled.
     * @param enabled Whether or not the actions are enabled.
     */
    public void setEnabled(final Collection<Action> actions, final boolean enabled);

    /**
     * Returns the map containing the enabled property for each action in this state.
     *
     * @return an unmodifiable map mirroring this action state.
     */
    public Map<Action, Boolean> getEnabledStates();

    /**
     * Returns the actions contained in this action state.
     *
     * @return the actions contained in this action state.
     */
    public Collection<Action> getActions();
}
