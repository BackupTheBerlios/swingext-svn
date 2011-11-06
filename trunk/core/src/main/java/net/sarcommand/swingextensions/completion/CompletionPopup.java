package net.sarcommand.swingextensions.completion;

import net.sarcommand.swingextensions.event.*;
import net.sarcommand.swingextensions.utilities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class implements the popup window shown by the CompletionSupport class in order to offer suggestions.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
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
public class CompletionPopup {
    /**
     * The list instance used. This instance won't be changed once it has been initialized, all changes are made through
     * the model.
     */
    private JList _list;

    /**
     * The dialog instance used to realize the popup. It is being configured to be always on top and non-focusable.
     */
    private JDialog _dialog;

    /**
     * The owner component for this popup. The popup will vanish as soon as this owner loses the shared focus.
     */
    private JComponent _owner;

    /**
     * Window listener used to track events which indicate that the owner's parent window has lost its focus.
     */
    private WindowListener _windowListener;

    /**
     * Window listener used to track events which indicate that the owner component has lost shared focus.
     */
    private FocusListener _focusListener;

    private EventSupport<ActionListener> _actionListeners;
    protected JScrollPane _contentPane;

    public CompletionPopup() {
        initialize();
    }

    public void addActionListener(final ActionListener listener) {
        _actionListeners.addListener(listener);
    }

    public void removeListener(final ActionListener listener) {
        _actionListeners.removeListener(listener);
    }

    protected void fireActionEvent() {
        final ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Item selected");
        _actionListeners.delegate().actionPerformed(event);
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _actionListeners = EventSupport.create(ActionListener.class);

        _list = new JList();
        _list.setModel(new DefaultListModel());
        _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _list.setFocusable(false);

        _contentPane = new JScrollPane(_list);
        _contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        _dialog = new JDialog();
        _dialog.setFocusable(false);

        _dialog.setContentPane(_contentPane);
        _dialog.setUndecorated(true);
        _dialog.setAlwaysOnTop(true);

    }

    protected void initLayout() {
    }

    protected void setupEventHandlers() {
        _list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    fireActionEvent();
            }
        });

        _windowListener = new WindowAdapter() {
            public void windowLostFocus(WindowEvent e) {
                hide();
            }

            public void windowDeactivated(WindowEvent e) {
                if (e.getOppositeWindow() != _dialog)
                    hide();
            }

            public void windowIconified(WindowEvent e) {
                hide();
            }

            public void windowClosing(WindowEvent e) {
                hide();
            }
        };

        _focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                if (!e.isTemporary() && e.getOppositeComponent() != _list)
                    hide();
            }
        };
    }

    public void setCellRenderer(final ListCellRenderer renderer) {
        _list.setCellRenderer(renderer);
    }

    /**
     * Displays the popup for the specified component. The popup's size and position will be adjusted to match this
     * component, and it will be assured that it is fully visible on screen at all times.
     *
     * @param owner Owner component for this popup.
     */
    public void show(final JComponent owner) {
        if (_owner != owner) {
            if (_owner != null) {
                _owner.removeFocusListener(_focusListener);
                SwingExtUtil.getWindowForComponent(_owner).removeWindowListener(_windowListener);
            }

            if (!owner.isShowing())
                return;
            _owner = owner;

            final int height = 120;
            final int maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
            final Point location = owner.getLocationOnScreen();
            final int y = location.y + owner.getHeight() + 120 > maxY ? location.y - 120
                    : location.y + owner.getHeight();
            _dialog.setSize(owner.getWidth(), height);
            _dialog.setLocation(location.x, y);
            _dialog.setVisible(true);

            _owner.addFocusListener(_focusListener);
            JOptionPane.getFrameForComponent(_owner).addWindowListener(_windowListener);
        }

        owner.requestFocus();
    }

    /**
     * Sets the elements which should be displayed in the enclosed JList.
     *
     * @param elements The elements which should be displayed in the enclosed JList.
     */
    public void setElements(final Collection elements) {
        final DefaultListModel model = (DefaultListModel) _list.getModel();
        model.clear();
        for (Object o : elements)
            model.addElement(o);
        _list.setSelectedIndex(0);
    }

    public void moveCursorDown() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final int index = _list.getSelectedIndex();
                final int listSize = _list.getModel().getSize() - 1;
                final int newIndex = index == listSize ? 0 : index + 1;
                _list.setSelectedValue(_list.getModel().getElementAt(newIndex), true);
            }
        });
    }

    public void moveCursorUp() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final int index = _list.getSelectedIndex();
                final int listSize = _list.getModel().getSize() - 1;
                final int newIndex = index == 0 ? listSize : index - 1;
                _list.setSelectedValue(_list.getModel().getElementAt(newIndex), true);
            }
        });
    }

    /**
     * Returns whether this popup is currently visible.
     *
     * @return Whether this popup is currently visible.
     */
    public boolean isVisible() {
        return _dialog.isShowing();
    }

    /**
     * Hides this popup.
     */
    public void hide() {
        _dialog.setVisible(false);
    }

    /**
     * Returns the currently selected value.
     *
     * @return The currently selected value.
     */
    public Object getSelectedValue() {
        return _list.getSelectedValue();
    }
}
