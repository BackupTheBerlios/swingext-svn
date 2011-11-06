package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.misc.CellRendererUtility;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * A TableCellEditor implementation backed by a JTextPane. The editor will support two types of values, Strings and
 * instances of the Document interface. The type which was set originally will be returned by getCellEditorValue(...).
 * The editor will automatically adapt the row height for the cell being edited, so while you are typing the cell will
 * expand (and shrink, if appropriate) according to your needs.
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
 */
public class TextPaneTableCellEditor extends AbstractTableCellEditor {
    protected boolean _valueIsDocument;
    protected JTextPane _textPane;

    protected JTable _owner;
    protected int _row;
    protected int _previousHeight;

    public TextPaneTableCellEditor() {
        initialize();
    }

    protected void initialize() {
        /* Make sure cancelCellEditing is invoked to restore the previous row heights */
        _textPane = new JTextPane() {
            public void removeNotify() {
                super.removeNotify();
                cancelCellEditing();
            }
        };

        /* Adjust the row height whenever the editor content changes */
        _textPane.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                if (_owner != null)
                    _owner.setRowHeight(_row, Math.max(_previousHeight, _textPane.getPreferredSize().height));
            }
        });
    }

    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row,
                                                 int column) {
        /* Make sure the value is of the required type */
        final Class clazz;
        if (value == null)
            clazz = table.getModel().getColumnClass(column);
        else
            clazz = value.getClass();

        if (clazz.equals(String.class))
            _valueIsDocument = false;
        else if (Document.class.isAssignableFrom(clazz))
            _valueIsDocument = true;
        else
            throw new RuntimeException("Illegal value class, will only support Strings and Documents, found "
                    + clazz.getName());

        if (_valueIsDocument)
            _textPane.setDocument((Document) value);
        else
            _textPane.setText((String) value);

        CellRendererUtility.adaptToTable(_textPane, table, false, false, row, column);

        /* Need those values to adapt the row size when required */
        _owner = table;
        _row = row;
        _previousHeight = _owner.getRowHeight(row);

        return _textPane;
    }

    /**
     * Restores the row height to the previous value in case it was modified
     */
    public void cancelCellEditing() {
        if (_owner != null) {
            final JTable owner = _owner;
            final int row = _row;
            final int previousHeight = _previousHeight;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    owner.setRowHeight(row, previousHeight);
                }
            });
        }

        _owner = null;
        fireEditingCancelled(this);

    }

    public boolean isCellEditable(EventObject anEvent) {
        return !(anEvent instanceof MouseEvent) || ((MouseEvent) anEvent).getClickCount() > 1;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        _owner = null;

        fireEditingStopped(this);
        return true;
    }

    public Object getCellEditorValue() {
        return _valueIsDocument ? _textPane.getDocument() : _textPane.getText();
    }
}
