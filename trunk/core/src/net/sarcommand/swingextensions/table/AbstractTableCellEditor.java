package net.sarcommand.swingextensions.table;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Abstract class for TableCellEditor implementations which handles the required event infrastructure.
 * <p/>
 * Copyright 2006 Torsten Heup
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
public abstract class AbstractTableCellEditor implements TableCellEditor {
    private Vector<CellEditorListener> _listeners;

    public AbstractTableCellEditor() {
        _listeners = new Vector<CellEditorListener>(2);
    }

    public void addCellEditorListener(CellEditorListener l) {
        _listeners.add(l);
    }


    public void removeCellEditorListener(CellEditorListener l) {
        _listeners.remove(l);
    }

    /**
     * Used to fire an event when editing has been cancelled.
     *
     * @param source The cell editor component.
     */
    protected void fireEditingCancelled(final Object source) {
        final LinkedList<CellEditorListener> copy = new LinkedList<CellEditorListener>(_listeners);
        for (CellEditorListener l : copy)
            l.editingCanceled(new ChangeEvent(source));
    }

    /**
     * Used to fire an event when editing has been stopped.
     *
     * @param source The cell editor component.
     */
    protected void fireEditingStopped(final Object source) {
        final LinkedList<CellEditorListener> copy = new LinkedList<CellEditorListener>(_listeners);
        for (CellEditorListener l : copy)
            l.editingStopped(new ChangeEvent(source));
    }
}
