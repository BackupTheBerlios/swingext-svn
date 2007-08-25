package net.sarcommand.swingextensions.gimmicks;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * This class provides a set of methods aroung image and graphics manipulation. Most of those manipulation are mere
 * gimmicks, but sometimes vanity is virtue.
 */
public class ImageOperations {
    /**
     * Creates a very simple implementation of a 3x3 box blur operation.
     *
     * @return a very simple implementation of a 3x3 box blur operation.
     */
    public static ConvolveOp createSimpleBoxBlurFilter() {
        final float[] filter = new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f};
        return new ConvolveOp(new Kernel(3, 3, filter));
    }

    /**
     * Applies the operation returned by createSimpleBoxBlurFilter() to the given image and returns the modified
     * version.
     *
     * @param sourceImage image used as source for the operation.
     * @return blurred image.
     */
    public static BufferedImage applySimpleBoxBlur(final BufferedImage sourceImage) {
        final BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(),
                sourceImage.getType());
        image.createGraphics().drawImage(sourceImage, createSimpleBoxBlurFilter(), 0, 0);
        return image;
    }
}
