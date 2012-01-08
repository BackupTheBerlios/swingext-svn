package net.sarcommand.swingextensions.table;

import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;

/**
 * This action will query the selected value of a JTable and set it to all subsequent rows. If, for instance, the
 * keyboard focus is set on [2/3], the value of that cell will be applied to the columns with the <i>view</i> index
 * [3/3], [4/3] and so on. If the selected cell's value is <code>null</code>, the subsequent values will be set to
 * <code>null</code> as well.
 * <p/>
 * Note that the source of the action event will be ignored, the action will always target the JTable specified in the
 * constructor. This allows you to install the action on other components like a JToolBar. <hr/> Copyright 2006-2012
 * Torsten Heup
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
public class ApplyValueToSubsequentRowsAction extends AbstractAction {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(ApplyValueToSubsequentRowsAction.class);
    private final JTable _table;

    public ApplyValueToSubsequentRowsAction(final JTable table) {
        _table = table;
    }

    public void actionPerformed(final ActionEvent e) {
        assert SwingUtilities.isEventDispatchThread() : "This method has to be invoked from the EDT";

        final int selectedRow = _table.getSelectedRow();
        final int selectedColumn = _table.getSelectedColumn();

        if (selectedColumn < 0 || selectedRow < 0) {
            __log.debug("Can't perform action, no cell selected");
            return;
        }

        final TableCellEditor tableCellEditor = _table.getCellEditor();
        if (tableCellEditor != null)
            if (!tableCellEditor.stopCellEditing())
                return;

        final int modelRow = _table.convertRowIndexToModel(selectedRow);
        final int modelColumn = _table.convertColumnIndexToModel(selectedColumn);

        final int rowCount = _table.getModel().getRowCount();
        final TableModel mdl = _table.getModel();
        final Object value = mdl.getValueAt(modelRow, modelColumn);

        for (int i = selectedRow; i < rowCount; i++) {
            final int nextRow = _table.convertRowIndexToModel(i);
            if (!mdl.isCellEditable(nextRow, modelColumn)) {
                __log.trace(String.format("Can't apply value %s to [%d,%d], cell is not editable",
                        value, nextRow, modelColumn));
                continue;
            }
            mdl.setValueAt(value, nextRow, modelColumn);
        }
    }

    public static void main(String[] args) {
        final JTable table = new JTable(new DefaultTableModel(5, 5));
        final JFrame frame = new JFrame();
        frame.setContentPane(new JScrollPane(table));
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
