package net.sarcommand.swingextensions.beaneditors;

import net.sarcommand.swingextensions.typedinputfields.DoubleInputField;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.prefs.Preferences;

/**
 * BeanEditor implementation used for Double properties. This implementation is backed by a DoubleInputField.
 */
public class DoubleFieldEditor extends BeanEditor<Double, DoubleInputField> {
    private DoubleInputField _inputField;
    private volatile boolean _internalUpdate;

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
                if (_internalUpdate)
                    return;
                _internalUpdate = true;
                setValue(_inputField.getValue());
                _internalUpdate = false;
            }
        });

        _inputField.addCaretListener(new CaretListener() {
            public void caretUpdate(final CaretEvent e) {
                if (_internalUpdate)
                    return;
                final Double newValue = _inputField.getValue();

                if (newValue != null && !newValue.equals(getValue())) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            _internalUpdate = true;
                            setValue(newValue);
                            _internalUpdate = false;
                        }
                    });
                }
            }
        });
        super.initialize();
    }

    protected void beanValueUpdated() {
        if (_internalUpdate)
            return;
        _inputField.setValue(getValue());
    }

    public DoubleInputField getEditor() {
        return _inputField;
    }

    public Class<Double> getValueClass() {
        return Double.class;
    }
}
