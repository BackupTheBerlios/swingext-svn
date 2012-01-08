package net.sarcommand.swingextensions.progress;

import java.util.EventListener;

/**
 * Event listener used to monitor an arbitrary long-running task in order to be notified when progress has been made.
 * This class works analogously to all other standard java event classes.
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
 *
 * @see net.sarcommand.swingextensions.progress.ProgressEvent
 */
public interface ProgressListener extends EventListener {
    /**
     * Notifies the listener implementation that progress has been made.
     *
     * @param event The generated ProgressEvent instance.
     */
    public void progressMade(final ProgressEvent event);
}
