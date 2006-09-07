package net.sarcommand.swingextensions.icon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * This class implements a simple text-based icon. For several occasions it may come in handy to use unicode
 * characters (like 0x2460) as simple icons instead of having to create images and store those as external resources.
 * Therefore, this icon implementation will render a short text or rather a single character and can be used anywhere
 * you would normally use an {@link ImageIcon}.
 *
 * Hint: You can set an affine transform for the font instance this icon uses to make text appear packed tightly or
 * rotate characters.
 *
 * Copyright 2006 Torsten Heup

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
public class TextIcon implements Icon {
    /**
     * Image buffer which contains the rendered string.
     */
    protected BufferedImage _imgBuffer;

    /**
     * Creates a new text icon using default font and color.
     * @param text Text to render. (required)
     */
    public TextIcon(final String text){
        initialize(text, null, null, 1f);
    }

    /**
     * Creates a new text icon using the default color.
     * @param text Text to render. (required)
     * @param font Font so use when rendering. (optional)
     */
    public TextIcon(final String text, final Font font) {
        initialize(text, font, null, 1f);
    }

    /**
     * Creates a new text icon using the default font.
     * @param text Text to render. (required)
     * @param color Foreground color to use. (optional)
     */
    public TextIcon(final String text, final Color color){
        initialize(text, null, color, 1f);
    }

    /**
     * Creates a new text icon using the given font and color.
     * @param text Text to render. (required)
     * @param font Font so use when rendering. (optional)
     * @param color Foreground color to use. (optional)
     */
    public TextIcon(final String text, final Font font, final Color color){
        initialize(text, font, color, 1f);
    }

    /**
     * Creates a new text icon using the given font and color. Additionally, an alpha composite may be
     * specified to make the text icon translucent.
     *
     * @param text Text to render.
     * @param font Font so use when rendering. (optional)
     * @param color Foreground color to use. (optional)
     * @param alpha Alpha composite to set when rendering the text.
     */
    public TextIcon(final String text, final Font font, final Color color, final float alpha) {
        initialize(text, font, color, alpha);
    }

    /**
     * Initializes the icon by filling the underlying image buffer.
     *
     * @param text Text that should be rendered.
     * @param font Font to use.
     * @param color Foreground color to use.
     * @param alpha Alpha composite which should be used when rendering.
     */
    private void initialize(final String text, Font font, Color color, final float alpha) {
        if (text == null)
            throw new IllegalArgumentException("Parameter 'text' must not be null!");
        if (font == null)
            font = UIManager.getFont("Label.font");
        if (color == null)
            color = UIManager.getColor("Label.foreground");

        final BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tempGraphics = temp.createGraphics();
        final FontMetrics fontMetrics = tempGraphics.getFontMetrics(font);
        final Rectangle2D stringBounds = fontMetrics.getStringBounds(text, tempGraphics);
        tempGraphics.dispose();

        _imgBuffer = new BufferedImage((int) Math.ceil(stringBounds.getWidth()), (int) Math.ceil(stringBounds.getHeight()), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = _imgBuffer.createGraphics();
        g2.setFont(font);
        g2.setColor(color);
        if (alpha != 1f)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.drawString(text, 0, (int) Math.ceil(-stringBounds.getY()));
        g2.dispose();
    }

    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return _imgBuffer.getHeight();
    }

    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return _imgBuffer.getWidth();
    }

    /**
     * Draw the icon at the specified location.  Icon implementations
     * may use the Component argument to get properties useful for
     * painting, e.g. the foreground or background color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.drawImage(_imgBuffer, x, y, c);
    }
}
