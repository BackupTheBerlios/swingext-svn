package net.sarcommand.swingextensions.internal;

/**
 * Interface implemented by the logging facility used by the swingext library.
 * <p/>
 * <b>This is an internal class, you should not have to deal with it directly</b>
 * <p/>
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
public interface SwingExtLogger {
    public void init(final Class clazz);

    public void trace(final String msg);

    public void trace(final String msg, final Throwable t);

    public void debug(final String msg);

    public void debug(final String msg, final Throwable t);

    public void config(final String msg);

    public void config(final String msg, final Throwable t);

    public void info(final String msg);

    public void info(final String msg, final Throwable t);

    public void warn(final String msg);

    public void warn(final String msg, final Throwable t);

    public void error(final String msg);

    public void error(final String msg, final Throwable t);

    public void fatal(final String msg);

    public void fatal(final String msg, final Throwable t);
}
