package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;
import java.awt.*;

/**
 * Default implementation for the ListCellEditor interface, building upon the DefaultCellEditor class.
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
public class DefaultListCellEditor extends DefaultCellEditor implements ListCellEditor {
    public DefaultListCellEditor(final JCheckBox checkBox) {
        super(checkBox);
    }

    public DefaultListCellEditor(final JComboBox comboBox) {
        super(comboBox);
    }

    public DefaultListCellEditor(final JTextField textField) {
        super(textField);
    }

    public Component getListCellEditorComponent(final JList list, final Object value,
                                                final boolean isSelected, final int row) {
        delegate.setValue(value);
        return editorComponent;
    }
}
