package net.sarcommand.swingextensions.filechooser;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * The AccessoryManager class enables you to add custom components to a JFileChooser instance. Other than JFileChooser's
 * setAccessory(JComponent) method, which forces you to use a platform dependent location for the accessory, this
 * class allows editing content on top of, below and on both sides of the FileView.
 * <p/>
 * Currently, two subclasses exist: The SimpleAccessoryManager will merely allow you install a custom component in those
 * locations. The MimeAccessoryManager on the other hand allows you to 'register' accessories for certain mime types,
 * for instance you could register an image preview for jpg and png types and a text preview for txt files. Please
 * refer to those subclasses for details.
 * <p/>
 * In order to use an AccessoryManager, all you have to do is tell it which JFileChooser to extend:
 * <code>
 * // create your file chooser
 * final JFileChooser myChooser = new JFileChooser();
 * // This conveniance constructor is equal to creating a manager through the default constructor and then invoking
 * // installAccessoryPane(chooser);
 * final SimpleAccessoryManager manager = new SimpleAccessoryManager(myChooser);
 * // Start adding your content
 * manager.setAccessory(new JLabel("Foo"), SwingConstants.SOUTH);
 * </code>
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
public abstract class AccessoryManager {
    /**
     * The panel used to extend the JFileChooser.
     */
    protected JPanel _accessoryPane;

    /**
     * A hashmap keeping track of the installed accessories.
     */
    protected HashMap<Integer, JComponent> _accessories;

    /**
     * The JFileChooser instance on which this manager was installed.
     */
    protected JFileChooser _chooser;

    /**
     * Creates a new AccessoryManager. Be aware that you will have to invoke installAccessoryPane(JFileChooser)
     * before using this instance.
     */
    public AccessoryManager() {
    }

    /**
     * Creates a new AccessoryManager and installs it on the given JFileChooser. Equal to invoking the default
     * contructor followed by installAccessoryPane(chooser).
     *
     * @param chooser file chooser on which this manager should be installed, non-null.
     */
    public AccessoryManager(final JFileChooser chooser) {
        this();
        installAccessoryPane(chooser);
    }

    /**
     * Installs the manager's accessory pane into the given file chooser. In order to do so, a rather crude hack
     * is being used, however, I am pretty sure that this implementation will work for all future swing
     * versions as well.
     *
     * @param chooser JFileChooser instance on which this manager should be installed
     */
    public void installAccessoryPane(final JFileChooser chooser) {
        _chooser = chooser;
        _accessoryPane = new JPanel();
        _accessories = new HashMap<Integer, JComponent>(4);

        final JScrollPane sPane = (JScrollPane)
                find(chooser, JScrollPane.class);
        sPane.setBorder(null);
        sPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        final JViewport newViewport = new JViewport();

        final JScrollPane copy = new JScrollPane();
        copy.setViewport(sPane.getViewport());

        _accessoryPane.setLayout(new BorderLayout());
        _accessoryPane.add(copy, BorderLayout.CENTER);
        _accessoryPane.setPreferredSize(_accessoryPane.getMinimumSize());

        newViewport.setView(_accessoryPane);
        sPane.setViewport(newViewport);
    }

    /**
     * Internal lookup method used to find the file view's JScrollPane.
     *
     * @param c     Component to search
     * @param clazz class to find
     * @return the component if it has been found, null otherwise.
     */
    protected JComponent find(final JComponent c, final Class<? extends JComponent> clazz) {
        final Component[] components = c.getComponents();
        for (Component comp : components) {
            if (comp.getClass().equals(clazz))
                return (JComponent) comp;
            if (comp instanceof JComponent) {
                final JComponent result = find((JComponent) comp, clazz);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
}
