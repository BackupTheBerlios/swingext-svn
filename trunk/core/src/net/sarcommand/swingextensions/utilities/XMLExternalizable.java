package net.sarcommand.swingextensions.utilities;

import org.w3c.dom.Element;


/**
 * Common interface for components which are able to externalize their current state into a dom
 * xml structure.
 */
public interface XMLExternalizable {
    /**
     * Write the current component state to xml.
     *
     * @param parentElement Element under which the configuration should be inserted.
     * @throws XMLFormatException If the dom structure could not be created.
     */
    public void writeExternal(final Element parentElement) throws XMLFormatException;

    /**
     * Read the component's state from xml.
     *
     * @param parentElement Element under which the configuration has been saved.
     * @throws XMLFormatException If the dom structure could not be parsed.
     */
    public void readExternal(final Element parentElement) throws XMLFormatException;
}
