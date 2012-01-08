package net.sarcommand.swingextensions.utilities;

/**
 * Interface for classes which alter the behaviour of vanilla swing components. See the differnent implementations for
 * details.
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
public interface ComponentVariation<T> {
    /**
     * Tells this variation to detach from the altered components, restoring its normal state. Implementations have to
     * ensure that no dangling references exist and they may be garbage collected if not referenced from another
     * location.
     */
    public void detach();

    /**
     * Returns the component altered by this instance.
     *
     * @return the component altered by this instance.
     */
    public T getAlteredComponent();
}
