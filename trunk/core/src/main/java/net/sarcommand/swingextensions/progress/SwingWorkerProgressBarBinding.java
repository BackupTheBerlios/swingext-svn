package net.sarcommand.swingextensions.progress;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class will bind a given JProgressBar to a SwingWorker instance, making it monitor the worker's overall progress.
 * When the worker is started, the progress bar will be set to indeterminate until it receives the first progress update
 * event. Once the worker completes, the binding will automatically detach itself from the worker to be garbage
 * collected.
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
class SwingWorkerProgressBarBinding implements PropertyChangeListener, ProgressBarVariation {
    private JProgressBar _progressBar;
    private SwingWorker _swingWorker;
    private boolean _indeterminate;

    public SwingWorkerProgressBarBinding(final JProgressBar progressBar, final SwingWorker swingWorker) {
        _progressBar = progressBar;
        _progressBar.setMinimum(0);
        _progressBar.setMaximum(100);
        _indeterminate = true;

        _swingWorker = swingWorker;
        _swingWorker.addPropertyChangeListener(this);
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            if (_indeterminate) {
                _indeterminate = false;
                _progressBar.setIndeterminate(false);
            }
            _progressBar.setValue(_swingWorker.getProgress());
        } else if ("state".equals(evt.getPropertyName())) {
            if (_swingWorker.getState().equals(SwingWorker.StateValue.DONE)) {
                _progressBar.setValue(0);
                detach();
            } else if (_swingWorker.getState().equals(SwingWorker.StateValue.STARTED)) {
                _indeterminate = true;
                _progressBar.setIndeterminate(true);
            }
        }
    }

    public void detach() {
        _swingWorker.removePropertyChangeListener(this);
    }

    public JProgressBar getAlteredComponent() {
        return _progressBar;
    }
}
