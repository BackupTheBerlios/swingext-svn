package net.sarcommand.swingextensions.actions;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ReflectedAction extends ManagedAction {
    private Object _target;
    private String _methodName;

    public ReflectedAction(final Object identifier, final Object target, final String methodName) {
        super(identifier);
        if (target == null)
            throw new IllegalArgumentException("Parameter 'target' must not be null!");
        if (methodName == null)
            throw new IllegalArgumentException("Parameter 'methodName' must not be null!");
        if (!(SwingExtUtil.hasMethod(target, methodName) || SwingExtUtil.hasMethod(target, methodName, Object.class,
                ActionEvent.class)))
            throw new IllegalArgumentException("Method " + methodName + " could not be found on proxy target " + target);
        _target = target;
        _methodName = methodName;
    }

    public void actionPerformed(ActionEvent e) {
        Method method = SwingExtUtil.getMethod(_target, _methodName, Object.class, ActionEvent.class);
        if (method != null) {
            try {
                method.invoke(_target, getIdentifier(), e);
                return;
            } catch (Exception e1) {
                throw new RuntimeException("Could not access method " + _methodName + " on target " + _target, e1);
            }
        }
        method = SwingExtUtil.getMethod(_target, _methodName);
        if (method != null) {
            try {
                method.invoke(_target);
                return;
            } catch (IllegalAccessException e1) {
                throw new RuntimeException("Could not access method " + _methodName + " on target " + _target, e1);
            } catch (IllegalArgumentException e1) {
                throw new RuntimeException("Could not access method " + _methodName + " on target " + _target, e1);
            } catch (InvocationTargetException e1) {
                throw new RuntimeException("Could not access method " + _methodName + " on target " + _target, e1);
            }
        }

        throw new RuntimeException("Could not access method " + _methodName + " on target " + _target);
    }
}
