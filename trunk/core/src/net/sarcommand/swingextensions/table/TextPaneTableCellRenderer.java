package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.misc.CellRendererUtility;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Document;
import java.awt.*;

/**
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
public class TextPaneTableCellRenderer implements TableCellRenderer {
    protected JTextPane _renderer;
    protected ColumnModelAdapter _listener;

    protected boolean _shouldUpdateRowHeights;

    public TextPaneTableCellRenderer() {
        _renderer = new JTextPane();
    }

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                   final boolean hasFocus, final int row, final int column) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                updateRowHeights();
            }
        });
        if (value == null)
            _renderer.setText("");
        else if (value instanceof String)
            _renderer.setText((String) value);
        else if (value instanceof Document)
            _renderer.setDocument((Document) value);
        else
            throw new RuntimeException("Illegal value type: This implementation can only display values of type " +
                    "String and Document, but found: " + value.getClass());

        CellRendererUtility.adaptToTable(_renderer, table, isSelected, hasFocus, row, column);

        return _renderer;
    }

    protected static class ColumnModelAdapter implements TableColumnModelListener {
        private JTable _table;

        public ColumnModelAdapter() {

        }

        public void columnAdded(final TableColumnModelEvent e) {
        }

        public void columnMarginChanged(final ChangeEvent e) {
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    final Component renderer = table.getCellRenderer(2, 2).getTableCellRendererComponent(table,
//                            mdl.getValueAt(2, 2), false, false, 2, 2);
//                    renderer.setSize(table.getColumnModel().getColumn(2).getWidth(), renderer.getHeight());
//                    table.setRowHeight(2, renderer.getPreferredSize().height);
//                }
//            });
        }

        public void columnMoved(final TableColumnModelEvent e) {
        }

        public void columnRemoved(final TableColumnModelEvent e) {
        }

        public void columnSelectionChanged(final ListSelectionEvent e) {
        }
    }

    public static void main(String[] args) {
        final JTable table = new JTable();
        final DefaultTableModel mdl = new DefaultTableModel(3, 3);
        mdl.setValueAt("Hello World this is a really really really long text which actually does not say anything " +
                "else than the beginning already gave away but i like the sound of my own void so...", 2, 2);
        table.setModel(mdl);
        table.setDefaultRenderer(Object.class, new TextPaneTableCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(10);

        final JFrame frame = new JFrame();
        frame.setContentPane(new JScrollPane(table));
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
