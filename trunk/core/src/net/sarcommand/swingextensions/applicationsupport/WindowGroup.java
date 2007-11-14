package net.sarcommand.swingextensions.applicationsupport;

import net.sarcommand.swingextensions.utilities.*;
import org.w3c.dom.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.List;

/**
 * This class is meant to help you handle a set of associated windows or frames. When writing a
 * multi-windowed application, you will want those windows to behave coherently. For instance,
 * when reactivating one of your frames after your application was obscured by some other window,
 * you might want all of your application's frames back on top instead of just the one
 * the user clicked in. Think of a document with a couple of toolbars placed in independent frames.
 * When you start editing the documents, you want your toolbars right back on top instead of having
 * to bring every single one to front manually.
 * <p/>
 * Winows added to a WindowGroup will behave that way. Furthermore, they will keep track of the
 * windows' visibility hierarchy, so if one windows was placed in front of another one it will reappear
 * on top when the set is brought back into focus (unless of course the obscured frame was the one
 * selected).
 * <p/>
 * If you are using a multi-windowed application, you will also want the windows to remember their
 * location and size between sessions so you won't have to relocate all toolbars manually every
 * single time. Therefore, a WindowGroup offers a set of methods that allows you to easily save the
 * current bounds for each frame in the set. When using an ApplicationSupport, you won't have to
 * worry about this at all, if not, you can easily serialize a WindowGroup to xml using the methods
 * defined in {@link XMLExternalizable}.
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
public class WindowGroup implements XMLExternalizable {
    public static final String BRING_TO_FRONT_PROPERTY = "shouldBringToFront";
    public static final String FOCUSED_WINDOW_PROPERTY = "focusedWindow";
    public static final String WINDOW_COUNT_PROPERTY = "windowCount";

    public static final String TAG_WINDOW = "window";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_SIZE = "size";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_X = "x";
    public static final String ATTRIBUTE_Y = "y";
    public static final String ATTRIBUTE_WIDTH = "width";
    public static final String ATTRIBUTE_HEIGHT = "height";

    public static WindowGroup createWindowSet() {
        return new WindowGroup();
    }

    protected PropertyChangeSupport _pcs;
    protected Map<String, Window> _idToWindowMapping;
    protected Map<Window, String> _windowToIdMapping;
    protected WindowFocusListener _focusListener;

    protected List<Window> _windowHierarchy;
    protected List<Window> _focusPopList;

    protected boolean _shouldBringAllToFront;

    /**
     * Creates a new, empty window group.
     */
    public WindowGroup() {
        _pcs = new PropertyChangeSupport(this);
        _shouldBringAllToFront = false;
        _windowHierarchy = Collections.synchronizedList(new LinkedList<Window>());
        _focusPopList = Collections.synchronizedList(new LinkedList<Window>());
        _idToWindowMapping = Collections.synchronizedMap(new HashMap<String, Window>(4));
        _windowToIdMapping = Collections.synchronizedMap(new HashMap<Window, String>(4));
        setupEventHandlers();
    }

    /**
     * Returns whether all windows in this group will be brought to front when one of them gains focus. See the
     * class doc for details.
     *
     * @return whether all windows in this group will be brought to front when one of them gains focus.
     */
    public boolean getShouldBringAllToFront() {
        return _shouldBringAllToFront;
    }

    /**
     * Defines whether all of the windows in this group should be brought to front when one gains focus. See the
     * class doc for details. Defaults to false.
     *
     * @param shouldBringAllToFront whether all of the windows in this group should be brought to front when one
     *                              gains focus.
     */
    public void setShouldBringAllToFront(boolean shouldBringAllToFront) {
        final boolean old = _shouldBringAllToFront;
        _shouldBringAllToFront = shouldBringAllToFront;
        _pcs.firePropertyChange(BRING_TO_FRONT_PROPERTY, old, shouldBringAllToFront);
    }

    /**
     * Adds a window to this group under the given identifier.
     *
     * @param identifier Name under which this window will be registered. Non-null.
     * @param window     Window to add. Non-null.
     */
    public void addWindow(final String identifier, final Window window) {
        if (identifier == null)
            throw new IllegalArgumentException("Parameter 'identifier' must not be null!");
        if (window == null)
            throw new IllegalArgumentException("Parameter 'window' must not be null!");

        final int oldCount = _idToWindowMapping.size();

        _idToWindowMapping.put(identifier, window);
        _windowToIdMapping.put(window, identifier);
        _windowHierarchy.add(window);

        window.addWindowFocusListener(_focusListener);

        _pcs.firePropertyChange(WINDOW_COUNT_PROPERTY, oldCount, _idToWindowMapping.size());
    }

    /**
     * Removes the window with the given identifier from this group.
     *
     * @param identifier Identifier under which the window was registered.
     */
    public void removeWindow(final String identifier) {
        final int oldCount = _idToWindowMapping.size();

        final Window window = _idToWindowMapping.get(identifier);
        _windowHierarchy.remove(window);
        _windowToIdMapping.remove(window);
        _idToWindowMapping.remove(identifier);
        window.removeWindowFocusListener(_focusListener);

        _pcs.firePropertyChange(WINDOW_COUNT_PROPERTY, oldCount, _idToWindowMapping.size());
    }

    /**
     * Returns the window which was registered under the given name.
     *
     * @param name Name of the window being queried.
     * @return the window which was registered under the given name, or null if there is none.
     */
    public Window getWindow(final String name) {
        return _idToWindowMapping.get(name);
    }

    /**
     * Returns the name under which the given window was registered.
     *
     * @param window the window being queried.
     * @return the name under which the given window was registered, or null if 'window' is not member of this group.
     */
    public String getWindowName(final Window window) {
        return _windowToIdMapping.get(window);
    }

    /**
     * Ensures that all of the windows in this group are visible on the screen and not obscured by others. The current
     * z-order of the windows will be retained.
     */
    public void bringToFront() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (Window w : _focusPopList)
                    w.requestFocus();
            }
        });
    }

    /**
     * Returns the window instances in this group.
     *
     * @return the window instances in this group.
     */
    public Collection<Window> getWindows() {
        return new ArrayList<Window>(_idToWindowMapping.values());
    }

    /**
     * Returns the names for the windows in this group.
     *
     * @return the names for the windows in this group.
     */
    public Collection<String> getWindowNames() {
        return new ArrayList<String>(_idToWindowMapping.keySet());
    }

    /**
     * Returns the number of window instances in this group.
     *
     * @return the number of window instances in this group.
     */
    public int getWindowCount() {
        return _idToWindowMapping.size();
    }

    /**
     * If a window in this group is the current focus owner, it will be returned. Otherwise this method will return
     * null.
     *
     * @return Returns the window in this group currently owning the focus if applicable, or null otherwise.
     */
    public Window getCurrentFocusOwner() {
        for (Window w : _windowHierarchy)
            if (w.isFocused())
                return w;
        return null;
    }

    protected void setupEventHandlers() {
        _focusListener = new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                final Window window = e.getWindow();
                _pcs.firePropertyChange(FOCUSED_WINDOW_PROPERTY, e.getOppositeWindow(), window);

                if (_shouldBringAllToFront) {
                    _windowHierarchy.remove(window);
                    _windowHierarchy.add(0, window);
                    if (_focusPopList.contains(window)) {
                        _focusPopList.remove(window);
                        return;
                    }

                    for (Window w : _windowHierarchy)
                        _focusPopList.add(0, w);
                    bringToFront();
                }
            }

            public void windowLostFocus(WindowEvent e) {
                if (_windowHierarchy.contains(e.getOppositeWindow()))
                    return;
                _pcs.firePropertyChange(FOCUSED_WINDOW_PROPERTY, e.getWindow(), null);
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
            for (Window w : _windowHierarchy) {
                String id = null;
                for (String idIter : _idToWindowMapping.keySet()) {
                    if (w.equals(_idToWindowMapping.get(idIter))) {
                        id = idIter;
                        break;
                    }
                }

                final Element frameElement = doc.createElement(TAG_WINDOW);
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
        final NodeList list = parentElement.getElementsByTagName(TAG_WINDOW);
        final int frameCount = list.getLength();
        for (int i = 0; i < frameCount; i++) {
            final Element e = (Element) list.item(i);
            final String id = e.getAttribute(ATTRIBUTE_ID);
            final Window w = _idToWindowMapping.get(id);
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _pcs.removePropertyChangeListener(listener);
    }
}                                                                            