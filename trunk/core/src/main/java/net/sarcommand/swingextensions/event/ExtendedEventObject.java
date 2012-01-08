package net.sarcommand.swingextensions.event;

import java.util.EventObject;
import java.util.Properties;

/**
 * This class extends the normal event object by adding an (optional) instance of properties. Event propagating classes
 * may use it to pass additional information to the consumer.
 * <p/>
 * Note that this class in mainly intended to be used for passing on debug and logging information and to simplify unit
 * testing. Using it to communicate properties relevant to the control flow will most likely obscure your code and
 * should therefore be avoided when possible.
 * <p/>
 * Since all event handling code should occur on the event dispatch thread, this class is not synchronized in any way
 * (actually, none of the event related classes in swing are). If required, it is advised that using threads synchronize
 * on the event object rather than on its properties instance as a general convention.
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
public abstract class ExtendedEventObject extends EventObject {
    /**
     * The properties instance associated with this event. This instance will be created lazily.
     */
    protected Properties _properties;

    /**
     * Default constructor of EventObject.
     *
     * @param source the source in which this event originated.
     */
    protected ExtendedEventObject(final Object source) {
        super(source);
    }

    /**
     * Returns the properties instance associated with this event.
     *
     * @return the properties instance associated with this event.
     */
    public Properties getProperties() {
        if (_properties == null)
            _properties = new Properties();
        return _properties;
    }
}
