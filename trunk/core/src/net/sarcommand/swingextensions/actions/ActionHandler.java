package net.sarcommand.swingextensions.actions;

import java.awt.event.ActionEvent;

/**
 * Interface which tags an object as a possible responder for triggered actions. Whenever an action managed by the
 * ActionManager class is invoked, the ActionManager will follow the responder chain until a suitable responder has
 * been found.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public interface ActionHandler {
    /**
     * When the ActionManager is trying to find a suitable responder for an action, it will usually follow the component
     * hierarchy. However, in some cases you may want to provide a custom ActionHandler at some point which is not a
     * component in the hierarchy. You can do so using Swing's client property system. Invoke putClientProperty on a
     * component in the chain, using this constant as the key and the new action handler as value.
     */
    public static final String NEXT_HANDLER = ActionHandler.class.getName() + ".nextHandler";

    /**
     * Requests that the implementing object should handle the given action. Implementors need to return whether or
     * not the action could actually be handled. If this method returns true, the ActionManager will stop searching
     * for responders.
     *
     * @param identifier Identifier of the action being triggered.
     * @param e          The according ActionEvent.
     * @return Whether or not the implementor could handle the action.
     */
    public boolean handleAction(final Object identifier, final ActionEvent e);
}
