package net.sarcommand.swingextensions.splitpane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A container which puts child components into variable cells. Just as with a JSplitPane, the cell size can be
 * adjusted by dragging the mouse over the cell border. Other than the split pane however, this component does not
 * display visible dividers, but instead relies on the components' border to make the gui less clotted.
 * <p/>
 * The panel will layout its components according to its orientation property, which has to be
 * SwingConstants.HORIZONTAL or SwingConstants.VERTICAL. The cells can be resized along this orientation axis while
 * taking the entire space available for the other axis. Cells can not be made smaller than the wrapped component's
 * minimum size.
 * <p/>
 * You can supply weights along with the components when adding them. If the panel is resized, excess space will be
 * attributed to components according to their weight relative to the total sum of weights, just as its done by
 * the GridBagLayout.
 * <p/>
 * When adding components you should take care of two things: First, make sure that the component has a visible
 * border, for it is going to be the handle used for resizing. Secondly, in order for the gui to behave well when
 * resizing, you should supply a suitable minimum size for components which do not already have one.
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
 */
public class MultiCellSplitPane extends JComponent {
    private JPanel _splitPane;
    private ArrayList<SplitLayoutCell> _cellList;
    private int _orientation;

    /**
     * Creates a new MultiCellSplitPane with horizontal orientation.
     */
    public MultiCellSplitPane() {
        initialize(SwingConstants.HORIZONTAL);
    }

    /**
     * Creates a new MultiCellSplitPane with vertical orientation.
     *
     * @param orientiation SwingConstants.HORIZONTAL or SwingConstants.VERTICAL.
     */
    public MultiCellSplitPane(final int orientiation) {
        initialize(orientiation);
    }

    protected void initialize(final int orientation) {
        _orientation = orientation;
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _splitPane = new JPanel();
        _cellList = new ArrayList<SplitLayoutCell>();
    }

    protected void initLayout() {
        super.setLayout(new GridLayout(1, 1));
        super.add(_splitPane);
        _splitPane.setLayout(new SplitLayout(_orientation));
    }

    protected void setupEventHandlers() {
    }

    /**
     * Adds a child component with a default weight of 1.0.
     *
     * @param c Component to add.
     * @return Added component.
     */
    public Component add(final Component c) {
        add(c, null);
        return c;
    }

    /**
     * Adds a component with the given weight.
     *
     * @param comp        Component to add.
     * @param constraints Double object containing the component's weight. When using autoboxing, be sure not to
     *                    confure this method with add(Component, int)
     */
    public void add(Component comp, Object constraints) {
        final SplitLayoutCell cell = new SplitLayoutCell(comp, _orientation);
        _splitPane.add(cell, constraints);
        if (_cellList.size() > 0)
            _cellList.get(_cellList.size() - 1).setNextCell(cell);
        _cellList.add(cell);
    }

    /**
     * Overwritten to prevent you from setting a custom layout.
     *
     * @param mgr The argument you really should not try to set :-).
     */
    public void setLayout(LayoutManager mgr) {
        throw new IllegalArgumentException("The splitpane for this class can not be adjusted.");
    }

    public void add(Component comp, Object constraints, int index) {
        add(comp, constraints);
    }

    public Component add(Component comp, int index) {
        return add(comp);
    }

    public Component add(String name, Component comp) {
        return add(comp);
    }
}
