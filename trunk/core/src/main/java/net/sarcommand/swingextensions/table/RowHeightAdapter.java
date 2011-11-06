package net.sarcommand.swingextensions.table;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A small utility class you can attach to a JTable, which will make sure that the table uses optimal row heights to
 * display all cells. Basically, a RowHeightAdapter will invoke the setPreferredRowHeights(...) method from
 * TableUtil whenever the table margins change or the model has been updated. You will probably want to install a
 * RowHeightAdapter if you are using complex table cell editors like the TextPaneTableCellEditor.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
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
public class RowHeightAdapter {
    protected JTable _table;
    protected TableColumnModel _columnModel;
    protected TableColumnModelListener _columnModelListener;
    protected TableModel _dataModel;
    protected TableModelListener _dataModelListener;

    public RowHeightAdapter() {
        initialize();
    }

    protected void initialize() {
        _columnModelListener = new TableColumnModelAdapter() {
            public void columnMarginChanged(ChangeEvent e) {
                TableUtil.setPreferredRowHeights(_table);
            }
        };

        _dataModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                TableUtil.setPreferredRowHeights(_table);
            }
        };
    }

    public JTable getTable() {
        return _table;
    }

    public void attach(JTable table) {
        if (_table != null)
            _table.putClientProperty(getClass().getName(), null);
        _table = table;
        if (_table != null)
            _table.putClientProperty(getClass().getName(), this);
        tableModelChanged();
        tableColumnModelChanged();
    }

    public void detach() {
        attach(null);
    }

    protected void tableModelChanged() {
        if (_dataModel != null)
            _dataModel.removeTableModelListener(_dataModelListener);
        _dataModel = _table == null ? null : _table.getModel();
        if (_dataModel != null)
            _dataModel.addTableModelListener(_dataModelListener);
    }

    protected void tableColumnModelChanged() {
        if (_columnModel != null)
            _columnModel.removeColumnModelListener(_columnModelListener);
        _columnModel = _table == null ? null : _table.getColumnModel();
        if (_columnModel != null)
            _columnModel.addColumnModelListener(_columnModelListener);
    }
}
