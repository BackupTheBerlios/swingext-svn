package net.sarcommand.swingextensions.editablelist;

import javax.swing.*;
import java.awt.*;

/**
 * Interface for CellEditor implementations used by a JEditableList.
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
