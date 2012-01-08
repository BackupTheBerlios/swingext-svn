package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;

/**
 * Default implementation of the EditableListModel interface, building upon a DefaultListModel.
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class DefaultEditableListModel extends DefaultListModel implements EditableListModel {
    /**
     * @see net.sarcommand.swingextensions.editablelist.EditableListModel#isIndexEditable(int)
     */
    public boolean isIndexEditable(final int row) {
        return true;
    }

    /**
     * @see net.sarcommand.swingextensions.editablelist.EditableListModel#setValue(Object, int)
     */
    public void setValue(final Object value, final int row) {
        setElementAt(value, row);
    }
}
