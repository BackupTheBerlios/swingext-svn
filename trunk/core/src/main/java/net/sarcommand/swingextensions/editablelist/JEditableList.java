package net.sarcommand.swingextensions.editablelist;

import net.sarcommand.swingextensions.actions.ReflectedAction;
import net.sarcommand.swingextensions.utilities.SwingExtUtil;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

/**
 * A JList implementation which can be made editable. This implementation subclasses JList (since quite a bit work has
 * to be done to add editing support) and can thus be used as any normal list, with one exception. Even though the API
 * allows setting any kind of ListModel, this implementation expects an instance of EditableListModel. If you set any
 * other model implementation, the JEditableList will behave like any other JList. <hr/> Copyright 2006-2012 Torsten
 * Heup
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
public class JEditableList extends JList {
    /**
     * Input map key for the 'cancelEditing' action.
     */
    protected static final String INPUT_MAP_CANCEL_EDITING = "cancelEditing";

    /**
     * Input map key for the 'startCellEditing' action.
     */
    protected static final String INPUT_MAP_START_EDITING = "startCellEditing";

    /**
     * The component currently used for editing.
     */
    protected Component _editorComponent;

    /**
     * The index of the element currently being edited.
     */
    protected int _editingIndex;

    /**
     * The ListCellEditor instance being used.
     */
    protected ListCellEditor _cellEditor;

    /**
     * The listener instance used to observe the current cell editor.
     */
    protected CellEditorListener _cellEditorListener;

    public JEditableList() {
        super();
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();

        setCellEditor(new DefaultListCellEditor(new JTextField()));
    }

    protected void initComponents() {
        final Action cancelAction = new ReflectedAction("cancelCellEditing", this, "cancelEditing");
        final Action startAction = new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                editIndex(getSelectedIndex());
            }
        };

        final InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), INPUT_MAP_CANCEL_EDITING);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), INPUT_MAP_START_EDITING);
        getActionMap().put(INPUT_MAP_CANCEL_EDITING, cancelAction);
        getActionMap().put(INPUT_MAP_START_EDITING, startAction);
    }

    protected void initLayout() {

    }

    protected void setupEventHandlers() {
        final MouseAdapter adapter = new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                checkForEdit(e);
            }

            public void mousePressed(final MouseEvent e) {
                checkForEdit(e);
            }

            public void mouseReleased(final MouseEvent e) {
                checkForEdit(e);
            }
        };

        _cellEditorListener = new CellEditorListener() {
            public void editingStopped(final ChangeEvent e) {
                JEditableList.this.editingStopped();
            }

            public void editingCanceled(final ChangeEvent e) {
                removeEditor();
            }
        };
        addMouseListener(adapter);

        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                if (isEditing() && (evt.getNewValue() == JEditableList.this || (evt.getNewValue() != null &&
                        !SwingExtUtil.isDescendant(JEditableList.this, (Component) evt.getNewValue())))) {
                    cancelEditing();
                }
            }
        };

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
                "permanentFocusOwner", listener);
    }

    /**
     * When invoked, this method will remove the current cell editor component.
     */
    protected void removeEditor() {
        remove(_editorComponent);
        revalidate();
    }

    /**
     * Returns whether the list is currently being edited.
     *
     * @return whether the list is currently being edited.
     */
    public boolean isEditing() {
        return _editorComponent != null;
    }

    /**
     * Returns the current editor component, if the list is being edited. Otherwise, null will be returned.
     *
     * @return the current editor component, or null if the list is not being edited.
     */
    public Component getEditorComponent() {
        return _editorComponent;
    }

    /**
     * Returns the index of the element currently being edited, or -1 if the list is not being edited.
     *
     * @return the index of the element currently being edited, or -1 if the list is not being edited.
     */
    public int getEditingIndex() {
        return _editingIndex;
    }

    /**
     * Invoked when the current edit has stopped. This works analogous to the JTable counterpart.
     */
    protected void editingStopped() {
        ((EditableListModel) getModel()).setValue(getCellEditor().getCellEditorValue(), _editingIndex);
        removeEditor();
    }

    /**
     * Checks if the given event object should trigger a new edit.
     *
     * @param e Event which might trigger a new edit.
     */
    protected void checkForEdit(final EventObject e) {

        final ListCellEditor listCellEditor = getCellEditor();
        if (listCellEditor == null)
            return;

        int row = e instanceof MouseEvent ? locationToIndex(((MouseEvent) e).getPoint()) : getSelectedIndex();
        if (row < 0)
            return;

        if (isEditing() && getEditingIndex() == row) {
            if (e instanceof MouseEvent) {
                final Component editor = getEditorComponent();
                final MouseEvent eventInEditor = SwingUtilities.convertMouseEvent(JEditableList.this,
                        (MouseEvent) e, editor);
                editor.dispatchEvent(eventInEditor);
            }
        }

        if (!(getModel() instanceof EditableListModel && ((EditableListModel) getModel()).isIndexEditable(row)))
            return;

        if (listCellEditor.isCellEditable(e))
            editIndex(row, e);
    }

    /**
     * Returns the installed cell editor.
     *
     * @return the installed cell editor.
     */
    public ListCellEditor getCellEditor() {
        return _cellEditor;
    }

    /**
     * Sets the cell editor to use for altering values.
     *
     * @param cellEditor the cell editor to use for altering values.
     */
    public void setCellEditor(final ListCellEditor cellEditor) {
        if (_cellEditor != null)
            _cellEditor.removeCellEditorListener(_cellEditorListener);
        _cellEditor = cellEditor;
        _cellEditor.addCellEditorListener(_cellEditorListener);
    }

    /**
     * Cancels the current edit, if applicable.
     */
    public void cancelEditing() {
        final ListCellEditor cellEditor = getCellEditor();

        final Runnable runnable = new Runnable() {
            public void run() {
                if (cellEditor != null)
                    cellEditor.cancelCellEditing();
            }
        };

        if (SwingUtilities.isEventDispatchThread())
            runnable.run();
        else {
            try {
                SwingUtilities.invokeAndWait(runnable);
                repaint();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Manually triggers editing the specified index.
     *
     * @param index index of the element which should be edited.
     */
    public void editIndex(final int index) {
        editIndex(index, null);
    }

    /**
     * Triggers editing the specified index, caused by the given event object. If the list is already being edited the
     * current edit will be cancelled.
     *
     * @param index index to be edited.
     * @param e     event object triggering this edit, may be null.
     */
    public void editIndex(final int index, final EventObject e) {
        final ListCellEditor listCellEditor = getCellEditor();
        if (listCellEditor == null)
            return;

        if (isEditing())
            cancelEditing();

        final Component editor = listCellEditor.getListCellEditorComponent(this, getModel().getElementAt(index),
                isSelectedIndex(index), index);
        if (editor == null)
            return;
        if (editor instanceof JComponent) {
            final JComponent c = (JComponent) editor;
            if (c.getNextFocusableComponent() != null)
                c.setNextFocusableComponent(this);
        }
        _editingIndex = index;
        _editorComponent = editor;

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
        editor.setBounds(getCellBounds(index, index));
        add(editor);
        revalidate();
        editor.requestFocusInWindow();
        if (e != null && e instanceof MouseEvent) {
            final MouseEvent eventInEditor = SwingUtilities.convertMouseEvent(JEditableList.this, (MouseEvent) e,
                    editor);
            editor.dispatchEvent(eventInEditor);
        }
//            }
//        });
    }
}
