package net.sarcommand.swingextensions.autowiring;

/**
 * Common interface for actions that are triggered by boolean conditions. <hr/> Copyright 2006-2012 Torsten Heup
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
public interface BooleanConditionedAction {
    /**
     * Triggers this action whenever the given conditions are all fulfilled.
     *
     * @param conditions A list of boolean conditions.
     * @return The boolean condition created, containing all previously set conditions plus the new ones.
     */
    public AggregateBooleanCondition whenever(final BooleanCondition... conditions);

    /**
     * Triggers this action unless the given conditions are all fulfilled.
     *
     * @param conditions A list of boolean conditions.
     * @return The boolean condition created, containing all previously set conditions plus the new ones.
     */
    public AggregateBooleanCondition unless(final BooleanCondition... conditions);
}
