package net.sarcommand.swingextensions.actions;

/**
 * Interface for classes used to instanciate Actions. Instances of this class will mainly be used by the ActionManager,
 * but can be employed by themselves as well.
 * <p/>
 * <hr/>
 * Copyright 2006-2008 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public interface ActionProvider {
    /**
     * Creates an Action instance for the given identifier. How the action is being created has to be
     * determined by the implementing class.
     *
     * @param identifier Unique identifier for an Action
     * @return the Action corresponding to the specified identifier.
     */
    public ManagedAction createAction(final Object identifier);
}
