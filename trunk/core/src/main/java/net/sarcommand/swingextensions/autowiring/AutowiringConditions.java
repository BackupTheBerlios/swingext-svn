package net.sarcommand.swingextensions.autowiring;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Conveniance class for using autowiring conditions.
 * <p/>
 * <hr/> Copyright 2006-2010 Torsten Heup
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
public class AutowiringConditions {
    public static HasSelectionCondition hasSelection(final JTable table) {
        return new HasSelectionCondition(table);
    }

    public static HasSelectionCondition hasSelection(final JList list) {
        return new HasSelectionCondition(list);
    }

    public static HasSelectionCondition hasSelection(final JTree tree) {
        return new HasSelectionCondition(tree);
    }

    public static HasTextCondition hasText(final JTextComponent textComponent) {
        return new HasTextCondition(textComponent);
    }

    public static ItemSelectedCondition whileSelected(final ItemSelectable selectable) {
        return new ItemSelectedCondition(selectable);
    }

    public static EnableComponentAction enableComponentWhen(final JComponent component,
                                                            final BooleanCondition... conditions) {
        final EnableComponentAction action = new EnableComponentAction(component, conditions);
        component.putClientProperty(EnableComponentAction.class.getName(), action);
        return action;
    }

    public static EnableComponentAction enableComponent(final JComponent component) {
        final EnableComponentAction action = new EnableComponentAction(component);
        component.putClientProperty(EnableComponentAction.class.getName(), action);
        return action;
    }

    private AutowiringConditions() {
    }
}
