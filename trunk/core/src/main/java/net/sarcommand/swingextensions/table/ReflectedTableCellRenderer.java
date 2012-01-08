package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.misc.CellRendererUtility;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * A reflection based cell renderer implementation which will automatically pass the value to be rendered to a given
 * component. All you have to specify is the renderer component and the name of the property which should be set.
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
public class ReflectedTableCellRenderer implements TableCellRenderer {
    protected JComponent _renderer;
    protected Keypath _keypath;

    public ReflectedTableCellRenderer(final JComponent renderer, final String keypath) {
        initialize(renderer, keypath);
    }

    protected void initialize(final JComponent renderer, final String keypath) {
        if (renderer == null)
            throw new IllegalArgumentException("Parameter 'renderer' must not be null!");
        if (keypath == null)
            throw new IllegalArgumentException("Parameter 'keypath' must not be null!");

        _renderer = renderer;
        _keypath = new Keypath(keypath);
    }

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                   final boolean hasFocus, final int row, final int column) {
        try {
            _keypath.set(_renderer, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not access setter using keypath " + _keypath.toString(), e);
        }
        CellRendererUtility.adaptToTable(_renderer, table, isSelected, hasFocus, row, column);

        return _renderer;
    }
}