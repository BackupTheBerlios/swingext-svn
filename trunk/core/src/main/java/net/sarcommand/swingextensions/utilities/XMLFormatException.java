package net.sarcommand.swingextensions.utilities;

/**
 * Exception class used when an error related to xml externalization methods occurs.
 *
 * @see XMLExternalizable
 */
public class XMLFormatException extends Exception {

    public XMLFormatException() {
        super();
    }

    public XMLFormatException(String message) {
        super(message);
    }

    public XMLFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLFormatException(Throwable cause) {
        super(cause);
    }
}
