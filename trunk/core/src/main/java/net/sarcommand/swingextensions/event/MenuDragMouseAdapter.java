package net.sarcommand.swingextensions.event;

import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;

/**
 * Conveniance adapter class for the MenuDragMouseListener interface.
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
public abstract class MenuDragMouseAdapter implements MenuDragMouseListener {
    public void menuDragMouseDragged(final MenuDragMouseEvent e) {
    }

    public void menuDragMouseEntered(final MenuDragMouseEvent e) {
    }

    public void menuDragMouseExited(final MenuDragMouseEvent e) {
    }

    public void menuDragMouseReleased(final MenuDragMouseEvent e) {
    }
}
