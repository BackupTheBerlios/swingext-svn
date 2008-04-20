package net.sarcommand.swingextensions.binding;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class KeypathAccessException extends RuntimeException {
    public KeypathAccessException(final String message) {
        super(message);
    }

    public KeypathAccessException(final String message, final Throwable t) {
        super(message, t);
    }
}
