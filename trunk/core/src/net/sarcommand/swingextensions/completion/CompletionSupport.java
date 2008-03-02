package net.sarcommand.swingextensions.completion;

import net.sarcommand.swingextensions.actions.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class offers completion support for all subclasses of JTextComponent. It is designed to be non-invasive, you
 * simply attach it to the instance you want to support. The CompletionSupport will then install the required listeners
 * and update mechanisms, which will be properly removed if you detach it at any point.
 * <p/>
 * Two basic completion functions are implemented here. While the shared is typing, this class will check for possible
 * completions based on the current token (see below). If a definite completion exists, the 'missing' letters will be
 * inserted after the caret and selected. By pressing enter, the shared can accept it and continue with the next word.
 * By pressing escape, the suggestion will be removed. Finally, the shared can just continue typing and the suggestion
 * will be adapted accordingly. This behavious is similar to the one implemented in OpenOffice.
 * <p/>
 * If multiple possible suggestions exists, the shared can prompt a popup by pressing CTRL+SPACE. He can select an
 * item from the popup or continue typing, incrementally narrowing the search scope. By default, the popup will only
 * show if it is requested manually. You can make it pop up by default by setting the 'automaticallyShowPopup'
 * property.
 * <p/>
 * You can configure what portion of the text should be considered to be a token - a word, a sentence, a line etc by
 * specifying a suitable TokenProvider implementation. All keystrokes can be configured as well by using the
 * according setter methods.
 * <p/>
 * Example:<br>
 * <code>
 * // You can use pretty much every text component<br>
 * final JTextField testTF = new JTextField(20);
 * <p/>
 * // Create a new CompletionSupport instance<br>
 * final CompletionSupport support = new CompletionSupport();
 * <p/>
 * // Create a completion model, which will be queried for possible completions<br>
 * final SimpleCompletionModel model = new SimpleCompletionModel(false);
 * <p/>
 * //Add some exemplary tokens<br>
 * model.addTokens(Arrays.asList("Abe", "Abel", "Abraham", "Abakus", "Baker", "Charlie", "Chipmunk", "Chukie"));
 * <p/>
 * // Set the model<br>
 * support.setModel(model);
 * <p/>
 * // And install the completion support. After this invocation, everything should be set up and running.
 * support.install(testTF);
 * </code>
 * <p/>
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
public class CompletionSupport {
    private static final String INPUT_MAP_PREFIX = "swingExt.completionSupport";

    protected enum State {
        NO_SUGGESTIONS, MULTIPLE_SUGGESTIONS, SINGLE_SUGGESTION
    }

    public static final String TRIGGER_COMPLETION_KEY = INPUT_MAP_PREFIX + ".triggerCompletionKey";
    public static final String NAV_UP_KEY = INPUT_MAP_PREFIX + ".navUpKey";
    public static final String NAV_DOWN_KEY = INPUT_MAP_PREFIX + ".navDownKey";
    public static final String CANCEL_KEY = INPUT_MAP_PREFIX + ".cancelKey";
    public static final String ACCEPT_KEY = INPUT_MAP_PREFIX + ".acceptKey";

    public static final String TOKEN_PROVIDER_WORD = "tokenProviderWord";
    public static final String TOKEN_PROVIDER_LINE = "tokenProviderLine";
    public static final String TOKEN_PROVIDER_ENTIRE_TEXT = "tokenProviderEntireText";

    private static HashMap<String, TokenProvider> __tokenProviderMap;

    public static TokenProvider getTokenProvider(final String name) {
        if (__tokenProviderMap == null)
            __tokenProviderMap = new HashMap<String, TokenProvider>(4);
        if (!__tokenProviderMap.containsKey(name)) {
            if (TOKEN_PROVIDER_WORD.equals(name)) {
                final TokenProvider t = new TokenProvider() {
                    public String getTokenAtPosition(int position, final String text) {
                        final char[] chars = text.toCharArray();
                        if (position >= chars.length)
                            position = chars.length - 1;
                        if (position < 0)
                            return "";

                        int end = position;
                        int start = position > 0 ? position - 1 : 0;

                        while (end < chars.length && chars[end] != ' ')
                            end++;
                        while (start > 0 && chars[start] != ' ')
                            start--;
                        return text.substring(start, end).trim();
                    }
                };
                __tokenProviderMap.put(name, t);
            }
        }
        return __tokenProviderMap.get(name);

    }

    protected JTextComponent _target;
    protected CompletionModel _model;

    protected KeyStroke _triggerCompletionKeyStroke;
    protected KeyStroke _navDownKeyStroke;
    protected KeyStroke _navUpKeyStroke;
    protected KeyStroke _cancelKeyStroke;
    protected KeyStroke _acceptKeyStroke;

    protected HashMap<KeyStroke, Object> _originalKeyMapping;
    protected CompletionPopup _completionPopup;

    protected TokenProvider _tokenProvider;

    protected DocumentListener _documentListener;

    protected State _state;
    protected volatile boolean _ignoringEvents;

    protected boolean _automaticallyShowingPopup;

    public CompletionSupport() {
        initialize();
    }

    private void initialize() {
        _tokenProvider = getTokenProvider(TOKEN_PROVIDER_WORD);

        _originalKeyMapping = new HashMap<KeyStroke, Object>(5);
        _triggerCompletionKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK);
        _navDownKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        _navUpKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        _cancelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        _acceptKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        _completionPopup = new CompletionPopup();
        _completionPopup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accept();
            }
        });

        _state = State.NO_SUGGESTIONS;
        _automaticallyShowingPopup = false;

        _documentListener = createDocumentListener();
    }

    /**
     * Creates a listener which will be installed into the target component's document. The listener will invoke
     * updateCompletions(false) whenever content is added and whenever content is removed while the completion
     * popup is visible (in order to update the list).
     *
     * @return a listener which will be installed into the target component's document.
     */
    protected DocumentListener createDocumentListener() {
        return new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {

            }

            public void insertUpdate(DocumentEvent e) {
                if (!_ignoringEvents) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            updateCompletions(false);
                        }
                    });
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (!_ignoringEvents && _state == State.MULTIPLE_SUGGESTIONS) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            updateCompletions(false);
                        }
                    });
                }
            }
        };
    }

    /**
     * Returns whether the completion popup will be shown automatically.
     *
     * @return whether the completion popup will be shown automatically.
     */
    public boolean isAutomaticallyShowingPopup() {
        return _automaticallyShowingPopup;
    }

    /**
     * Determines whether the completion popup should be shown automatically.
     *
     * @param automaticallyShowingPopup whether the completion popup should be shown automatically.
     */
    public void setAutomaticallyShowingPopup(boolean automaticallyShowingPopup) {
        _automaticallyShowingPopup = automaticallyShowingPopup;
    }

    /**
     * Installs this CompletionSupport instance on the given target component. This method will install all
     * listeners required to provide completions.
     *
     * @param target the text component to support with auto completion.
     */
    public void install(final JTextComponent target) {
        _target = target;
        installKey(_triggerCompletionKeyStroke, TRIGGER_COMPLETION_KEY);

        final ActionMap actionMap = _target.getActionMap();
        actionMap.put(TRIGGER_COMPLETION_KEY, new ReflectedAction("triggerCompletion", this, "triggerCompletion"));
        actionMap.put(NAV_DOWN_KEY, new ReflectedAction("moveCursorDown", this, "moveCursorDown"));
        actionMap.put(NAV_UP_KEY, new ReflectedAction("moveCursorUp", this, "moveCursorUp"));
        actionMap.put(CANCEL_KEY, new ReflectedAction("cancel", this, "cancel"));
        actionMap.put(ACCEPT_KEY, new ReflectedAction("accept", this, "accept"));

        _target.getDocument().addDocumentListener(_documentListener);
    }

    /**
     * Uninstalls the completion support, removing all previously installed listeners.
     */
    public void uninstall() {
        final ActionMap actionMap = _target.getActionMap();
        actionMap.remove(TRIGGER_COMPLETION_KEY);
        actionMap.remove(NAV_DOWN_KEY);
        actionMap.remove(NAV_UP_KEY);
        actionMap.remove(CANCEL_KEY);
        actionMap.remove(ACCEPT_KEY);

        uninstallKey(_triggerCompletionKeyStroke);

        _target.getDocument().removeDocumentListener(_documentListener);

        _target = null;
    }

    /**
     * Triggers the completion function, showing the completion popup if appropriate. Use this method if you want to
     * manuall display the popup (invoking this method will override the 'automaticallyShowingPopup' property). This
     * method will also be invoked whenever the use presses the triggerCompletionKeyStroke.
     */
    public void triggerCompletion() {
        if (_model == null)
            return;

        updateCompletions(true);
    }

    /**
     * Moves the cursor in the completion popup down one item.
     */
    public void moveCursorDown() {
        _completionPopup.moveCursorDown();
    }

    /**
     * Moves the cursor in the completion popup up one item.
     */
    public void moveCursorUp() {
        _completionPopup.moveCursorUp();
    }

    /**
     * Signals that the current suggestion should be accepted:
     * <li>If no suggestion has been made, nothing will happen</li>
     * <li>If a single suggestion has been made, that suggestion will be accepted</li>
     * <li>If multiple suggestions have been made, the currently selected item from the completion popup
     * will be chosen</li>
     */
    public void accept() {
        switch (_state) {
            case NO_SUGGESTIONS:
                break;
            case SINGLE_SUGGESTION:
                _target.setCaretPosition(_target.getSelectionEnd());
                break;
            case MULTIPLE_SUGGESTIONS:
                final String word = getTokenAtPosition();
                final String completion = ((String) _completionPopup.getSelectedValue()).substring(word.length());
                try {
                    _target.getDocument().insertString(_target.getCaretPosition(), completion, null);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                setPopupVisible(false);
                break;
        }
        if (_state != State.NO_SUGGESTIONS) {
            uninstallKey(getAcceptKeyStroke());
            uninstallKey(getCancelKeyStroke());
            _state = State.NO_SUGGESTIONS;
        }
    }

    /**
     * Cancels the current suggestion, hiding the popup if it's visible.
     */
    public void cancel() {
        switch (_state) {
            case NO_SUGGESTIONS:
                break;
            case SINGLE_SUGGESTION:
                final int start = _target.getSelectionStart();
                try {
                    _target.getDocument().remove(start, _target.getSelectionEnd() - start);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                break;
            case MULTIPLE_SUGGESTIONS:
                setPopupVisible(false);
        }
    }

    /**
     * Returns the currently used completion model.
     *
     * @return the currently used completion model.
     */
    public CompletionModel getModel() {
        return _model;
    }

    /**
     * Sets the model to be used when searching for text completions.
     *
     * @param model the model to be used when searching for text completions.
     */
    public void setModel(CompletionModel model) {
        _model = model;
    }

    /**
     * Returns the keystroke for the 'Accept' action.
     *
     * @return the keystroke for the 'Accept' action.
     * @see #accept()
     */
    public KeyStroke getAcceptKeyStroke() {
        return _acceptKeyStroke;
    }

    /**
     * Sets the keystroke for the 'Accept' action.
     *
     * @param acceptKeyStroke the keystroke for the 'Accept' action.
     * @see #accept()
     */
    public void setAcceptKeyStroke(KeyStroke acceptKeyStroke) {
        uninstallKey(_acceptKeyStroke);
        installKey(acceptKeyStroke, ACCEPT_KEY);
        _acceptKeyStroke = acceptKeyStroke;
    }

    /**
     * Returns the keystroke for the 'Cancel' action.
     *
     * @return the keystroke for the 'Cancel' action.
     * @see #cancel()
     */
    public KeyStroke getCancelKeyStroke() {
        return _cancelKeyStroke;
    }

    /**
     * Sets the keystroke for the 'Cancel' action.
     *
     * @param cancelKeyStroke the keystroke for the 'Cancel' action.
     * @see #cancel()
     */
    public void setCancelKeyStroke(KeyStroke cancelKeyStroke) {
        uninstallKey(_cancelKeyStroke);
        installKey(cancelKeyStroke, CANCEL_KEY);
        _cancelKeyStroke = cancelKeyStroke;
    }

    /**
     * Returns the keystroke for the 'moveCursorDown' action.
     *
     * @return the keystroke for the 'moveCursorDown' action.
     * @see #moveCursorDown()
     */
    public KeyStroke getNavDownKeyStroke() {
        return _navDownKeyStroke;
    }

    /**
     * Sets the keystroke for the 'moveCursorDown' action.
     *
     * @param navDownKeyStroke the keystroke for the 'moveCursorDown' action.
     * @see #moveCursorDown()
     */
    public void setNavDownKeyStroke(KeyStroke navDownKeyStroke) {
        uninstallKey(_navDownKeyStroke);
        installKey(navDownKeyStroke, NAV_DOWN_KEY);
        _navDownKeyStroke = navDownKeyStroke;
    }

    /**
     * Returns the keystroke for the 'moveCursorUp' action.
     *
     * @return the keystroke for the 'moveCursorUp' action.
     * @see #moveCursorUp()
     */
    public KeyStroke getNavUpKeyStroke() {
        return _navUpKeyStroke;
    }

    /**
     * Sets the keystroke for the 'moveCursorUp' action.
     *
     * @param navUpKeyStroke the keystroke for the 'moveCursorUp' action.
     * @see #moveCursorUp()
     */
    public void setNavUpKeyStroke(KeyStroke navUpKeyStroke) {
        uninstallKey(_navUpKeyStroke);
        installKey(navUpKeyStroke, NAV_UP_KEY);
        _navUpKeyStroke = navUpKeyStroke;
    }

    /**
     * Returns the keystroke for the 'triggerCompletion' action.
     *
     * @return the keystroke for the 'triggerCompletion' action.
     * @see #triggerCompletion() ()
     */
    public KeyStroke getTriggerCompletionKeyStroke() {
        return _triggerCompletionKeyStroke;
    }

    /**
     * Sets the keystroke for the 'triggerCompletion' action.
     *
     * @param triggerCompletionKeyStroke the keystroke for the 'triggerCompletion' action.
     * @see #triggerCompletion()
     */
    public void setTriggerCompletionKeyStroke(KeyStroke triggerCompletionKeyStroke) {
        uninstallKey(_triggerCompletionKeyStroke);
        installKey(triggerCompletionKeyStroke, TRIGGER_COMPLETION_KEY);
        _triggerCompletionKeyStroke = triggerCompletionKeyStroke;
    }

    /**
     * Installs a keystroke on the target component. If this keystroke is used for some other function, the operation
     * which would originally have been triggered will be stored in an internal map. When uninstallKey(KeyStroke) is
     * invoked for the keystroke, the original function will be restored.
     *
     * @param newKey KeyStroke to assign
     * @param key    ActionKey to assign
     */
    protected void installKey(final KeyStroke newKey, final String key) {
        final InputMap inputMap = _target.getInputMap();
        final Object originalKey = inputMap.get(newKey);
        if (!(originalKey != null && originalKey instanceof String &&
                ((String) originalKey).startsWith(INPUT_MAP_PREFIX))) {
            _originalKeyMapping.put(newKey, originalKey);
        }
        inputMap.put(newKey, key);
    }

    /**
     * Removes an installed key from the target component and replaces it with the original key.
     *
     * @param key The keystroke which should be removed.
     */
    protected void uninstallKey(final KeyStroke key) {
        final InputMap inputMap = _target.getInputMap();
        if (inputMap.get(key) instanceof String && ((String) inputMap.get(key)).startsWith(INPUT_MAP_PREFIX)) {
            if (_originalKeyMapping.get(key) == null)
                inputMap.remove(key);
            else
                inputMap.put(key, _originalKeyMapping.get(key));
        }
    }

    /**
     * Shows completion the popup, (un)-installing the keys required for moving the list selection up and down.
     *
     * @param visible whether the completion popup should be shown.
     */
    protected void setPopupVisible(final boolean visible) {
        if (_completionPopup.isVisible() == visible)
            return;
        if (visible) {
            installKey(_navDownKeyStroke, NAV_DOWN_KEY);
            installKey(_navUpKeyStroke, NAV_UP_KEY);
            _completionPopup.show(_target);
        } else {
            uninstallKey(_navDownKeyStroke);
            uninstallKey(_navUpKeyStroke);
            _completionPopup.hide();
        }
    }

    /**
     * Returns the word at the target component's current caret position. This method will return "" if the cursor
     * is positioned between two blanks or the text component is empty.
     *
     * @return the word at the target component's current caret position.
     */
    protected String getTokenAtPosition() {
        final String text = _target.getText();
        int caretPosition = _target.getCaretPosition();
        return _tokenProvider.getTokenAtPosition(caretPosition, text);
    }

    /**
     * This method will examine the current token at the caret position, look for completions and react accordingly.
     * If a single completion can be found, it will be suggested to the shared by adding a selected region containing
     * the missing letters. In case of multiple selections, a popup from which the shared can select the proper
     * completion will be shown.
     *
     * @param showPopup Indicates whether the popup should be shown regardless of the 'automaticallyShowPopup'
     *                  property.
     */
    public void updateCompletions(final boolean showPopup) {
        if (!_completionPopup.isVisible() && !showPopup)
            return;
        final String word = getTokenAtPosition();
        final Collection<String> completions = _model.getPossibleCompletions(_target, word);
        if (completions.size() > 0 && _state == State.NO_SUGGESTIONS) {
            installKey(_acceptKeyStroke, ACCEPT_KEY);
            installKey(_cancelKeyStroke, CANCEL_KEY);
        } else if (completions.size() == 0 && _state != State.NO_SUGGESTIONS) {
            uninstallKey(_acceptKeyStroke);
            uninstallKey(_cancelKeyStroke);
        }
        switch (completions.size()) {
            case 0:
                if (_state != State.NO_SUGGESTIONS)
                    cancel();
                _state = State.NO_SUGGESTIONS;
                return;
            case 1:
                _state = State.SINGLE_SUGGESTION;
                if (_completionPopup.isVisible())
                    setPopupVisible(false);
                final int caretPosition = _target.getCaretPosition();
                final String token = completions.iterator().next();

                try {
                    _ignoringEvents = true;

                    final String suggestionText = token.substring(word.length());
                    _target.getDocument().insertString(caretPosition, suggestionText, null);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            _target.setCaretPosition(caretPosition + suggestionText.length());
                            _target.moveCaretPosition(caretPosition);
                        }
                    });

                } catch (BadLocationException e) {
                    throw new RuntimeException("Could not insert token at position " + caretPosition, e);
                } finally {
                    _ignoringEvents = false;
                }

                break;
            default:
                _state = State.MULTIPLE_SUGGESTIONS;
                _completionPopup.setElements(completions);
                if (!_completionPopup.isVisible() && (_automaticallyShowingPopup || showPopup))
                    setPopupVisible(true);
        }
    }
}

