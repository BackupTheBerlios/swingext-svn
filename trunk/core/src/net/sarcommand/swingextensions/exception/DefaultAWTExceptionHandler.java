package net.sarcommand.swingextensions.exception;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class DefaultAWTExceptionHandler extends AWTExceptionHandler {
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

    public boolean handle(Throwable t) {
        getDialog().display(null, __message, t);
        return true;
    }

    public void install() {
        AWTExceptionHandler.clearHandlers();
        AWTExceptionHandler.addHandler(this);
    }
}
