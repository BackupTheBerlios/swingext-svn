package net.sarcommand.swingextensions.formatters;

import java.io.File;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * A format implementation working on file instances. Conversion is done by simply using the file's absolute path.
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
