package net.sarcommand.swingextensions.exception;

import javax.swing.*;
import java.awt.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ExceptionDialogPane extends JComponent {
    public static final String UI_KEY = "ExceptionDialogPane.UI";
    private ExceptionDialog _dialog;

    protected ExceptionDialogPane(final ExceptionDialog parent) {
        _dialog = parent;
        UIManager.getUI(this).installUI(this);
    }

    public String getUIClassID() {
        System.out.println("getUIClass");
        if (!UIManager.getDefaults().containsKey(UI_KEY))
            UIManager.getDefaults().put(UI_KEY, BasicExceptionDialogPaneUI.class.getName());
        return UI_KEY;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(0, 0, 100, 100);
    }

    public ExceptionDialog getDialog() {
        return _dialog;
    }
}
