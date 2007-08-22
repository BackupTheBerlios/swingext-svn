package net.sarcommand.swingextensions.table;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * Implements a popup menu which can be used to show or hide columns in a JTable. This class is meant to work along
 * with the FilteringTableColumnModel class, which has to be installed as the table's current column model.
 * The FilteringTableModelPopup realizes a context menu containing a checkbox for each column in the
 * model which will be selected if the column is visible. Clicking the checkbox will make the popup invoke the
 * setColumnVisible(String) method on filtering column model.
 * <p/>
 * <p/>
 * <p/>
 * <hr/>
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
 *
 * @see FilteringTableColumnModel
 */
public class FilteringTableColumnModelPopup extends JPopupMenu {
    protected FilteringTableColumnModel _model;
    protected ActionListener _actionListener;

    public FilteringTableColumnModelPopup() {
        initialize();
    }

    /**
     * Creates a new FilteringTableModelPopup. Convenience constructor equal to invoking setModel after calling
     * the default constructor.
     *
     * @param mdl the model which will be notified when a column should be shown or hidden.
     */
    public FilteringTableColumnModelPopup(final FilteringTableColumnModel mdl) {
        initialize();
        setModel(mdl);
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
        _actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                if (item.isSelected())
                    _model.setColumnVisible(item.getText(), true);
                else if (_model.getColumnCount() > 1)
                    _model.setColumnVisible(item.getText(), false);
            }
        };
    }

    /**
     * Overwritten to update the menu before displaying it. If no model has been set for the popup, it will not
     * display when invoking this method since it has no content.
     *
     * @param b visibility flag
     */
    public void setVisible(boolean b) {
        if (b) {
            /* If we have no model, bail */
            if (_model == null)
                return;

            /* Update the items */
            removeAll();
            final Collection<Object> identifiers = _model.getAllColumnIdentfiers();
            for (Object s : identifiers) {
                final JCheckBoxMenuItem item = new JCheckBoxMenuItem(s.toString(), _model.isColumnVisible(s));
                item.addActionListener(_actionListener);
                add(item);
            }
            revalidate();
        }
        super.setVisible(b);
    }
}
