package net.sarcommand.swingextensions.glasspane;

import javax.swing.*;
import java.awt.*;

/**
 * Instances of this class can be used to display a notification for the user on top of a glass pane. You can use this
 * base class as a JPanal and add custom content to it, or you might refer to one of the existing subclasses. <hr/>
 * Copyright 2006-2012 Torsten Heup
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
public class GlassPaneNotification extends JPanel {
    public GlassPaneNotification() {
        setOpaque(false);
        setBackground(new Color(128, 128, 128, 90));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
    }
}
