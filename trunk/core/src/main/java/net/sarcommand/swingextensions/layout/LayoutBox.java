package net.sarcommand.swingextensions.layout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * A utility class for laying out components in a horizontal or vertical line. This class encapsulates a GridBagLayout
 * instance and will arrange components with a given weight along the main axis while maximizing the other axis.
 * <p/>
 * Several shortcut methods exist for adding JLabels, glues, struts etc. The class is intended to be used for method
 * chaining.
 * <p/>
 * Example: <code>LayoutBox.horizontalLayoutBox().add("First name:",0d).addStrut(6).add(nameTF,1d);</code>
 * <p/>
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
    /**
     * Factory method for creating instances with the HORIZONTAL alignment.
     *
     * @return a new LayoutBox instance.
     */
    public static LayoutBox horizontalLayoutBox() {
        return new LayoutBox(SwingConstants.HORIZONTAL);
    }

    /**
     * Factory method for creating instances with the VERTICAL alignment.
     *
     * @return a new LayoutBox instance.
     */
    public static LayoutBox verticalLayoutBox() {
        return new LayoutBox(SwingConstants.VERTICAL);
    }

    /**
     * Indicates whether this box has a horizontal (true) or vertical (false) alignment.
     */
    protected final boolean _horizontal;

    /**
     * The default component insets to use.
     */
    protected Insets _componentInsets;

    /**
     * Creates a new horizontally aligned LayoutBox.
     */
    public LayoutBox() {
        this(SwingConstants.HORIZONTAL, (Border) null);
    }

    /**
     * Creates a new LayoutBox with the given default insets.
     *
     * @param componentInsets The default insets to use for added components.
     */
    public LayoutBox(final Insets componentInsets) {
        this();
        _componentInsets = componentInsets;
    }

    /**
     * Creates a new empty layout box with the given alignment.
     *
     * @param alignment SwingConstants.VERTICAL or SwingConstants.HORIZONTAL
     */
    public LayoutBox(final int alignment) {
        this(alignment, (Border) null);
    }

    /**
     * Creates a new empty layout box with the given alignment and a titled border with the given string.
     *
     * @param alignment SwingConstants.VERTICAL or SwingConstants.HORIZONTAL
     * @param title     Title to set in the box' border.
     */
    public LayoutBox(final int alignment, final String title) {
        this(alignment, BorderFactory.createTitledBorder(title));
    }

    /**
     * Creates a new empty layout box with the given alignment and border.
     *
     * @param alignment SwingConstants.VERTICAL or SwingConstants.HORIZONTAL
     * @param border    border instance to set.
     */
    public LayoutBox(final int alignment, final Border border) {
        _horizontal = alignment == SwingConstants.HORIZONTAL;
        if (border != null)
            setBorder(border);
        setLayout(new GridBagLayout());
        _componentInsets = new Insets(3, 3, 3, 3);
    }

    /**
     * Adds the given component with the specified weight.
     *
     * @param component component to add.
     * @param weight    the component's weight.
     * @return this (for method chaining)
     */
    public LayoutBox add(final Component component, final double weight) {
        return add(component, weight, _componentInsets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    /**
     * Adds the given component with the specified weight and insets.
     *
     * @param component component to add.
     * @param weight    the component's weight.
     * @param insets    the insets to set for the component.
     * @return this (for method chaining)
     */
    public LayoutBox add(final Component component, final double weight, final Insets insets) {
        return add(component, weight, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    /**
     * Adds the given component with the specified weight, fill and anchor properties.
     *
     * @param component component to add.
     * @param weight    the component's weight.
     * @param anchor    the anchor property to set (see GridBagLayout for details)
     * @param fill      the fill property to set (see GridBagLayout for details)
     * @return this (for method chaining)
     */
    public LayoutBox add(final Component component, final double weight, final int anchor, final int fill) {
        return add(component, weight, _componentInsets, anchor, fill);
    }

    /**
     * Adds the given component with the specified weight, fill and anchor properties.
     *
     * @param component component to add.
     * @param weight    the component's weight.
     * @param insets    to use for the component
     * @param anchor    the anchor property to set (see GridBagLayout for details)
     * @param fill      the fill property to set (see GridBagLayout for details)
     * @return this (for method chaining)
     */
    public LayoutBox add(final Component component, final double weight, final Insets insets,
                         final int anchor, final int fill) {
        add(component, new GridBagConstraints(_horizontal ? -1 : 0, _horizontal ? 0 : -1, 1, 1,
                _horizontal ? weight : 1.0, _horizontal ? 1.0 : weight, anchor, fill, insets, 0, 0));
        return this;
    }

    /**
     * Shortcut for adding a new JLabel with the given text and a weight of 0.
     *
     * @param text Text for the added label.
     * @return this (for method chaining)
     */
    public LayoutBox add(final String text) {
        return add(new JLabel(text), 0, _componentInsets,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    /**
     * Shortcut for adding a new JLabel with the given text and the specified weight.
     *
     * @param text   Text for the added label.
     * @param weight weight for the added label.
     * @return this (for method chaining)
     */
    public LayoutBox add(final String text, final double weight) {
        return add(new JLabel(text), weight, _componentInsets,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    /**
     * Shortcut for adding a new JLabel with the given text and the specified weight and insets.
     *
     * @param text   Text for the added label.
     * @param weight weight for the added label.
     * @param insets Insets for the added label.
     * @return this (for method chaining)
     */
    public LayoutBox add(final String text, final double weight, final Insets insets) {
        return add(new JLabel(text), weight, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    /**
     * Shortcut for adding a new JLabel with the given text and the specified weight, anchor and fill.
     *
     * @param text   Text for the added label.
     * @param weight weight for the added label.
     * @param anchor the anchor property to set (see GridBagLayout for details)
     * @param fill   the fill property to set (see GridBagLayout for details)
     * @return this (for method chaining)
     */
    public LayoutBox add(final String text, final double weight, final int anchor, final int fill) {
        return add(new JLabel(text), weight, _componentInsets, anchor, fill);
    }

    /**
     * Shortcut for adding a new JLabel with the given text and the specified weight, anchor and fill.
     *
     * @param text   Text for the added label.
     * @param weight weight for the added label.
     * @param insets Insets for the added label.
     * @param anchor the anchor property to set (see GridBagLayout for details)
     * @param fill   the fill property to set (see GridBagLayout for details)
     * @return this (for method chaining)
     */
    public LayoutBox add(final String text, final double weight, final Insets insets,
                         final int anchor, final int fill) {
        return add(new JLabel(text), weight, insets, anchor, fill);
    }

    /**
     * Adds a glue with the specified weight. A glue works as a filler, leaving an empty space in the LayoutBox. For
     * fixed-size spacers use addStrut(int).
     *
     * @param weight Weight property for the added glue.
     * @return this (for method chaining)
     */
    public LayoutBox addGlue(final double weight) {
        return add(Box.createGlue(), weight);
    }

    /**
     * Adds a strut of the given size. A strut is a fixed-size filler, leaving an empty space in the LayoutBox. For
     * variable-size spacers use addGlue(double).
     *
     * @param extent The size of this strut in pixels.
     * @return this (for method chaining)
     */
    public LayoutBox addStrut(final int extent) {
        return add(_horizontal ? Box.createHorizontalStrut(extent) : Box.createVerticalStrut(extent), 0d);
    }

    /**
     * Adds a JSeparator instance to this LayoutBox.
     *
     * @return this (for method chaining)
     */
    public LayoutBox addSeparator() {
        return add(new JSeparator(_horizontal ? JSeparator.VERTICAL : JSeparator.HORIZONTAL), 0d);
    }

    /**
     * Returns the default component insets used when adding components.
     *
     * @return the default component insets used when adding components.
     */
    public Insets getComponentInsets() {
        return _componentInsets;
    }

    /**
     * Sets the default component insets used when adding components.
     *
     * @param insets the default component insets used when adding components.
     * @return this (for method chaining)
     */
    public LayoutBox setComponentInsets(final Insets insets) {
        _componentInsets = insets;
        return this;
    }

    /**
     * Sets this LayoutBox's border to a titled border with the given title.
     *
     * @param title title for the new border.
     * @return this (for method chaining)
     */
    public LayoutBox withTitledBorder(final String title) {
        if (title == null)
            throw new IllegalArgumentException("Parameter 'title' must not be null!");

        setBorder(BorderFactory.createTitledBorder(title));
        return this;
    }

    /**
     * Sets this LayoutBox's border to the given instance.
     *
     * @param border new Border for this LayoutBox.
     * @return this (for method chaining)
     */
    public LayoutBox withBorder(final Border border) {
        setBorder(border);
        return this;
    }
}
