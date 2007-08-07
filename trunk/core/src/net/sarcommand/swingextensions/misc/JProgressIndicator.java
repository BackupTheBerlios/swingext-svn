package net.sarcommand.swingextensions.misc;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * This class implements a simple, ajax-like progress indicator. Other than the JProgressBar, this widget will not
 * display a percentage of a process which has been completed, but merely indicates that something is happening at all.
 * You might want to use a JProgressIndicator over a JProgressBar when the progress being made can't easily be measured
 * or you want to indicate background activity (and, of course, if you think that the 'indeterminate' state of the
 * JProgressBar is ugly :-).
 * <p/>
 * By default, the progress indicator will look like the most common ajax progress indicators. You can install
 * different looks by providing an according UI class. See the ProgressIndicatorUI class for details.
 * <p/>
 * <hr/>
 * Copyright 2006 Torsten Heup
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
public class JProgressIndicator extends JComponent {
    public static final String UI_KEY = "ProgressIndicator.UI";

    /**
     * UI currently being used.
     */
    protected ProgressIndicatorUI _ui;

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
        _ui = (ProgressIndicatorUI) UIManager.getUI(this);
        _ui.installUI(this);
    }

    public void startProgress() {
        _indicatingProgress = true;
        _ui.startProgress();
    }

    public void stopProgress() {
        _indicatingProgress = false;
        _ui.stopProgress();
    }

    protected void setUI(final ComponentUI newUI) {
        if (newUI == null)
            throw new IllegalArgumentException("Parameter 'newUI' must not be null!");

        if (!(newUI instanceof ProgressIndicatorUI))
            throw new IllegalArgumentException("Attempting to install a wrong ui class, instance of ProgressIndicatorUI" +
                    "is required, found " + newUI.getClass().getName());

        _ui = (ProgressIndicatorUI) newUI;
        super.setUI(newUI);
    }

    public ProgressIndicatorUI getUI() {
        return _ui;
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

    public String getUIClassID() {
        if (!UIManager.getDefaults().containsKey(UI_KEY))
            UIManager.getDefaults().put(UI_KEY, DefaultProgressIndicatorUI.class.getName());
        return UI_KEY;
    }

    /**
     * Delegates the paint call to the installed UI class.
     *
     * @param g Graphics instance to use.
     */
    public void paint(final Graphics g) {
        getUI().paint(g, this);
    }
}
