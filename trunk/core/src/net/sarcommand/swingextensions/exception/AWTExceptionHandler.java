package net.sarcommand.swingextensions.exception;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public abstract class AWTExceptionHandler {
    public AWTExceptionHandler() {

    }

    public void install() {
        System.setProperty("sun.awt.exception.handler", getClass().getName());
    }

    public abstract void handle(final Throwable t);
}
