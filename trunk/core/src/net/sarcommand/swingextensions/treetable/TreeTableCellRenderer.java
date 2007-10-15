package net.sarcommand.swingextensions.treetable;

import javax.swing.tree.TreePath;
import java.awt.*;

/**
 */
public interface TreeTableCellRenderer {
    public Component getTreeTableCellRendererComponent(JTreeTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       TreePath path, int row, int column);
}
