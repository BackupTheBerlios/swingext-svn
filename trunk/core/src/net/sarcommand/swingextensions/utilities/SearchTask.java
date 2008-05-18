package net.sarcommand.swingextensions.utilities;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
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
public abstract class SearchTask<T, V> implements Runnable {
    protected Semaphore _semaphore;
    protected Thread _thread;
    protected volatile boolean _cancelled;
    protected LinkedBlockingQueue<T> _queue;

    public SearchTask() {
        _semaphore = new Semaphore(1);
        _queue = new LinkedBlockingQueue<T>(1);
    }

    public void search(final T searchToken) {
        if (_thread == null || _thread.isInterrupted()) {
            _thread = new Thread(this);
            _thread.setName("SearchThread");
            _thread.setPriority(Thread.NORM_PRIORITY);
            _thread.start();
        }

        _cancelled = true;
        _semaphore.acquireUninterruptibly();
        _cancelled = false;
        try {
            _queue.put(searchToken);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            while (true) {
                final T searchToken;
                searchToken = _queue.take();
                final V result = performSearch(searchToken);
                if (!isCancelled()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            done(result);
                        }
                    });
                }
                _semaphore.release();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void cancel() {
        _cancelled = true;
        _semaphore.acquireUninterruptibly();
        _semaphore.release();
    }

    public boolean isCancelled() {
        return _cancelled;
    }

    protected abstract void done(final V result);

    protected abstract V performSearch(final T searchToken);
}
