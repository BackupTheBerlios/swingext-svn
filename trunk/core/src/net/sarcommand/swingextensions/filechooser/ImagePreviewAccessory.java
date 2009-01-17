package net.sarcommand.swingextensions.filechooser;

import net.sarcommand.swingextensions.image.ImageLoaderTask;
import net.sarcommand.swingextensions.imagepanel.JImagePanel;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;
import net.sarcommand.swingextensions.progress.JProgressIndicator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A prebuilt FileChooserAccessory used for previewing images. This implementation will load images on a background
 * thread, making sure that the ui remains responsive. While the image is being loaded, a JProgressIndicator will be
 * displayed. If the currently selected file is not an image, an according message will be displayed.
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class ImagePreviewAccessory extends AbstractFileChooserAccessory {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(ImagePreviewAccessory.class);
    protected static final String IMAGE_PANEL_LAYOUT_KEY = "imagePanel";
    protected static final String NO_IMAGE_LABEL_LAYOUT_KEY = "noImageLabel";
    protected static final String PROGRESS_INDICATOR_LAYOUT_KEY = "progressIndicator";


    protected JPanel _accessoryPanel;
    protected JLabel _noImageLabel;
    protected JProgressIndicator _loadIndicator;
    protected JImagePanel _imagePanel;

    protected ImageLoaderTask _loaderTask;
    protected CardLayout _cardLayout;

    public ImagePreviewAccessory() {
        initialize();
    }

    private void initialize() {
        _imagePanel = new JImagePanel();
        _imagePanel.setScaleMode(JImagePanel.SCALE_BOTH);

        _loadIndicator = new JProgressIndicator();
        _loadIndicator.setIndicatingProgress(true);

        _noImageLabel = new JLabel("no image", JLabel.CENTER);

        _cardLayout = new CardLayout();
        _accessoryPanel = new JPanel();
        _accessoryPanel.setLayout(_cardLayout);
        _accessoryPanel.setPreferredSize(new Dimension(200, 200));
        _accessoryPanel.setMinimumSize(new Dimension(150, 150));
        _accessoryPanel.add(_imagePanel, IMAGE_PANEL_LAYOUT_KEY);
        _accessoryPanel.add(_loadIndicator, PROGRESS_INDICATOR_LAYOUT_KEY);
        _accessoryPanel.add(_noImageLabel, NO_IMAGE_LABEL_LAYOUT_KEY);

        _cardLayout.show(_accessoryPanel, NO_IMAGE_LABEL_LAYOUT_KEY);

        setTitle("");

        setAccessoryComponent(_accessoryPanel);
        setPosition(SwingConstants.EAST);
    }

    public void setTitle(final String title) {
        _imagePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    }

    public boolean accept(final File file) {
        final String name = file.getName();
        final String extension = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : null;
        if (extension == null)
            return false;

        final String[] readerFormatNames = ImageIO.getReaderFormatNames();
        for (String s : readerFormatNames)
            if (s.equalsIgnoreCase(extension))
                return true;

        return false;
    }

    public void dispose() {
        _loaderTask.dispose();
        _loaderTask = null;
    }

    public void setFile(final File file) {
        if (file == null || !file.isFile() || !file.canRead()) {
            _cardLayout.show(_accessoryPanel, NO_IMAGE_LABEL_LAYOUT_KEY);
            return;
        }
        try {
            final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
            if (_loaderTask == null || _loaderTask.isDisposed()) {
                _loaderTask = new ImageLoaderTask() {
                    public void imageLoaded(final BufferedImage image) {
                        if (image != null) {
                            _imagePanel.setImage(image);
                            _imagePanel.setScaleMode(JImagePanel.SCALE_BOTH);
                            _cardLayout.show(_accessoryPanel, IMAGE_PANEL_LAYOUT_KEY);
                        } else
                            _cardLayout.show(_accessoryPanel, NO_IMAGE_LABEL_LAYOUT_KEY);
                    }
                };
            }
            _cardLayout.show(_accessoryPanel, PROGRESS_INDICATOR_LAYOUT_KEY);
            _loaderTask.loadImage(inStream);
        } catch (FileNotFoundException e) {
            __log.error("Could not read image " + file.getAbsolutePath(), e);
        }
    }
}
