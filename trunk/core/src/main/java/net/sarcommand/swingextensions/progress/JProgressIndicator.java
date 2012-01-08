package net.sarcommand.swingextensions.progress;

import javax.swing.*;

/**
 * This class implements a simple, ajax-like progress indicator. Other than the JProgressBar, this widget will not
 * display a percentage of a process which has been completed, but merely indicates that something is happening at all.
 * You might want to use a JProgressIndicator over a JProgressBar when the progress being made can't easily be measured
 * or you want to indicate background activity (and, of course, if you think that the 'indeterminate' state of the
 * JProgressBar is ugly :-).
 * <p/>
 * By default, the progress indicator will look like the most common ajax progress indicators. You can install different
 * looks by providing an according UI class. See the ProgressIndicatorUI class for details.
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
public class JProgressIndicator extends JComponent {
    public static final String UI_KEY = "ProgressIndicatorUI";

    /**
     * Delay after which the indicator should be updated while indicating that progress is being made.
     */
    protected int _updateDelay;

    /**
     * Whether or not the indicator is currently indicating progress.
     */
    protected boolean _indicatingProgress;

    public JProgressIndicator() {
        _updateDelay = 50;
        _indicatingProgress = false;
        updateUI();
    }

    protected void setUI(final ProgressIndicatorUI newUI) {
        if (newUI == null)
            throw new IllegalArgumentException("Parameter 'newUI' must not be null!");
        super.setUI(newUI);
    }

    public String getUIClassID() {
        return UI_KEY;
    }

    public void updateUI() {
        if (UIManager.get(getUIClassID()) == null)
            setUI(new DefaultProgressIndicatorUI());
        else
            setUI(UIManager.getUI(this));
    }

    public void setIndicatingProgress(final boolean indicatingProgress) {
        _indicatingProgress = indicatingProgress;
        if (indicatingProgress) {
            _indicatingProgress = true;
            getUI().startProgress();
        } else {
            _indicatingProgress = false;
            getUI().stopProgress();
        }
    }

    public boolean isIndicatingProgress() {
        return _indicatingProgress;
    }

    public int getUpdateDelay() {
        return _updateDelay;
    }

    public void setUpdateDelay(final int updateDelay) {
        _updateDelay = updateDelay;
    }

    protected ProgressIndicatorUI getUI() {
        return (ProgressIndicatorUI) ui;
    }
}
