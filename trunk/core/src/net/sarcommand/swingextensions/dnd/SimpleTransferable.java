package net.sarcommand.swingextensions.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * A simple implementation of the transferable interface, offering support for transfering a single object with a single
 * data flavor.
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
public class SimpleTransferable implements Transferable {
    private Object _data;
    private DataFlavor _flavor;

    public SimpleTransferable(final Object data, final DataFlavor flavor) {
        _data = data;
        _flavor = flavor;
    }

    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!flavor.equals(_flavor))
            throw new UnsupportedFlavorException(flavor);

        return _data;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{_flavor};
    }

    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        return flavor.equals(_flavor);
    }
}
