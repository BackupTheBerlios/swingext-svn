package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
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
