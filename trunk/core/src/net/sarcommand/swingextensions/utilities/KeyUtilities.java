package net.sarcommand.swingextensions.utilities;

import java.awt.event.InputEvent;

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
}
