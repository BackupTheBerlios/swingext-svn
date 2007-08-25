package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.misc.BlockingGlassPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo class for the BlockingGlassPane class.
 * <hr/>
 * Copyright 2006 Torsten Heup
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
public class BlockingGlassPaneDemo extends DemoClass {
    private JDesktopPane _desktop;
    private JInternalFrame _internalFrame;

    private BlockingGlassPane _glassPane;

    private JCheckBox _visibleCB;

    private Timer _messageTimer;
    private int _messageIndex;

    public BlockingGlassPaneDemo() {
        initialize();
    }

    public String getDemoName() {
        return "Blocking GlassPane Demo";
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initDemoFrame() {
        _internalFrame = new JInternalFrame("An exemplary frame");

        final JTextPane textPane = new JTextPane();
        textPane.setText("Some text...");

        final JPanel cPane = new JPanel(new GridBagLayout());
        cPane.setBorder(BorderFactory.createTitledBorder("Just a random bunch of components"));

        cPane.add(new JTree(), new GridBagConstraints(0, 0, 1, 2, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        cPane.add(textPane, new GridBagConstraints(1, 0, 2, 1, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        cPane.add(new JLabel("A Label", JLabel.CENTER), new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        cPane.add(new JButton("A Button"), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        _internalFrame.setContentPane(cPane);
        _internalFrame.setGlassPane(_glassPane);
        _internalFrame.setResizable(true);
        _internalFrame.setLocation(100, 30);
        _internalFrame.setSize(600, 300);
        _internalFrame.setVisible(true);
    }

    protected void initComponents() {
        _glassPane = new BlockingGlassPane();

        initDemoFrame();
        _desktop = new JDesktopPane();
        _desktop.add(_internalFrame);

        _visibleCB = new JCheckBox("GlassPane visible");
    }

    protected void initLayout() {
        setLayout(new GridBagLayout());
        add(_desktop, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(3, 3, 3, 3), 0, 0));
        add(_visibleCB, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(3, 3, 3, 3), 0, 0));
    }

    protected void setupEventHandlers() {
        _messageTimer = new Timer(2500, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                timerUpdate();
            }
        });
        _visibleCB.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                _glassPane.setVisible(_visibleCB.isSelected());
                if (_visibleCB.isSelected()) {
                    _messageIndex = 0;
                    _messageTimer.restart();
                    timerUpdate();
                }
            }
        });
    }

    private void timerUpdate() {
        switch (_messageIndex) {
            case 0:
                _glassPane.setMessage("This is the blocking glass pane in action...");
                break;
            case 1:
                _glassPane.setMessage("...you can set custom messages here...");
                break;
            case 2:
                final SimpleAttributeSet set = new SimpleAttributeSet();
                StyleConstants.setItalic(set, true);
                StyleConstants.setUnderline(set, true);
                StyleConstants.setForeground(set, Color.RED);
                final StyledDocument doc = _glassPane.getMessageDocument();
                try {
                    doc.remove(0, doc.getLength());
                    doc.insertString(0, "...including styled content...", set);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                break;
            case 3:
                final SimpleAttributeSet set2 = new SimpleAttributeSet();
                StyleConstants.setBackground(set2, Color.BLUE);
                StyleConstants.setForeground(set2, Color.WHITE);
                final StyledDocument doc2 = _glassPane.getMessageDocument();

                try {
                    doc2.remove(0, doc2.getLength());
                    doc2.insertString(0, "...if you enjoy colors...", set2);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                break;
            case 4:
                _glassPane.setMessage("Obviously,\nmultiline\ntext\nworks\nas\nwell");
                break;
            case 5:
                _glassPane.setMessage(":-)");
        }
        _messageIndex++;
        if (_messageIndex > 5)
            _messageIndex = 0;
    }


}
