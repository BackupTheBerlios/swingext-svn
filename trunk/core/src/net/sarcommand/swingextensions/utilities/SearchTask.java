package net.sarcommand.swingextensions.utilities;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * The SearchTask class can be used to implement an incremental search function, realized using a background thread. It
 * will handle all the synchronization necessary for starting and stopping the search properly. Basically, each time you
 * want to update the search token (e.g. the string being searched for), the current search will be interrupted cleanly
 * and a new one will be invoked. Searching will be performed on a background thread, while results will be published on
 * the event dispatch thread for synchronization.
 * <p/>
 * This implementation is reuseable. If you cancel() a search, you can start a new one using the search(T) function at
 * any time.
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
public abstract class SearchTask<T, V> implements Runnable {
    protected Semaphore _semaphore;
    protected Thread _thread;
    protected volatile boolean _cancelled;
    protected LinkedBlockingQueue<T> _queue;

    protected volatile boolean _running;

    public SearchTask() {
        _semaphore = new Semaphore(1);
        _queue = new LinkedBlockingQueue<T>(1);
    }

    /**
     * Searches for the given token. When this method is invoked, it will interrupt the currently running search (if
     * applicable) and invoke a new one, reusing the same background thread.
     *
     * @param searchToken object being searched for.
     */
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

    /**
     * Do not invoke manually.
     */
    public void run() {
        try {
            while (true) {
                final T searchToken;
                searchToken = _queue.take();
                _running = true;
                final V result = performSearch(searchToken);
                if (!isCancelled()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            done(result);
                        }
                    });
                }
                _running = false;
                _semaphore.release();
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
     * Cancels the currently running search, if there is one. This method will block until the search has ended.
     */
    public void cancel() {
        if (_running) {
            _cancelled = true;
            _semaphore.acquireUninterruptibly();
            _semaphore.release();
        }
    }

    /**
     * Returns whether the search has been cancelled.
     *
     * @return whether the search has been cancelled.
     */
    public boolean isCancelled() {
        return _cancelled;
    }

    /**
     * Invoked when the search has finished. This method will be invoked on the EDT.
     *
     * @param result The search result produced by the implementation of performSearch(T).
     */
    protected abstract void done(final V result);

    /**
     * This method has to be implemented with the actual search code, depending on the context. It will be invoked on
     * the background search thread with the token passed to search(T).
     *
     * @param searchToken
     * @return
     */
    protected abstract V performSearch(final T searchToken);
}
