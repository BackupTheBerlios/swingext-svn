package net.sarcommand.swingextensions.progress;

/**
 * Interface used to bind a JProgressBar to some kind of event source indicating a tasks progress. Implementations may
 * for instance be used to track the progress of a SwingWorker instance or a ProgressMonitorInputStream.
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
 */
public interface ProgressBarBinding {
    /**
     * Invoked when the ProgressBarBinding should remove itself from the JProgressBar. Implementations have to make sure
     * that they uninstall properly, so that they may be garbage collected if no other references on them exist.
     */
    public void detach();
}
