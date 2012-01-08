package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;

/**
 * Interface for list models which can be edited through a component. It exposes the same methods as a normal list model
 * and adds the methods setValue(Object, int) and isIndexEditable(int).
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
