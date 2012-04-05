package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.binding.Keypath;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Conveniance class for using autowiring conditions.
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
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
public class Autowiring {
    /**
     * Connects a list of conditions with an 'and' operator.
     *
     * @param conditions The conditions to add.
     * @return The created aggregate condition.
     */
    public static AggregateBooleanCondition and(final BooleanCondition... conditions) {
        final AggregateBooleanCondition result = new AggregateBooleanCondition(AggregateBooleanCondition.Operator.and);
        result.addConditions(conditions);
        return result;
    }

    /**
     * Connects a list of conditions with an 'or' operator.
     *
     * @param conditions The conditions to add.
     * @return The created aggregate condition.
     */
    public static AggregateBooleanCondition or(final BooleanCondition... conditions) {
        final AggregateBooleanCondition result = new AggregateBooleanCondition(AggregateBooleanCondition.Operator.or);
        result.addConditions(conditions);
        return result;
    }

    /**
     * Connects a list of conditions with an 'xor' operator.
     *
     * @param conditions The conditions to add.
     * @return The created aggregate condition.
     */
    public static AggregateBooleanCondition xor(final BooleanCondition... conditions) {
        final AggregateBooleanCondition result = new AggregateBooleanCondition(AggregateBooleanCondition.Operator.xor);
        result.addConditions(conditions);
        return result;
    }

    /**
     * Creates a boolean condition that returns true whenever the given table has a selection.
     *
     * @param table The table to monitor.
     * @return a boolean condition that returns true whenever the given table has a selection.
     */
    public static HasSelectionCondition hasSelection(final JTable table) {
        return new HasSelectionCondition(table);
    }

    /**
     * Creates a boolean condition that returns true whenever the given list has a selection.
     *
     * @param list The list to monitor.
     * @return a boolean condition that returns true whenever the given list has a selection.
     */
    public static HasSelectionCondition hasSelection(final JList list) {
        return new HasSelectionCondition(list);
    }

    /**
     * Creates a boolean condition that returns true whenever the given tree has a selection.
     *
     * @param tree The tree to monitor.
     * @return a boolean condition that returns true whenever the given tree has a selection.
     */
    public static HasSelectionCondition hasSelection(final JTree tree) {
        return new HasSelectionCondition(tree);
    }

    /**
     * Creates a boolean condition that returns true whenever the given text component has a text of length > 0.
     *
     * @param textComponent The text component to monitor.
     * @return a boolean condition that returns true whenever the given text component has a text of length > 0.
     */
    public static HasTextCondition hasText(final JTextComponent textComponent) {
        return new HasTextCondition(textComponent);
    }

    /**
     * Creates a boolean condition that reflects the selection state of a given component.
     *
     * @param selectable The ItemSelectable component (e.g. JCheckBox) to monitor.
     * @return the created condition.
     */
    public static ItemSelectedCondition isSelected(final ItemSelectable selectable) {
        return new ItemSelectedCondition(selectable);
    }

    /**
     * Creates a boolean condition that reflects whether a JTextComponent's current text matches a given regex pattern.
     *
     * @param component The component being monitored.
     * @param pattern   The regex pattern to look for.
     * @return the created condition.
     */
    public static TextMatchesRegexCondition textMatches(final JTextComponent component, final Pattern pattern) {
        return new TextMatchesRegexCondition(component, pattern);
    }

    /**
     * Creates a boolean condition that reflects whether a JTextComponent's current text contains a given regex
     * pattern.
     *
     * @param component The component being monitored.
     * @param pattern   The regex pattern to look for.
     * @return the created condition.
     */
    public static TextContainsRegexCondition textContains(final JTextComponent component, final Pattern pattern) {
        return new TextContainsRegexCondition(component, pattern);
    }

    /**
     * Creates an action that enables or disables a component depending on whether conditions are fulfilled.
     *
     * @param component the component to adapt.
     * @return the created action object.
     */
    public static EnableComponentAction enableComponent(final JComponent component) {
        final EnableComponentAction action = new EnableComponentAction(component);
        component.putClientProperty(EnableComponentAction.class.getName(), action);
        return action;
    }

    /**
     * Sets the icon for a given component depending on whether conditions are fulfilled.
     *
     * @param target        The component to adapt.
     * @param iconWhenTrue  The icon to set when the conditions resolve to true.
     * @param iconWhenFalse The icon to set when the conditions resolve to false.
     * @return the created action.
     */
    public static SetPropertyAction<Icon> setIcon(final JComponent target, final Icon iconWhenTrue,
                                                  final Icon iconWhenFalse) {
        return new SetPropertyAction<Icon>(target, new Keypath<Icon>("icon"), iconWhenTrue, iconWhenFalse);
    }

    /**
     * Sets the foreground for a given component depending on whether conditions are fulfilled.
     *
     * @param target         The component to adapt.
     * @param colorWhenTrue  The color to set when the conditions resolve to true.
     * @param colorWhenFalse The color to set when the conditions resolve to false.
     * @return the created action.
     */
    public static SetPropertyAction<Color> setForeground(final JComponent target, final Color colorWhenTrue,
                                                         final Color colorWhenFalse) {
        return new SetPropertyAction<Color>(target, new Keypath<Color>("foreground"), colorWhenTrue, colorWhenFalse);
    }

    /**
     * Sets the background for a given component depending on whether conditions are fulfilled.
     *
     * @param target         The component to adapt.
     * @param colorWhenTrue  The color to set when the conditions resolve to true.
     * @param colorWhenFalse The color to set when the conditions resolve to false.
     * @return the created action.
     */
    public static SetPropertyAction<Color> setBackground(final JComponent target, final Color colorWhenTrue,
                                                         final Color colorWhenFalse) {
        return new SetPropertyAction<Color>(target, new Keypath<Color>("background"), colorWhenTrue, colorWhenFalse);
    }

    /**
     * This class can not be instanciated.
     */
    private Autowiring() {
    }
}
