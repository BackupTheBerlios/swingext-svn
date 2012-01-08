package net.sarcommand.swingextensions.internal;

/**
 * A dummy implementation which will be returned if the logging mechanisms are for some reason unavailable.
 * <p/>
 * <b>This is an internal class. You should never have to deal with it directly</b>
 * <p/>
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
public class SwingExtDummyLogger implements SwingExtLogger {
    public void init(final Class clazz) {

    }

    public void config(final String msg) {

    }

    public void config(final String msg, final Throwable t) {

    }

    public void debug(final String msg) {

    }

    public void debug(final String msg, final Throwable t) {

    }

    public void error(final String msg) {

    }

    public void error(final String msg, final Throwable t) {

    }

    public void fatal(final String msg) {

    }

    public void fatal(final String msg, final Throwable t) {

    }

    public void info(final String msg) {

    }

    public void info(final String msg, final Throwable t) {

    }

    public void trace(final String msg) {

    }

    public void trace(final String msg, final Throwable t) {

    }

    public void warn(final String msg) {

    }

    public void warn(final String msg, final Throwable t) {

    }

    public boolean isTraceEnabled() {
        return false;
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public boolean isWarnEnabled() {
        return false;
    }
}
