package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 */
public interface TreeTableCellEditor extends CellEditor {
    public Component getTreeTableCellEditorComponent(JTreeTable table, Object value, boolean isSelected,
                                                     TreePath path, int row, int column);
}
