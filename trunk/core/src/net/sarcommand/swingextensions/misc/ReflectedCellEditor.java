package net.sarcommand.swingextensions.misc;

import net.sarcommand.swingextensions.table.AbstractTableCellEditor;
import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * The ReflectedCellEditor class can be used to create simple editors for JTables using reflection rather
 * than creating a custom subclass. You create an instance of the ReflectedCellEditor by passing a JComponent which
 * will be used as editor and the name of this component's value property. Using reflection and relying on the
 * JavaBean naming scheme, the getter and setter methods will be obtained automatically. For instance, when using
 * a JTextField, you would specify 'text' as the value property. The ReflectedCellEditor will then invoke
 * setText(String) and getText() when required to. Optionally, you can also give the name of a callback method. This
 * method has to be a parameter-less method returning a boolean value. If this method can be found on your editor
 * class, it will be invoked when the JTable calls stopEditing(). Editing will only stop if your callback method
 * returns true.
 */
public class ReflectedCellEditor extends AbstractTableCellEditor {
    protected JComponent _cellEditor;
    protected Method _getter;
    protected Method _setter;
    protected Method _stopEditingCallback;

    protected int _clickCountToStart;

    public ReflectedCellEditor(final JComponent cellEditor, final String valueProperty) {
        initialize(cellEditor, valueProperty, null);
    }

    public ReflectedCellEditor(final JComponent cellEditor, final String valueProperty,
                               final String stopEditingMethod) {
        initialize(cellEditor, valueProperty, stopEditingMethod);
    }

    protected void initialize(final JComponent cellEditor, final String valueProperty, final String stopEditingMethod) {
        if (cellEditor == null)
            throw new IllegalArgumentException("Parameter 'cellEditor' must not be null!");
        if (valueProperty == null)
            throw new IllegalArgumentException("Parameter 'valueProperty' must not be null!");
        _cellEditor = cellEditor;
        _setter = SwingExtUtil.getSetter(cellEditor, valueProperty);
        if (_setter == null)
            throw new IllegalArgumentException("Could not find a suitable setter for property " + valueProperty);

        _getter = SwingExtUtil.getGetter(cellEditor, valueProperty);
        if (_getter == null)
            throw new IllegalArgumentException("Could not find a suitable getter for property " + valueProperty);

        _stopEditingCallback = null;
        if (stopEditingMethod != null) {
            _stopEditingCallback = SwingExtUtil.getMethod(cellEditor, stopEditingMethod);
            if (_stopEditingCallback == null || !Boolean.class.isAssignableFrom(_stopEditingCallback.getReturnType()))
                throw new IllegalArgumentException("Could not find the stopEditing implementation " + stopEditingMethod);
        }
    }

    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
                                                 final int row, final int column) {

        try {
            SwingExtUtil.invokeSetter(_setter, _cellEditor, value);
        } catch (Exception e1) {
            throw new RuntimeException("Could not access setter " + _setter.getName(), e1);
        }
        return _cellEditor;
    }

    public void cancelCellEditing() {
    }

    public Object getCellEditorValue() {
        try {
            return _getter.invoke(_cellEditor);
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke getter " + _getter.getName(), e);
        }
    }

    public boolean isCellEditable(final EventObject anEvent) {
        return !(anEvent instanceof MouseEvent) || ((MouseEvent) anEvent).getClickCount() >= _clickCountToStart;
    }

    public boolean shouldSelectCell(final EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        if (_stopEditingCallback == null) {
            fireEditingStopped(_cellEditor);
            return true;
        }
        try {
            final Boolean result = (Boolean) _stopEditingCallback.invoke(_cellEditor);
            fireEditingStopped(_cellEditor);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Could not access stopEditing callback " + _stopEditingCallback.getName(), e);
        }
    }

    /**
     * Returns the number of clicks required to start editing.
     *
     * @return the number of clicks required to start editing.
     */
    public int getClickCountToStart() {
        return _clickCountToStart;
    }

    /**
     * Sets the number of clicks required to start editing.
     *
     * @param clickCountToStart the number of clicks required to start editing.
     */
    public void setClickCountToStart(final int clickCountToStart) {
        _clickCountToStart = clickCountToStart;
    }
}
