package net.sarcommand.swingextensions.utilities;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A collection of small utilities for component management and reflection handling.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class SwingExtUtil {
    private static ExecutorService _executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Returns the parent window for the given component. Unlike the according method in JOptionPane (who the heck
     * put it there anyway?), this method will also resolve popup menus. If you invoke getWindowForComponent on an
     * item in a popup menu, it will return the window from which the popup was invoked rather than some anonymous
     * instance.
     * Another important difference lies in the fact that this method will resolve to a window rather than a frame.
     * When using JOptionPane to resolve the frame for a component in a JDialog, you might get the shared root frame,
     * which most likely is not what you will want.
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

    /**
     * Returns a getter method for the given property, or null if there is none.
     *
     * @param target       Target exposing the getter.
     * @param propertyName The getter's property.
     * @return a getter method for the given property, or null if there is none.
     */
    public static synchronized Method getGetter(final Object target, final String propertyName) {
        Method m;
        m = getGetterWithPrefix(target, propertyName, "get");
        if (m != null)
            return m;
        m = getGetterWithPrefix(target, propertyName, "is");
        if (m != null)
            return m;
        m = getGetterWithPrefix(target, propertyName, "has");
        return m;
    }

    /**
     * Returns a setter method for the given property, or null if there is none.
     *
     * @param target       Target exposing the setter.
     * @param propertyName The setter's property.
     * @return a setter method for the given property, or null if there is none.
     */
    public static synchronized Method getSetter(final Object target, final String propertyName) {
        String setterName = "set" + Character.toUpperCase(propertyName.charAt(0));
        if (setterName.length() > 1)
            setterName += propertyName.substring(1);

        final Class targetClass = target.getClass();
        final Method[] methods = targetClass.getMethods();

        for (Method m : methods) {
            if (m.getName().equals(setterName) && m.getParameterTypes().length == 1)
                return m;
        }
        return null;
    }

    /**
     * Internal method used by the getGetter method to try the different possible prefixes 'get', 'has' and 'is'.
     *
     * @param target   Target object exposing the getter.
     * @param property Property being searched.
     * @param prefix   Prefix for the supposed getter.
     * @return The getter method if it exists, null otherwise.
     */
    protected static Method getGetterWithPrefix(final Object target, final String property, final String prefix) {
        String name = prefix + Character.toUpperCase(property.charAt(0));
        if (property.length() > 1)
            name = name + property.substring(1);

        return getMethod(target, name);
    }

    /**
     * Invokes the given runnable in an asychronous worker thread.
     *
     * @param r Runnable to invoke.
     */
    public static void invokeAsWorker(final Runnable r) {
        _executor.execute(r);
    }

    /**
     * Invokes the given method on an asynchronous worker thread.
     *
     * @param target     Target object exposing the specified method.
     * @param methodName Method to invoke.
     * @param args       Arguments to pass to the method.
     */
    public static void invokeAsWorker(final Object target, final String methodName, final Object... args) {
        final Class[] types = new Class[args.length];
        for (int i = 0; i < types.length; i++)
            types[i] = args[i].getClass();
        final Method m = getMethod(target, methodName, types);
        if (m == null)
            throw new RuntimeException("No such method: " + methodName + '(' + Arrays.toString(types) + ") found for target" +
                    "class " + target.getClass().getName());
        invokeAsWorker(new Runnable() {
            public void run() {
                try {
                    m.invoke(target, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Invokes the target runnable on the event dispatch thread. This is equivalent to SwingUtilities.invokeLater(...),
     * this method was just put here to have all the invoke-methods in one place.
     *
     * @param r Runnable to start.
     */
    public static void invokeOnEDT(final Runnable r) {
        SwingUtilities.invokeLater(r);
    }

    /**
     * Invokes the given method on the EDT.
     *
     * @param target     Target object exposing the specified method.
     * @param methodName Method to invoke.
     * @param args       Arguments to pass to the method.
     */
    public static void invokeOnEDT(final Object target, final String methodName, final Object... args) {
        final Class[] types = new Class[args.length];
        for (int i = 0; i < types.length; i++)
            types[i] = args[i].getClass();
        final Method m = getMethod(target, methodName, types);
        if (m == null)
            throw new RuntimeException("No such method: " + methodName + '(' + Arrays.toString(types) + ") found for target" +
                    "class " + target.getClass().getName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    m.invoke(target, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * This method converts a given number into a target class. This method does not change the value (except when
     * explicitly casting to a more general type, e.g. from double to int), just the internal type representation.
     * While this is unnecessary while using normal java code, reflection based access to method parameters is a
     * bit more difficult. As far as possible, this method will prevent the ArgumentMismatch error when passing
     * numbers as parameters.
     * <p/>
     * If the value can not be converted to the given target class, it will be returned unchanged.
     *
     * @param targetClass Class to which the number should be converted, if possible.
     * @param value       Number value to convert.
     * @return 'value' converted to an instance of 'targetClass'.
     */
    public static Object convertNumber(final Class targetClass, final Number value) {
        if (targetClass.equals(Double.class) || targetClass.equals(Double.TYPE))
            return value.doubleValue();
        if (targetClass.equals(Integer.class) || targetClass.equals(Integer.TYPE))
            return value.intValue();
        if (targetClass.equals(Long.class) || targetClass.equals(Long.TYPE))
            return value.longValue();
        if (targetClass.equals(Short.class) || targetClass.equals(Short.TYPE))
            return value.shortValue();
        if (targetClass.equals(Byte.class) || targetClass.equals(Byte.TYPE))
            return value.byteValue();
        if (targetClass.equals(Character.class) || targetClass.equals(Character.TYPE))
            return value.intValue();
        if (targetClass.equals(Float.class) || targetClass.equals(Float.TYPE))
            return value.floatValue();
        return value;
    }

    /**
     * Conveniance method used to invoke a JavaBeans-compatible setter method. As far as possible, this method
     * will take care of parameter conversions necessary for reflection access.
     *
     * @param setter Method to invoke.
     * @param target Target object on which 'setter' should be invoked.
     * @param value  Value to pass to the setter.
     * @throws IllegalAccessException    normal reflection exception
     * @throws InvocationTargetException normal reflection exception
     */
    public static void invokeSetter(final Method setter, final Object target, Object value)
            throws IllegalAccessException, InvocationTargetException {

        if (value instanceof Number)
            value = convertNumber(setter.getParameterTypes()[0], (Number) value);
        setter.invoke(target, value);
    }
}
