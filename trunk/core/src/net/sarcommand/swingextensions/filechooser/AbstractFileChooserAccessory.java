package net.sarcommand.swingextensions.filechooser;

import javax.swing.*;

/**
 * Abstract conveniance implementation for prebuilt file chooser accessories. <hr/> Copyright 2006-2008 Torsten Heup
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
public abstract class AbstractFileChooserAccessory implements FileChooserAccessory {
    private int _position;
    private JComponent _accessoryComponent;

    public AbstractFileChooserAccessory() {
    }

    public AbstractFileChooserAccessory(final JComponent accessoryComponent, final int position) {
        _accessoryComponent = accessoryComponent;
        _position = position;
    }

    /**
     * @see FileChooserAccessory#getAccessoryComponent()
     */
    public JComponent getAccessoryComponent() {
        return _accessoryComponent;
    }

    /**
     * Sets the component enclosed by this accessory.
     *
     * @param accessoryComponent the component enclosed by this accessory.
     */
    protected void setAccessoryComponent(final JComponent accessoryComponent) {
        _accessoryComponent = accessoryComponent;
    }

    /**
     * Returns the position this accessory is installed at. The returned value will be one of the following: <li>{@link
     * javax.swing.SwingConstants#NORTH}</li> <li>{@link javax.swing.SwingConstants#SOUTH}</li> <li>{@link
     * javax.swing.SwingConstants#EAST}</li> <li>{@link javax.swing.SwingConstants#WEST}</li>
     *
     * @return the position this accessory is installed at.
     */
    public int getPosition() {
        return _position;
    }

    /**
     * Sets the position this accessory should appear at. Has to be one of <li>{@link
     * javax.swing.SwingConstants#NORTH}</li> <li>{@link javax.swing.SwingConstants#SOUTH}</li> <li>{@link
     * javax.swing.SwingConstants#EAST}</li> <li>{@link javax.swing.SwingConstants#WEST}</li>
     *
     * @param position the position this accessory should appear at
     */
    public void setPosition(final int position) {
        _position = position;
    }

    /**
     * This method is merely defined as a conveniance and will not actually do anything.
     */
    public void dispose() {
    }
}
