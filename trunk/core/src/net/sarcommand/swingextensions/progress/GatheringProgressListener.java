package net.sarcommand.swingextensions.progress;

import net.sarcommand.swingextensions.event.EventSupport;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

/**
 * This class allows you to monitor the progress for multiple concurrent tasks by 'gathering' the individual progress
 * from each by implementing the selector pattern. Basically, this class functions as a factory for special
 * ProgressListener instances you can attach to your tasks. Each of those instances will represent a fraction of the
 * total progress to be made. Whenever one of the tasks singals that progress has been made, the overall completion
 * percentage will be calculated based on these fractions.
 * <p/>
 * Consider an example where you have three long-running tasks, taskA, taskB and taskC. Assuming that taskA and taskB
 * will approximately take 25% of the total computation each, while taskC requires twice as much, you would use the
 * GatheringProgressListener as follows:
 * <p/>
 * <pre> final GatheringProgressListener gatheringListener = new GatheringProgressListener();
 * taskA.addProgressListener(gatheringListener.createListener(0.25f);
 * taskB.addProgressListener(gatheringListener.createListener(0.25f);
 * taskC.addProgressListener(gatheringListener.createListener(0.5f);
 * </pre>
 * <p/>
 * You can then proceed by adding another ProgressListener to the GatheringProgressListener instance. This listener will
 * be notified with the overall progress. The message, source and properties of the event will be copied from the
 * original event.
 * <p/>
 * Note: This class uses weak references to keep track of the listeners used by the createListener(float) method in
 * order to prevent memory leaks. Make sure that you keep a hard reference to the task objects as long as required.
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
public class GatheringProgressListener {
    /**
     * The internal listener support, keeping track of listeners installed on the GatheringProgressListener using the
     * addProgressListener(ProgressListener) method.
     */
    protected EventSupport<ProgressListener> _listenerSupport;

    /**
     * Weakly referenced list of the ProgressListener instances created. Whenever a ProgressEvent is fired, it will
     * iterate over all references to compute the total completion percentage.
     */
    protected LinkedList<WeakReference<GatheringProgressListenerImpl>> _installedListeners;

    /**
     * Creates a new GatheringProgressListener instance.
     */
    public GatheringProgressListener() {
        _listenerSupport = EventSupport.create(ProgressListener.class);
        _installedListeners = new LinkedList<WeakReference<GatheringProgressListenerImpl>>();
    }

    /**
     * Adds a ProgressListener to this instance.
     *
     * @param listener listener to add.
     */
    public void addProgressListener(final ProgressListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _listenerSupport.addListener(listener);
    }

    /**
     * Removes a previously installed listener from this instance.
     *
     * @param listener listener to remove.
     */
    public void removeProgressListener(final ProgressListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("Parameter 'listener' must not be null!");
        _listenerSupport.removeListener(listener);
    }

    /**
     * Creates a new ProgressListener instance, which will monitor a task representing the given fraction of the overall
     * progress to be made.
     *
     * @param fraction The fraction of the overall progress to be made the monitored tasks amounts to.
     * @return a new ProgressListener instance, which will monitor a task representing the given fraction of the overall
     *         progress to be made.
     */
    public ProgressListener createListener(final float fraction) {
        final GatheringProgressListenerImpl listener = new GatheringProgressListenerImpl(this, fraction);
        _installedListeners.add(new WeakReference<GatheringProgressListenerImpl>(listener));
        return listener;
    }

    /**
     * Signalled internally by the ProgressListener instances created by createListener(float). This method is
     * responsible for computing the overall progress.
     *
     * @param evt The last ProgressEvent that occured.
     */
    protected synchronized void progressMade(final ProgressEvent evt) {
        float totalProgress = 0;
        boolean allIndeterminate = true;
        for (Iterator<WeakReference<GatheringProgressListenerImpl>> it = _installedListeners.iterator();
             it.hasNext();) {
            final WeakReference<GatheringProgressListenerImpl> ref = it.next();
            final GatheringProgressListenerImpl l = ref.get();
            if (l == null)
                it.remove();
            else if (l.getCompletionPercentage() >= 0) {
                allIndeterminate = false;
                totalProgress += l.getCompletionPercentage() * l.getFraction();
            }
        }
        final ProgressEvent newEvent = new ProgressEvent(evt.getSource(), evt.getMessage(),
                allIndeterminate ? -1 : totalProgress);
        final Properties oldProperties = evt.getProperties();
        final Properties newProperty = newEvent.getProperties();

        for (Object o : oldProperties.keySet())
            newProperty.setProperty((String) o, oldProperties.getProperty((String) o));

        _listenerSupport.delegate().progressMade(newEvent);
    }

    /**
     * Internal ProgressListener implementation returned by {@link GatheringProgressListener#createListener(float)}.
     * Basically, this implementation will keep track of the last progress value reported and of the fraction of the
     * total task length it amounts to.
     */
    protected static class GatheringProgressListenerImpl implements ProgressListener {
        /**
         * The GatheringProgressListener which created this instance, used as a callback.
         */
        protected GatheringProgressListener _callback;

        /**
         * The fraction of the overall progress this listener's target amounts to.
         */
        protected float _fraction;

        /**
         * The last reported completion percentage.
         */
        protected float _completionPercentage;

        /**
         * Only used internally, so there should be no reason to instanciate this class.
         *
         * @param callback The GatheringProgressListener which created this instance.
         * @param fraction The fraction of the overall progress this listener's target amounts to.
         */
        protected GatheringProgressListenerImpl(final GatheringProgressListener callback,
                                                final float fraction) {
            _callback = callback;
            _fraction = fraction;
        }

        public float getFraction() {
            return _fraction;
        }

        public float getCompletionPercentage() {
            return _completionPercentage;
        }

        public void progressMade(final ProgressEvent event) {
            _completionPercentage = event.getCompletionPercentage();
            _callback.progressMade(event);
        }
    }
}
