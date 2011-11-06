package net.sarcommand.swingextensions.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Conveniance adapter class for the DocumentListener interface. In addition to the methods declared in
 * DocumentListener, it also offers a generic documentChaned(DocumentEvent) method, if you care for all kinds of
 * events.
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
 * @see DocumentListener
 */
public abstract class DocumentAdapter implements DocumentListener {
    public void changedUpdate(final DocumentEvent e) {
        documentChanged(e);
    }

    public void insertUpdate(final DocumentEvent e) {
        documentChanged(e);
    }

    public void removeUpdate(final DocumentEvent e) {
        documentChanged(e);
    }

    /**
     * Invoked whenever any kind of change occurs in the document. You can examine the event's type to find out whether
     * it is a change, insert or removeUpdate.
     *
     * @param e the generated DocumentEvent.
     */
    public void documentChanged(final DocumentEvent e) {
    }
}