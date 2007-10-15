package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 */
public class TreeTableRootColumn extends TableColumn {
    protected JTree _nestedTree;

    public TreeTableRootColumn(final JTree nestedTree) {
        _nestedTree = nestedTree;
    }

    /**
     * Returns the preferred width of the <code>TableColumn</code>.
     * The default preferred width is 75.
     *
     * @return the <code>preferredWidth</code> property
     * @see #setPreferredWidth
     */
    @Override
    public int getPreferredWidth() {
        return _nestedTree.getPreferredSize().width;
    }


    /**
     * Returns the maximum width for the <code>TableColumn</code>. The
     * <code>TableColumn</code>'s width can't be made larger than this
     * either by the user or programmatically.  The default maxWidth
     * is Integer.MAX_VALUE.
     *
     * @return the <code>maxWidth</code> property
     * @see #setMaxWidth
     */
    @Override
    public int getMaxWidth() {
        return _nestedTree.getMaximumSize().width;
    }


    /**
     * Returns the minimum width for the <code>TableColumn</code>. The
     * <code>TableColumn</code>'s width can't be made less than this either
     * by the user or programmatically.  The default minWidth is 15.
     *
     * @return the <code>minWidth</code> property
     * @see #setMinWidth
     */
    @Override
    public int getMinWidth() {
        return _nestedTree.getMinimumSize().width;
    }


    /**
     * Returns the <code>identifier</code> object for this column.
     * Note identifiers are not used by <code>JTable</code>,
     * they are purely a convenience for external use.
     * If the <code>identifier</code> is <code>null</code>,
     * <code>getIdentifier()</code> returns <code>getHeaderValue</code>
     * as a default.
     *
     * @return the <code>identifier</code> property
     * @see #setIdentifier
     */
    @Override
    public Object getIdentifier() {
        return "TreeViewColumn";
    }


    /**
     * Returns the <code>Object</code> used as the value for the header
     * renderer.
     *
     * @return the <code>headerValue</code> property
     * @see #setHeaderValue
     */
    @Override
    public Object getHeaderValue() {
        return "TreeViewColumn";
    }
}
