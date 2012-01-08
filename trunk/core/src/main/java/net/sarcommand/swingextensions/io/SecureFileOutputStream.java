package net.sarcommand.swingextensions.io;

import java.io.*;

/**
 * Implements a FileOutputStream which will write to a temp file until the stream is closed and then transfer the
 * contents to the real destination file. You may find this class useful if you're performing non-trivial write
 * operations (such as the creation of large xml files) which might fail due to an exception. Rather than overwriting
 * the destination file and potentially destroying it, this implementation will preserve the destination file until you
 * 'commit' to the change by invoking close.
 * <p/>
 * The created temp file will be deleted automatically when the application exits or the stream is closed. Furthermore,
 * you can invoke dispose when writing has failed to remove the temp file imediately.
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
public class SecureFileOutputStream extends OutputStream {
    private final File _file;
    private final File _tempFile;
    private FileOutputStream _outStream;

    public SecureFileOutputStream(final String fileName) throws IOException {
        this(new File(fileName));
    }

    public SecureFileOutputStream(final File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("Parameter 'file' must not be null!");
        _file = file;

        _tempFile = File.createTempFile("java", "temp");
        _tempFile.deleteOnExit();

        _outStream = new FileOutputStream(_tempFile);
    }

    /**
     * Closes the stream, moving the written contents to the streams destination file. This will copy the contents of
     * the underlying temp file and remove it.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        _outStream.close();

        final FileOutputStream out = new FileOutputStream(_file);
        final FileInputStream in = new FileInputStream(_tempFile);

        in.getChannel().transferTo(0, _tempFile.length(), out.getChannel());
        out.close();
        in.close();
        if (!_tempFile.delete())
            _tempFile.deleteOnExit();
    }

    /**
     * Disposes of this stream. You should invoke this method when writing to the output stream has failed to remove the
     * created temp file. After invoking dispose, the stream should be considered.
     *
     * @throws IOException If the temp file could not be deleted.
     */
    public void dispose() throws IOException {
        _outStream.close();
        if (!_tempFile.delete())
            _tempFile.deleteOnExit();
    }

    public void write(final byte b[]) throws IOException {
        _outStream.write(b);
    }

    public void write(final byte b[], final int off, final int len) throws IOException {
        _outStream.write(b, off, len);
    }

    public void write(final int b) throws IOException {
        _outStream.write(b);
    }
}
