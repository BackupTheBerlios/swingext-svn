package net.sarcommand.swingextensions.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Provides a default implementation for FileFilter instances which will filter by looking at the file's
 * extension. This is a mere conveniance class to keep you the efford from writing an anonymous
 * implementation every time you require an extension-based filter.
 * <p/>
 * todo add demo code
 * <p/>
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
public class ExtensionFileFilter extends FileFilter {
    private final String[] _extensions;
    private final String _description;
    private final boolean _acceptDirectories;

    /**
     * Create a new FileFilter instance which will accept a file if it's extension is among the given list of
     * valid extensions. All directories will be accepted as well.
     *
     * @param extensions  A single extension or a list of extensions to be accepted by this FileFilter.
     * @param description The description which should be displayed in the FileChooser when using this filter.
     */
    public ExtensionFileFilter(final String description, final String... extensions) {
        this(description, true, extensions);
    }

    /**
     * Create a new FileFilter instance which will accept a file if it's extension is among the given list of
     * valid extensions. If required you can have the filter deny all directories by setting the 'acceptDirectories'
     * parameter accordingly.
     *
     * @param extensions        A single extensions or a list of suffixes to be accepted by this FileFilter.
     * @param description       The description which should be displayed in the FileChooser when using this filter.
     * @param acceptDirectories Whether directories should be accepted by this filter.
     */
    public ExtensionFileFilter(final String description, final boolean acceptDirectories, final String... extensions) {
        _extensions = extensions;
        _description = description;
        _acceptDirectories = acceptDirectories;
    }

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f) {
        if (_acceptDirectories && f.isDirectory())
            return true;
        final String fileName = f.getName();
        final int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex <= 0)
            return false;

        final String extension = fileName.substring(dotIndex, fileName.length());
        for (final String ext : _extensions)
            if (extension.equalsIgnoreCase(ext))
                return true;
        return false;
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     *
     * @see javax.swing.filechooser.FileView#getName
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Returns the file name extensions which will be accepted by this filter.
     *
     * @return One or more extensions which will be accepted by this filter.
     */
    public String[] getExtensions() {
        return _extensions;
    }

    /**
     * Returns whether this filter will accept directories.
     *
     * @return true if filter accepts directories, false otherwise.
     */
    public boolean doesAcceptDirectories() {
        return _acceptDirectories;
    }
}
