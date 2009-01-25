package net.sarcommand.swingextensions.treetable;

import javax.swing.table.DefaultTableColumnModel;

/**
 * todo [heup] add docs
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
