package net.sarcommand.swingextensions.layout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class realizes the layout manager for a MultiCellSplitPane. It is only used internally and you should not have
 * to bother with it directly at any point.
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
public class SplitLayout implements LayoutManager2 {
    private ArrayList<SplitLayoutCell> _arrangedComponents;

    private HashMap<SplitLayoutCell, Double> _weights;

    protected boolean _horizontal;

    public SplitLayout() {
        this(SwingConstants.HORIZONTAL);
    }

    public SplitLayout(final int orientation) {
        _arrangedComponents = new ArrayList<SplitLayoutCell>(8);
        _weights = new HashMap<SplitLayoutCell, Double>(8);
        _horizontal = orientation == SwingConstants.HORIZONTAL;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        _arrangedComponents.add((SplitLayoutCell) comp);
        if (constraints == null)
            _weights.put((SplitLayoutCell) comp, 1.0);
        else
            _weights.put((SplitLayoutCell) comp, (Double) constraints);
    }

    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, null);
    }

    public void removeLayoutComponent(Component comp) {
        _arrangedComponents.remove((SplitLayoutCell) comp);
        _weights.remove((SplitLayoutCell) comp);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    public void invalidateLayout(Container target) {

    }

    public Dimension maximumLayoutSize(Container target) {
        //Todo fix this
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void layoutContainer(Container parent) {
        final Dimension size = parent.getSize();
        final Insets insets = parent.getInsets();

        size.width = size.width - insets.left - insets.right;
        size.height = size.height - insets.top - insets.bottom;

        final Dimension d = preferredLayoutSize(parent);

        final HashMap<SplitLayoutCell, Size> sizeMap = new HashMap<SplitLayoutCell, Size>();
        for (SplitLayoutCell c : _arrangedComponents)
            sizeMap.put(c, c.getLayoutSize());

        if (_horizontal) {
            int x = 0;
            if (Math.abs(d.width - size.width) >= 1)
                adjustSizes(sizeMap, size.width - d.width);

            for (SplitLayoutCell c : _arrangedComponents) {
                final Size cSize = sizeMap.get(c);
                final Rectangle bounds = new Rectangle(x, 0, (int) Math.ceil(cSize.width), (int) Math.ceil(size.height));
                c.setBounds(bounds);

                x += Math.ceil(cSize.width);
            }
        } else {
            int y = 0;
            if (Math.abs(d.height - size.height) >= 1)
                adjustSizes(sizeMap, size.height - d.height);

            for (SplitLayoutCell c : _arrangedComponents) {
                final Size cSize = sizeMap.get(c);
                final Rectangle bounds = new Rectangle(0, y, (int) Math.ceil(size.width), (int) Math.ceil(cSize.height));
                c.setBounds(bounds);

                y += Math.ceil(cSize.height);
            }
        }
    }

    protected void adjustSizes(final HashMap<SplitLayoutCell, Size> sizes, final int delta) {
        double totalWeight = 0;
        for (SplitLayoutCell c : sizes.keySet())
            totalWeight = totalWeight + _weights.get(c);

        if (delta > 0) {
            for (SplitLayoutCell c : sizes.keySet()) {
                final Size layoutSize = c.getLayoutSize();
                final double modificator = (_weights.get(c) / totalWeight) * delta;
                final Size newSize;
                if (_horizontal)
                    newSize = new Size(layoutSize.width + modificator, layoutSize.height);
                else
                    newSize = new Size(layoutSize.width, layoutSize.height + modificator);

                sizes.put(c, newSize);
                c.setLayoutSize(newSize);
            }
        } else {
            double remainingDelta = -delta;
            double delta2 = -delta;
            final double[] shrinks = new double[sizes.size()];

            boolean flag = true;
            while (flag && remainingDelta > 0) {
                flag = false;
                int index = 0;
                for (SplitLayoutCell c : sizes.keySet()) {
                    final Size layoutSize = c.getLayoutSize();
                    final double shrinkAmount;
                    if (_horizontal)
                        shrinkAmount = shrinks[index] + Math.min(((_weights.get(c) / totalWeight) * delta2),
                                Math.max(layoutSize.width - c.getMinimumSize().width, 0));
                    else
                        shrinkAmount = shrinks[index] + Math.min(((_weights.get(c) / totalWeight) * delta2),
                                Math.max(layoutSize.height - c.getMinimumSize().height, 0));
                    if (shrinkAmount != shrinks[index])
                        flag = true;
                    shrinks[index] = shrinkAmount;
                    remainingDelta -= shrinkAmount;
                    index++;
                }
                delta2 = remainingDelta;
            }
            int index = 0;
            for (SplitLayoutCell c : sizes.keySet()) {
                final Size layoutSize = c.getLayoutSize();

                final Size newSize;
                if (_horizontal)
                    newSize = new Size(layoutSize.width - shrinks[index], layoutSize.height);
                else
                    newSize = new Size(layoutSize.width, layoutSize.height - shrinks[index]);
                index++;
                sizes.put(c, newSize);
                c.setLayoutSize(newSize);
            }
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        int val0 = 0;
        int val1 = 0;

        for (SplitLayoutCell c : _arrangedComponents) {
            val0 += _horizontal ? c.getMinimumSize().width : c.getMinimumSize().height;
            val1 = Math.max(val1, _horizontal ? c.getMinimumSize().height : c.getMinimumSize().width);
        }

        return _horizontal ? new Dimension(val0, val1) : new Dimension(val1, val0);
    }

    public Dimension preferredLayoutSize(Container parent) {
        final Size d = new Size();
        for (SplitLayoutCell c : _arrangedComponents) {
            if (_horizontal)
                d.width += c.getLayoutSize().width;
            else
                d.height += c.getLayoutSize().height;
        }
        return new Dimension((int) Math.ceil(d.width), (int) Math.ceil(d.height));
    }
}
