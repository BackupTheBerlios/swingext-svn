package net.sarcommand.swingextensions.filechooser;

import static net.sarcommand.swingextensions.internal.SwingExtResources.getResource;

import javax.imageio.ImageIO;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ImageFileFilter extends ExtensionFileFilter {
    public static final String DESCRIPTION = "ImageFileFilter.description";

    public ImageFileFilter() {
        super(getResource(DESCRIPTION), ImageIO.getReaderFileSuffixes());
    }
}
