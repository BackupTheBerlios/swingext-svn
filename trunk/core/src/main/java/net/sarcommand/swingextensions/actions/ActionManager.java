package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The ActionManager class is a utility meant to help you handle the javax.swing.Action instances within your
 * application. It combines the following three major functions:
 * <p/>
 * <li> Creation and management of singleton instances for each Action, organized by identifiers.</li> <li> Handling
 * action execution by implementing a responder chain mechanism</li>
 * <p/>
 * <h3> Creating and managing actions</h3>
 * <p/>
 * Actions are managed using a unique identifier. Any object can be used as an identifier, but it is strongly
 * recommended that you create an enumeration for the actions within your application since is makes development a lot
 * easier. You can request a singleton instance of the Action for an identifier by using the getAction method. The
 * ActionManager keeps track of the Actions which already have been created, so no two instances will exist for the same
 * identifier.
 * <p/>
 * If the requested action does not exist yet, the ActionProvider will be invoked to create it. An ActionProvider is
 * basically a pluggable factory which can be set using the setActionProvider method. Setting an appropriate
 * ActionProvider when initializing your application is pretty much the only thing you have to do in order to get the
 * ActionManager up and running. Several ActionProvider instances exist which should cover most of the required
 * functions.
 * <p/>
 * <h3>Handling action execution</h3>
 * <p/>
 * The ActionManager will (in most cases) handle execution of actions using a responder chain pattern. A responder chain
 * is basically a hierarchical list of entities which might be able to handle a given action. Triggered actions will
 * move along this list until a suitable handler has been found. In order to do so, Responder candidates have to
 * implement the ActionHandler interface.
 * <p/>
 * The root of the responder chain will be determined by distinguishing between component actions (like those installed
 * in generic control buttons) and focus actions, which may originate in a control but really target the currently
 * focused one. A component action's responder chain will be identical to the trigger control's component hierarchy,
 * while a focus action's responder chain originates in the element which currently posesses the keyboard focus. A
 * simple example for a focus action is the copy command. If you installed the copy action to the menu bar and attached
 * an accelerator key, you will want to copy the current selection when pressing ctrl+c (unless of course if you're
 * using a mac). The ActionManager will identify this action as a focus action (see below) and start looking for
 * responders with the currently focused element. For instance, you might have been typing in a text field. The first
 * possible responder will therefore be the text field itself. If the text field can't handle the action, its parent
 * component will be asked to do so, then the parent's parent, and so on until a responder has been found.
 * <p/>
 * The ActionHandler will distinguish between normal actions and focus actions by looking at the action's
 * RESPONDER_CHAIN_ROOT property. This property can take two values, 'component' or 'focus'. If the property has not
 * been set, the ActionManager will assume that the action is a focus action since this should apply in the majority of
 * cases.
 * <p/>
 * <hr/> Copyright 2006-2009 Torsten Heup
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
public class ActionManager {
    /**
     * The ActionProvider being used to create new Action instances.
     */
    private ActionProvider _actionProvider;

    /**
     * The default handler to which all actions will be passed if no suitable ActionHandler could be found in the
     * ResponderChain.
     */
    private ActionHandler _defaultActionHandler;

    /**
     * This map is being used to cache loaded actions, making sure that each action identifier resolves to a singleton
     * action.
     */
    private HashMap<Object, Action> _actionMap;

    /**
     * This map will be used to keep track of action groups.
     */
    private HashMap<Object, Collection<Action>> _groups;

    /**
     * Flag determining whether the ActionManager has been properly initialized.
     */
    protected boolean _initialized;

    /**
     * This field will be used to track the last permanent focus owner for actions which should origin in the focued
     * component.
     */
    protected Component _lastFocusOwner;

    /**
     * This field implements a weak references stack of the last focused windows. If an action is declared to origin in
     * the currently focued component, the ActionManager will use this list to make sure that the enclosing window of
     * the focused component is still visible.
     */
    protected LinkedList<WeakReference<Window>> _focusedWindows;

    public ActionManager() {
        initialize();
    }

    /**
     * Initializes the ActionManager. This method will automatically be triggered by getAction.
     */
    protected void initialize() {
        if (_initialized)
            return;
        _focusedWindows = new LinkedList<WeakReference<Window>>();

        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final Object newValue = evt.getNewValue();
                if (evt.getPropertyName().equals("permanentFocusOwner") && newValue != null
                        && !isActionControl(newValue)) {
                    _lastFocusOwner = (Component) newValue;
                } else if (evt.getPropertyName().equals("focusedWindow")) {
                    if (newValue != null) {
                        /* Check if the window is already part of the stack */
                        WeakReference<Window> wref;
                        Window w;
                        synchronized (ActionManager.class) {
                            for (Iterator<WeakReference<Window>> it = _focusedWindows.iterator(); it.hasNext();) {
                                wref = it.next();
                                w = wref.get();
                                if (w == null || w.equals(newValue))
                                    it.remove();
                            }
                            _focusedWindows.add(new WeakReference<Window>((Window) newValue));
                        }
                    }
                }
            }
        };

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(listener);

        _initialized = true;
    }

    /**
     * Sets the action provider, a factory used to create new actions.
     *
     * @param provider a factory used to create new actions.
     */
    public void setActionProvider(final ActionProvider provider) {
        _actionProvider = provider;
    }

    /**
     * Returns the action for the given identifier. If the action already exists, it will simply be returned, otherwise
     * the action provider will be used to create a new singleton instance.
     *
     * @param identifier Identifier of the action being requested, non-null.
     * @return action for the given identifier.
     */
    public Action getAction(final Object identifier) {
        if (!_initialized)
            initialize();

        if (identifier == null)
            throw new IllegalArgumentException("Parameter 'identifier' must not be null!");

        if (_actionMap == null)
            _actionMap = new HashMap<Object, Action>(32);

        if (_actionProvider == null)
            _actionProvider = new DefaultActionProvider();

        if (!_actionMap.containsKey(identifier)) {
            final ManagedAction action = new ManagedAction(identifier, this);
            _actionProvider.configurePropertiesForAction(identifier, action);
            _actionMap.put(identifier, action);

            final Object group = action.getValue(ManagedAction.GROUP_KEY);
            if (group != null) {
                if (_groups == null)
                    _groups = new HashMap<Object, Collection<Action>>(4);
                Collection<Action> col = _groups.get(group);
                if (col == null) {
                    col = new LinkedList<Action>();
                    _groups.put(group, col);
                }
                col.add(action);
            }
        }

        return _actionMap.get(identifier);
    }

    /**
     * Returns whether the specified object is an action control (e.g. a button, a menu item etc.).
     *
     * @param object Object to check.
     * @return whether the specified object is an action control
     */
    protected boolean isActionControl(final Object object) {
        if (object == null)
            return false;
        if (object instanceof JMenuItem)
            return true;
        if (object instanceof Component)
            return ((Component) object).getParent() instanceof JToolBar;
        return false;
    }

    /**
     * Invokes the specified method.
     *
     * @param actionID      id of the method which should be invoked
     * @param source        source of the ActionEvent fired
     * @param actionCommand action command to be passed to the ActionEvent
     */
    public void invoke(final Object actionID, final Object source, final String actionCommand) {
        final Action action = getAction(actionID);
        if (action == null)
            throw new IllegalArgumentException("Illegal actionID: " + action);
        action.actionPerformed(new ActionEvent(source, ActionEvent.ACTION_PERFORMED, actionCommand));
    }

    /**
     * Invoked by ManagedActions when they have been invoked. The ActionManager will search the responder chain for a
     * suitable action handle to process the triggered action.
     *
     * @param action The ManagedAction instance that was triggered.
     * @param event  The generated action event.
     */
    public void actionPerformed(final ManagedAction action, final ActionEvent event) {
        if (action == null)
            throw new IllegalArgumentException("Parameter 'action' must not be null!");
        if (event == null)
            throw new IllegalArgumentException("Parameter 'event' must not be null!");

        final Object actionIdentifier = action.getIdentifier();
        boolean eventWasConsumed = false;

        /* Get the action for the given identifier */
        final Action a = getAction(actionIdentifier);
        if (a == null)
            throw new IllegalArgumentException("Unknown action: " + actionIdentifier);

        /* Get the source for the triggered action */
        final Object source = event.getSource();

        /* Check if this action is marked as a focus action */
        final Object focusActionProperty = a.getValue(ManagedAction.RESPONDER_CHAIN_ROOT);
        final boolean isComponentAction = _lastFocusOwner == null || (focusActionProperty != null &&
                focusActionProperty.equals(ManagedAction.RESPONDER_CHAIN_ROOT_COMPONENT));

        /* Check if a direct delegate has been installed */
        final Object directDelegate = (source instanceof JComponent) ?
                ((JComponent) source).getClientProperty(ActionHandler.NEXT_HANDLER) : null;

        /* Try to find a proper handler for the action */
        Object runner = directDelegate != null ? directDelegate :
                isComponentAction ? (Component) source : _lastFocusOwner;

        /* If the last focused component is installed in a non-visible window, find a visible window and redispatch */
        if (!isComponentAction && runner instanceof JComponent) {
            final Window w = SwingExtUtil.getWindowForComponent((Component) runner);
            if (w == null || !w.isVisible()) {
                synchronized (this) {
                    WeakReference<Window> wref;
                    Window window;
                    while (true) {
                        wref = _focusedWindows.getLast();
                        window = wref.get();
                        if (window == null || !window.isVisible())
                            _focusedWindows.removeLast();
                        else {
                            final Window newFocus = window;
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    newFocus.requestFocus();
                                    event.setSource(newFocus.getFocusOwner());
                                    actionPerformed(action, event);
                                }
                            });
                            return;
                        }
                    }
                }
            }
        }

        /* Move up the hierarchy to find a suitable responder */
        event.setSource(runner);
        while (true) {
            if (runner instanceof ActionHandler) {
                eventWasConsumed = ((ActionHandler) runner).handleAction(actionIdentifier, event);
                if (eventWasConsumed)
                    break;
            }
            if (runner instanceof JComponent) {
                final Object nextHandler = ((JComponent) runner).getClientProperty(ActionHandler.NEXT_HANDLER);
                if (nextHandler != null)
                    runner = nextHandler;
                else
                    runner = SwingExtUtil.getParent((Component) runner);
            } else if (runner instanceof Component)
                runner = SwingExtUtil.getParent((Component) runner);
            else
                break;

            if (runner == null)
                break;
        }

        /* Forward the action to the default action consumer */
        if (!eventWasConsumed && _defaultActionHandler != null)
            _defaultActionHandler.handleAction(actionIdentifier, event);

        /* Check if the action was a toggle action (got a group). If so, toggle the other actions' selection state */
        final Object group = action.getValue(ManagedAction.GROUP_KEY);
        if (_groups != null && group != null && _groups.containsKey(group)) {
            action.putValue(Action.SELECTED_KEY, true);
            final Collection<Action> actions = _groups.get(group);
            for (Action groupAction : actions) {
                if (!action.equals(groupAction))
                    groupAction.putValue(Action.SELECTED_KEY, false);
            }
        }
    }

    /**
     * Returns the provider instance responsible for creating new actions.
     *
     * @return the provider instance responsible for creating new actions.
     */
    public ActionProvider getActionProvider() {
        return _actionProvider;
    }

    /**
     * Returns the default action handler instance which will be used when no suitable responder for an action could be
     * found.
     *
     * @return action handler instance which will be used when no suitable responder for an action could be found, may
     *         be null.
     */
    public ActionHandler getDefaultActionHandler() {
        return _defaultActionHandler;
    }

    /**
     * Sets the default action handler, which is being used when no suitable responder for an action can be found.
     *
     * @param defaultActionHandler action handler which is being used when no suitable responder for an action can be
     *                             found, may be null.
     */
    public void setDefaultActionHandler(ActionHandler defaultActionHandler) {
        _defaultActionHandler = defaultActionHandler;
    }
}
