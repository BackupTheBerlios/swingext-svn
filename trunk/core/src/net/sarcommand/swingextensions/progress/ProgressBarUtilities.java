package net.sarcommand.swingextensions.progress;

import javax.swing.*;

/**
 * Various smaller utility-/conveniance methods around the JProgressBar class.
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
public class ProgressBarUtilities {
    /**
     * Name of the client property used to store ProgressBarBindings on a JProgressBar.
     *
     * @see ProgressBarVariation
     */
    public static final String PROGRESS_BAR_BINDING = "SwingExt.ProgressBarUtilities.progressBarBinding";

    /**
     * Utility method which will make a JProgressBar instance monitor the progress of a SwingWorker. Basically, all this
     * class does is properly detach any previously attached ProgressBarBinding and install a new SwingWorkerBinding.
     *
     * @param progressBar ProgressBar used to monitor the progress of 'worker'.
     * @param worker      SwingWorker instance being monitored.
     * @see SwingWorkerProgressBarBinding
     */
    public static void attachToSwingWorker(final JProgressBar progressBar, final SwingWorker worker) {
        final ProgressBarVariation previousVariation =
                (ProgressBarVariation) progressBar.getClientProperty(PROGRESS_BAR_BINDING);
        if (previousVariation != null)
            previousVariation.detach();

        final SwingWorkerProgressBarBinding progressBarBinding = new SwingWorkerProgressBarBinding(progressBar, worker);
        progressBar.putClientProperty(PROGRESS_BAR_BINDING, progressBarBinding);
    }
}
