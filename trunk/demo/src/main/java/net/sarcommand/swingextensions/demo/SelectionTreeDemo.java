package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.selectiontree.DefaultSelectionTreeNode;
import net.sarcommand.swingextensions.selectiontree.JSelectionTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * <hr/> Copyright 2006-2008 Torsten Heup
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
public class SelectionTreeDemo extends DemoClass {
    public SelectionTreeDemo() {
        initialize();
    }

    protected void initialize() {
        final DefaultSelectionTreeNode root0 = new DefaultSelectionTreeNode("Items to install");

        final DefaultSelectionTreeNode doc = root0.addChild("Documentation");
        doc.addChild("JavaDoc");
        doc.addChild("Developer FAQ");

        final DefaultSelectionTreeNode comps = root0.addChild("Components");
        comps.addChild("Core components");
        comps.addChild("Source code");
        comps.addChild("Demo examples");

        final JSelectionTree tree0 = new JSelectionTree(root0);

        final DefaultMutableTreeNode root1 = new DefaultMutableTreeNode("Text root");
        final DefaultMutableTreeNode textChild1 = new DefaultMutableTreeNode("Text child 1");
        root1.add(textChild1);

        final DefaultMutableTreeNode textChild2 = new DefaultMutableTreeNode("Text child 2");
        root1.add(textChild2);

        final DefaultSelectionTreeNode selectionChild1 = new DefaultSelectionTreeNode("Selection child 1");
        textChild1.add(selectionChild1);
        selectionChild1.addChild("Selection child 2");
        selectionChild1.addChild("Selection child 3");

        final JSelectionTree tree1 = new JSelectionTree(root1);
        tree1.expandRow(0);
        tree1.expandRow(1);
        tree1.expandRow(2);

        tree0.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tree1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new GridLayout(2, 1));

        add(new JScrollPane(tree0), BorderLayout.CENTER);
        add(new JScrollPane(tree1), BorderLayout.CENTER);
    }

    public String getDemoName() {
        return "Selection tree demo";
    }
}
