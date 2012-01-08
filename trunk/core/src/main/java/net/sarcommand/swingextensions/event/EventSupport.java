package net.sarcommand.swingextensions.event;

import javax.swing.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * This class provides a simple way of handling lists of event listeners, analogous to the PropertyChangeSupport class,
 * but for all kinds of listeners. You obtain an EventSupport by using the create(Class) method: <code> final
 * EventSupport<ActionListener> actionListeners = EventSupport.create(ActionListener.class); </code> You can then
 * (un-)register listeners using the according addListener(T) and removeListener(T) methods, T being the type of
 * listener supported by the EventSupport object obtain. If you want to propagate an event to all registered listener
 * instances, you can use the delegate() method. It will return a proxy of the correct class, which will delegate all
 * method calls to each of the listeners. For instance, an EventSupport created to hold ActionListeners would be used
 * like following: <code> final ActionEvent event = new ActionEvent(...); actionListeners.delegate().actionPerformed(event);
 * </code> By doing so, event propagation becomes a one-liner. Since the delegate() method returns a full implementation
 * of the listener interface, listeners with multiple methods are supported as well.
 * <p/>
 * Be aware that there is another implementation available which uses weak references to store listeners. If you prefer
 * this approach, you might want to use a <code>WeakEventSupport</code>.
 * <p/>
 * <p/>
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
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
 * @see WeakEventSupport
 */
public class EventSupport<T extends EventListener> {
    /**
     * Creates a new EventSupport for the given listener class.
     *
     * @param listenerClass Class of the listener type this instance is being created for.
     * @return a new EventSupport for the given listener class.
     */
    public static <T extends EventListener> EventSupport<T> create(final Class<T> listenerClass) {
        return new EventSupport<T>(listenerClass, false);
    }

    /**
     * Creates a new EventSupport for the given listener class. You may also specify whether all events should
     * automatically be delegated to the event dispatch thread.
     *
     * @param listenerClass Class of the listener type this instance is being created for.
     * @param dispatchOnEDT Whether events should automatically be dispatched on the EDT.
     * @return a new EventSupport for the given listener class.
     */
    public static <T extends EventListener> EventSupport<T> create(final Class<T> listenerClass,
                                                                   final boolean dispatchOnEDT) {
        return new EventSupport<T>(listenerClass, dispatchOnEDT);
    }

    /**
     * Determines whether this instance will automatically dispatch all events on the EDT.
     */
    protected final boolean _dispatchingOnEDT;

    /**
     * The proxy object used to delegate all method invocations to the list of registered listeners.
     */
    protected T _delegate;

    /**
     * The list of listeners being hold.
     */
    protected Vector<T> _elements;

    /**
     * Internal constructor. You should use the factory method create(Class) to obtain a new instance.
     *
     * @param listenerClass    The listener type supported by the new instance.
     * @param dispatchingOnEDT Whether events will automatically be dispatched on the EDT.
     */
    protected EventSupport(final Class<T> listenerClass, final boolean dispatchingOnEDT) {
        _dispatchingOnEDT = dispatchingOnEDT;
        final InvocationHandler handler = new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                final Runnable r = new Runnable() {
                    public void run() {
                        try {
                            if (proxy == _delegate) {
                                for (int i = _elements.size() - 1; i >= 0; i--)
                                    method.invoke(_elements.get(i), args);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                if (!isDispatchingOnEDT() || SwingUtilities.isEventDispatchThread())
                    r.run();
                else
                    SwingUtilities.invokeLater(r);
                return null;
            }
        };
        _delegate = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{listenerClass}, handler);
        _elements = new Vector<T>();
    }

    /**
     * Adds a listener to this list.
     *
     * @param listener Listener to register.
     */
    public void addListener(final T listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _elements.add(listener);
    }

    /**
     * Removes a listener from this list.
     *
     * @param listener Listener to remove.
     */
    public void removeListener(final T listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        for (Iterator<T> it = _elements.iterator(); it.hasNext(); ) {
            if (it.next().equals(listener)) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Returns whether this instance will autmatically dispatch all events on the EDT.
     *
     * @return whether this instance will autmatically dispatch all events on the EDT.
     */
    public boolean isDispatchingOnEDT() {
        return _dispatchingOnEDT;
    }

    /**
     * This method returns a proxy object which will mimic the listener class this instance has been created for. The
     * returned value will appear to be an EventListener of the specified type and delegate all method calls to the list
     * of registered listeners.
     *
     * @return a proxy object which will mimic the listener class this instance has been created for.
     */
    public T delegate() {
        return _delegate;
    }

    /**
     * Returns a copy of the event listener list held by this instance.
     *
     * @return a copy of the event listener list held by this instance.
     */
    public Collection<T> getListeners() {
        return new ArrayList(_elements);
    }
}
