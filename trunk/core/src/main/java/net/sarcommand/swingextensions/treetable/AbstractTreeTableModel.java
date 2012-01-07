package net.sarcommand.swingextensions.treetable;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * BETA
 * <p/>
 * 8/4/11
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */

/*
 * Copyright 2005-2011 Torsten Heup
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

public abstract class AbstractTreeTableModel extends DefaultTreeModel implements TreeTableModel {
    protected AbstractTreeTableModel(final TreeNode treeNode) {
        super(treeNode);
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return false;
    }

    @Override
    public void setValueAt(final Object treeNode, final Object value, final int column) {
    }
}
