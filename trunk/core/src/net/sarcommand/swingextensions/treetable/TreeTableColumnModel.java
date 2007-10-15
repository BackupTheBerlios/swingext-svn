package net.sarcommand.swingextensions.treetable;

import javax.swing.table.DefaultTableColumnModel;

/**
 */
public class TreeTableColumnModel extends DefaultTableColumnModel {
    public TreeTableColumnModel(final TreeTableRootColumn rootColumn) {
        super();
        addColumn(rootColumn);
    }

    /**
     * Moves the column and heading at <code>columnIndex</code> to
     * <code>newIndex</code>.  The old column at <code>columnIndex</code>
     * will now be found at <code>newIndex</code>.  The column
     * that used to be at <code>newIndex</code> is shifted
     * left or right to make room.  This will not move any columns if
     * <code>columnIndex</code> equals <code>newIndex</code>.  This method
     * also posts a <code>columnMoved</code> event to its listeners.
     *
     * @param columnIndex the index of column to be moved
     * @param newIndex    new index to move the column
     * @throws IllegalArgumentException if <code>column</code> or
     *                                  <code>newIndex</code>
     *                                  are not in the valid range
     */
    @Override
    public void moveColumn(int columnIndex, int newIndex) {
        if (columnIndex != 0 && newIndex != 0)
            super.moveColumn(columnIndex, newIndex);
    }

}
