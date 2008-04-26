package net.sarcommand.swingextensions.event;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class basically performs the same tasks as the EventSupport (see this class for usage detail), however, it uses
 * weak references to hold the listeners. Whether or not you should use weak or hard references for holding listener
 * lists is still a source of dispute for programmers. One the one hand, hard references are probably one of the leading
 * causes of memory leaks in java applications (and as such extremely hard to track down). On the other hand, the use of
 * weak listeners prevents you from installing anonymous listeners and may lead to unpredictable results if you lack the
 * required coding discipline. The only proper answer to this question is that the decision is a matter of usage context
 * and personal preference.
 *
 * @see EventSupport
 *      <p/>
 *      <hr/> Copyright 2006-2008 Torsten Heup
 *      <p/>
 *      Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 *      with the License. You may obtain a copy of the License at
 *      <p/>
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      <p/>
 *      Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *      on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *      for the specific language governing permissions and limitations under the License.
 */
public class WeakEventSupport<T> {
    protected T _delegate;

    /**
     * The list of listeners being hold.
     */
    protected Vector<WeakReference<T>> _elements;

    /**
     * Internal constructor. You should use the factory method create(Class) to obtain a new instance.
     *
     * @param listenerClass The listener type supported by the new instance.
     */
    protected WeakEventSupport(final Class<T> listenerClass) {
        final InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (proxy == _delegate) {
                    for (int i = _elements.size() - 1; i >= 0; i--)
                        method.invoke(_elements.get(i), args);
                }
                return null;
            }
        };
        _delegate = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{listenerClass}, handler);
        _elements = new Vector<WeakReference<T>>();
    }

    /**
     * Adds a listener to this list.
     *
     * @param listener Listener to register.
     */
    public void addListener(final T listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _elements.add(new WeakReference<T>(listener));
    }

    /**
     * Removes a listener from this list.
     *
     * @param listener Listener to remove.
     */
    public void removeListener(final T listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        for (Iterator<WeakReference<T>> it = _elements.iterator(); it.hasNext();) {
            final WeakReference<T> weakReference = it.next();
            final T element = weakReference.get();

            if (element == null || element.equals(listener)) {
                it.remove();
                break;
            }
        }
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
        final ArrayList arrayList = new ArrayList(_elements.size());
        for (Iterator<WeakReference<T>> it = _elements.iterator(); it.hasNext();) {
            final T listener = it.next().get();
            if (listener == null)
                it.remove();
            else
                arrayList.add(listener);
        }
        return arrayList;
    }
}
