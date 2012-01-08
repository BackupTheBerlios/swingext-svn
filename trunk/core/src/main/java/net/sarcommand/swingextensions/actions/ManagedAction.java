package net.sarcommand.swingextensions.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * ManagedActions are subclasses of swing's Action interface which by themselves carry means to resolve the
 * actionPerformend method.  Basically, a ManagedAction extends a normal AbstractAction by adding an identifier to each
 * action and by supplying an implementation for actionPerformed(ActionEvent). This class' default implementation will
 * simply notify the ActionManager that the action has been triggered and leave the actual execution to it.
 * <p/>
 * <hr> Copyright 2006-2012 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ManagedAction extends AbstractAction {
    /**
     * The token which should be used then assigning the menu keymask to accelerator keys. If you want to define a
     * platform independent accelerator key, you should for example use "menu c" rather than "control c" or "meta c" for
     * the copy action.
     */
    public static final String MENU_ACCELERATOR_TOKEN = "menu";

    /**
     * A grouping key used to define exclusive action groups. Only one item of an action group may be selected at a
     * time, so you can use the group key to implement toggle actions.
     */
    public static final String GROUP_KEY = "Group";

    /**
     * Defines which component should be used as root when creating the responder chain. See the javadoc for the
     * ActionManager class for details on his concept.
     */
    public static final String RESPONDER_CHAIN_ROOT = "ResponderChainRoot";

    /**
     * Defines the component which triggered the action as root for the responder chain.
     */
    public static final String RESPONDER_CHAIN_ROOT_COMPONENT = "component";

    /**
     * Defines the current focus owner as root for the responder chain.
     */
    public static final String RESPONDER_CHAIN_ROOT_FOCUS = "focus";

    /**
     * Identifier of this action.
     */
    private Object _identifier;

    /**
     * The ActionManager instance responsible for executing this action.
     */
    private ActionManager _actionManager;

    /**
     * Creates a new ManagedAction using the given identifier.
     *
     * @param identifier    identifier of this action.
     * @param actionManager The ActionManager instance which created this instance.
     */
    public ManagedAction(final Object identifier, final ActionManager actionManager) {
        _identifier = identifier;
        _actionManager = actionManager;
    }

    /**
     * Returns this action's identifier.
     *
     * @return this action's identifier.
     */
    public Object getIdentifier() {
        return _identifier;
    }

    /**
     * Forwards the action's execution to the ActionManager class.
     *
     * @param e ActionEvent generated when triggering this action.
     */
    public void actionPerformed(ActionEvent e) {
        _actionManager.actionPerformed(this, e);
    }
}
