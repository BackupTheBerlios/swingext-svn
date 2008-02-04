package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.layout.MultiCellSplitPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class MultiCellSplitPaneDemo extends DemoClass {
    private JDesktopPane _desktop;
    private MultiCellSplitPane _cPane;

    public MultiCellSplitPaneDemo() {
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initSplitPanel() {
        final JTextArea textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createTitledBorder("A textArea"));
        textArea.setMinimumSize(new Dimension(40, 80));

        final JTable table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][]{{"John", "Doe"}, {"Jane", "Doe"}},
                new Object[]{"First", "Last"}));
        final JScrollPane tablePane = new JScrollPane(table);
        tablePane.setMinimumSize(new Dimension(120, 80));
        tablePane.setPreferredSize(new Dimension(240, 80));

        final JScrollPane treePane = new JScrollPane(new JTree());
        treePane.setMinimumSize(new Dimension(100, 1));

        final MultiCellSplitPane center = new MultiCellSplitPane(SwingConstants.VERTICAL);
        center.add(new JLabel("Label:"), 0d);
        center.add(new JScrollPane(new JTextArea()), 1d);
        center.add(new JButton("Button"), 0d);

        final MultiCellSplitPane northern = new MultiCellSplitPane();
        northern.add(treePane);
        northern.add(center);
        northern.add(tablePane);

        _cPane = new MultiCellSplitPane(SwingConstants.VERTICAL);
        _cPane.add(northern);
        _cPane.add(textArea);
    }

    protected void initComponents() {
        initSplitPanel();
        _desktop = new JDesktopPane();

        final JInternalFrame frame = new JInternalFrame("An exemplary frame");
        frame.setContentPane(_cPane);
        frame.setClosable(false);
        frame.setResizable(true);
        frame.setSize(600, 300);
        frame.setLocation(100, 30);
        _desktop.add(frame);
        frame.setVisible(true);
    }

    protected void initLayout() {
        setLayout(new GridLayout(1, 1));
        add(_desktop);
    }

    protected void setupEventHandlers() {

    }

    public String getDemoName() {
        return "MultiSplitPane Demo";
    }

}
