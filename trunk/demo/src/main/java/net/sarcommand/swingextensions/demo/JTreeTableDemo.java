package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.treetable.AbstractTreeTableModel;
import net.sarcommand.swingextensions.treetable.JTreeTable;
import net.sarcommand.swingextensions.treetable.TreeTableModel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class JTreeTableDemo extends DemoClass {
    private JTreeTable _treeTable;

    public JTreeTableDemo() {
        initialize();
    }

    public String getDemoName() {
        return "JTreeTable Demo";
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
    }

    protected void initComponents() {
        _treeTable = new JTreeTable();
        final TreeTableModel tableModel = new AbstractTreeTableModel(createTree()) {
            public int getColumnCount() {
                return 1;
            }

            public String getColumnLabel(final int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return "Size";
                    default:
                        return null;
                }
            }

            public Object getValueAt(final TreePath path, final int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        final File f = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                        return f.length();
                    default:
                        return null;
                }
            }
        };
        _treeTable.setModel(tableModel);
        _treeTable.setShowGrid(false);
    }

    protected void initLayout() {
        setLayout(new GridLayout(1, 1));
        add(new JScrollPane(_treeTable));
    }

    protected void setupEventHandlers() {

    }

    private DefaultMutableTreeNode createTree() {
        final File home = FileSystemView.getFileSystemView().getHomeDirectory();
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode(home);
        addFilesRecursively(root, home, 0);
        return root;
    }

    private void addFilesRecursively(final DefaultMutableTreeNode node, final File file, final int depth) {
        final File[] files = file.listFiles();
        for (File f : files) {
            final DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
            node.add(child);

            if (f.isDirectory() && depth < 4)
                addFilesRecursively(child, f, depth + 1);
        }
    }
}
