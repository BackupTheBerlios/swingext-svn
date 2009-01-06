package net.sarcommand.swingextensions.utilities;

import java.awt.*;
import java.awt.image.*;

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
        final Graphics2D graphics2D = image.createGraphics();
        graphics2D.drawImage(sourceImage, createSimpleBoxBlurFilter(), 0, 0);
        graphics2D.dispose();
        return image;
    }

    public static BufferedImage applyGaussianBlur(final BufferedImage image, final int filterRadius) {
        /* todo this can be handled more efficiently */
        if (filterRadius < 1)
            throw new IllegalArgumentException("Illegal filter radius: expected to be >= 1, was " + filterRadius);

        float[] kernel = new float[2 * filterRadius + 1];

        final float sigma = filterRadius / 3f;
        final float alpha = 2f * sigma * sigma;
        final float rootAlphaPI = (float) Math.sqrt(alpha * Math.PI);
        float sum = 0;
        for (int i = -0; i < kernel.length; i++) {
            final int d = -((i - filterRadius) * (i - filterRadius));
            kernel[i] = (float) (Math.exp(d / alpha) / rootAlphaPI);
            sum += kernel[i];
        }

        for (int i = 0; i < kernel.length; i++)
            kernel[i] /= sum;

        final Kernel horizontalKernel = new Kernel(1, kernel.length, kernel);
        final Kernel verticalKernel = new Kernel(kernel.length, 1, kernel);

        final BufferedImage interim = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D interimGraphics = interim.createGraphics();
        interimGraphics.drawImage(image, new ConvolveOp(horizontalKernel, ConvolveOp.EDGE_NO_OP, null), 0, 0);
        interimGraphics.dispose();

        final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D resultGraphics = result.createGraphics();
        resultGraphics.drawImage(interim, new ConvolveOp(verticalKernel, ConvolveOp.EDGE_NO_OP, null), 0, 0);
        resultGraphics.dispose();

        return result;
    }

    public static BufferedImage applyGlow(final BufferedImage image, final Color paint, final int radius) {
        final BufferedImage blurred = applyGaussianBlur(image, radius);

        final BufferedImage glow = new BufferedImage(image.getWidth() + 2 * radius, image.getHeight() + 2 * radius,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = glow.createGraphics();

        final int red = paint.getRed();
        final int green = paint.getGreen();
        final int blue = paint.getBlue();
        final LookupTable table = new LookupTable(0, 4) {
            @Override
            public int[] lookupPixel(final int[] src, final int[] dest) {
                dest[0] = red;
                dest[1] = green;
                dest[2] = blue;
                dest[3] = src[3];
                return dest;
            }
        };

        g2.drawImage(blurred, new LookupOp(table, null), 0, 0);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return glow;
    }
}
