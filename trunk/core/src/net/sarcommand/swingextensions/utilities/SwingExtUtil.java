package net.sarcommand.swingextensions.utilities;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

/**
 * A collection of small utilities for component management and reflection handling.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class SwingExtUtil {
    /**
     * Returns the parent window for the given component. Unlike the according method in JOptionPane (who the heck
     * put it there anyway?), this method will also resolve popup menus. If you invoke getWindowForComponent on an
     * item in a popup menu, it will return the frame from which the popup was invoked rather than some anonymous
     * instance.
     *
     * @param c Component to query.
     * @return Parent window for the given component.
     */
    public static synchronized Window getWindowForComponent(Component c) {
        while (c != null && !(c instanceof Window)) {
            if (c instanceof JPopupMenu)
                c = ((JPopupMenu) c).getInvoker();
            else
                c = c.getParent();
        }
        return (Window) c;
    }

    /**
     * Returns the parent for the given component. Other than Component#getParent(), this method will work for
     * popup menus as well, returning the menu's invoker rather than its own window instance.
     *
     * @param c Component to query
     * @return Parent element of the given component.
     */
    public static synchronized Component getParent(final Component c) {
        if (c instanceof JPopupMenu)
            return ((JPopupMenu) c).getInvoker();
        else
            return c.getParent();
    }

    /**
     * Returns whether a given object has the specified method.  This is merely a conveniance method to avoid
     * reflection's exceptions.
     *
     * @param target        Target object to check
     * @param methodName    Name of the method being queried
     * @param argumentTypes Types of the method's arguments
     * @return whether or not the method exists
     */
    public static synchronized boolean hasMethod(final Object target, final String methodName,
                                                 final Class... argumentTypes) {
        return getMethod(target, methodName, argumentTypes) != null;
    }

    /**
     * Returns a the specified method of the target object, or null if the method does not exist.  This is merely a
     * conveniance method to avoid reflection's exceptions.
     *
     * @param target        Target object to check
     * @param methodName    Name of the method being queried
     * @param argumentTypes Types of the method's arguments
     * @return Method instance if the method exists, false otherwise.
     */
    public static synchronized Method getMethod(final Object target, final String methodName,
                                                final Class... argumentTypes) {
        final Method method;
        try {
            method = target.getClass().getMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return method;
    }
}
