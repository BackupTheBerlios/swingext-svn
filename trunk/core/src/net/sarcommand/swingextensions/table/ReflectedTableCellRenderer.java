package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.lang.reflect.Method;

/**
 * A reflection based cell renderer implementation which will automatically pass the value to be rendered to a given
 * component. All you have to specify is the renderer component and the name of the property which should be set.
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
public class ReflectedTableCellRenderer implements TableCellRenderer {
    protected JComponent _renderer;
    protected Method _setter;
    private CellRendererUtility _utility;

    public ReflectedTableCellRenderer(final JComponent renderer, final String property) {
        initialize(renderer, property);
    }

    protected void initialize(final JComponent renderer, final String valueProperty) {
        if (renderer == null)
            throw new IllegalArgumentException("Parameter 'renderer' must not be null!");
        if (valueProperty == null)
            throw new IllegalArgumentException("Parameter 'valueProperty' must not be null!");

        _renderer = renderer;
        _utility = new CellRendererUtility();
        _setter = SwingExtUtil.getSetter(renderer, valueProperty);
        if (_setter == null)
            throw new IllegalArgumentException("Component " + _renderer + " has no setter for property " + valueProperty);
    }

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                   final boolean hasFocus, final int row, final int column) {
        try {
            SwingExtUtil.invokeSetter(_setter, _renderer, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not access setter " + _setter.getName(), e);
        }
        _utility.adaptToTable(_renderer, table, isSelected, hasFocus, row, column);

        return _renderer;
    }
}
