package net.sarcommand.swingextensions.exception;

import static net.sarcommand.swingextensions.internal.SwingExtResources.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;
import java.awt.*;
import java.beans.*;
import java.io.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class BasicExceptionDialogPaneUI extends ComponentUI {
    public static final String ERROR_ICON = "BasicExceptionDialogPaneUI.errorIcon";
    protected JScrollPane _errorSP;

    public static ComponentUI createUI(JComponent c) {
        if (!(c instanceof ExceptionDialogPane))
            throw new IllegalArgumentException("Cannot create UI for class " + c.getClass().getName());
        return new BasicExceptionDialogPaneUI();
    }

    private JTextPane _messagePane;
    private JTextPane _errorPane;
    private JPanel _contentPane;
    private JPanel _actionPanel;
    private JLabel _iconLabel;

    private SimpleAttributeSet _errorAttributes;

    private ExceptionDialogPane _target;

    public void installUI(final JComponent c) {
        if (c == null)
            throw new IllegalArgumentException("Parameter 'c' must not be null!");
        if (!(c instanceof ExceptionDialogPane))
            throw new IllegalArgumentException("Illegal target object, expected ExceptionDialog, found " +
                    c.getClass().getName());

        _target = (ExceptionDialogPane) c;
        _target.removeAll();

        initComponents();
        initLayout();
        setupEventHandlers();

        _target.setLayout(new GridLayout(1, 1));
        _target.add(_contentPane);

        exceptionPropertyChanged();
        messagePropertyChanged();
    }

    public void uninstallUI(final JComponent c) {
        if (c == null)
            throw new IllegalArgumentException("Parameter 'c' must not be null!");
        if (!(c instanceof ExceptionDialogPane))
            throw new IllegalArgumentException("Illegal target object, expected ExceptionDialog, found " +
                    c.getClass().getName());
        c.removeAll();
    }

    protected void initComponents() {
        _contentPane = new JPanel();
        _actionPanel = new JPanel();

        _messagePane = new JTextPane();
        _messagePane.setEditable(false);
        _messagePane.setBorder(null);
        _messagePane.setOpaque(false);

        _errorAttributes = new SimpleAttributeSet();
        StyleConstants.setForeground(_errorAttributes, Color.RED);

        _errorPane = new JTextPane();
        _errorPane.setEditable(false);
        _errorSP = new JScrollPane(_errorPane);
        _errorSP.setVisible(false);
        _errorSP.setPreferredSize(new Dimension(_errorPane.getPreferredSize().width, 200));

        _iconLabel = new JLabel(getIconResource(ERROR_ICON));
    }

    protected void initLayout() {
        _actionPanel.setLayout(new GridBagLayout());
        layoutActionPanel();

        _contentPane.setLayout(new GridBagLayout());
        _contentPane.add(_iconLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(16, 8, 8, 8), 0, 0));
        _contentPane.add(_messagePane, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(16, 8, 3, 8), 0, 0));
        _contentPane.add(_errorSP, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 6, 3, 6), 0, 0));
        _contentPane.add(_actionPanel, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {
        final ExceptionDialog dlg = _target.getDialog();
        dlg.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String name = evt.getPropertyName();
                if (name.equals(ExceptionDialog.EXCEPTION_PROPERTY))
                    exceptionPropertyChanged();
                else if (name.equals(ExceptionDialog.MESSAGE_PROPERTY))
                    messagePropertyChanged();
            }
        });
    }

    protected void exceptionPropertyChanged() {
        final Throwable e = _target.getDialog().getException();
        _errorPane.setText("");
        if (e != null) {
            final StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            final String stackTrace = writer.toString();
            _errorPane.setText(stackTrace);
            _errorPane.getStyledDocument().setCharacterAttributes(0, stackTrace.length(), _errorAttributes, true);

            if (_target.getDialog().getMessage() == null)
                _messagePane.setText(e.getMessage());
            setDetailSectionVisible(true);
            _errorPane.setCaretPosition(0);
        } else
            setDetailSectionVisible(false);

    }

    protected void messagePropertyChanged() {
        String message = _target.getDialog().getMessage();
        if (message == null) {
            final Throwable throwable = _target.getDialog().getException();
            message = throwable != null ? throwable.getMessage() : "";
        }

        _messagePane.setText(message);
    }

    protected boolean isDetailSectionVisible() {
        return _errorSP.isVisible();
    }

    public void toggleDetailSectionVisible() {
        setDetailSectionVisible(!isDetailSectionVisible());
    }

    public void setDetailSectionVisible(boolean visible) {
        _errorSP.setVisible(visible);
        _contentPane.validate();
    }

    protected void layoutActionPanel() {
        _actionPanel.removeAll();
        final Action[] actions = _target.getDialog().getActions();
        for (Action a : actions) {
            _actionPanel.add(new JButton(a), new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 4, 4));
        }
    }
}

