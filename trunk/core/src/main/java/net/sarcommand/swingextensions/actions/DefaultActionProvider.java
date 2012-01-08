package net.sarcommand.swingextensions.actions;

import javax.swing.*;


/**
 * Default action provider used by the ActionManager. This instance will return null for all action identifiers, it
 * merely works as a placeholder.
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
public class DefaultActionProvider implements ActionProvider {
    /**
     * @see ActionProvider#configurePropertiesForAction(Object, javax.swing.Action)
     */
    public void configurePropertiesForAction(Object identifier, Action action) {
        action.putValue(Action.NAME, identifier.toString());
    }
}
