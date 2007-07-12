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
}
