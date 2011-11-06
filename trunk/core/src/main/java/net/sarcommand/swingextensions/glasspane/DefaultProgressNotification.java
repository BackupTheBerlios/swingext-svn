package net.sarcommand.swingextensions.glasspane;

import javax.swing.*;
import java.awt.*;

/**
 * Implements a simple GlassPaneNotification which displays a JLabel (indicating the current activity) and a progress
 * bar. This class implements the ProgressListener interface, so you can directly attach it to any task supporting this
 * event type.
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
public class DefaultProgressNotification extends GlassPaneNotification {
    protected JLabel _label;
    protected JProgressBar _progressbar;

    public DefaultProgressNotification() {
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _label = new JLabel("", JLabel.CENTER);
        _label.setFont(_label.getFont().deriveFont(Font.BOLD));
        _label.setForeground(getForeground());

        _progressbar = new JProgressBar();
        _progressbar.setIndeterminate(true);
    }

    protected void initLayout() {
        setLayout(new GridLayout(2, 1));
        add(_label);
        add(_progressbar);
    }

    protected void setupEventHandlers() {

    }

    /**
     * Sets the text displayed by the text label.
     *
     * @param text the text to be displayed by the text label.
     */
    public void setText(final String text) {
        _label.setText(text);
    }

    /**
     * Sets the current progress made. This method expects a value between 0 and 1. For values < 0, the progress bar
     * will switch to indeterminate mode. For values > 1, an exception will be thrown.
     *
     * @param percentage the current progress made (between 0 and 1, values < 0 will turn the progress bar
     *                   indeterminate).
     */
    public void setProgress(final float percentage) {
        if (percentage < 0)
            _progressbar.setIndeterminate(true);
        else {
            _progressbar.setIndeterminate(false);
            _progressbar.setValue((int) (percentage * 100));
        }
    }

    @Override
    public void setForeground(final Color fg) {
        super.setForeground(fg);
        if (_label != null)
            _label.setForeground(fg);
    }
}
