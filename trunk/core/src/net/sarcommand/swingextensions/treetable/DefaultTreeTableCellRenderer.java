package net.sarcommand.swingextensions.treetable;


import net.sarcommand.swingextensions.misc.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

/**
 * Default renderer used by JTreeTable instances.
 */
public class DefaultTreeTableCellRenderer implements TreeTableCellRenderer {
    protected JLabel _renderer;
    protected CellRendererUtility _cellRendererUtility;

    public DefaultTreeTableCellRenderer() {
        _cellRendererUtility = new CellRendererUtility();
        _renderer = new JLabel();
        _renderer.setOpaque(true);
    }

    public Component getTreeTableCellRendererComponent(final JTreeTable table, final Object value,
                                                       final boolean isSelected, final boolean hasFocus,
                                                       final TreePath path, final int row, final int column) {
        _renderer.setText(value == null ? "" : value.toString());
        _cellRendererUtility.adaptToTreeTable(_renderer, table, isSelected, hasFocus, row, column);
        return _renderer;
    }
}
