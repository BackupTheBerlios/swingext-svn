package net.sarcommand.swingextensions.applicationsupport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
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
         * Tells the ImageCache to return an empty dummy image if an image could not be loaded.
         */
        ON_ERROR_RETURN_DUMMY
    }

    /**
     * Caches images which have been loaded before.
     * HashMap<String:filename, BufferedImage:actualImage>
     */
    private static HashMap<String, BufferedImage> __cache;

    /**
     * Holds the currently set error policy which should be followed if an image could not be accessed.
     */
    private static ErrorPolicy __errorPolicy = ErrorPolicy.ON_ERROR_THROW_EXCEPTION;

    /**
     * Holds a collection of locations which should be searched for images.
     */
    private static LinkedList<URL> __additionalSearchPaths = new LinkedList<URL>();

    /**
     * Loads the specified image and returns it as an instance of ImageIcon.
     *
     * @param iconName Name of the icon to load.
     * @return ImageIcon holding the specified image.
     */
    public static ImageIcon loadIcon(final String iconName) {
        return new ImageIcon(loadImage(iconName));
    }

    /**
     * Loads the specified image.
     *
     * @param imageName Name of the image which should be loaded.
     * @return BufferedImage holding the specified image.
     */
    public static BufferedImage loadImage(final String imageName) {
        if (__cache == null)
            __cache = new HashMap<String, BufferedImage>();

        if (__cache.containsKey(imageName))
            return __cache.get(imageName);

        final LinkedList<URL> searchPath = new LinkedList<URL>();
        final URL resource = ImageCache.class.getClassLoader().getResource(imageName);
        if (resource == null) {
            for (URL path : __additionalSearchPaths) {
                try {
                    searchPath.add(new URL(path, imageName));
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Malformed search path: " + path + " + " + imageName);
                }
            }
        } else
            searchPath.add(resource);

        BufferedImage image = loadImage(searchPath);
        if (image == null) {
            switch (__errorPolicy) {
                case ON_ERROR_RETURN_DUMMY:
                    return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                case ON_ERROR_THROW_EXCEPTION:
                    final StringBuilder searchPathBuffer = new StringBuilder(256);
                    for (URL path : searchPath)
                        searchPathBuffer.append(path.toString()).append(' ');
                    throw new RuntimeException("Could not access resource, searchpath is " + searchPathBuffer);
            }
        }

        __cache.put(imageName, image);
        return image;
    }

    /**
     * Adds another location which should be searched for images upon lookup.
     *
     * @param searchPath URL pointing to a location which should be searched for images.
     */
    public static void addAdditionalSearchPath(final URL searchPath) {
        __additionalSearchPaths.add(searchPath);
    }

    /**
     * Removes a location which should be searched for images upon lookup.
     *
     * @param searchPath URL pointing to a location which should be searched for images.
     */
    public static void removeAdditionalSearchPath(final URL searchPath) {
        __additionalSearchPaths.remove(searchPath);
    }

    /**
     * Returns a collection of the locations which will be searched for images in addition to the current class path.
     *
     * @return Collection<URL:searchLocations>
     */
    public static Collection<URL> getAdditionalSearchPaths() {
        return Collections.unmodifiableCollection(__additionalSearchPaths);
    }

    /**
     * Sets the current error policy, telling the ImageCache how to react if an image could not be loaded.
     *
     * @param errorPolicy see the ErrorPolicy enum
     */
    public static void setErrorPolicy(final ErrorPolicy errorPolicy) {
        __errorPolicy = errorPolicy;
    }

    /**
     * Returns the current error policy specifying the ImageCache's behaviour if an image could not be loaded.
     *
     * @return see the ErrorPolicy enum
     */
    public ErrorPolicy getErrorPolicy() {
        return __errorPolicy;
    }

    /**
     * Internal method used to load an image from a set of possible locations. The first location providing a matching
     * image will be chosen.
     *
     * @param searchPaths Collection<URL:searchPath>
     * @return the image if it could be loaded, null otherwise.
     */
    protected static BufferedImage loadImage(final Collection<URL> searchPaths) {
        for (URL url : searchPaths) {
            try {
                return ImageIO.read(url);
            } catch (IOException e) {
                //ignored at this point
            }
        }
        return null;
    }
}
