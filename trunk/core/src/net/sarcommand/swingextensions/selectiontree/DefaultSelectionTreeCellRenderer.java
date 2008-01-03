package net.sarcommand.swingextensions.selectiontree;

import net.sarcommand.swingextensions.resources.SwingExtResources;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Default implementation for a JSelectionTree's renderer. This implementation will simply add suitable
 * checkbox icons in front of each node where applicable.
 * <p/>
 * If you want to change the icons being used, you can either use the getter/setter methods or globally define
 * your own icons by providing a custom ResourceBundle for the swingextensions package.
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
 *
 * @see SwingExtResources *
 */
public class DefaultSelectionTreeCellRenderer extends DefaultTreeCellRenderer {
    private static final String ICON_NONE_SELECTED = "SelectionTree.iconNoneSelected";
    private static final String ICON_SOME_SELECTED = "SelectionTree.iconSomeSelected";
    private static final String ICON_ALL_SELECTED = "SelectionTree.iconAllSelected";

    private Icon _iconNoneSelected;
    private Icon _iconSomeSelected;
    private Icon _iconAllSelected;

    public DefaultSelectionTreeCellRenderer() {
        _iconNoneSelected = SwingExtResources.getIconResource(ICON_NONE_SELECTED);
        _iconSomeSelected = SwingExtResources.getIconResource(ICON_SOME_SELECTED);
        _iconAllSelected = SwingExtResources.getIconResource(ICON_ALL_SELECTED);
    }

    public Icon getIconAllSelected() {
        return _iconAllSelected;
    }

    public void setIconAllSelected(final Icon iconAllSelected) {
        _iconAllSelected = iconAllSelected;
    }

    public Icon getIconNoneSelected() {
        return _iconNoneSelected;
    }

    public void setIconNoneSelected(final Icon iconNoneSelected) {
        _iconNoneSelected = iconNoneSelected;
    }

    public Icon getIconSomeSelected() {
        return _iconSomeSelected;
    }

    public void setIconSomeSelected(final Icon iconSomeSelected) {
        _iconSomeSelected = iconSomeSelected;
    }

    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
                                                  final boolean expanded, final boolean leaf, final int row,
                                                  final boolean hasFocus) {
        final Component cellRendererComponent = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);
        if (value instanceof SelectionTreeNode) {
            final Icon icon;
            final SelectionTreeNode node = (SelectionTreeNode) value;
            if (node.getState() == null)
                icon = _iconNoneSelected;
            else {
                switch (node.getState()) {
                    case ALL_SELECTED:
                        icon = _iconAllSelected;
                        break;
                    case NONE_SELECTED:
                        icon = _iconNoneSelected;
                        break;
                    case SOME_SELECTED:
                        icon = _iconSomeSelected;
                        break;
                    default:
                        throw new RuntimeException("Illegal state:" + node.getState());
                }
            }
            setIcon(icon);
        }
        return cellRendererComponent;
    }
}
