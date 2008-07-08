package net.sarcommand.swingextensions.event;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * Conveniance adapter class for the TreeModelListener interface.
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @see javax.swing.event.DocumentListener
 */
public abstract class TreeModelAdapter implements TreeModelListener {
    public void treeNodesChanged(final TreeModelEvent e) {
    }

    public void treeNodesInserted(final TreeModelEvent e) {
    }

    public void treeNodesRemoved(final TreeModelEvent e) {
    }

    public void treeStructureChanged(final TreeModelEvent e) {
    }
}
