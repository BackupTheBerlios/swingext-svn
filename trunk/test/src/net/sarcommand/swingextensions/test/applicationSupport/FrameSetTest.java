package net.sarcommand.swingextensions.test.applicationSupport;

import junit.framework.TestCase;
import net.sarcommand.swingextensions.applicationsupport.FrameSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sarcan
 * Date: 15.09.2006
 * Time: 01:01:30
 * To change this template use File | Settings | File Templates.
 */
public class FrameSetTest extends TestCase {
    public void testExternaliztation() throws Exception {
        final Frame frame1 = new Frame();
        final Frame frame2 = new Frame();
        final Frame frame3 = new Frame();
        final Frame frame4 = new Frame();

        frame1.setBounds(50, 60, 70, 80);
        frame2.setBounds(110, 120, 130, 140);

        final FrameSet set1 = new FrameSet();
        final FrameSet set2 = new FrameSet();

        set1.registerFrame("test1", frame1);
        set1.registerFrame("test2", frame2);
        set2.registerFrame("test1", frame3);
        set2.registerFrame("test2", frame4);

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        final Document document = documentBuilder.newDocument();
        final Element rootElement = document.createElement("root");

        set1.writeExternal(rootElement);
        set2.readExternal(rootElement);

        assertEquals(frame1.getBounds(), frame3.getBounds());
        assertEquals(frame2.getBounds(), frame4.getBounds());
    }
}
