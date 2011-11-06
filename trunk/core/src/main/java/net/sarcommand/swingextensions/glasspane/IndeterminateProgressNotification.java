package net.sarcommand.swingextensions.glasspane;

import net.sarcommand.swingextensions.progress.JProgressIndicator;

import javax.swing.*;
import java.awt.*;

/**
 * A simple GlassPaneNotification to be used when operations of indeterminate length are being performed. Other than the
 * DefaultProgressNotification, this implementation will display a JProgressIndicator while the glass pane is visible.
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
public class IndeterminateProgressNotification extends GlassPaneNotification {
    protected JLabel _label;
    protected JProgressIndicator _progressIndicator;

    public IndeterminateProgressNotification() {
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

        _progressIndicator = new JProgressIndicator();
        _progressIndicator.setIndicatingProgress(true);
    }

    protected void initLayout() {
        setLayout(new GridBagLayout());
        add(_progressIndicator, new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(_label, new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {

    }

    public void setText(final String text) {
        _label.setText(text);
    }
}
