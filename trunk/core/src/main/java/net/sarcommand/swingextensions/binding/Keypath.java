package net.sarcommand.swingextensions.binding;

import java.beans.PropertyChangeListener;

/**
 * Keypaths are the central elements employed by the swingext library to get and set properties using reflection. A
 * keypath is created using a list of property names, contatenated in a string and separated by dots. Starting from a
 * given entry point object, the keypath class will try to resolve those properties one by one.
 * <p/>
 * Consider, for instance, a class Person, which exposes two properies: 'age' of type int and 'father' of type Person.
 * Starting from any person instance (let's call him Bob), the keypath "father.father.age" can then be used to set or
 * get this Bob's grandfather's age. Still, the field for Bob's father may not be initialized. Obviously, there is no
 * way to access his grandfather (and therefore retrieve the age property), so the get(Object) method will return null
 * in this case. Trying to set the age for Bob's grandfather will consequently result in an exception to be thrown. You
 * can check in advance if all the properties on the way to the last one are present (read: the keypath can be resolved)
 * by invoking the canResolve(Object) method, passing Bob as the methods argument.
 * <p/>
 * In order to obtain the elements of a keypath, the class will first try to find a bean convention getter or setter,
 * e.g. a public Person getFather() for the 'father' property. If this method can not be found, the keypath will then
 * look for a public field. If this field is not present either, the keypath will then attempt to find a protected,
 * package-local or private method and set it accessible, using the current authentication context. If this fails as
 * well, the keypath will as a last attempt look for a protected, package-local or private field and once again try to
 * make it accessible. If the field is not available either, a MalformedKeyPath exception will be thrown.
 * <p/>
 * A word of caution considering performance: The keypath class will attempt to cache accessors for the different
 * keypath elements to speed up both the get and set method. However, even with caching, resolving a long key path
 * requires several reflection based accessor calls, which are still significantly slower than normal method access. For
 * almost ui-related applications, the keypath implementation should be fast enough, but there are definitely exceptions
 * (for instance when using keypaths to render large tables). Therefore, keypath should be used with a certain caution.
 * <p/>
 * <hr/> Copyright 2006-2009 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class Keypath<T> {
    private final String _stringRepresentation;
    private final boolean _ignoreAccessControl;
    private final String[] _properties;

    /**
     * Creates a Keypath from the given path string. You can specify whether access control should be ignored.
     *
     * @param keypath             A string representation of the keypath being created.
     * @param ignoreAccessControl Whether the normal java access control should be ignored. Defaults to true.
     */
    public Keypath(final String keypath, final boolean ignoreAccessControl) {
        _stringRepresentation = keypath;
        _ignoreAccessControl = ignoreAccessControl;
        _properties = keypath.split("\\.");
    }

    /**
     * Creates a Keypath from the given path string.
     *
     * @param keypath A string representation of the keypath being created.
     */
    public Keypath(final String keypath) {
        this(keypath, true);
    }

    /**
     * Returns the value of the last element of this key path, or null if the key path can not be resolved. You can use
     * the canResolve(Object) method to find out whether the last element in the path is null or a previous one could
     * not be obtained.
     *
     * @param entryPoint The entry point from which this key path will be resolved.
     * @return the value of the last element of this key path, or null if the key path can not be resolved.
     * @throws MalformedKeypathException if the keypath is malformed
     * @throws KeypathAccessException    If an exception occured while using one the accessors.
     */
    public T get(final Object entryPoint) throws KeypathAccessException, MalformedKeypathException {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");

        final Object lastElement = resolve(entryPoint);
        if (lastElement == null)
            return null;

        final Class clazz = lastElement.getClass();
        final KeypathElement accessor = KeypathElementCache.getElement(clazz,
                _properties[_properties.length - 1], _ignoreAccessControl);
        return (T) accessor.get(lastElement);
    }

    /**
     * Resolves this keypath and attempts to set the last value. If the keypath is not resolvable, a
     * KeypathNotResolvableException will be thrown.
     *
     * @param entryPoint the entry point from which this keypath will be resolved.
     * @param value      The value to set for the last element.
     * @throws MalformedKeypathException if the keypath is malformed
     * @throws KeypathAccessException    If an exception occured while using one the accessors.
     */
    public void set(final Object entryPoint, final T value) {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");

        final Object lastElement = resolve(entryPoint);
        if (lastElement == null)
            throw new KeypathAccessException("Could not set value for keypath " + _stringRepresentation + ", keypath " +
                    "could not be resolved");

        final Class clazz = lastElement.getClass();
        final KeypathElement accessor = KeypathElementCache.getElement(clazz, _properties[_properties.length - 1],
                _ignoreAccessControl);
        accessor.set(lastElement, value);
    }

    /**
     * Returns whether this key path can be resolved to its last element. A key path is considered resolvable if all
     * elements except the last one can be obtained and are non-null. If set(Object, T) is invoked and the key path is
     * not resolvable, a KeypathNotResolvableException will be thrown. If get(Object) returns null, you can use this
     * method to check whether the last element or a previous one is null .
     *
     * @param entryPoint the entry point from which the keypath would be resolveed.
     * @return whether this key path can be resolved to its last element.
     */
    public boolean canResolve(final Object entryPoint) {
        final Object lastElement = resolve(entryPoint);
        return lastElement != null;
    }

    /**
     * Returns whether a set(...)-operation can be performed on this Keypath if it is applied to the given entry point.
     * If this method returns false, a subsequent call to {@link net.sarcommand.swingextensions.binding.Keypath#set(Object,
     * Object)} will result in an exception.
     *
     * @param entryPoint The entry point this keypath would be applied to in a subsequent set operation.
     * @return whether a set(...)-operation can be performed on this Keypath if it is applied to the given entry point.
     */
    public boolean canSet(final Object entryPoint) {
        final Object lastElement = resolve(entryPoint);
        if (lastElement == null)
            return false;
        final Class clazz = lastElement.getClass();
        final KeypathElement accessor = KeypathElementCache.getElement(clazz, _properties[_properties.length - 1],
                _ignoreAccessControl);
        return accessor.canPerformSet();
    }

    /**
     * Resolves the key path to the last KeypathElement.
     *
     * @param entryPoint The entry point for resolving the keypath.
     * @return the last KeypathElement.
     * @throws MalformedKeypathException If the keypath is malformed, meaning that required accessors for a property on
     *                                   the path could not be found.
     */
    protected Object resolve(final Object entryPoint) throws MalformedKeypathException {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");

        Class clazz;
        KeypathElement currentElement;
        Object runner = entryPoint;
        for (int i = 0; i < _properties.length - 1; i++) {
            final String property = _properties[i];
            clazz = runner.getClass();
            currentElement = KeypathElementCache.getElement(clazz, property, _ignoreAccessControl);
            runner = currentElement.get(runner);
            if (runner == null)
                return null;
        }
        return runner;
    }

    /**
     * Returns the KeypathElements that make up this path, starting at a given entry point.
     *
     * @param entryPoint the entry point to resolve elements for.
     * @return the KeypathElements that make up this path, starting at a given entry point.
     */
    protected KeypathElement[] resolveElements(final Object entryPoint) {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");

        Class clazz;
        Object runner = entryPoint;
        final KeypathElement[] elements = new KeypathElement[_properties.length];
        for (int i = 0; i < _properties.length; i++) {
            clazz = runner.getClass();
            elements[i] = KeypathElementCache.getElement(clazz, _properties[i], _ignoreAccessControl);
            runner = elements[i].get(runner);
            if (runner == null)
                break;
        }
        return elements;
    }

    /**
     * This method is used internally to resolve the values (the result of get()-calls) of all elements within this
     * keypath.
     *
     * @param entryPoint The entry point this keypath is applied to.
     * @return an object[] containing the values of all keypath elements within this path.
     */
    protected Object[] resolveValues(final Object entryPoint) {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");

        Class clazz;
        Object runner = entryPoint;
        KeypathElement element;
        final Object[] values = new Object[_properties.length + 1];
        values[0] = entryPoint;
        for (int i = 0; i < _properties.length; i++) {
            clazz = runner.getClass();
            element = KeypathElementCache.getElement(clazz, _properties[i], _ignoreAccessControl);
            runner = element.get(runner);
            values[i + 1] = runner;
            if (runner == null)
                break;
        }
        return values;
    }

    /**
     * Returns whether a KeypathObserver can be added to this path. In order to do so, all elements along the path have
     * to support PropertyChangeListeners.
     *
     * @param entryPoint The entry point which is to observe.
     * @return whether a KeypathObserver can be added to this path
     */
    public boolean isObservable(final Object entryPoint) {
        final KeypathElement[] keypathElements = resolveElements(entryPoint);
        for (KeypathElement element : keypathElements)
            if (!element.isObservable())
                return false;
        return true;
    }

    /**
     * Creates a KeypathObserver, monitoring the given entry point object and notifying the specified
     * PropertyChangeListener of changes along the path.
     *
     * @param entryPoint The entry point being monitored.
     * @param delegate   A PropertyChangeListener instance being notified of changes along the path.
     * @return a KeypathObserver instance monitoring this Keypath.
     */
    public KeypathObserver createObserver(final Object entryPoint, final PropertyChangeListener delegate) {
        if (entryPoint == null)
            throw new IllegalArgumentException("Parameter 'entryPoint' must not be null!");

        return new KeypathObserver(entryPoint, this, delegate);
    }

    /**
     * Returns this keypath's hashcode and string representation.
     *
     * @return this keypath's hashcode and string representation.
     */
    public String toString() {
        return "Keypath@" + hashCode() + "[" + _stringRepresentation + "]";
    }

    /**
     * Returns the value class of this Keypath's last element. Effectively, this method returns the class of a value
     * returned by a call to get(Object), or the parameter expected when invoking set(Object, Object).
     *
     * @param entryPoint The object to which the keypath is being applied.
     * @return the value class of this Keypath's last element.
     */
    public Class getValueClass(final Object entryPoint) {
        final KeypathElement[] keypathElements = resolveElements(entryPoint);
        return keypathElements[keypathElements.length - 1].getValueClass();
    }
}
