package net.sarcommand.swingextensions.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * Implements a popup menu which can be used to show or hide columns in a JTable. This class is meant to work along with
 * the FilteringTableColumnModel class, which has to be installed as the table's current column model. The
 * FilteringTableModelPopup realizes a context menu containing a checkbox for each column in the model which will be
 * selected if the column is visible. Clicking the checkbox will make the popup invoke the setColumnVisible(String)
 * method on filtering column model.
 * <p/>
 * <b>Fair Warning:</b>Because of bug #6586009, you should <emph>not</emph> add the popup menu to the JTableHeader.
 * Doing so will cause an ArrayIndexOutOfBoundsException deep inside the swing rendering code. I really can't to
 * anything about it. This bug will only occur on windows. I'd recomment adding a component to the corner of the
 * enclosing JScrollPane instead.
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
 *
 * @see FilteringTableColumnModel
 */
public class FilteringTableColumnModelPopup extends JPopupMenu {
    protected FilteringTableColumnModel _model;
    protected ActionListener _actionListener;

    private boolean _hidingUponSelection;

    public FilteringTableColumnModelPopup() {
        initialize();
    }

    /**
     * Creates a new FilteringTableModelPopup. Convenience constructor equal to invoking setModel after calling the
     * default constructor.
     *
     * @param mdl the model which will be notified when a column should be shown or hidden.
     */
    public FilteringTableColumnModelPopup(final FilteringTableColumnModel mdl) {
        initialize();
        setModel(mdl);
    }

    public FilteringTableColumnModelPopup(final FilteringTableColumnModel mdl, final boolean hidingUponSelection) {
        initialize();
        setModel(mdl);
        setHidingUponSelection(hidingUponSelection);
    }

    /**
     * Returns the model which will be notified when a column should be shown or hidden.
     *
     * @return the model which will be notified when a column should be shown or hidden.
     */
    public FilteringTableColumnModel getModel() {
        return _model;
    }

    /**
     * Sets the model which will be notified when a column should be shown or hidden.
     *
     * @param model the model which will be notified when a column should be shown or hidden.
     */
    public void setModel(FilteringTableColumnModel model) {
        _model = model;
    }

    protected void initialize() {
        _hidingUponSelection = true;
        _actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JCheckBox item = (JCheckBox) e.getSource();
                if (item.isSelected())
                    _model.setColumnVisible(item.getText(), true);
                else if (_model.getColumnCount() > 1)
                    _model.setColumnVisible(item.getText(), false);
                if (_hidingUponSelection)
                    setVisible(false);
            }
        };
    }

    public boolean isHidingUponSelection() {
        return _hidingUponSelection;
    }

    public void setHidingUponSelection(final boolean hidingUponSelection) {
        _hidingUponSelection = hidingUponSelection;
    }

    /**
     * Overwritten to update the menu before displaying it. If no model has been set for the popup, it will not display
     * when invoking this method since it has no content.
     *
     * @param b visibility flag
     */
    public void setVisible(boolean b) {
        if (b) {
            /* If we have no model, bail */
            if (_model == null)
                return;

            final Collection<Object> identifiers = _model.getAllColumnIdentfiers();

            /* Update the items */
            removeAll();
            final JPanel panel = new JPanel(new GridBagLayout());
            for (Object s : identifiers) {
                final JCheckBox item = new JCheckBox(s.toString(), _model.isColumnVisible(s));
                item.addActionListener(_actionListener);
                panel.add(item, new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
            }
            add(panel);

            revalidate();
        }
        super.setVisible(b);
    }
}
