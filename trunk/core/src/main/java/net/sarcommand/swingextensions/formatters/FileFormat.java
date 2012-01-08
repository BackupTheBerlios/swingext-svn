package net.sarcommand.swingextensions.formatters;

import java.io.File;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * A format implementation working on file instances. Conversion is done by simply using the file's absolute path.
 * <p/>
 * <hr/> Copyright 2006-2012 Torsten Heup
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
public class FileFormat extends Format {
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (obj == null)
            throw new IllegalArgumentException("Parameter 'value' must not be null!");
        if (!(obj instanceof File))
            throw new IllegalArgumentException("This instance can only handle values of type File");

        toAppendTo.insert(pos.getBeginIndex(), ((File) obj).getAbsolutePath());
        return toAppendTo;
    }

    public Object parseObject(final String source, final ParsePosition pos) {
        final File file = new File(source);
        pos.setIndex(source.length());
        return file;
    }
}
