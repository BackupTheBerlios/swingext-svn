package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;
import java.awt.*;

/**
 * Interface for CellEditor implementations used by a JEditableList.
 */
public interface ListCellEditor extends CellEditor {
    /**
     * Returns a suitable editor component for the given list.
     *
     * @param list       The JList instance being edited.
     * @param value      The value being edited, may be null.
     * @param isSelected Whether or not the index being edited was selected.
     * @param index      The index being edited.
     * @return a suitable component for editing the given index in the list.
     */
    public Component getListCellEditorComponent(JList list, Object value, boolean isSelected, int index);
}
