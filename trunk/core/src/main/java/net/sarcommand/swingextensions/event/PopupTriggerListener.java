package net.sarcommand.swingextensions.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A MouseListener implementation which will listen for events which should trigger a popup menu in the appropriate,
 * platform-specific manner.
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
public abstract class PopupTriggerListener extends MouseAdapter {
    public void mouseClicked(final MouseEvent e) {
        if (e.isPopupTrigger())
            popupTriggered(e);
    }

    public void mousePressed(final MouseEvent e) {
        if (e.isPopupTrigger())
            popupTriggered(e);
    }

    public void mouseReleased(final MouseEvent e) {
        if (e.isPopupTrigger())
            popupTriggered(e);
    }

    /**
     * Invoked when the popup menu is being triggered.
     *
     * @param e the triggering event
     */
    public abstract void popupTriggered(final MouseEvent e);
}
