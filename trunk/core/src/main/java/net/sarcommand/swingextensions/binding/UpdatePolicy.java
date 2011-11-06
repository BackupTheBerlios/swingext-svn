package net.sarcommand.swingextensions.binding;

/**
 * Determines when a SwingBinding should update its target bean.
 *
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public enum UpdatePolicy {
    /**
     * Specifies that the target bean should be updated whenever the component changes.
     */
    ON_CHANGE,

    /**
     * Specifies that the target bean should be updated whenever the component loses its focus.
     */
    ON_FOCUS_LOST,

    /**
     * Specifies that the target bean should not be updated automatically.
     */
    NO_UPDATE
}
