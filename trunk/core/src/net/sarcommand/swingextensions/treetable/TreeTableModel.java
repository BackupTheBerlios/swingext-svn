package net.sarcommand.swingextensions.treetable;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Created by IntelliJ IDEA.
 * User: heup
 * Date: Jul 25, 2006
 * Time: 12:30:07 PM
 */
public interface TreeTableModel extends TreeModel {
    public int getColumnCount();

    public String getColumnLabel(final int columnIndex);

    public Object getValueAt(final TreePath path, final int columnIndex);

    public Class getColumnClass(final int columnIndex);

    public void setValueAt(final Object value, final TreePath path, final int columnIndex);

    public boolean isCellEditable(final TreePath path, final int columnIndex);
}
