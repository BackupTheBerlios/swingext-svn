package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;

/**
 * Interface for list models which can be edited through a component. It exposes the same methods as a normal list model
 * and adds the methods setValue(Object, int) and isIndexEditable(int).
 */
public interface EditableListModel extends ListModel {
    /**
     * Sets the value at the specified index to the given value.
     *
     * @param value New value to be set at 'index'.
     * @param index Index of the item to be replaced.
     */
    public void setValue(Object value, int index);

    /**
     * Returns whether the item at the given index is editable.
     *
     * @param index Index to be edited.
     * @return whether the item at the given index is editable.
     */
    public boolean isIndexEditable(int index);
}
