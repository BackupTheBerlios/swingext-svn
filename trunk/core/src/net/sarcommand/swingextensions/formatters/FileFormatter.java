package net.sarcommand.swingextensions.formatters;

import java.io.File;
import java.text.ParseException;

/**
 * A formatter implementation working on file instances. Conversion is done by simply using the file's absolute path.
 */
public class FileFormatter implements Formatter<File> {
    /**
     * Returns the absolute path of the given file.
     *
     * @param value file to convert.
     * @return the absolute path of the given file.
     */
    public String convertToString(final File value) {
        if (value == null)
            throw new IllegalArgumentException("Parameter 'value' must not be null!");

        return value.getAbsolutePath();
    }

    /**
     * Returns a file pointing to the given absolute path.
     *
     * @param stringRepresentation absolute path of a file.
     * @return a file pointing to the given absolute path.
     * @throws ParseException Will not be thrown by this implementation.
     */
    public File convertToValue(final String stringRepresentation) throws ParseException {
        if (stringRepresentation == null)
            throw new IllegalArgumentException("Parameter 'stringRepresentation' must not be null!");
        return new File(stringRepresentation);
    }
}
