package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.applicationsupport.ImageCache;
import net.sarcommand.swingextensions.exception.ExceptionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo for the exception dialog class.
 */
public class ExceptionDialogDemo extends DemoClass {
    protected ExceptionDialog _normalDialog;
    protected ExceptionDialog _customizedDialog;

    public ExceptionDialogDemo() {
        _normalDialog = new ExceptionDialog();
        _normalDialog.getActionShutdown().setEnabled(false);
        _normalDialog.setTitle("Application error");

        _customizedDialog = new ExceptionDialog();
        _customizedDialog.getActionShutdown().setEnabled(false);
        _customizedDialog.setTitle("A customized exception dialog");
        final Action action = new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
            }
        };
        action.putValue(Action.NAME, "Submit a bugreport");
        action.putValue(Action.SMALL_ICON, ImageCache.loadIcon("images/iconMail_16x16.png"));
        _customizedDialog.setActions(_customizedDialog.getActionShutdown(), action,
                _customizedDialog.getActionContinue());

        setLayout(new GridBagLayout());

        final JButton throwExceptionButton = new JButton("Throw Exception");
        final JButton showCustomizedButton = new JButton("Show customized ExceptionDialog");

        final ActionListener listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                throwException(e.getSource() == showCustomizedButton);
            }
        };

        throwExceptionButton.addActionListener(listener);
        showCustomizedButton.addActionListener(listener);

        add(throwExceptionButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        add(showCustomizedButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));

    }

    public String getDemoName() {
        return "ExceptionDialog demo";
    }

    public static void main(String[] args) {
        new ExceptionDialogDemo();
    }

    public void throwException(final boolean useCustomizedDialog) {
        try {
            throw new RuntimeException("");
        } catch (Exception e) {
            final ExceptionDialog dlg = useCustomizedDialog ? _customizedDialog : _normalDialog;
            dlg.display(null, "An unexpected error has occured in the application. You might be able to continue working, " +
                    "but it is highly advised that you save your work and restart afterwards. Please " +
                    "submit a bugreport, reiterating the steps which lead to this incident.", e);
        }
    }
}
