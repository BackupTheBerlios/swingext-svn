package net.sarcommand.swingextensions.formatters;

import java.text.ParseException;

/**
 * Formatter implementations convert java objects to their string representation and vice versa. They are primarily
 * used to store values in the preferences or serialize them to xml.
 */
public interface Formatter<T> {
    /**
     * Implement this method to convert a string representation into a java object. If the string representation is
     * invalid or malformed you should throw a ParseException with the offset of the first illegal character.
     *
     * @param stringRepresentation The string representation of the object to convert. Non-null.
     * @return the java object corresponding to the given string representation.
     * @throws ParseException if the given string representation is invalid or malformed.
     */
    public T convertToValue(final String stringRepresentation) throws ParseException;

    /**
     * Converts the given value into its string representation.
     *
     * @param value value to convert.
     * @return a parseable string representation of the given value.
     */
    public String convertToString(final T value);
}
