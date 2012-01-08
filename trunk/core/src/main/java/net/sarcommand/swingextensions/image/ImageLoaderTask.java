package net.sarcommand.swingextensions.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * A reuseable, interruptible task used for loading images on a background thread. The loadImage will immediately
 * interrupt any loading operation already in progress and import data from the given InputStream. Once the image is
 * loaded, the abstract imageLoaded method will be invoked on the event dispatch thread. The cancel method will block
 * until the pending load operation has been terminated, or return immediately if no image is being loaded.
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
public abstract class ImageLoaderTask implements Runnable {
    protected Semaphore _syncSemaphore;
    protected Thread _thread;
    protected volatile boolean _cancelled;
    protected volatile boolean _running;
    protected volatile boolean _disposed;
    protected LinkedBlockingQueue<InputStream> _queue;

    public ImageLoaderTask() {
        _syncSemaphore = new Semaphore(1);
        _queue = new LinkedBlockingQueue<InputStream>(1);
    }

    /**
     * Will attempt to load an image from the given location on a background thread. If another image is currently being
     * loaded, the previous load operation will be cancelled.
     *
     * @param source input source from which to load the image data.
     */
    public void loadImage(final InputStream source) {
        if (_thread == null || _thread.isInterrupted()) {
            _thread = new Thread(this);
            _thread.setName("SearchThread");
            _thread.setPriority(Thread.NORM_PRIORITY);
            _thread.start();
        }

        _cancelled = true;
        _syncSemaphore.acquireUninterruptibly();
        _cancelled = false;
        try {
            _queue.put(source);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            while (!_disposed) {
                final InputStream inStream = _queue.take();
                _running = true;

                final InputStream streamWrapper = new InputStream() {
                    public int read() throws IOException {
                        return isCancelled() ? -1 : inStream.read();
                    }
                };

                final BufferedImage result = ImageIO.read(streamWrapper);

                if (!isCancelled()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            imageLoaded(result);
                        }
                    });
                }
                _running = false;
                _syncSemaphore.release();
            }
        } catch (final Exception e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Cancels the currently running load operation, if there is one. This method will block until loading has been
     * terminated..
     */
    public void cancel() {
        if (_running) {
            _cancelled = true;
            _syncSemaphore.acquireUninterruptibly();
            _syncSemaphore.release();
        }
    }

    /**
     * Returns whether the loading operation has been cancelled.
     *
     * @return whether the loading operation has been cancelled.
     */
    public boolean isCancelled() {
        return _cancelled;
    }

    /**
     * Disposes of this task, freeing the underlying background thread.
     */
    public void dispose() {
        _disposed = true;
        cancel();
        _thread = null;
    }

    /**
     * Returns whether this task has been disposed of.
     *
     * @return whether this task has been disposed of.
     */
    public boolean isDisposed() {
        return _disposed;
    }

    /**
     * Callback method invoked when an image was successfully loaded.
     *
     * @param image image loaded by a previus loadImage(InputStream) call.
     */
    public abstract void imageLoaded(final BufferedImage image);
}
