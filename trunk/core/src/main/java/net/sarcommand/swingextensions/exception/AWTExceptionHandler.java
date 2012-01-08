package net.sarcommand.swingextensions.exception;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This utility class allows you to install an uncaught exception handler onto the AWTEventThread. It is recommendable
 * to do so in every application which has a gui, since runtime exceptions caused by ui interaction are almost
 * guaranteed to occur in every complex piece of software. If necessary, you can install multiple exception handlers at
 * the same time, for instance to notify loggers or display messages to the user.
 * <p/>
 * <pre>
 * AWTExceptionHandler.addHandler(new Thread.UncaughtExceptionHandler() {
 *      public void uncaughtException(final Thread t, final Throwable e) {
 *          __log.fatal("Uncaught runtime exception in AWT event thread: ",e);
 *      }
 * });
 * </pre>
 * <hr/> Copyright 2006-2012 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public abstract class AWTExceptionHandler {
    protected static boolean __handlerInstalled;
    protected static List<Thread.UncaughtExceptionHandler> __handlers =
            Collections.synchronizedList(new ArrayList<Thread.UncaughtExceptionHandler>(2));

    /**
     * Adds an UncaughtExceptionHandler to the AWTEvent thread.
     *
     * @param handler UncaughtExceptionHandler to be notified when a runtime exception occurs.
     */
    public static void addHandler(final Thread.UncaughtExceptionHandler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Parameter 'handler' must not be null!");

        __handlers.add(handler);

        if (!__handlerInstalled) {
            if (SwingUtilities.isEventDispatchThread())
                Thread.currentThread().setUncaughtExceptionHandler(new ExceptionHandlerProxy());
            else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            Thread.currentThread().setUncaughtExceptionHandler(new ExceptionHandlerProxy());
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Removes a previously installed UncaughtExceptionHandler from the AWTEventThread.
     *
     * @param handler handler instance to remove.
     */
    public static void removeHandler(final Thread.UncaughtExceptionHandler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Parameter 'handler' must not be null!");

        __handlers.remove(handler);
    }

    /**
     * Removes all installed UncaughtExceptionHandlers from the AWTEventThread.
     */
    public static void clearHandlers() {
        __handlers.clear();
    }

    /**
     * Returns the list of UncaughtExceptionHandlers installed on the AWTEvent thread.
     *
     * @return the list of UncaughtExceptionHandlers installed on the AWTEvent thread.
     */
    public static List<Thread.UncaughtExceptionHandler> getHandlers() {
        return Collections.unmodifiableList(__handlers);
    }

    /**
     * Manages a list of UncaughtExceptionHandlers. If an exception occurs, they will be notified in the order in which
     * they were installed.
     * <p/>
     * <hr/> Copyright 2006-2012 Torsten Heup
     * <p/>
     * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
     * with the License. You may obtain a copy of the License at
     * <p/>
     * http://www.apache.org/licenses/LICENSE-2.0
     * <p/>
     * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
     * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     * the specific language governing permissions and limitations under the License.
     */
    public static class ExceptionHandlerProxy implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(final Thread t, final Throwable e) {
            for (Thread.UncaughtExceptionHandler handler : AWTExceptionHandler.getHandlers())
                handler.uncaughtException(t, e);
        }
    }
}
