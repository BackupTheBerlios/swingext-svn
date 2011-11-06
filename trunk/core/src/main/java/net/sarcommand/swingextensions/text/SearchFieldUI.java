package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.event.DocumentAdapter;
import net.sarcommand.swingextensions.internal.SwingExtResources;
import net.sarcommand.swingextensions.utilities.KeyUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A ui class for JTextFields, turning them into a search field. Doing so will display a search icon in the text field,
 * and when content is entered a 'clear' button will appear. When the user presses escape, all content will be removed
 * from the field. All in all, the modified text field will behave like the native search field on Max OSX would.
 * <p/>
 * Be aware that each instance of the SearchFieldUI can only copy with a single JTextField.
 * <p/>
 * This ui class will delegate all the usual painting class to the normal ui, therefore it should work equally on all
 * LookAndFeels.
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
 * @see TextUtilities#turnIntoSearchField(JTextField)
 */
public class SearchFieldUI extends DelegatingTextUI {
    /**
     * UI key for the small magnifying glass icon.
     */
    public static final String SEARCHFIELD_SEARCH_ICON = "SearchFieldUI.searchIcon";

    /**
     * UI key for the clear icon.
     */
    public static final String SEARCHFIELD_CLEAR_ICON = "SearchFieldUI.clearIcon";

    /**
     * Action key used in the search field's input map for mappings the escape keystroke to the appropriate action.
     */
    protected static final String ACTION_KEY_CLEAR = "swingext.SearchFieldUI.clearAction";

    /**
     * The icon used in the left part of the text field.
     */
    protected BufferedImage _searchIcon;

    /**
     * The icon displaying for the clear action.
     */
    protected BufferedImage _clearIcon;

    /**
     * The JTextField this ui class is wrapping.
     */
    protected JTextComponent _target;

    /**
     * Boolean flag indicating whether or not text has been entered.
     */
    protected boolean _hasText;

    /**
     * Boolean flag indicating whether or not the cursor is currently over the clear button.
     */
    protected boolean _cursorOverClearButton;

    /**
     * Mouse listener used to adapt the cursor style and handle clicks on the clear button.
     */
    protected MouseAdapter _mouseListener;

    /**
     * Document listener used to be notified of changes of the content.
     */
    protected DocumentListener _documentListener;

    /**
     * PropertyChangeListener used to be notified of changes to the 'document' property.
     */
    protected PropertyChangeListener _propertyChangeListener;

    /**
     * The action triggered when 'clear' is invoked.
     */
    protected ClearAction _clearAction;

    /**
     * The previous key binding for the escape key, used to revert the JTextField to the previous state upon uninstall.
     */
    protected Object _previouskeyBindingForClearAction;

    /**
     * Creates a new search field ui, delegating all relevant calls to the specified ui instance.
     *
     * @param delegate Delegate to forward all invocations to.
     */
    public SearchFieldUI(final TextUI delegate) {
        super(delegate);
        initialize();
    }

    /**
     * Sets up the ui. Invoked by the constructor.
     */
    protected void initialize() {
        _searchIcon = SwingExtResources.getImageResource(SEARCHFIELD_SEARCH_ICON);
        _clearIcon = SwingExtResources.getImageResource(SEARCHFIELD_CLEAR_ICON);

        _clearAction = new ClearAction();

        _mouseListener = new MouseAdapter() {
            public void mouseMoved(final MouseEvent e) {
                final boolean inside = e.getX() >= _target.getWidth() - (_target.getMargin().right);
                _target.setCursor(Cursor.getPredefinedCursor(inside ? Cursor.DEFAULT_CURSOR : Cursor.TEXT_CURSOR));
                final boolean needsRepaint = _cursorOverClearButton == inside;
                _cursorOverClearButton = inside;
                if (needsRepaint)
                    _target.repaint();
            }

            public void mouseExited(final MouseEvent e) {
                final boolean repaintNeeded = _cursorOverClearButton;
                _cursorOverClearButton = false;
                if (repaintNeeded)
                    _target.repaint();
            }

            public void mouseClicked(final MouseEvent e) {
                if (_cursorOverClearButton)
                    _clearAction.actionPerformed(new ActionEvent(_target, ActionEvent.ACTION_PERFORMED, ""));
            }
        };

        _propertyChangeListener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("document".equals(evt.getPropertyName()))
                    documentChanged((Document) evt.getOldValue(), (Document) evt.getNewValue());
            }
        };

        _documentListener = new DocumentAdapter() {
            public void documentChanged(final DocumentEvent e) {
                contentChanged();
            }
        };
    }

    public void installUI(final JComponent c) {
        _delegate.installUI(c);
        final JTextComponent editor = (JTextComponent) c;
        editor.setMargin(new Insets(0, 20, 0, 18));
        editor.addMouseMotionListener(_mouseListener);
        editor.addMouseListener(_mouseListener);

        _previouskeyBindingForClearAction = KeyUtilities.setActionKeyBinding(editor,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), _clearAction);

        _target = editor;
        documentChanged(null, _target.getDocument());
    }

    public void uninstallUI(final JComponent c) {
        _delegate.uninstallUI(c);
        if (_target.getDocument() != null)
            _target.getDocument().removeDocumentListener(_documentListener);

        _target.setMargin(null);
        _target.removePropertyChangeListener(_propertyChangeListener);

        final InputMap map = _target.getInputMap(JComponent.WHEN_FOCUSED);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), _previouskeyBindingForClearAction);
        _target.setInputMap(JComponent.WHEN_FOCUSED, map);
    }

    public void update(final Graphics g, final JComponent c) {
        _delegate.update(g, c);

        g.setClip(null);
        g.drawImage(_searchIcon, (22 - _searchIcon.getWidth()) / 2,
                (c.getHeight() - _searchIcon.getHeight()) / 2, null);

        if (_hasText) {
            if (_cursorOverClearButton)
                g.drawImage(_clearIcon, c.getWidth() - 17, (c.getHeight() - _clearIcon.getHeight()) / 2, null);
            else {
                final Graphics2D g2 = (Graphics2D) g;
                final Composite composite = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2.drawImage(_clearIcon, c.getWidth() - 17, (c.getHeight() - _clearIcon.getHeight()) / 2, null);
                g2.setComposite(composite);
            }
        }
    }

    /**
     * Method invoked when the 'document' property changes on the wrapped text field.
     *
     * @param prevDoc The previous document.
     * @param newDoc  The new document.
     */
    protected void documentChanged(final Document prevDoc, final Document newDoc) {
        if (prevDoc != null)
            prevDoc.removeDocumentListener(_documentListener);
        if (newDoc != null)
            newDoc.addDocumentListener(_documentListener);
        contentChanged();
    }

    /**
     * Invoked when the text field's content changes.
     */
    protected void contentChanged() {
        final boolean hasText = _target.getText().length() > 0;
        final boolean repaintNeeded = hasText != _hasText;
        _hasText = hasText;
        if (repaintNeeded)
            _target.repaint();
    }

    /**
     * Action used to clear the search field.
     */
    protected static class ClearAction extends AbstractAction {
        public ClearAction() {
            putValue(Action.NAME, "SearchFieldUI.clearAction");
        }

        public void actionPerformed(final ActionEvent e) {
            final JTextComponent source = (JTextComponent) e.getSource();
            source.setText("");
        }
    }
}
