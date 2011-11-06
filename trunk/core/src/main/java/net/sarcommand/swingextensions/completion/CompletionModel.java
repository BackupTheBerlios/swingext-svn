package net.sarcommand.swingextensions.completion;

import java.util.Collection;

/**
 * Common interface for classes which provide an auto-complete functionality.
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
public interface CompletionModel<T> {
    /**
     * Returns a list of possible completions for incomplete shared input.
     *
     * @param target The target component for which a completion is requested. This parameter may come in useful if you
     *               want to use the same model for different components.
     * @param token  The incomplete shared input for which completions have to be found.
     * @return A list of possible completions for incomplete shared input.
     */
    public Collection<String> getPossibleCompletions(final T target, final String token);
}
