package net.sarcommand.swingextensions.applicationsupport;

import net.sarcommand.swingextensions.utilities.XMLExternalizable;
import net.sarcommand.swingextensions.utilities.XMLFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.*;
import java.util.List;

/**
 * This class is meant to help you handle a set of associated windows or frames. When writing a
 * multi-windowed application, you will want those windows to behave coherently. For instance,
 * when reactivating one of your frames after your application was obscured by some other window,
 * you will usually want all of your application's frames back on top instead of just the one
 * the user clicked in. Think of a document with a couple of toolbars placed in independent frames.
 * When you start editing the documents, you want your toolbars right back on top instead of having
 * to bring every single one to front manually.
 * <p/>
 * Winows added to a FrameSet will behave that way. Furthermore, they will keep track of the
 * frames' visibility hierarchy, so if one frame was placed in front of another one it will reappear
 * on top when the set is brought back into focus (unless of course the obscured frame was the one
 * selected).
 * <p/>
 * If you are using a multi-windowed application, you will also want the frames to remember their
 * location and size between sessions so you won't have to relocate all toolbars manually every
 * single time. Therefore, a FrameSet offers a set of methods that allows you to easily save the
 * current bounds for each frame in the set. When using an ApplicationSupport, you won't have to
 * worry about this at all, if not, you can easily serialize a FrameSet to xml using the methods
 * defined in externizable.
 * <p/>
 * <p/>
 * todo add demo code
 * <p/>
 * Copyright 2006 Torsten Heup
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
public class FrameSet implements XMLExternalizable {
    public static final String TAG_FRAME = "frame";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_SIZE = "size";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_X = "x";
    public static final String ATTRIBUTE_Y = "y";
    public static final String ATTRIBUTE_WIDTH = "width";
    public static final String ATTRIBUTE_HEIGHT = "height";

    public static FrameSet createFrameSet() {
        return new FrameSet();
    }

    protected Map<String, Window> _frames;
    protected WindowFocusListener _focusListener;

    protected List<Window> _frameHierarchy;
    protected List<Window> _focusPopList;

    public FrameSet() {
        _frameHierarchy = new LinkedList<Window>();
        _focusPopList = Collections.synchronizedList(new LinkedList<Window>());
        _frames = new HashMap<String, Window>(4);
        setupEventHandlers();
    }

    public void registerFrame(final String identifier, final Window frame) {
        _frames.put(identifier, frame);
        _frameHierarchy.add(frame);

        frame.addWindowFocusListener(_focusListener);
    }

    protected void setupEventHandlers() {
        _focusListener = new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                final Window window = e.getWindow();
                _frameHierarchy.remove(window);
                _frameHierarchy.add(0, window);
                if (_focusPopList.contains(window)) {
                    _focusPopList.remove(window);
                    return;
                }

                for (Window w : _frameHierarchy)
                    _focusPopList.add(0, w);

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        for (Window w : _focusPopList)
                            w.requestFocus();
                    }
                });
            }

            public void windowLostFocus(WindowEvent e) {
            }
        };
    }

    /**
     * Write the current component state to xml.
     *
     * @param parentElement Element under which the configuration should be inserted.
     * @throws XMLFormatException If the dom structure could not be created.
     */
    public void writeExternal(final Element parentElement) throws XMLFormatException {
        try {
            final Document doc = parentElement.getOwnerDocument();
            for (Window w : _frameHierarchy) {
                String id = null;
                for (String idIter : _frames.keySet()) {
                    if (w.equals(_frames.get(idIter))) {
                        id = idIter;
                        break;
                    }
                }

                final Element frameElement = doc.createElement(TAG_FRAME);
                final Element locationElement = doc.createElement(TAG_LOCATION);
                final Element sizeElement = doc.createElement(TAG_SIZE);

                frameElement.setAttribute(ATTRIBUTE_ID, id);
                locationElement.setAttribute(ATTRIBUTE_X, "" + w.getX());
                locationElement.setAttribute(ATTRIBUTE_Y, "" + w.getY());
                sizeElement.setAttribute(ATTRIBUTE_WIDTH, "" + w.getWidth());
                sizeElement.setAttribute(ATTRIBUTE_HEIGHT, "" + w.getHeight());

                frameElement.appendChild(locationElement);
                frameElement.appendChild(sizeElement);

                parentElement.appendChild(frameElement);
            }
        } catch (Exception e) {
            throw new XMLFormatException("Could not externalize frame set", e);
        }
    }

    /**
     * Read the component's state from xml.
     *
     * @param parentElement Element under which the configuration has been saved.
     * @throws XMLFormatException If the dom structure could not be parsed.
     */
    public void readExternal(final Element parentElement) throws XMLFormatException {
        final NodeList list = parentElement.getElementsByTagName(TAG_FRAME);
        final int frameCount = list.getLength();
        for (int i = 0; i < frameCount; i++) {
            final Element e = (Element) list.item(i);
            final String id = e.getAttribute(ATTRIBUTE_ID);
            final Window w = _frames.get(id);
            if (w == null)
                continue;

            final Element location = (Element) e.getElementsByTagName(TAG_LOCATION).item(0);
            final Element size = (Element) e.getElementsByTagName(TAG_SIZE).item(0);

            final int x = Integer.parseInt(location.getAttribute(ATTRIBUTE_X));
            final int y = Integer.parseInt(location.getAttribute(ATTRIBUTE_Y));
            final int width = Integer.parseInt(size.getAttribute(ATTRIBUTE_WIDTH));
            final int height = Integer.parseInt(size.getAttribute(ATTRIBUTE_HEIGHT));

            w.setLocation(x, y);
            w.setSize(width, height);
        }
    }
}