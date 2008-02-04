package net.sarcommand.swingextensions.utilities;

import java.awt.*;

/**
 * This class offers a few utility methods useful when handling Colors.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
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
public class ColorUtilities {
    /**
     * Returns a hex string representing the given color object.
     *
     * @param color color to be converted to hex string, non-null.
     * @return a hex string representing the given color.
     */
    public static String asHexString(final Color color) {
        if (color == null)
            throw new IllegalArgumentException("Parameter 'color' must not be null!");

        final StringBuilder builder = new StringBuilder(6);
        builder.append(Integer.toHexString(color.getRed()));
        if (builder.length() < 2)
            builder.insert(0, '0');
        builder.append(Integer.toHexString(color.getGreen()));
        if (builder.length() < 4)
            builder.insert(2, '0');
        builder.append(Integer.toHexString(color.getBlue()));
        if (builder.length() < 6)
            builder.insert(4, '0');

        return builder.toString();
    }

    /**
     * Parses a color instance from the given hex string.
     *
     * @param string string to parse color value from.
     * @return a color instance parsed from the given hex string.
     */
    public static Color fromHexString(final String string) {
        if (string == null)
            throw new IllegalArgumentException("Parameter 'string' must not be null!");

        return new Color(Integer.parseInt(string, 16));
    }

    /**
     * Returns a lighter version of the given color.
     *
     * @param color     Base color to use.
     * @param rgbOffset Offset by which the color should be lightened.
     * @return a lighter version of the given color.
     */
    public static Color lighter(final Color color, final int rgbOffset) {
        if (color == null)
            throw new IllegalArgumentException("Parameter 'color' must not be null!");
        final int red = Math.min(color.getRed() + rgbOffset, 255);
        final int green = Math.min(color.getGreen() + rgbOffset, 255);
        final int blue = Math.min(color.getBlue() + rgbOffset, 255);
        return new Color(red, green, blue, color.getAlpha());
    }

    /**
     * Returns a darker version of the given color.
     *
     * @param color     Base color to use.
     * @param rgbOffset Offset by which the color should be darkened.
     * @return a darker version of the given color.
     */
    public static Color darker(final Color color, final int rgbOffset) {
        if (color == null)
            throw new IllegalArgumentException("Parameter 'color' must not be null!");
        final int red = Math.max(color.getRed() - rgbOffset, 0);
        final int green = Math.max(color.getGreen() - rgbOffset, 0);
        final int blue = Math.max(color.getBlue() - rgbOffset, 0);
        return new Color(red, green, blue, color.getAlpha());
    }
}