package net.sarcommand.swingextensions.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public abstract class AWTExceptionHandler {
    protected static List<AWTExceptionHandler> __handlers =
            Collections.synchronizedList(new ArrayList<AWTExceptionHandler>(2));

    public static void addHandler(final AWTExceptionHandler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Parameter 'handler' must not be null!");

        __handlers.add(handler);
        System.setProperty("sun.awt.exception.handler", ExceptionHandlerProxy.class.getName());
    }

    public static void removeHandler(final AWTExceptionHandler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Parameter 'handler' must not be null!");

        __handlers.remove(handler);
    }

    public static void clearHandlers() {
        __handlers.clear();
    }

    public static List<AWTExceptionHandler> getHandlers() {
        return Collections.unmodifiableList(__handlers);
    }

    public abstract boolean handle(final Throwable t);

    public static class ExceptionHandlerProxy {
        public void handle(final Throwable t) {
            for (AWTExceptionHandler handler : AWTExceptionHandler.getHandlers()) {
                if (handler.handle(t))
                    break;
            }
        }
    }
}
