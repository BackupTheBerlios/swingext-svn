package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Default implementation for the TreeTableCellEditor interface. This class works analogous to DefaultTableCellEditor.
 * It takes the component used for editing as a constructor parameter and delegates all calls to it accordingly.
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
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
public class DefaultTreeTableCellEditor extends DefaultCellEditor implements TreeTableCellEditor {
    public DefaultTreeTableCellEditor(final JCheckBox checkBox) {
        super(checkBox);
    }

    public DefaultTreeTableCellEditor(final JComboBox comboBox) {
        super(comboBox);
    }

    public DefaultTreeTableCellEditor(final JTextField textField) {
        super(textField);
    }

    public Component getTreeTableCellEditorComponent(final JTreeTable table, final Object value,
                                                     final boolean isSelected, final TreePath path,
                                                     final int row, final int column) {
        delegate.setValue(value);
        return editorComponent;
    }
}
