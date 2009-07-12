package net.sarcommand.swingextensions.actions;

import javax.swing.*;

/**
 * Interface for classes used to configure Actions. Instances of this class will mainly be used by the ActionManager,
 * but can be employed by themselves as well.
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
public interface ActionProvider {
    /**
     * Sets the properties this provider associates with the specified identifier to the given action. This method does
     * not guarantee any specific properties to be set.
     *
     * @param identifier The unique identifier of the action to be mapped.
     * @param action     The action being mapped.
     */
    public void configurePropertiesForAction(Object identifier, Action action);
}
