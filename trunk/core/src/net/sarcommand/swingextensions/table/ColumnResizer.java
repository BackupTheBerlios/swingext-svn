package net.sarcommand.swingextensions.table;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;

public class ColumnResizer {
    public static final String CLIENT_PROPERTY = "swingExt.ColumnResizer";

    protected JTable _table;
    protected TableModel _model;
    protected boolean _adjustSizeOnUpdate;

    protected PropertyChangeListener _changeListener;
    protected TableModelListener _tableModelListener;

    public ColumnResizer() {
        initialize(null, true);
    }

    public ColumnResizer(final JTable attachTo) {
        if (attachTo == null)
            throw new IllegalArgumentException("Parameter 'attachTo' must not be null");
        initialize(attachTo, true);
    }

    public ColumnResizer(final JTable attachTo, final boolean adjustSizeOnUpdate) {
        initialize(attachTo, adjustSizeOnUpdate);
    }

    protected void initialize(final JTable attachTo, final boolean adjustSizeOnUpdate) {
        _changeListener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("model"))
                    tableModelChanged();
            }
        };

        _tableModelListener = new TableModelListener() {
            public void tableChanged(final TableModelEvent e) {
                tableDataChanged();
            }
        };
        attachTo(attachTo);
        setAdjustSizeOnUpdate(adjustSizeOnUpdate);                  
        tableDataChanged();
    }

    public void attachTo(final JTable table) {
        _table = table;
        _table.putClientProperty(CLIENT_PROPERTY, this);
        _table.addPropertyChangeListener(_changeListener);
        _model = table.getModel();
        if(_model != null)
            _model.addTableModelListener(_tableModelListener);
    }

    public void detach() {
        _table.putClientProperty(CLIENT_PROPERTY, null);
        _table.removePropertyChangeListener(_changeListener);
        if (_model != null)
            _model.removeTableModelListener(_tableModelListener);
    }

    public JTable getTable() {
        return _table;
    }

    public void setAdjustSizeOnUpdate(final boolean adjustSizeOnUpdate) {
        _adjustSizeOnUpdate = adjustSizeOnUpdate;
    }

    public boolean isAdjustingSizeOnUpdate() {
        return _adjustSizeOnUpdate;
    }

    protected void tableModelChanged() {
        if (_model != null)
            _model.removeTableModelListener(_tableModelListener);
        _model = _table.getModel();
        if (_model != null)
            _model.addTableModelListener(_tableModelListener);
    }

    protected void tableDataChanged(){
        if (_adjustSizeOnUpdate) {
            adjustSizes();
        }
    }

    protected void adjustSizes(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final TableColumnModel columnModel = _table.getColumnModel();
                final int columnCount = columnModel.getColumnCount();
                for (int i = 0; i < columnCount; i ++) {
                    int maxSize = 0;
                    final int rowCount = _model.getRowCount();

                    for (int j = 0; j < rowCount; j++) {
                        maxSize = Math.max(_table.getCellRenderer(j,i).
                            getTableCellRendererComponent(_table, _model.getValueAt(j,i),false,
                            false, j, i).getPreferredSize().width, maxSize);
                    }

                    final TableColumn column = columnModel.getColumn(i);
                    TableCellRenderer headerRenderer = column.getHeaderRenderer();
                    if (headerRenderer == null)
                        headerRenderer = _table.getTableHeader().getDefaultRenderer();
                    final Object headerValue = column.getHeaderValue();
                    maxSize = Math.max(headerRenderer.getTableCellRendererComponent(_table, headerValue, false, false, 0, i)
                        .getPreferredSize().width, maxSize);

                    System.out.println("Setting "+headerValue+" to "+maxSize);
                    column.setPreferredWidth(maxSize);
                }

                _table.revalidate();
            }
        });
    }

    public static void main(String[] args) {
        final String[] names = new String[] {"#","First Name", "Last Name", "ID"};
        final String[][] rows = new String[][] {
            {"1","Bob","Thornton","153"},
            {"2","Alice","Campbell","634"},
            {"3","Jessica","Decan","637"}
        };
        final JTable table = new JTable(rows, names);
        final ColumnResizer resizer = new ColumnResizer(table);

        final JPanel cPane = new JPanel(new GridLayout(1,1));
        cPane.add(new JScrollPane(table));
        final JFrame frame = new JFrame();
        frame.setContentPane(cPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                final DefaultTableModel model = new DefaultTableModel(rows, names);
//                System.out.println("Setting new Model");
//                table.setModel(model);
//            }
//        });
    }
}
