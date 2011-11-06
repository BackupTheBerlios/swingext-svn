package net.sarcommand.swingextensions.menuitemfactory;

import javax.swing.*;

/**
 * This interface identifies factory classes for JMenuItems.
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
public interface MenuItemFactory<T> {
    /**
     * Creates a suitable JMenuItem for the given value.
     *
     * @param value value for which an item should be created.
     * @return a suitable JMenuItem for the given value.
     */
    public JMenuItem createItem(final T value);
}
