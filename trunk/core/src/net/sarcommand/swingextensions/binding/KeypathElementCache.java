package net.sarcommand.swingextensions.binding;

import net.sarcommand.swingextensions.utilities.Pair;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * This class is used by Keypath instances to cache KeypathElements. Since the reflection-based lookup of accessors can
 * be pretty expensive, this greatly increases overall performance of keypaths.
 * <p/>
 * This class is a thread-save singleton.
 * <p/>
 * <b>This is an internal class. You should never have to deal with it directly</b>
 */
public class KeypathElementCache {
    private static SoftReference<HashMap<Pair<Class, String>, KeypathElement>> __elementCache;

    /**
     * Returns the cached KeypathElement for accessing the given property on the specified class. If no cached
     * KeypathElement can be found, a new one will be created.
     *
     * @param clazz    class being accessed by the Keypathelement.
     * @param property name of the property being accessed.
     * @return the cached KeypathElement for accessing the given property on the specified class.
     */
    public static synchronized KeypathElement getElement(final Class clazz, final String property) {
        HashMap<Pair<Class, String>, KeypathElement> cache = __elementCache == null ? null : __elementCache.get();

        if (cache == null) {
            cache = new HashMap<Pair<Class, String>, KeypathElement>(64);
            __elementCache = new SoftReference<HashMap<Pair<Class, String>, KeypathElement>>(cache);
        }

        final Pair<Class, String> key = new Pair<Class, String>(clazz, property);
        KeypathElement element = cache.get(key);

        if (element == null) {
            element = new KeypathElement(clazz, property);
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
