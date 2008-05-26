package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;
import java.awt.*;

/**
 * Interface for CellEditor implementations used by a JEditableList.
 */
public interface ListCellEditor extends CellEditor {
    public Component getListCellEditorComponent(JList list, Object value, boolean isSelected, int row);
}
