package net.sarcommand.swingextensions.typedinputfields;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Default implementation for the TypedInputFieldEditCallback interface. This implementation will set a red border on
 * the input field if the current input data is incomplete, and perform Toolkit.getToolkit().beep if illegal input
 * has been made.
 */
public class DefaultInputFieldEditCallback implements TypedInputFieldEditCallback {
    protected JComponent _component;
    protected Border _defaultBorder;
    protected Border _errorBorder;

    public DefaultInputFieldEditCallback(final JComponent target) {
        _component = target;
        _defaultBorder = _component.getBorder();
        _errorBorder = BorderFactory.createLineBorder(Color.RED);
    }

    public void inputIllegal(final TypedInputField source) {
        if (source == _component)
            Toolkit.getDefaultToolkit().beep();
    }

    public void inputIncomplete(final TypedInputField source) {
        if (source == _component)
            _component.setBorder(_errorBorder);

    }

    public void inputLegal(final TypedInputField source) {
        if (source == _component)
            _component.setBorder(_defaultBorder);
    }
}
