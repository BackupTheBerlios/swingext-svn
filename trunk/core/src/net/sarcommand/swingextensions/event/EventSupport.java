package net.sarcommand.swingextensions.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class EventSupport<T extends EventListener> {
    public static <T extends EventListener> EventSupport<T> create(final Class<T> listenerClass) {
        return new EventSupport<T>(listenerClass);
    }

    protected T _delegate;
    protected Vector<T> _elements;

    protected EventSupport(final Class<T> listenerClass) {
        final InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (proxy == _delegate) {
                    for (T ref : _elements)
                        method.invoke(ref, args);
                }
                return null;
            }
        };
        _delegate = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{listenerClass}, handler);
        _elements = new Vector<T>();
    }

    public void addListener(final T listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _elements.add(listener);
    }

    public void removeListener(final T listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        for (Iterator<T> it = _elements.iterator(); it.hasNext();) {
            if (it.next().equals(listener)) {
                it.remove();
                break;
            }
        }
    }

    public T delegate() {
        return _delegate;
    }

    public Collection<T> getListeners() {
        return Collections.unmodifiableCollection(_elements);
    }
}
