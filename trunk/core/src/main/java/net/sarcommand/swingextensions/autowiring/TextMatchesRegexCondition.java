package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.event.DocumentAdapter;
import net.sarcommand.swingextensions.event.DocumentListenerGlue;

import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.util.regex.Pattern;

/**
 * Condition that reflects whether a JTextComponent's text property matches a given regex. Note: The condition will
 * update every time the text changes and may therefore become rather CPU intensive for complex expressions. Use with
 * care.
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
public class TextMatchesRegexCondition extends BooleanCondition {
    /**
     * The monitored component.
     */
    protected JTextComponent _component;

    /**
     * The pattern to be matched.
     */
    protected Pattern _pattern;

    /**
     * Creates a new condition that reflects whether the given JTextComponent's text matches a pattern.
     *
     * @param component The component to monitor.
     * @param pattern   The pattern to match.
     */
    public TextMatchesRegexCondition(final JTextComponent component, final Pattern pattern) {
        if (component == null)
            throw new IllegalArgumentException("Parameter 'component' must not be null!");
        if (pattern == null)
            throw new IllegalArgumentException("Parameter 'pattern' must not be null!");

        _component = component;
        _pattern = pattern;

        new DocumentListenerGlue(_component, new DocumentAdapter() {
            @Override
            public void documentChanged(final DocumentEvent e) {
                fireConditionUpdated();
            }
        });
    }

    @Override
    public Boolean getState() {
        return _component.getText() != null && _pattern.matcher(_component.getText()).matches();
    }
}
