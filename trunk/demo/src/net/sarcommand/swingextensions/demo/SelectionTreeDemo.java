package net.sarcommand.swingextensions.demo;

import net.sarcommand.swingextensions.selectiontree.DefaultSelectionTreeNode;
import net.sarcommand.swingextensions.selectiontree.JSelectionTree;

import javax.swing.*;
import java.awt.*;

/**
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
public class SelectionTreeDemo extends DemoClass {
    public SelectionTreeDemo() {
        initialize();
    }

    protected void initialize() {
        final DefaultSelectionTreeNode root = new DefaultSelectionTreeNode("Items to install");

        final DefaultSelectionTreeNode doc = root.addChild("Documentation");
        doc.addChild("JavaDoc");
        doc.addChild("Developer FAQ");

        final DefaultSelectionTreeNode comps = root.addChild("Components");
        comps.addChild("Core components");
        comps.addChild("Source code");
        comps.addChild("Demo examples");

        final JSelectionTree tree = new JSelectionTree(root);
        setLayout(new BorderLayout());
        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    public String getDemoName() {
        return "Selection tree demo";
    }
}
