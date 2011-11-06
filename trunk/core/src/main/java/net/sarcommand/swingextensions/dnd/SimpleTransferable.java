package net.sarcommand.swingextensions.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;

/**
 * A simple implementation of the transferable interface, using a HashMap to map DataFlavor instances to transfer data.
 * This class is useful when you are either using a single data flavor or transfer operations or if you have distinct
 * data classes for different transfer flavors. For instance, if you want to transfer a file both as a File instance and
 * as an abstract path string, it is often less effort to just add those representations to a SimpleTransferable rather
 * than writing a custom Transferable implementation for doing a value conversion.
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
 */
public class SimpleTransferable implements Transferable {
    private HashMap<DataFlavor, Object> _data;

    /**
     * Creates a new SimpleTransferable. Transfer data has to be added via {@link net.sarcommand.swingextensions.dnd.SimpleTransferable#addTransferData(java.awt.datatransfer.DataFlavor,
     * Object)}
     */
    public SimpleTransferable() {
        _data = new HashMap<DataFlavor, Object>(2);
    }

    /**
     * Creates a new SimpleTransferable encapulating the given transfer data. Further transfer data representations may
     * be added using {@link net.sarcommand.swingextensions.dnd.SimpleTransferable#addTransferData(java.awt.datatransfer.DataFlavor,
     * Object)}
     *
     * @param data   The actual transfer data to be added.
     * @param flavor The data flavor of the added transfer data.
     */
    public SimpleTransferable(final Object data, final DataFlavor flavor) {
        this();
        _data.put(flavor, data);
    }

    /**
     * Adds another representation of the enclosed transfer data to this transferable.
     *
     * @param flavor       The flavor of the transfer data being added.
     * @param transferData The transfer data to add.
     */
    public void addTransferData(final DataFlavor flavor, final Object transferData) {
        _data.put(flavor, transferData);
    }

    /**
     * @see Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        final Object transferData = _data.get(flavor);
        if (transferData == null)
            throw new UnsupportedFlavorException(flavor);

        return transferData;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return _data.keySet().toArray(new DataFlavor[_data.size()]);
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        return _data.get(flavor) != null;
    }
}
