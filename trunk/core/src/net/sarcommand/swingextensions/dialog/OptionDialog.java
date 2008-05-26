package net.sarcommand.swingextensions.dialog;

import net.sarcommand.swingextensions.internal.SwingExtResources;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * A dialog implementation which can be used to prompt the user to select a value from a list. This works pretty much
 * analogous to the JOptionPane counterpart, only that a proper JList is being used, making this implementation more
 * suitable for a larger number of items. <hr/> Copyright 2006-2008 Torsten Heup
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
public class OptionDialog extends JDialog {
    public static final String OK_BUTTON_ACTION = "OptionDialog.buttonOK";
    public static final String CANCEL_BUTTON_ACTION = "OptionDialog.buttonCancel";
    public static final String INFO_ICON = "OptionDialog.infoIcon";

    private static OptionDialog __sharedDialog;

    public static synchronized Object display(final Component parent, final String message,
                                              final Object... choices) {
        return display(parent, message, Arrays.asList(choices), null);
    }

    public static synchronized Object display(final Component parent, final String message,
                                              final Collection choices) {
        return display(parent, message, choices, null);
    }

    public static synchronized Object display(final Component parent, final String message,
                                              final Collection choices, final Object selection) {
        if (__sharedDialog == null)
            __sharedDialog = new OptionDialog();

        __sharedDialog.setMessage(message);
        __sharedDialog.setContent(choices);
        if (selection != null)
            __sharedDialog.setSelection(selection);

        return __sharedDialog.show(parent);
    }

    private JLabel _infoLabel;
    private JList _list;
    private JTextPane _messagePane;
    private JScrollPane _messageSection;
    private JButton _okButton;
    private JButton _cancelButton;
    private JPanel _contentPane;

    private Action _okAction;
    private Action _cancelAction;

    private Object _ret;

    public OptionDialog() {
        initialize();
    }

    public OptionDialog(final String message, final Collection elements) {
        initialize();
        setMessage(message);
        setContent(elements);
    }

    public OptionDialog(final String message, final Collection elements, final Object selection) {
        initialize();
        setMessage(message);
        setContent(elements);
        setSelection(selection);
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
        initFrame();
    }

    protected void initComponents() {
        _contentPane = new JPanel();

        _list = new JList();
        _infoLabel = new JLabel(SwingExtResources.getIconResource(INFO_ICON));

        _messagePane = new JTextPane();
        _messagePane.setEditable(false);
        _messagePane.setOpaque(false);
        final JPanel interim = new JPanel(new GridBagLayout());
        interim.add(_messagePane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        _messageSection = new JScrollPane(interim);
        _messageSection.getViewport().setOpaque(false);
        _messageSection.setBorder(null);
        _messageSection.setOpaque(false);

        _okAction = SwingExtResources.getActionResource(OK_BUTTON_ACTION, this, "handleOK");
        _okButton = new JButton(_okAction);
        _cancelAction = SwingExtResources.getActionResource(CANCEL_BUTTON_ACTION, this, "handleCancel");
        _cancelButton = new JButton(_cancelAction);
    }

    protected void initLayout() {
        _contentPane.setLayout(new GridBagLayout());

        _contentPane.add(_infoLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(16, 16, 16, 8), 0, 0));
        _contentPane.add(_messageSection, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(16, 8, 16, 16), 0, 0));
        _contentPane.add(new JScrollPane(_list), new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 16, 3, 16), 0, 0));
        _contentPane.add(_okButton, new GridBagConstraints(0, 2, 2, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(3, 8, 8, 8), 0, 0));
        _contentPane.add(_cancelButton, new GridBagConstraints(2, 2, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(3, 8, 8, 8), 0, 0));
    }

    protected void setupEventHandlers() {
        _list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "okAction");
        _list.getActionMap().put("okAction", _okAction);
        _list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    _okAction.actionPerformed(new ActionEvent(_list, ActionEvent.ACTION_PERFORMED, ""));
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                _list.requestFocusInWindow();
            }
        });
    }

    protected void initFrame() {
        setSize(400, 250);
        setLocationRelativeTo(null);
        setContentPane(_contentPane);
        setModal(true);
    }

    public void setContent(final Collection elements) {
        final DefaultListModel model = new DefaultListModel();
        for (Object o : elements)
            model.addElement(o);
        _list.setModel(model);
        _list.setSelectedIndex(0);
    }

    public void setSelection(final Object selection) {
        _list.setSelectedValue(selection, true);
    }

    public void setMessage(final String message) {
        _messagePane.setText(message);
    }

    public Object show(final Component parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
        return _ret;
    }

    public StyledDocument getMessageDocument() {
        return _messagePane.getStyledDocument();
    }

    public Action getOKAction() {
        return _okAction;
    }

    public Action getCancelAction() {
        return _cancelAction;
    }

    public void handleOK() {
        _ret = _list.getSelectedValue();
        setVisible(false);
    }

    public void handleCancel() {
        _ret = null;
        setVisible(false);
    }

    public ListCellRenderer getRenderer() {
        return _list.getCellRenderer();
    }

    public void setCellRenderer(final ListCellRenderer renderer) {
        _list.setCellRenderer(renderer);
    }
}
