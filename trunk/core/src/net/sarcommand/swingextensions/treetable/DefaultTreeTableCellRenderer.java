package net.sarcommand.swingextensions.treetable;

import net.sarcommand.swingextensions.table.AbstractCellRenderer;

import javax.swing.tree.TreePath;
import java.awt.*;

/**
 */
public class DefaultTreeTableCellRenderer extends AbstractCellRenderer implements TreeTableCellRenderer {
    public Component getTreeTableCellRendererComponent(final JTreeTable table, final Object value,
                                                       final boolean isSelected, final boolean hasFocus,
                                                       final TreePath path, final int row, final int column) {
        setText(value == null ? "" : value.toString());
        adaptToTreeTable(this, table, isSelected, hasFocus, row, column);
        return this;
    }
}
