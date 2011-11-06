package net.sarcommand.swingextensions.image;

import java.awt.*;
import java.awt.image.*;
import java.lang.ref.SoftReference;

/**
 * This class provides a set of methods aroung image and graphics manipulation. Most of those manipulation are mere
 * gimmicks, but sometimes vanity is virtue.
 * <p/>
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
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
public class ImageUtilities {
    private static SoftReference<BufferedImage> _buffer0;
    private static SoftReference<BufferedImage> _buffer1;

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

    /**
     * Applies a gaussian blur filter to the given image. Apart from the filter radius, you can also specify an alpha
     * factor which will be multiplied with the filter's result. Also, you can specify whether the blurred image should
     * be rendered into a newly created BufferedImage instance or into the original image. If you request a new image
     * instance, the result will be larger than the original one as a (2*filterradius) pixel wide padding will be
     * applied.
     *
     * @param image                         the image to be blurred.
     * @param filterRadius                  the radius of the gaussian filter to apply. The corresponding kernel will be
     *                                      sized 2 * filterRadius + 1;
     * @param alphaFactor                   a factor which will be multiplied with the filtered image. You can use this
     *                                      parameter to weaken or strengthen the colors in the blurred image.
     * @param useOriginalImageAsDestination Determines whether the blur result should be rendered into the original
     *                                      image or into a new image instance. If you choose to create a new image
     *                                      instance, the result will be larger than the original image to provide the
     *                                      required padding for the blur effect.
     * @return An image instance containing a blurred version of the given image.
     */
    public static BufferedImage applyGaussianBlur(final BufferedImage image, final int filterRadius,
                                                  final float alphaFactor,
                                                  final boolean useOriginalImageAsDestination) {
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

        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= sum;
            kernel[i] *= alphaFactor;
        }

        final Kernel horizontalKernel = new Kernel(kernel.length, 1, kernel);
        final Kernel verticalKernel = new Kernel(1, kernel.length, kernel);

        synchronized (ImageUtilities.class) {
            final int blurredWidth = useOriginalImageAsDestination ? image.getWidth() :
                    image.getWidth() + 4 * filterRadius;
            final int blurredHeight = useOriginalImageAsDestination ? image.getHeight() :
                    image.getHeight() + 4 * filterRadius;

            final BufferedImage img0 = ensureBuffer0Capacity(blurredWidth, blurredHeight);
            final Graphics2D graphics0 = img0.createGraphics();
            graphics0.drawImage(image, null, useOriginalImageAsDestination ? 0 : 2 * filterRadius,
                    useOriginalImageAsDestination ? 0 : 2 * filterRadius);
            graphics0.dispose();

            final BufferedImage img1 = ensureBuffer1Capacity(blurredWidth, blurredHeight);
            final Graphics2D graphics1 = img1.createGraphics();
            graphics1.drawImage(img0, new ConvolveOp(horizontalKernel, ConvolveOp.EDGE_NO_OP, null), 0, 0);
            graphics1.dispose();

            BufferedImage destination = useOriginalImageAsDestination ? image : new BufferedImage(blurredWidth,
                    blurredHeight, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D destGraphics = destination.createGraphics();
            destGraphics.drawImage(img1, new ConvolveOp(verticalKernel, ConvolveOp.EDGE_NO_OP, null), 0, 0);
            destGraphics.dispose();

            return destination;
        }
    }

    /**
     * Applies a glow effect to the given image. This is done by first creating a blurred version of the image using
     * {@link ImageUtilities#applyGaussianBlur(java.awt.image.BufferedImage,
     * int, float, boolean)}. The result of this operation is then rendered below the original using a LookupTable.
     *
     * @param image                         The image to apply the glow effect to.
     * @param radius                        The radius of the gaussian blur being applied.
     * @param color                         The color of the glow effect.
     * @param alphaFactor                   the alpha factor as specified in {@link ImageUtilities#applyGaussianBlur(java.awt.image.BufferedImage,
     *                                      int, float, boolean)}
     * @param useOriginalImageAsDestination Whether to render the result into the original image or a newly created
     *                                      image instance. If a new image is created, it will be larger than the
     *                                      original one to make room for the blur effect.
     * @return a 'glowing' version of the original image.
     */
    public static BufferedImage applyGlow(final BufferedImage image, final int radius, final Color color,
                                          final float alphaFactor, final boolean useOriginalImageAsDestination) {
        final BufferedImage blurred = applyGaussianBlur(image, radius, alphaFactor, false);

        final BufferedImage dest = useOriginalImageAsDestination ? image :
                new BufferedImage(image.getWidth() + 2 * radius, image.getHeight() + 2 * radius,
                        BufferedImage.TYPE_INT_ARGB);

        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();
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

        final Graphics2D g2 = dest.createGraphics();

        if (useOriginalImageAsDestination) {
            g2.setComposite(AlphaComposite.DstOver);
            g2.drawImage(blurred, new LookupOp(table, null), -2 * radius, -2 * radius);
        } else {
            g2.drawImage(blurred, new LookupOp(table, null), -radius, -radius);
            g2.drawImage(image, radius, radius, null);
        }
        g2.dispose();

        return dest;
    }

    /**
     * Changes the color saturation of the given RGB image.
     *
     * @param image image expected to contain a 4 band rgba color model.
     * @param s     The factor with which the saturation value should be multiplied.
     */
    public static void changeRGBSaturation(final BufferedImage image, final double s) {
        double RW = 0.3086;
        double RG = 0.6084;
        double RB = 0.0820;

        final double a = (1 - s) * RW + s;
        final double b = (1 - s) * RW;
        final double c = (1 - s) * RW;
        final double d = (1 - s) * RG;
        final double e = (1 - s) * RG + s;
        final double f = (1 - s) * RG;
        final double g = (1 - s) * RB;
        final double h = (1 - s) * RB;
        final double i = (1 - s) * RB + s;

        final int width = image.getWidth();
        final int height = image.getHeight();
        final double[] red = new double[width * height];
        final double[] green = new double[width * height];
        final double[] blue = new double[width * height];

        final WritableRaster raster = image.getRaster();
        raster.getSamples(0, 0, width, height, 0, red);
        raster.getSamples(0, 0, width, height, 1, green);
        raster.getSamples(0, 0, width, height, 2, blue);

        for (int x = 0; x < red.length; x++) {
            final double r0 = red[x];
            final double g0 = green[x];
            final double b0 = blue[x];
            red[x] = a * r0 + d * g0 + g * b0;
            green[x] = b * r0 + e * g0 + h * b0;
            blue[x] = c * r0 + f * g0 + i * b0;
        }

        raster.setSamples(0, 0, width, height, 0, red);
        raster.setSamples(0, 0, width, height, 1, green);
        raster.setSamples(0, 0, width, height, 2, blue);
    }

    private static BufferedImage ensureBuffer0Capacity(final int width, final int height) {
        BufferedImage img0 = _buffer0 != null ? _buffer0.get() : null;
        img0 = ensureBufferCapacity(width, height, img0);
        _buffer0 = new SoftReference<BufferedImage>(img0);
        return img0;
    }

    private static BufferedImage ensureBuffer1Capacity(final int width, final int height) {
        BufferedImage img1 = _buffer1 != null ? _buffer0.get() : null;
        img1 = ensureBufferCapacity(width, height, img1);
        _buffer1 = new SoftReference<BufferedImage>(img1);
        return img1;
    }

    private static BufferedImage ensureBufferCapacity(final int width, final int height, BufferedImage img) {
        if (img == null || img.getWidth() < width || img.getHeight() < height) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        } else {
            final Graphics2D g2 = img.createGraphics();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, width, height);
            g2.dispose();
        }
        return img;
    }
}
