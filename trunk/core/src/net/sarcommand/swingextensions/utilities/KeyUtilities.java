package net.sarcommand.swingextensions.utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.HashSet;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class KeyUtilities {
    public static String getModifiersAsText(int modifiers) {
        final StringBuilder buffer = new StringBuilder();

        if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0)
            buffer.append("shift ");
        if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0)
            buffer.append("ctrl ");
        if ((modifiers & InputEvent.META_DOWN_MASK) != 0)
            buffer.append("meta ");
        if ((modifiers & InputEvent.ALT_DOWN_MASK) != 0)
            buffer.append("alt ");
        if ((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0)
            buffer.append("altGraph ");
        if ((modifiers & InputEvent.SHIFT_MASK) != 0)
            buffer.append("shift ");
        if ((modifiers & InputEvent.CTRL_MASK) != 0)
            buffer.append("ctrl ");
        if ((modifiers & InputEvent.META_MASK) != 0)
            buffer.append("meta ");
        if ((modifiers & InputEvent.ALT_MASK) != 0)
            buffer.append("alt ");
        if ((modifiers & InputEvent.ALT_GRAPH_MASK) != 0)
            buffer.append("altGraph ");
        return buffer.toString();
    }

    public static void addFocusTraversalKey(final JComponent target, final int keyCode, final int modifiers, final int id) {
        addFocusTraversalKey(target, KeyStroke.getAWTKeyStroke(keyCode, modifiers), id);
    }

    public static void addFocusTraversalKey(final JComponent target, final AWTKeyStroke stroke, final int id) {
        final HashSet<AWTKeyStroke> strokes = new HashSet<AWTKeyStroke>(target.getFocusTraversalKeys(id));
        strokes.add(stroke);
        target.setFocusTraversalKeys(id, strokes);
    }

    public static void removeFocusTraversalKey(final JComponent target, final int keyCode, final int modifiers, final int id) {
        removeFocusTraversalKey(target, KeyStroke.getAWTKeyStroke(keyCode, modifiers), id);
    }

    public static void removeFocusTraversalKey(final JComponent target, final AWTKeyStroke stroke, final int id) {
        final HashSet<AWTKeyStroke> strokes = new HashSet<AWTKeyStroke>(target.getFocusTraversalKeys(id));
        strokes.remove(stroke);
        target.setFocusTraversalKeys(id, strokes);
    }

    /**
     * Registers the given action for a keystroke.
     *
     * @param component Compontent the action should be registered on.
     * @param condition The condition for the input map (as you would pass to JComponent#getInputMap(int))
     * @param keyStroke Keystroke the action should be registered for.
     * @param action    Action to register for the given keystroke.
     * @return The previous action key bound to the keystroke, if there is one.
     */
    public static Object setActionKeyBinding(final JComponent component, final int condition,
                                             final KeyStroke keyStroke, final Action action) {
        if (component == null)
            throw new IllegalArgumentException("Parameter 'component' must not be null!");
        if (keyStroke == null)
            throw new IllegalArgumentException("Parameter 'keyStroke' must not be null!");
        if (action == null)
            throw new IllegalArgumentException("Parameter 'action' must not be null!");

        String actionKey = (String) action.getValue(Action.NAME);
        if (actionKey != null)
            actionKey = "action" + System.identityHashCode(action);

        final InputMap map = component.getInputMap(condition);
        final Object previousKeyBinding = map.get(keyStroke);
        map.put(keyStroke, actionKey);
        component.setInputMap(condition, map);
        component.getActionMap().put(actionKey, action);
        return previousKeyBinding;
    }

    /**
     * Registers the given action for a keystroke, using JComponent.WHEN_FOCUSED as condition for the input map.
     *
     * @param component Compontent the action should be registered on.
     * @param keyStroke Keystroke the action should be registered for.
     * @param action    Action to register for the given keystroke.
     * @return The previous action key bound to the keystroke, if there is one.
     */
    public static Object setActionKeyBinding(final JComponent component, final KeyStroke keyStroke,
                                             final Action action) {
        return setActionKeyBinding(component, JComponent.WHEN_FOCUSED, keyStroke, action);
    }
}
