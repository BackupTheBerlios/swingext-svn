package net.sarcommand.swingextensions.binding;

import net.sarcommand.swingextensions.utilities.Triple;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * This class is used by Keypath instances to cache KeypathElements. Since the reflection-based lookup of accessors can
 * be pretty expensive, this greatly increases overall performance of keypaths.
 * <p/>
 * This class is a thread-save singleton.
 * <p/>
 * <b>This is an internal class. You should never have to deal with it directly</b>
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
 */
public class KeypathElementCache {
    private static SoftReference<HashMap<Triple<Class, String, Boolean>, KeypathElement>> __elementCache;

    /**
     * Returns the cached KeypathElement for accessing the given property on the specified class. If no cached
     * KeypathElement can be found, a new one will be created.
     *
     * @param clazz               class being accessed by the Keypathelement.
     * @param property            name of the property being accessed.
     * @param ignoreAccessControl whether the normal access control mechanisms should be ignored
     * @return the cached KeypathElement for accessing the given property on the specified class.
     */
    public static synchronized KeypathElement getElement(final Class clazz, final String property,
                                                         final Boolean ignoreAccessControl) {
        HashMap<Triple<Class, String, Boolean>, KeypathElement> cache =
                __elementCache == null ? null : __elementCache.get();

        if (cache == null) {
            cache = new HashMap<Triple<Class, String, Boolean>, KeypathElement>(64);
            __elementCache = new SoftReference<HashMap<Triple<Class, String, Boolean>, KeypathElement>>(cache);
        }

        final Triple<Class, String, Boolean> key = new Triple<Class, String, Boolean>(clazz, property,
                ignoreAccessControl);

        KeypathElement element = cache.get(key);

        if (element == null) {
            element = new KeypathElement(clazz, property, ignoreAccessControl);
            cache.put(key, element);
        }

        return element;
    }

    /**
     * This class can not be instanciated.
     */
    private KeypathElementCache() {
    }
}
