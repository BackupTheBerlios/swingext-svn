package net.sarcommand.swingextensions.exception;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class DefaultAWTExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionDialog __dialog;
    private static String __message;

    public void setMessage(final String message) {
        __message = message;
    }

    public void setTitle(String title) {
        getDialog().setTitle(title);
    }

    public ExceptionDialog getDialog() {
        if (__dialog == null)
            __dialog = new ExceptionDialog();
        return __dialog;
    }

    public void uncaughtException(final Thread t, final Throwable e) {
        getDialog().display(null, __message, e);
    }

    public void install() {
        AWTExceptionHandler.clearHandlers();
        AWTExceptionHandler.addHandler(this);
    }
}
