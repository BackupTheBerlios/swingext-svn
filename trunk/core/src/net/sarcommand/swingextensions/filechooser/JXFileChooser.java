package net.sarcommand.swingextensions.filechooser;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

/**
 * This class implements a few basic extensions to the JFileChooser widget.
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
public class JXFileChooser extends JFileChooser {
    public static JComponent find(final JComponent c, final Class<?
            extends JComponent> clazz) {
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

    protected JPanel _accessoryPane;
    protected HashMap<Integer, JComponent> _accessories;

    public JXFileChooser() {
        super();
        installAccessoryPane();
    }

    public JXFileChooser(final File currentDirectory) {
        super(currentDirectory);
        installAccessoryPane();
    }

    public JXFileChooser(final File currentDirectory, final FileSystemView fsv) {
        super(currentDirectory, fsv);
        installAccessoryPane();
    }

    public JXFileChooser(final String currentDirectoryPath) {
        super(currentDirectoryPath);
        installAccessoryPane();
    }

    public JXFileChooser(final String currentDirectoryPath, final FileSystemView fsv) {
        super(currentDirectoryPath, fsv);
        installAccessoryPane();
    }

    public JXFileChooser(final FileSystemView fsv) {
        super(fsv);
        installAccessoryPane();
    }

    /**
     * Installs a proper accessory pane which allows installing additional controls in different locations. In order to
     * do so, the installed UI will be torn apart and put back together in a very very crude fashion. Still, it appears
     * to work fine on all UIs I've seen so far.
     */
    protected void installAccessoryPane() {
        _accessoryPane = new JPanel();
        _accessories = new HashMap<Integer, JComponent>(4);

        final JScrollPane sPane = (JScrollPane)
                find(this, JScrollPane.class);
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
     * Installs an accessory in one of the four locations EAST, SOUTH, WEST and NORTH (relative to the file view).
     * You can install any JComponent, however it is up to you to ensure that the resulting dialog does not become
     * unbearably ugly.
     *
     * @param component Accessory component to install.
     * @param position  Position at which the accessory should be installed: SwingConstants.NORTH, SwingConstants.WEST,
     *                  SwingConstants.SOUTH or SwingConstants.EAST
     */
    public void setAccessory(final JComponent component, final int position) {
        if (_accessories.containsKey(position))
            _accessoryPane.remove(_accessories.get(position));
        switch (position) {
            case SwingConstants.EAST:
                _accessoryPane.add(component, BorderLayout.EAST);
                break;
            case SwingConstants.NORTH:
                _accessoryPane.add(component, BorderLayout.NORTH);
                break;
            case SwingConstants.WEST:
                _accessoryPane.add(component, BorderLayout.WEST);
                break;
            case SwingConstants.SOUTH:
                _accessoryPane.add(component, BorderLayout.SOUTH);
                break;
            default:
                throw new IllegalArgumentException("Illegal position, expected SwingConstants#EAST, WEST, SOUTH " +
                        "or NORTH, got " + position);
        }
        _accessories.put(position, component);
    }
}
