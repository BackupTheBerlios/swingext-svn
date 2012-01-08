package net.sarcommand.swingextensions.autowiring;

import net.sarcommand.swingextensions.event.DocumentAdapter;
import net.sarcommand.swingextensions.event.DocumentListenerGlue;

import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;

/**
 * A BooleanCondition implementation indicating whether a given JTextComponent has actual text content. <hr/> Copyright
 * 2006-2012 Torsten Heup
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
public class HasTextCondition extends BooleanCondition {
    private DocumentListenerGlue _glue;
    private JTextComponent _textComponent;

    public HasTextCondition(final JTextComponent textComponent) {
        _textComponent = textComponent;
        final DocumentAdapter adapter = new DocumentAdapter() {
            @Override
            public void documentChanged(final DocumentEvent e) {
                fireConditionUpdated();
            }
        };
        _glue = new DocumentListenerGlue(textComponent, adapter);
    }

    @Override
    public Boolean getState() {
        return _textComponent.getText().length() > 0;
    }
}
