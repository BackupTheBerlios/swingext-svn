package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.applicationsupport.ImageCache;
import net.sarcommand.swingextensions.editablelist.DefaultEditableListModel;
import net.sarcommand.swingextensions.editablelist.JEditableList;
import net.sarcommand.swingextensions.editablelist.ListCellEditor;
import net.sarcommand.swingextensions.misc.CellRendererUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;

/**
 * Demo class for the JEditableList class. <hr/> Copyright 2006-2008 Torsten Heup
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
public class JEditableListDemo extends DemoClass {
    private JDesktopPane _desktop;
    private JEditableList _list;

    public JEditableListDemo() {
        _list = new JEditableList();
        final DefaultEditableListModel model = new DefaultEditableListModel();
        for (int i = 0; i < 10; i++)
            model.addElement("Item " + i);
        _list.setModel(model);
        _list.setCellRenderer(new DemoRenderer());
//        _list.setCellEditor(new DemoEditor());

        final JRadioButton verticalRadio = new JRadioButton("Vertical", true);
        final JRadioButton horizontalWrapRadio = new JRadioButton("Horizontal Wrap");
        final JRadioButton verticalWrapRadio = new JRadioButton("Vertical Wrap");
        final ButtonGroup group = new ButtonGroup();

        final ActionListener listener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Object source = e.getSource();
                if (source == verticalRadio)
                    _list.setLayoutOrientation(JList.VERTICAL);
                else if (source == verticalWrapRadio)
                    _list.setLayoutOrientation(JList.VERTICAL_WRAP);
                else if (source == horizontalWrapRadio)
                    _list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            }
        };

        final Collection<JRadioButton> buttons = Arrays.asList(verticalRadio, verticalWrapRadio, horizontalWrapRadio);
        for (JRadioButton button : buttons) {
            group.add(button);
            button.addActionListener(listener);
        }

        _desktop = new JDesktopPane();
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(verticalRadio);
        buttonPanel.add(verticalWrapRadio);
        buttonPanel.add(horizontalWrapRadio);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Layout orientation"));

        setLayout(new BorderLayout());
        add(_desktop, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        initInternalFrame();
    }

    public String getDemoName() {
        return "JEditableList demo";
    }

    protected void initInternalFrame() {
        final JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(new JScrollPane(_list));
        final JInternalFrame frame = new JInternalFrame("An editable JList");
        frame.setContentPane(panel);
        frame.setClosable(false);
        frame.setResizable(true);
        frame.setSize(300, 120);
        frame.setLocation(100, 30);
        _desktop.add(frame);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        final JEditableList list = new JEditableList();

        final DefaultEditableListModel mdl = new DefaultEditableListModel();
        mdl.addElement("Foo");
        mdl.addElement("Bar");
        list.setModel(mdl);

        final JFrame frame = new JFrame();
        frame.setContentPane(new JScrollPane(list));
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    protected static class DemoRenderer extends DefaultListCellRenderer {
        public DemoRenderer() {
            super();
        }

        public Component getListCellRendererComponent(final JList list, final Object value, final int index,
                                                      final boolean isSelected, final boolean cellHasFocus) {
            final JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            renderer.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            renderer.setIcon(ImageCache.loadIcon("images/iconFileHtml_16x16.png"));
            return renderer;
        }
    }

    protected static class DemoEditor extends AbstractCellEditor implements ListCellEditor {
        private JPanel _renderer;
        private CellRendererUtility _utility;
        protected JTextField _textField;

        public DemoEditor() {
            _textField = new JTextField();
            _renderer = new JPanel(new BorderLayout());
            _renderer.add(new JLabel(ImageCache.loadIcon("images/iconFileHtml_16x16.png")), BorderLayout.WEST);
            _renderer.add(_textField, BorderLayout.CENTER);

            _utility = new CellRendererUtility();
        }

        public Component getListCellEditorComponent(final JList list, final Object value, final boolean isSelected,
                                                    final int row) {
            _textField.setText(value.toString());
            _utility.adaptToList(_renderer, list, value, row, isSelected, true);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    _textField.requestFocusInWindow();
                }
            });
            return _renderer;
        }

        public Object getCellEditorValue() {
            return _textField.getText();
        }

        public boolean isCellEditable(final EventObject e) {
            return e instanceof MouseEvent && ((MouseEvent) e).getClickCount() > 1;
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
