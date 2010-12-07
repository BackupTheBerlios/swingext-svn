package net.sarcommand.swingextensions.autowiring;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A BooleanCondition implementation indicating whether a selectable component is currently select. <hr/> Copyright
 * 2006-2010 Torsten Heup
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
public class ItemSelectedCondition extends BooleanCondition {
    private ItemSelectable _selectable;

    public ItemSelectedCondition(final ItemSelectable selectable) {
        _selectable = selectable;
        _selectable.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                fireConditionUpdated();
            }
        });
    }

    @Override
    public Boolean getState() {
        return _selectable.getSelectedObjects() != null;
    }
}
