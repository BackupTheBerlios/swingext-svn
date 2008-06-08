package net.sarcommand.swingextensions.filechooser;

import javax.swing.*;
import static javax.swing.SwingConstants.*;
import java.awt.*;

/**
 * The SimpleAccessoryManager allows you to install custom accessories into a JFileChooser at different locations. This
 * implementation reserves four fields for your extensions, north, south, east and west. Only one accessory can be
 * installed into one of those fields at a time, however, the components you install may be arbitrarily complex.
 * <p/>
 * <hr/> Copyright 2006 Torsten Heup
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
public class SimpleAccessoryManager extends AccessoryManager {
    /**
     * Creates a new AccessoryManager. Be aware that you will have to invoke installAccessoryPane(JFileChooser) before
     * using this instance.
     */
    public SimpleAccessoryManager() {
    }

    /**
     * Creates a new AccessoryManager and installs it on the given JFileChooser. Equal to invoking the default
     * contructor followed by installAccessoryPane(chooser).
     *
     * @param chooser file chooser on which this manager should be installed, non-null.
     */
    public SimpleAccessoryManager(final JFileChooser chooser) {
        super(chooser);
    }

    public FileChooserAccessory setAccessory(final FileChooserAccessory accessory, final int position) {
        if (_chooser == null)
            throw new IllegalStateException("This manager has not been installed on a file chooser yet." +
                    "Invoke installAccessoryPane(JFileChooser) before using this instance.");
        if (!(position == NORTH || position == SOUTH || position == WEST || position == EAST)) {
            throw new IllegalArgumentException("Illegal position: Expected one of SwingConstants#NORTH, SOUTH," +
                    "WEST or EAST, got " + position);
        }

        final FileChooserAccessory previousAccessory = _fileChooserAccesories.get(position);
        setAccessory(accessory.getAccessoryComponent(), position);
        _fileChooserAccesories.put(position, accessory);

        if (previousAccessory != null)
            previousAccessory.dispose();

        return previousAccessory;
    }

    /**
     * Add a component to the given location in the file chooser. If an accessory has already been installed to this
     * location, it will be replaced and returned by this method.
     *
     * @param accessory component to install.
     * @param position  desired location of the component. Has to be one of <li>SwingConstants.NORTH</li>
     *                  <li>SwingConstants.SOUTH</li> <li>SwingConstants.EAST</li> <li>SwingConstants.WEST</li>
     * @return The previously installed accessory, if applicable.
     */
    public JComponent setAccessory(final JComponent accessory, final int position) {
        if (_chooser == null)
            throw new IllegalStateException("This manager has not been installed on a file chooser yet." +
                    "Invoke installAccessoryPane(JFileChooser) before using this instance.");
        if (!(position == NORTH || position == SOUTH || position == WEST || position == EAST)) {
            throw new IllegalArgumentException("Illegal position: Expected one of SwingConstants#NORTH, SOUTH," +
                    "WEST or EAST, got " + position);
        }

        JComponent previousAccessory = null;
        if (_accessoryComponents.get(position) != null) {
            previousAccessory = _accessoryComponents.get(position);
            _accessoryPane.remove(previousAccessory);
            _accessoryComponents.remove(position);
        }

        if (accessory != null) {
            switch (position) {
                case SwingConstants.EAST:
                    _accessoryPane.add(accessory, BorderLayout.EAST);
                    break;
                case SwingConstants.NORTH:
                    _accessoryPane.add(accessory, BorderLayout.NORTH);
                    break;
                case SwingConstants.WEST:
                    _accessoryPane.add(accessory, BorderLayout.WEST);
                    break;
                case SwingConstants.SOUTH:
                    _accessoryPane.add(accessory, BorderLayout.SOUTH);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal position, expected SwingConstants#EAST, WEST, SOUTH " +
                            "or NORTH, got " + position);
            }
        }
        _accessoryPane.revalidate();
        _accessoryComponents.put(position, accessory);
        return previousAccessory;
    }
}
