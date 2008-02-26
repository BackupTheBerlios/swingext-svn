package net.sarcommand.swingextensions.beaneditors;

import net.sarcommand.swingextensions.typedinputfields.DoubleInputField;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.prefs.Preferences;

/**
 * BeanEditor implementation used for Double properties. This implementation is backed by a DoubleInputField.
 */
public class DoubleFieldEditor extends BeanEditor<Double, DoubleInputField> {
    private DoubleInputField _inputField;

    public DoubleFieldEditor(final Object targetBean, final String property) {
        super(targetBean, property);
        initialize();
    }

    public DoubleFieldEditor(final Object targetBean, final String property, final String prefKey) {
        super(targetBean, property, prefKey);
        initialize();
    }

    public DoubleFieldEditor(final Object targetBean, final String property, final String prefKey, final Preferences prefs) {
        super(targetBean, property, prefKey, prefs);
        initialize();
    }

    protected void initialize() {
        _inputField = new DoubleInputField();
        setLayout(new GridLayout(1, 1));
        add(_inputField);

        _inputField.addFocusListener(new FocusAdapter() {
            public void focusLost(final FocusEvent e) {
                setValue(_inputField.getValue());
            }
        });
        super.initialize();
    }

    protected void beanValueUpdated() {
        _inputField.setValue(getValue());
    }

    public DoubleInputField getEditor() {
        return _inputField;
    }

    public Class<Double> getValueClass() {
        return Double.class;
    }
}
