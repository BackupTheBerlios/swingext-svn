package net.sarcommand.swingextensions.typedinputfields;


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements a Document which restricts its data to match one or more regular expressions. This class is
 * used internally by the text based TypedInputField implementations.
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
public class RegexpConstrainedDocument extends PlainDocument {
    /**
     * The collection of regexp patterns which will be accepted by this document.
     */
    protected Collection<Pattern> _patterns;

    /**
     * The TypedInputField instance using this document, used as a callback.
     */
    protected TypedInputField _field;

    public RegexpConstrainedDocument(final TypedInputField field, final String... patterns) {
        _patterns = new LinkedList<Pattern>();
        for (String pattern : patterns)
            _patterns.add(Pattern.compile(pattern));

        _field = field;
    }

    /**
     * Inserts the given string into the document.
     *
     * @param offs the starting offset >= 0
     * @param str  the string to insert; does nothing with null/empty strings
     * @param a    the attributes for the inserted content
     * @throws javax.swing.text.BadLocationException
     *          the given insert position is not a valid position within the document
     * @see javax.swing.text.Document#insertString
     */
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        try {
            final Content content = getContent();
            final StringBuffer buffer = new StringBuffer(content.getString(0, content.length() - 1));
            buffer.insert(offs, str);

            boolean matches = false;
            boolean hitEnd = false;
            for (Pattern p : _patterns) {
                final Matcher m = p.matcher(buffer);
                if (m.matches()) {
                    matches = true;
                    break;
                } else if (m.hitEnd()) {
                    hitEnd = true;
                }
            }

            final TypedInputFieldEditCallback callback = _field.getInputFieldEditFeedback();
            if (matches) {
                callback.inputLegal(_field);
                super.insertString(offs, str, a);
            } else if (hitEnd) {
                callback.inputIncomplete(_field);
                super.insertString(offs, str, a);
            } else {
                callback.inputIllegal(_field);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException("Internal error:", e);
        }
    }


    /**
     * Removes the given substring from the document.
     *
     * @param offs the starting offset >= 0
     * @param len  the number of characters to remove >= 0
     * @throws javax.swing.text.BadLocationException
     *          the given remove position is not a valid position within the document
     * @see javax.swing.text.Document#remove
     */
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        try {
            final Content content = getContent();
            final StringBuffer buffer = new StringBuffer(content.getString(0, content.length() - 1));
            buffer.delete(offs, offs + len);

            boolean matches = false;
            for (Pattern p : _patterns) {
                final Matcher m = p.matcher(buffer);
                if (m.matches()) {
                    matches = true;
                    break;
                }
            }

            final TypedInputFieldEditCallback callback = _field.getInputFieldEditFeedback();
            if (matches) {
                callback.inputLegal(_field);
                super.remove(offs, len);
            } else {
                callback.inputIncomplete(_field);
                super.remove(offs, len);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException("Internal error:", e);
        }
    }
}

