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

    public LayoutBox() {
        this(SwingConstants.HORIZONTAL, (Border) null);
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
    }


    public LayoutBox add(final Component component, final double weight) {
        return add(component, weight, new Insets(3, 3, 3, 3), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final Component component, final double weight, final Insets insets) {
        return add(component, weight, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final Component component, final double weight, final int anchor, final int fill) {
        return add(component, weight, new Insets(3, 3, 3, 3), anchor, fill);
    }

    public LayoutBox add(final Component component, final double weight, final Insets insets,
                         final int anchor, final int fill) {
        add(component, new GridBagConstraints(_horizontal ? -1 : 0, _horizontal ? 0 : -1, 1, 1,
                _horizontal ? weight : 1.0, _horizontal ? 1.0 : weight, anchor, fill, insets, 0, 0));
        return this;
    }

    public LayoutBox add(final String text) {
        return add(new JLabel(text), 0, new Insets(3, 3, 3, 3),
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final String text, final double weight) {
        return add(new JLabel(text), weight, new Insets(3, 3, 3, 3),
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final String text, final double weight, final Insets insets) {
        return add(new JLabel(text), weight, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    public LayoutBox add(final String text, final double weight, final int anchor, final int fill) {
        return add(new JLabel(text), weight, new Insets(3, 3, 3, 3), anchor, fill);
    }

    public LayoutBox add(final String text, final double weight, final Insets insets,
                         final int anchor, final int fill) {
        return add(new JLabel(text), weight, insets, anchor, fill);
    }
}
