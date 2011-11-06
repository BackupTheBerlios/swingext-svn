package net.sarcommand.swingextensions.dnd;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * TransferHandler subclass used to easily handle drop events for a component. You can specify a list of callback
 * methods for the data flavors your component should accept. Multiple callbacks can be registered for a single data
 * flavor. In this case, the callbacks will be invoked in sequence until one signals that the data was properly
 * imported.
 * <p/>
 * The following exmaple shows how to use a DropHandler for a JLabel, which will then accept String or File objects
 * to be dropped upon it.
 * <p/>
 * <code>
 * final JLabel myLabel = new JLabel();
 * final DropHandler dropHandler = new DropHandler();
 * <p/>
 * dropHandler.addCallback(DataFlavor.stringFlavor, new DropHandlerCallback() {
 * public boolean importData(final Object data) {
 * myLabel.setText((String)data);
 * }
 * });
 * dropHandler.addCallback(DataFlavor.javaFileListFlavor, new DropHandlerCallback() {
 * public boolean importData(final Object data) {
 * myLabel.setText(((List<File>) data).get(0).getName());
 * return true;
 * }
 * });
 * <p/>
 * myLabel.setTransferHandler(dropHandler);
 * </code>
 * <p/>
 * Jun 18, 2010
 * <p/>
 * <hr/>
 * Copyright 2006-2010 Torsten Heup
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
public class DropHandler extends TransferHandler {
    private final HashMap<DataFlavor, LinkedList<DropHandlerCallback>> _callbackMapping;

    /**
     * Creates a new DropHandler instance with no registered data flavor callbacks.
     */
    public DropHandler() {
        _callbackMapping = new HashMap<DataFlavor, LinkedList<DropHandlerCallback>>();
    }

    /**
     * Creates a new DropHandler and registers the given callback for the specified data flavor.
     *
     * @param dataFlavor The DataFlavor this handler should accept.
     * @param callback   The callback to invoke when a drop event for the given flavor occurs.
     */
    public DropHandler(final DataFlavor dataFlavor, final DropHandlerCallback callback) {
        this();
        addCallback(dataFlavor, callback);
    }

    /**
     * Registers the given callback for the specified data flavor.
     *
     * @param dataFlavor The DataFlavor this handler should accept.
     * @param callback   The callback to invoke when a drop event for the given flavor occurs.
     * @return The same instance to allow method chaining.
     */
    public synchronized DropHandler addCallback(final DataFlavor dataFlavor, final DropHandlerCallback callback) {
        if (dataFlavor == null)
            throw new IllegalArgumentException("Parameter 'dataFlavor' must not be null!");
        if (callback == null)
            throw new IllegalArgumentException("Parameter 'callback' must not be null!");

        LinkedList<DropHandlerCallback> callbacks = _callbackMapping.get(dataFlavor);
        if (callbacks == null) {
            callbacks = new LinkedList<DropHandlerCallback>();
            _callbackMapping.put(dataFlavor, callbacks);
        }

        callbacks.add(callback);
        return this;
    }

    /**
     * Removes a previously registered DropHandlerCallback.
     *
     * @param dataFlavor The flavor for which the callback was registered.
     * @param callback   The callback to be removed.
     * @return Whether the callback has been removed. If this returns false, you are trying to remove a callback
     *         that has not been registered.
     */
    public synchronized boolean removeCallback(final DataFlavor dataFlavor, final DropHandlerCallback callback) {
        final LinkedList<DropHandlerCallback> callbacks = _callbackMapping.get(dataFlavor);
        if (callbacks == null)
            return false;
        return callbacks.remove(callback);
    }

    /**
     * Checks whether a callback has been registered for any of the data flavors supported by this drop event.
     *
     * @param support The TransferSupport describing the drop event.
     * @return Whether or not a callback for any of the support's data flavors has been registered.
     */
    @Override public boolean canImport(final TransferSupport support) {
        for (DataFlavor flavor : _callbackMapping.keySet()) {
            final LinkedList<DropHandlerCallback> callbackLinkedList = _callbackMapping.get(flavor);
            if (callbackLinkedList.size() > 0 && support.isDataFlavorSupported(flavor))
                return true;
        }
        return false;
    }

    /**
     * Imports the drop data by going over the list of supported callbacks. This method will return true if a callback
     * could be found that could successfully import the drop data, or false otherwise.
     *
     * @param support The TransferSupport describing the drop event.
     * @return true if a callback could be found that could successfully import the drop data, or false otherwise.
     */
    @Override public boolean importData(final TransferSupport support) {
        try {
            final DataFlavor[] flavors = support.getDataFlavors();
            for (DataFlavor flavor : flavors) {
                final LinkedList<DropHandlerCallback> callbacks = _callbackMapping.get(flavor);
                if (callbacks == null)
                    continue;
                final Object data = support.getTransferable().getTransferData(flavor);
                for (DropHandlerCallback callback : callbacks)
                    if (callback.importData(support.getComponent(), data))
                        return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Callback used by classes using a DropHandler. A DropHandlerCallback is created for a single data flavor and
     * then registered with a DropHandler. If a drop event for this flavor occurs, the DropHandler will invoke this
     * callback.
     *
     * @param <T> The type of the transfer data (optional).
     */
    public static interface DropHandlerCallback<T> {
        /**
         * Prompts the callback to attempt to import the drop transfer data.
         *
         * @param dropTarget The component upon which the data was dropped.
         * @param data       The data being transferred.
         * @return Whether this callback could successfully accept the drop.
         */
        public boolean importData(final Component dropTarget, final T data);
    }
}
