package net.sarcommand.swingextensions.utilities;

import java.awt.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ColorUtilities {
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

    public static Color fromHexString(final String string) {
        if (string == null)
            throw new IllegalArgumentException("Parameter 'string' must not be null!");

        return new Color(Integer.parseInt(string, 16));
    }

    public static Color lighter(final Color color, final int rgbOffset) {
        if (color == null)
            throw new IllegalArgumentException("Parameter 'color' must not be null!");
        final int red = Math.min(color.getRed() + rgbOffset, 255);
        final int green = Math.min(color.getGreen() + rgbOffset, 255);
        final int blue = Math.min(color.getBlue() + rgbOffset, 255);
        return new Color(red, green, blue, color.getAlpha());
    }

    public static Color darker(final Color color, final int rgbOffset) {
        if (color == null)
            throw new IllegalArgumentException("Parameter 'color' must not be null!");
        final int red = Math.max(color.getRed() - rgbOffset, 0);
        final int green = Math.max(color.getGreen() - rgbOffset, 0);
        final int blue = Math.max(color.getBlue() - rgbOffset, 0);
        return new Color(red, green, blue, color.getAlpha());
    }

    public static void main(String[] args) {
        System.out.println(asHexString(Color.BLUE));
        System.out.println(asHexString(Color.RED));
        System.out.println(asHexString(Color.GREEN));
        System.out.println(fromHexString(asHexString(Color.BLUE)).equals(Color.BLUE));
        System.out.println(fromHexString(asHexString(Color.RED)).equals(Color.RED));
        System.out.println(fromHexString(asHexString(Color.GREEN)).equals(Color.GREEN));
    }
}
