package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.event.DocumentAdapter;
import net.sarcommand.swingextensions.utilities.SearchTask;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.TextUI;
import javax.swing.text.Document;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This variation will turn the given text field into a search field:
 * <p/>
 * <li>It will be supplied with an appropriate icon</li> <li>If text is entered, a clear button will appear</li> <li>The
 * user can clear the field by pressing escape</li> <li>Optionally, you cann a SearchTask which will be triggered
 * whenever the textfield's content changes</li>
 * <p/>
 * The text field will be transformed with the help of the SearchFieldUI class. On Mac OSX, or rather when using the
 * Aqua LookAndFeel, a better-looking, more native-like implementation is available by setting a client property. This
 * method will revert to this implementation when the aqua Lookandfeel is detected.
 * <p/>
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
 *
 * @see TextUtilities
 * @see SearchTask
 */
public class SearchFieldVariation implements TextVariation<JTextField> {
    private JTextField _targetComponent;
    private SearchTask _searchTask;
    private TextUI _originalUI;
    protected DocumentAdapter _documentListener;
    protected PropertyChangeListener _propertyChangeListener;

    public SearchFieldVariation(final JTextField targetComponent, final SearchTask<String, ?> searchTask) {
        if (targetComponent == null)
            throw new IllegalArgumentException("Parameter 'targetComponent' must not be null!");

        _targetComponent = targetComponent;
        _searchTask = searchTask;

        if (UIManager.getLookAndFeel().getName().toLowerCase().contains("aqua"))
            _targetComponent.putClientProperty("JTextField.variant", "search");
        else {
            _originalUI = _targetComponent.getUI();
            _targetComponent.setUI(new SearchFieldUI(_originalUI));
        }

        if (_searchTask != null) {
            _documentListener = new DocumentAdapter() {
                public void documentChanged(final DocumentEvent e) {
                    final String text = _targetComponent.getText();
                    if (text.length() > 0)
                        _searchTask.search(text);
                    else
                        _searchTask.cancel();
                }
            };
            _propertyChangeListener = new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent evt) {
                    final Document prevDoc = (Document) evt.getOldValue();
                    final Document newDoc = (Document) evt.getNewValue();
                    if (prevDoc != null)
                        prevDoc.removeDocumentListener(_documentListener);
                    if (newDoc != null)
                        newDoc.addDocumentListener(_documentListener);
                }
            };

            _targetComponent.addPropertyChangeListener(_propertyChangeListener);
            _targetComponent.getDocument().addDocumentListener(_documentListener);
        }
    }

    public void detach() {
        if (UIManager.getLookAndFeel().getName().toLowerCase().contains("aqua"))
            _targetComponent.putClientProperty("JTextField.variant", null);
        else
            _targetComponent.setUI(_originalUI);
        _targetComponent.getDocument().removeDocumentListener(_documentListener);
    }

    public JTextField getAlteredComponent() {
        return _targetComponent;
    }
}
