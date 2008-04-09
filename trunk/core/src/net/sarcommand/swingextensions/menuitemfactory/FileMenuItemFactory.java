package net.sarcommand.swingextensions.menuitemfactory;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * Factory class used to create JMenuItem entries for files. The created items will use the file's name as text, the
 * absolute path as tooltip and obtain their icon from the file system view.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
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
public class FileMenuItemFactory implements MenuItemFactory<File> {
    public FileMenuItemFactory() {
    }

    /**
     * Creates a suitable JMenuItem for the given value.
     *
     * @param value value for which an item should be created.
     * @return a suitable JMenuItem for the given value.
     */
    public JMenuItem createItem(final File value) {
        if (value == null)
            throw new IllegalArgumentException("Parameter 'value' must not be null!");

        final JMenuItem item = new JMenuItem(value.getName());
        final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        item.setIcon(fileSystemView.getSystemIcon(value));
        item.setToolTipText(value.getAbsolutePath());

        return item;
    }
}
