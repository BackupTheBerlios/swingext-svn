package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.utilities.ComponentVariation;

/**
 * Tagging interface for ComponentVariation implementation targetting JTextComponents.
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
public interface TextVariation<T> extends ComponentVariation<T> {
    public static final String CLIENT_PROPERTY = "swingExt.TextVariation.clientProperty";
}
