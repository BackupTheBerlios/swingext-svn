package net.sarcommand.swingextensions.progress;

import net.sarcommand.swingextensions.event.ExtendedEventObject;

/**
 * An event indicating that progress has been made by an arbitrary operation. This is the EventObject subclass
 * corresponding to the ProgressListener class.
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @see net.sarcommand.swingextensions.event.ExtendedEventObject
 */
public class ProgressEvent extends ExtendedEventObject {
    /**
     * Constant used to denote that no reliable information can be given on the at the current time.
     */
    public static float PROGRESS_INDETERMINATE = -1;

    /**
     * An optional message passed along with the progress event. May be used to specifiy the current activity.
     */
    private final String _message;

    /**
     * The current completion percentage. Should be 0 <= percentage <= 1, while negative values denote an indeterminate
     * progress.
     */
    private final float _completionPercentage;

    /**
     * Time stamp taken then the event was created.
     */
    private final long _when;

    /**
     * Creates a new ProgressEvent. The 'when' property will be set to the current system time.
     *
     * @param source               Source in which the event originated.
     * @param message              An optional message to be passed along with the progress event.
     * @param completionPercentage The current completion percentage. Should be 0 <= percentage <= 1, while negative
     *                             values denote an indeterminate progress.
     */
    public ProgressEvent(final Object source, final String message, final float completionPercentage) {
        this(source, message, completionPercentage, System.currentTimeMillis());
    }

    /**
     * Creates a new ProgressEvent.
     *
     * @param source               Source in which the event originated.
     * @param message              An optional message to be passed along with the progress event.
     * @param completionPercentage The current completion percentage. Should be 0 <= percentage <= 1, while negative
     *                             values denote an indeterminate progress.
     * @param when                 A time stamp indicating when the event occured.
     */
    public ProgressEvent(final Object source, final String message, final float completionPercentage, final long when) {
        super(source);
        _message = message;
        _completionPercentage = completionPercentage;
        _when = when;
    }

    /**
     * Returns the current completion percentage. Should be 0 <= percentage <= 1, while negative values denote an
     * indeterminate progress.
     *
     * @return The current completion percentage. Should be 0 <= percentage <= 1, while negative values denote an
     *         indeterminate progress.
     */
    public float getCompletionPercentage() {
        return _completionPercentage;
    }

    /**
     * Returns an optional message passed along with the progress event. May be used to specifiy the current activity.
     *
     * @return An optional message passed along with the progress event. May be used to specifiy the current activity.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Returns when the event occured (in miliseconds).
     *
     * @return when the event occured (in miliseconds).
     */
    public long getWhen() {
        return _when;
    }
}
