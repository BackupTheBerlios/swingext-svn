package net.sarcommand.swingextensions.layout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Feb 10, 2010
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class LayoutBox extends JPanel {
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

    public Insets getComponentInsets() {
        return _componentInsets;
    }

    public void setComponentInsets(final Insets insets) {
        _componentInsets = insets;
    }
}
