package net.sarcommand.swingextensions.exception;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class DefaultAWTExceptionHandler extends AWTExceptionHandler {
    private static ExceptionDialog __dialog;
    private static String __message;


    public static void setMessage(final String message) {
        __message = message;
    }

    public static void setTitle(String title) {
        getDialog().setTitle(title);
    }

    public static ExceptionDialog getDialog() {
        if (__dialog == null)
            __dialog = new ExceptionDialog();
        return __dialog;
    }

    public void handle(Throwable t) {
        getDialog().display(null, __message, t);
    }
}
