package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.event.TableSelectionListenerGlue;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * A BooleanCondition implementation indicating whether a component (JTable, JTree, JList) has a current selection.
 * <hr/> Copyright 2006-2010 Torsten Heup
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
public class HasSelectionCondition extends BooleanCondition {
    private JComponent _component;

    public HasSelectionCondition(final JTree tree) {
        if (tree == null)
            throw new IllegalArgumentException("Parameter 'tree' must not be null!");

        _component = tree;
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(final TreeSelectionEvent e) {
                fireConditionUpdated();
            }
        });
    }

    public HasSelectionCondition(final JTable table) {
        if (table == null)
            throw new IllegalArgumentException("Parameter 'table' must not be null!");

        _component = table;
        final TableSelectionListenerGlue glue = new TableSelectionListenerGlue(table, new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                fireConditionUpdated();
            }
        });
    }

    public HasSelectionCondition(final JList list) {
        if (list == null)
            throw new IllegalArgumentException("Parameter 'list' must not be null!");

        _component = list;
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                fireConditionUpdated();
            }
        });
    }

    @Override
    public Boolean getState() {
        if (_component instanceof JTable)
            return ((JTable) _component).getSelectedRow() >= 0;
        if (_component instanceof JList)
            return ((JList) _component).getSelectedValue() != null;
        if (_component instanceof JTree)
            return ((JTree) _component).getSelectionCount() > 0;
        return null;
    }
}
