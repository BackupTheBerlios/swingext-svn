package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The ActionManager class is a utility meant to help you handle the javax.swing.Action instances within your
 * application. It combines the following three major functions:
 * <p/>
 * <li> Creation and management of singleton instances for each Action, organized by identifiers.</li>
 * <li> Handling action execution by implementing a responder chain mechanism</li>
 * <li> Management of action states (which actions are available and which aren't at a certain point</li>
 * <p/>
 * <p/>
 * <h3> Creating and managing actions</h3>
 * <p/>
 * Actions are managed using a unique identifier. Any object can be used as an identifier, but it is strongly
 * recommended that you create an enumeration for the actions within your application since is makes development a
 * lot easier. You can request a singleton instance of the Action for an identifier by using the getAction method.
 * The ActionManager keeps track of the Actions which already have been created, so no two instances will exist for
 * the same identifier.
 * <p/>
 * If the requested action does not exist yet, the ActionProvider will be invoked to create it.
 * An ActionProvider is basically a pluggable factory which can be set using the setActionProvider method.
 * Setting an appropriate ActionProvider when initializing your application is pretty much the only thing you have to
 * do in order to get the ActionManager up and running. Several ActionProvider instances exist which should cover
 * most of the required functions.
 * <p/>
 * <h3>Handling action execution</h3>
 * <p/>
 * The ActionManager will (in most cases) handle execution of actions using a responder chain pattern. A responder chain
 * is basically a hierarchical list of entities which might be able to handle a given action. Triggered actions
 * will move along this list until a suitable handler has been found. In order to do so, Responder candidates have to
 * implement the ActionHandler interface.
 * <p/>
 * The root of the responder chain will be determined by distinguishing between
 * component actions (like those installed in generic control buttons) and focus actions, which may originate in a
 * control but really target the currently focused one. A component action's responder chain will be identical to the
 * trigger control's component hierarchy, while a focus action's responder chain originates in the element which
 * currently posesses the keyboard focus. A simple example for a focus action is the copy command.
 * If you installed the copy action to the menu bar and attached an accelerator key, you will want to copy the current
 * selection when pressing ctrl+c (unless of course if you're using a mac). The ActionManager will identify this
 * action as a focus action (see below) and start looking for responders with the currently focused element. For
 * instance, you might have been typing in a text field. The first possible responder will therefore be the text field
 * itself. If the text field can't handle the action, its parent component will be asked to do so, then the parent's
 * parent, and so on until a responder has been found.
 * <p/>
 * The ActionHandler will distinguish between normal actions and focus actions by looking at the action's
 * RESPONDER_CHAIN_ROOT property. This property can take two values, 'component' or 'focus'. If the property has not
 * been set, the ActionManager will assume that the action is a focus action since this should apply in the majority
 * of cases.
 * <p/>
 * todo [heup] pending completion
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
public class ActionManager {
    private static ActionProvider __actionProvider;
    private static ActionHandler __defaultActionHandler;

    private static HashMap<Object, Action> __actionMap;
    private static HashMap<Object, Collection<Action>> __groups;
    private static HashMap<Object, ActionState> __actionStates;

    /**
     * Returns the action for the given identifier. If the action already exists, it will simply be returned,
     * otherwise the action provider will be used to create a new singleton instance.
     *
     * @param identifier Identifier of the action being requested, non-null.
     * @return action for the given identifier.
     */
    public static Action getAction(final Object identifier) {
        if (identifier == null)
            throw new IllegalArgumentException("Parameter 'identifier' must not be null!");

        if (__actionMap == null)
            __actionMap = new HashMap<Object, Action>(32);

        if (__actionProvider == null)
            __actionProvider = new DefaultActionProvider();

        if (!__actionMap.containsKey(identifier)) {
            final Action action = __actionProvider.createAction(identifier);
            __actionMap.put(identifier, action);

            final Object group = action.getValue(ManagedAction.GROUP_KEY);
            if (group != null) {
                if (__groups == null)
                    __groups = new HashMap<Object, Collection<Action>>(4);
                Collection<Action> col = __groups.get(group);
                if (col == null) {
                    col = new LinkedList<Action>();
                    __groups.put(group, col);
                }
                col.add(action);
            }
        }

        return __actionMap.get(identifier);
    }

    /**
     * Invoked by ManagedActions when they have been invoked. The ActionManager will search the responder chain for
     * a suitable action handle to process the triggered action.
     *
     * @param actionIdentifier Identifier of the action that has been fired.
     * @param event            The generated action event.
     */
    public static void actionPerformed(final Object actionIdentifier, final ActionEvent event) {
        if (actionIdentifier == null)
            throw new IllegalArgumentException("Parameter 'actionIdentifier' must not be null!");
        if (event == null)
            throw new IllegalArgumentException("Parameter 'event' must not be null!");


        boolean eventWasConsumed = false;

        /* Get the action for the given identifier */
        final Action a = getAction(actionIdentifier);
        if (a == null)
            throw new IllegalArgumentException("Unknown action: " + actionIdentifier);

        /* Get the source for the triggered action */
        final Object source = event.getSource();

        /* Check if this action is marked as a focus action */
        final Object focusActionProperty = a.getValue(ManagedAction.RESPONDER_CHAIN_ROOT_FOCUS);
        boolean isFocusAction = focusActionProperty != null && focusActionProperty.equals(Boolean.TRUE);

        /* Try to find a proper handler for the action */
        if (source instanceof Component) {
            /* Move up the hierarchy to find a suitable responder */
            Component runner = isFocusAction ? KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()
                    : (Component) source;
            event.setSource(runner);
            while ((runner = SwingExtUtil.getParent(runner)) != null) {
                if (runner instanceof ActionHandler) {
                    eventWasConsumed = ((ActionHandler) runner).handleAction(actionIdentifier, event);
                    if (eventWasConsumed)
                        break;
                }
            }
        }

        /* Forward the action to the default action consumer */
        if (!eventWasConsumed && __defaultActionHandler != null)
            __defaultActionHandler.handleAction(actionIdentifier, event);

        /* Check if the action was a toggle action (got a group). If so, toggle the other actions' selection state */
        final Action action = getAction(actionIdentifier);
        final Object group = action.getValue(ManagedAction.GROUP_KEY);
        if (__groups != null && group != null && __groups.containsKey(group)) {
            action.putValue(Action.SELECTED_KEY, true);
            final Collection<Action> actions = __groups.get(group);
            for (Action groupAction : actions) {
                if (!action.equals(groupAction))
                    groupAction.putValue(Action.SELECTED_KEY, false);
            }
        }
    }

    /**
     * Returns the current action state. This method is useful if you need to 'remember' the state of your
     * application, for instance if you want to temporarily disable all actions while loading a file and then
     * restore the previous state.
     *
     * @return the current action state.
     */
    public static ActionState getCurrentActionState() {
        final ActionState state = new SimpleActionState();
        for (Object key : __actionMap.keySet()) {
            final Action action = __actionMap.get(key);
            if (action != null)
                state.setEnabled(action, action.isEnabled());
        }
        return state;
    }

    /**
     * Sets the current action state. This will cause the actions' enabled states to be adjusted accoring to the
     * given state.
     *
     * @param state The action state to set.
     */
    public static void setCurrentActionState(final ActionState state) {
        if (state == null)
            throw new IllegalArgumentException("Parameter 'state' must not be null!");

        final Map<Action, Boolean> states = state.getEnabledStates();
        for (Action a : states.keySet())
            a.setEnabled(state.isEnabled(a));
    }

    /**
     * Sets the current action state, using a previously stored action state.
     *
     * @param stateKey The identifier under which the state has previously been stored using putState.
     */
    public static void setCurrentActionState(final Object stateKey) {
        if (stateKey == null)
            throw new IllegalArgumentException("Parameter 'stateKey' must not be null!");

        final ActionState state = __actionStates.get(stateKey);
        if (state == null)
            throw new IllegalArgumentException("Illegal state key:" + stateKey);

        final Map<Action, Boolean> states = state.getEnabledStates();
        for (Action a : states.keySet())
            a.setEnabled(state.isEnabled(a));
    }

    /**
     * Returns the action state which was previously stored under the given identifier using putActionState. Note
     * that this is not necessarily the current action state, this is just a conveniance method which allows you
     * to store action states.
     *
     * @param identifier Identifier under which the action state has been stored.
     * @return the action state which was previously stored under the given identifier using putActionState.
     */
    public static ActionState getActionState(final Object identifier) {
        return __actionStates == null ? null : __actionStates.get(identifier);
    }

    /**
     * Registers an action state under the given identifier. Invoking this method will not have any effect on the
     * actions, this is merely a conveniance method used to 'store' action states for later use.
     *
     * @param identifier Identifier under which this state should be stored, non-null.
     * @param state      ActionState to store, may be null.
     */
    public static void putActionState(final Object identifier, final ActionState state) {
        if (identifier == null)
            throw new IllegalArgumentException("Parameter 'identifier' must not be null!");

        if (__actionStates == null)
            __actionStates = new HashMap<Object, ActionState>(4);
        __actionStates.put(identifier, state);
    }

    /**
     * Sets the action provider, a factory used to create new actions.
     *
     * @param provider a factory used to create new actions.
     */
    public static void setActionProvider(final ActionProvider provider) {
        __actionProvider = provider;
    }

    /**
     * Returns the provider instance responsible for creating new actions.
     *
     * @return the provider instance responsible for creating new actions.
     */
    public static ActionProvider getActionProvider() {
        return __actionProvider;
    }

    /**
     * Returns the default action handler instance which will be used when no suitable responder for an action could
     * be found.
     *
     * @return action handler instance which will be used when no suitable responder for an action could
     *         be found, may be null.
     */
    public static ActionHandler getDefaultActionHandler() {
        return __defaultActionHandler;
    }

    /**
     * Sets the default action handler, which is being used when no suitable responder for an action can be found.
     *
     * @param defaultActionHandler action handler which is being used when no suitable responder for an action
     *                             can be found, may be null.
     */
    public static void setDefaultActionHandler(ActionHandler defaultActionHandler) {
        __defaultActionHandler = defaultActionHandler;
    }

    /**
     * This class can not be instanciated
     */
    private ActionManager() {
    }
}
