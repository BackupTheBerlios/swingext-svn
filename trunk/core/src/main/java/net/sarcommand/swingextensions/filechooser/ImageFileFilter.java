package net.sarcommand.swingextensions.filechooser;

import javax.imageio.ImageIO;

import static net.sarcommand.swingextensions.internal.SwingExtResources.getResource;

/**
 * A file filter implementation which will accept all image files that can be read through ImageIO. <hr/> Copyright
 * 2006-2012 Torsten Heup
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
public class ImageFileFilter extends ExtensionFileFilter {
    public static final String DESCRIPTION = "ImageFileFilter.description";

    public ImageFileFilter() {
        super(getResource(DESCRIPTION), ImageIO.getReaderFileSuffixes());
    }
}
