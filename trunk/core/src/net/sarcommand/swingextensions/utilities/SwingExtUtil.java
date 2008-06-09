package net.sarcommand.swingextensions.utilities;

import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A collection of small utilities for component management and reflection handling.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class SwingExtUtil {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(SwingExtUtil.class);
    private static ExecutorService __executor = Executors.newCachedThreadPool();
    private static Thread.UncaughtExceptionHandler __uncaughtExceptionHandler;

    /**
     * Returns the uncaught exception handler which will be called if an error occurs within one of the worker threads.
     *
     * @return uncaught exception handler which will be called if an error occurs within one of the worker threads.
     */
    public static Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return __uncaughtExceptionHandler;
    }

    /**
     * Sets an uncaught exception handler which will be called if an error occurs within one of the worker threads.
     *
     * @param uncaughtExceptionHandler an uncaught exception handler which will be called if an error occurs within one
     *                                 of the worker threads.
     */
    public static void setUncaughtExceptionHandler(final Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        __uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    /**
     * Returns the parent window for the given component. Unlike the according method in JOptionPane (who the heck put
     * it there anyway?), this method will also resolve popup menus. If you invoke getWindowForComponent on an item in a
     * popup menu, it will return the window from which the popup was invoked rather than some anonymous instance.
     * Another important difference lies in the fact that this method will resolve to a window rather than a frame. When
     * using JOptionPane to resolve the frame for a component in a JDialog, you might get the shared root frame, which
     * most likely is not what you will want.
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
     * Returns the parent for the given component. Other than Component#getParent(), this method will work for popup
     * menus as well, returning the menu's invoker rather than its own window instance.
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
        return getMethod(target.getClass(), methodName, argumentTypes);
    }

    /**
     * Attempts to find a method with the given name and argument types on the specified class. Other than Class's
     * getMethod(String, Class...) method, this method will also return protected and private methods.
     *
     * @param clazz         Class supposed to offer the method being searched.
     * @param methodName    Name of the method.
     * @param argumentTypes The arguments found in the method signature, if any.
     * @return a method with the given name and argument types on the specified class, if any.
     */
    public static synchronized Method getMethod(final Class clazz, final String methodName,
                                                final Class... argumentTypes) {
        try {
            return clazz.getMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            /* Ok, so there's no public method... */
        }

        Class runner = clazz;
        while (runner != null) {
            try {
                return runner.getDeclaredMethod(methodName, argumentTypes);
            } catch (NoSuchMethodException e) {
                /* No luck here either */
            }
            runner = runner.getSuperclass();
        }

        /* Still no luck, means there is no suitable method */
        return null;
    }

    /**
     * Returns a getter method for the given property, or null if there is none.
     *
     * @param target       Target exposing the getter.
     * @param propertyName The getter's property.
     * @return a getter method for the given property, or null if there is none.
     */
    public static Method getGetter(final Object target, final String propertyName) {
        if (target == null)
            throw new IllegalArgumentException("Parameter 'target' must not be null!");
        if (propertyName == null)
            throw new IllegalArgumentException("Parameter 'propertyName' must not be null!");
        return getGetter(target.getClass(), propertyName);
    }

    /**
     * Retrieves the a field with the specified name from the given class. Other than Class's getField(String) method,
     * this method will also return protected and private fields.
     *
     * @param clazz     Class from which the field should be obtained.
     * @param fieldName Name of the field.
     * @return field with the specified name.
     */
    public static Field getField(final Class clazz, final String fieldName) {
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            /* Means there's no public field, let's keep looking */
        }

        Class runner = clazz;
        while (runner != null) {
            try {
                return runner.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                /* No luck here either */
            }
            runner = runner.getSuperclass();
        }

        return null;
    }

    /**
     * Returns a suitable getter for the given property from the specified class. This method will try the different
     * bean naming conventions getProperty, isProperty and hasProperty, and will return the first method it finds.
     *
     * @param clazz        Class to obtain getter from.
     * @param propertyName Name of the property returned by the getter.
     * @return a suitable getter for the given property from the specified class
     */
    public static Method getGetter(final Class clazz, final String propertyName) {
        if (clazz == null)
            throw new IllegalArgumentException("Parameter 'clazz' must not be null!");
        if (propertyName == null)
            throw new IllegalArgumentException("Parameter 'propertyName' must not be null!");

        Method m;
        m = getGetterWithPrefix(clazz, propertyName, "get");
        if (m != null)
            return m;
        m = getGetterWithPrefix(clazz, propertyName, "is");
        if (m != null)
            return m;
        m = getGetterWithPrefix(clazz, propertyName, "has");
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
        return getSetter(target.getClass(), propertyName);
    }

    public static synchronized Method getSetter(final Class targetClass, final String propertyName) {
        String setterName = "set" + Character.toUpperCase(propertyName.charAt(0));
        if (setterName.length() > 1)
            setterName += propertyName.substring(1);

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
    protected static Method getGetterWithPrefix(final Class target, final String property, final String prefix) {
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
        __executor.execute(r);
    }

    /**
     * Invokes the given runnable in an asychronous worker thread.
     * <p/>
     * IMPORTANT: Since this method will return a Future, exceptions will not be thrown as usual. Instead, the Future's
     * get() method will return an ExcecutionException. This behaviour is determined by the ExecutorService. If you want
     * to preserve normal exception handling, you will have to wrap your code in try/catch.
     *
     * @param c Callable to invoke.
     * @return A future instance representing the task.
     */
    public static Future invokeAsWorker(final Callable c) {
        return __executor.submit(c);
    }

    /**
     * Invokes the given method on an asynchronous worker thread.
     * <p/>
     * IMPORTANT: Since this method will return a Future, exceptions will not be thrown as usual. Instead, the Future's
     * get() method will return an ExcecutionException. This behaviour is determined by the ExecutorService. If you want
     * to preserve normal exception handling, you will have to wrap your code in try/catch.
     *
     * @param target     Target object exposing the specified method.
     * @param methodName Method to invoke.
     * @param args       Arguments to pass to the method.
     * @return A future instance representing the task.
     */
    public static Future invokeAsWorker(final Object target, final String methodName, final Object... args) {
        final Class[] types = new Class[args.length];
        for (int i = 0; i < types.length; i++)
            types[i] = args[i].getClass();
        final Method m = getMethod(target, methodName, types);
        if (m == null)
            throw new RuntimeException("No such method: " + methodName + '(' + Arrays.toString(types) + ") found for target" +
                    "class " + target.getClass().getName());
        if (!m.isAccessible())
            m.setAccessible(true);
        return invokeAsWorker(new Callable() {
            public Object call() throws Exception {
                try {
                    return m.invoke(target, args);
                } catch (Exception e) {
                    __log.error("Error invoking method " + m.getName(), e);
                    if (__uncaughtExceptionHandler != null)
                        __uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
                    else
                        throw new RuntimeException(e);
                    return e;
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
        if (!m.isAccessible())
            m.setAccessible(true);
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
     * explicitly casting to a more general type, e.g. from double to int), just the internal type representation. While
     * this is unnecessary while using normal java code, reflection based access to method parameters is a bit more
     * difficult. As far as possible, this method will prevent the ArgumentMismatch error when passing numbers as
     * parameters.
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
     * Conveniance method used to invoke a JavaBeans-compatible setter method. As far as possible, this method will take
     * care of parameter conversions necessary for reflection access.
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

    /**
     * Returns whether a component is a direct descendant of another one. This method will traverse the component
     * hierarchy, following the 'parent' relation for normal components and the 'invoker' relation for JPopupMenu
     * instances.
     *
     * @param supposedParent The supposed ancestor component
     * @param supposedChild  The supposed child component
     * @return if 'supposedChild' is a descendant of 'supposedParent'
     */
    public static boolean isDescendant(final Component supposedParent, final Component supposedChild) {
        if (supposedParent == null)
            throw new IllegalArgumentException("Parameter 'supposedParent' must not be null!");
        if (supposedChild == null)
            throw new IllegalArgumentException("Parameter 'supposedChild' must not be null!");

        Component runner = supposedChild;
        while (runner != null) {
            if (runner == supposedParent)
                return true;
            if (runner instanceof JPopupMenu)
                runner = ((JPopupMenu) runner).getInvoker();
            else
                runner = runner.getParent();
        }
        return false;
    }
}
