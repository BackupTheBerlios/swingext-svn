package net.sarcommand.swingextensions.tree;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.LinkedList;

/**
 * Contains a set of utility methods revolving around the JTree class. <hr/> Copyright 2006-2012 Torsten Heup
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
public class TreeUtilities {
    public static TreePath getTreePathForNode(final TreeNode node) {
        return new TreePath(getObjectPathForNode(node));
    }

    public static Object[] getObjectPathForNode(final TreeNode node) {
        final LinkedList<TreeNode> list = new LinkedList<TreeNode>();
        TreeNode runner = node;
        while (runner != null) {
            list.add(0, runner);
            runner = runner.getParent();
        }
        return list.toArray(new TreeNode[list.size()]);
    }
}
