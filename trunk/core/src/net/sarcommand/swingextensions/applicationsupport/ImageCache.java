package net.sarcommand.swingextensions.applicationsupport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Creates a static cache for images and icons which should help to reduce an application's loading time.
 * Images as well as icons should be referenced using their file names. By default, the cache will attempt to load
 * the respective image from the class path. Additionally, further search paths can be specified, pointing to
 * local folders in the file system or web resources alike.
 * <p/>
 * You can specify which action the cache should take if an image could not be located in any of the search locations
 * using the <code>ErrorPolicy</code> property. For now, you can choose whether an exception should be thrown or an
 * empty dummy image should be returned.
 * <p/>
 * <hr>
 * Copyright 2006 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ImageCache {
    /**
     * Constants used to instruct the ImageCache what to do if an image could not be located in the search path.
     */
    public static enum ErrorPolicy {
        /**
         * Tells the ImageCache to throw a RuntimeException if an image could not be loaded.
         */
        ON_ERROR_THROW_EXCEPTION,
        /**
         * Tells the ImageCache to return null if an image could not be loaded.
         */
        ON_ERROR_RETURN_NULL,
        /**
         * Tells the ImageCache to return an empty dummy image if an image could not be loaded.
         */
        ON_ERROR_RETURN_DUMMY
    }

    /**
     * Caches images which have been loaded before.
     * HashMap<String:filename, BufferedImage:actualImage>
     */
    private static HashMap<String, SoftReference<BufferedImage>> __cache;

    /**
     * Holds the currently set error policy which should be followed if an image could not be accessed.
     */
    private static ErrorPolicy __errorPolicy = ErrorPolicy.ON_ERROR_THROW_EXCEPTION;

    /**
     * Holds a collection of locations which should be searched for images.
     */
    private static LinkedList<URI> __additionalSearchPaths = new LinkedList<URI>();

    /**
     * Loads the specified image and returns it as an instance of ImageIcon.
     *
     * @param iconName Name of the icon to load.
     * @return ImageIcon holding the specified image.
     */
    public static synchronized ImageIcon loadIcon(final String iconName) {
        return new ImageIcon(loadImage(iconName));
    }

    /**
     * Loads the specified image using the given error policy and returns it as an instance of ImageIcon.
     *
     * @param iconName Name of the icon to load.
     * @param policy   error policy to apply when the icon could not be loaded.
     * @return ImageIcon holding the specified image.
     */
    public static synchronized ImageIcon loadIcon(final String iconName, final ErrorPolicy policy) {
        final ErrorPolicy prev = __errorPolicy;
        setErrorPolicy(policy);
        final ImageIcon icon = loadIcon(iconName);
        setErrorPolicy(prev);
        return icon;
    }

    /**
     * Loads the specified image.
     *
     * @param imageName Name of the image which should be loaded.
     * @return BufferedImage holding the specified image.
     */
    public static synchronized BufferedImage loadImage(final String imageName) {
        if (imageName == null)
            return imageNotLoaded(imageName, null);

        if (__cache == null)
            __cache = new HashMap<String, SoftReference<BufferedImage>>();

        if (__cache.containsKey(imageName)) {
            final BufferedImage cachedImage = __cache.get(imageName).get();
            if (cachedImage != null)
                return cachedImage;
        }

        final LinkedList<URI> searchPath = new LinkedList<URI>();
        final URL resource = ImageCache.class.getClassLoader().getResource(imageName);


        if (resource == null) {
            for (URI path : __additionalSearchPaths)
                searchPath.add(path.resolve(imageName));
        } else {
            try {
                searchPath.add(resource.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        BufferedImage image = loadImage(searchPath);
        if (image == null) {
            return imageNotLoaded(imageName, searchPath);
        }

        __cache.put(imageName, new SoftReference(image));
        return image;
    }

    /**
     * Loads the specified image using the given error policy and returns it as an instance of BufferedImage.
     *
     * @param imageName Name of the image to load.
     * @param policy    error policy to apply when the image could not be loaded.
     * @return BufferedImage holding the specified image.
     */
    public static synchronized BufferedImage loadImage(final String imageName, final ErrorPolicy policy) {
        final ErrorPolicy prev = __errorPolicy;
        setErrorPolicy(policy);
        final BufferedImage image = loadImage(imageName);
        setErrorPolicy(prev);
        return image;
    }

    protected static BufferedImage imageNotLoaded(final String imageName, final Collection<URI> searchPath) {
        switch (__errorPolicy) {
            case ON_ERROR_RETURN_NULL:
                return null;
            case ON_ERROR_RETURN_DUMMY:
                return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            case ON_ERROR_THROW_EXCEPTION:
                final StringBuilder searchPathBuffer = new StringBuilder(256);
                for (URI path : searchPath)
                    searchPathBuffer.append(path.toString()).append(' ');
                throw new RuntimeException("Could not access resource " + imageName + ", searchpath is " + searchPathBuffer);
            default:
                return null;
        }
    }

    /**
     * Adds another location which should be searched for images upon lookup.
     *
     * @param searchPath URL pointing to a location which should be searched for images.
     */
    public static synchronized void addAdditionalSearchPath(final URI searchPath) {
        __additionalSearchPaths.add(searchPath);
    }

    /**
     * Removes a location which should be searched for images upon lookup.
     *
     * @param searchPath URL pointing to a location which should be searched for images.
     */
    public static synchronized void removeAdditionalSearchPath(final URI searchPath) {
        __additionalSearchPaths.remove(searchPath);
    }

    /**
     * Returns a collection of the locations which will be searched for images in addition to the current class path.
     *
     * @return Collection<URL:searchLocations>
     */
    public static synchronized Collection<URI> getAdditionalSearchPaths() {
        return Collections.unmodifiableCollection(__additionalSearchPaths);
    }

    /**
     * Sets the current error policy, telling the ImageCache how to react if an image could not be loaded.
     *
     * @param errorPolicy see the ErrorPolicy enum
     */
    public static synchronized void setErrorPolicy(final ErrorPolicy errorPolicy) {
        __errorPolicy = errorPolicy;
    }

    /**
     * Returns the current error policy specifying the ImageCache's behaviour if an image could not be loaded.
     *
     * @return see the ErrorPolicy enum
     */
    public static synchronized ErrorPolicy getErrorPolicy() {
        return __errorPolicy;
    }

    /**
     * Internal method used to load an image from a set of possible locations. The first location providing a matching
     * image will be chosen.
     *
     * @param searchPaths Collection<URL:searchPath>
     * @return the image if it could be loaded, null otherwise.
     */
    protected static BufferedImage loadImage(final Collection<URI> searchPaths) {
        for (URI uri : searchPaths) {
            try {
                if (uri.isAbsolute())
                    return ImageIO.read(uri.toURL());
                InputStream stream = ImageCache.class.getClassLoader().getResourceAsStream(uri.toString());
                if (stream != null)
                    return ImageIO.read(stream);
                return ImageIO.read(new File(uri.toString()));
            } catch (IOException e) {
                //ignored at this point
            }
        }
        return null;
    }
}
