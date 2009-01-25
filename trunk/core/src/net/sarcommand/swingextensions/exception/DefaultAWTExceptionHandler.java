package net.sarcommand.swingextensions.exception;

/**
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
