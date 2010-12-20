package net.sarcommand.swingextensions.event;

import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * ListenerGlue implementation attaching a DocumentListener to an arbitrary JTextComponent, regardless of changes to the
 * component's document. <hr/> Copyright 2006-2010 Torsten Heup
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
public class DocumentListenerGlue extends ListenerGlue<Document> {
    private DocumentListener _listener;

    public DocumentListenerGlue(final JTextComponent target, final DocumentListener listener) {
        _listener = listener;
        initialize(target, "document");
        attachNestedListener(target.getDocument());
    }

    @Override
    protected void attachNestedListener(final Document model) {
        if (model != null)
            model.addDocumentListener(_listener);
    }

    @Override
    protected void detachNestedListener(final Document model) {
        if (model != null)
            model.removeDocumentListener(_listener);
    }
}
