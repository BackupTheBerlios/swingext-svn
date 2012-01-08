package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.event.EventSupport;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * Abstract class for TableCellEditor implementations which handles the required event infrastructure.
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
public abstract class AbstractTableCellEditor implements TableCellEditor {
    protected final EventSupport<CellEditorListener> _listeners;

    public AbstractTableCellEditor() {
        _listeners = EventSupport.create(CellEditorListener.class);
    }

    public void addCellEditorListener(CellEditorListener l) {
        _listeners.addListener(l);
    }


    public void removeCellEditorListener(CellEditorListener l) {
        _listeners.removeListener(l);
    }

    /**
     * Used to fire an event when editing has been cancelled.
     *
     * @param source The cell editor component.
     */
    protected void fireEditingCancelled(final Object source) {
        final ChangeEvent event = new ChangeEvent(source);
        _listeners.delegate().editingCanceled(event);
    }

    /**
     * Used to fire an event when editing has been stopped.
     *
     * @param source The cell editor component.
     */
    protected void fireEditingStopped(final Object source) {
        final ChangeEvent event = new ChangeEvent(source);
        _listeners.delegate().editingStopped(event);
    }
}
