package net.sarcommand.swingextensions.layout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * A utility class for laying out components in a horizontal or vertical line. This class encapsulates a GridBagLayout
 * instance and will arrange components with a given weight along the main axis while maximizing the other axis.
 *
 * Several shortcut methods exist for adding JLabels, glues, struts etc. The class is intended to be used for
 * method chaining.
 *
 * Example:
 * <code>LayoutBox.horizontalLayoutBox().add("First name:",0d).addStrut(6).add(nameTF,1d);</code>
 *
 * Feb 10, 2010
 * <p/>
 * <hr/> Copyright 2006-2011 Torsten Heup
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
public class LayoutBox extends JPanel {
    public static LayoutBox horizontalLayoutBox() {
        return new LayoutBox(SwingConstants.HORIZONTAL);
    }

    public static LayoutBox verticalLayoutBox() {
        return new LayoutBox(SwingConstants.VERTICAL);
    }

    private final boolean _horizontal;
    private Insets _componentInsets;

    public LayoutBox() {
        this(SwingConstants.HORIZONTAL, (Border) null);
    }

    public LayoutBox(final Insets componentInsets) {
        this();
        _componentInsets = componentInsets;
    }

    public LayoutBox(final int alignment) {
        this(alignment, (Border) null);
    }

    public LayoutBox(final int alignment, final String title) {
        this(alignment, BorderFactory.createTitledBorder(title));
    }

    public LayoutBox(final int alignment, final Border border) {
        _horizontal = alignment == SwingConstants.HORIZONTAL;
        if (border != null)
            setBorder(border);
        setLayout(new GridBagLayout());
        _componentInsets = new Insets(3, 3, 3, 3);
    }

    public LayoutBox add(final Component component, final double weight) {
        return add(component, weight, _componentInsets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final Component component, final double weight, final Insets insets) {
        return add(component, weight, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final Component component, final double weight, final int anchor, final int fill) {
        return add(component, weight, _componentInsets, anchor, fill);
    }

    public LayoutBox add(final Component component, final double weight, final Insets insets,
                         final int anchor, final int fill) {
        add(component, new GridBagConstraints(_horizontal ? -1 : 0, _horizontal ? 0 : -1, 1, 1,
                _horizontal ? weight : 1.0, _horizontal ? 1.0 : weight, anchor, fill, insets, 0, 0));
        return this;
    }

    public LayoutBox add(final String text) {
        return add(new JLabel(text), 0, _componentInsets,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final String text, final double weight) {
        return add(new JLabel(text), weight, _componentInsets,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final String text, final double weight, final Insets insets) {
        return add(new JLabel(text), weight, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final String text, final double weight, final int anchor, final int fill) {
        return add(new JLabel(text), weight, _componentInsets, anchor, fill);
    }

    public LayoutBox add(final String text, final double weight, final Insets insets,
                         final int anchor, final int fill) {
        return add(new JLabel(text), weight, insets, anchor, fill);
    }

    public LayoutBox addGlue(final double weight) {
        return add(Box.createGlue(), weight);
    }

    public LayoutBox addStrut(final int extent) {
        return add(_horizontal ? Box.createHorizontalStrut(extent) : Box.createVerticalStrut(extent), 0d);
    }

    public LayoutBox addSeparator() {
        return add(new JSeparator(_horizontal ? JSeparator.VERTICAL : JSeparator.HORIZONTAL), 0d);
    }

    public Insets getComponentInsets() {
        return _componentInsets;
    }

    public LayoutBox setComponentInsets(final Insets insets) {
        _componentInsets = insets;
        return this;
    }

    public LayoutBox withTitledBorder(final String title) {
        if (title == null)
            throw new IllegalArgumentException("Parameter 'title' must not be null!");

        setBorder(BorderFactory.createTitledBorder(title));
        return this;
    }

    public LayoutBox withBorder(final Border border) {
        setBorder(border);
        return this;
    }
}
