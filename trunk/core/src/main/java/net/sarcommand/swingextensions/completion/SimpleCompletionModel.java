package net.sarcommand.swingextensions.completion;

import java.text.*;
import java.util.*;

/**
 * A simple default implementation of the CompletionModel interface. This class is backed by a TreeSet which you can
 * fill with tokens. The lookup time should be pretty effective, even though the maximum dictionary size is obviously
 * somewhat limited.
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
public class SimpleCompletionModel implements CompletionModel {
    private TreeSet<String> _tokens;

    public SimpleCompletionModel() {
        this((Comparator<String>) null);
    }

    public SimpleCompletionModel(final boolean caseSensitive) {
        if (!caseSensitive) {
            final Collator collator = Collator.getInstance();
            collator.setStrength(Collator.PRIMARY);
            _tokens = new TreeSet<String>(collator);
        } else
            _tokens = new TreeSet<String>();
    }

    public SimpleCompletionModel(final Comparator<String> comparator) {
        _tokens = new TreeSet<String>(comparator);
    }

    public SimpleCompletionModel(final String... tokens) {
        this(null, Arrays.asList(tokens));
    }

    public SimpleCompletionModel(final Comparator<String> comparator, final String... tokens) {
        this(comparator, Arrays.asList(tokens));
    }

    public SimpleCompletionModel(final Collection<String> tokens) {
        this((Comparator<String>) null);
        addTokens(tokens);
    }

    public SimpleCompletionModel(final Comparator<String> comparator, final Collection<String> tokens) {
        this(comparator);
        addTokens(tokens);
    }

    public void addTokens(final Collection<String> tokens) {
        _tokens.addAll(tokens);
    }

    public void removeTokens(final Collection<String> tokens) {
        _tokens.removeAll(tokens);
    }

    public Collection<String> getPossibleCompletions(final Object target, String token) {
        String ceil = _tokens.ceiling(token);
        final LinkedList<String> result = new LinkedList<String>();
        if (ceil == null)
            return result;
        final Set<String> candidates = _tokens.tailSet(ceil);
        for (String candidate : candidates) {
            if (!candidate.startsWith(token))
                break;
            result.add(candidate);
        }
        return result;
    }

    public void clear() {
        _tokens.clear();
    }

    public Collection<String> getTokens() {
        return Collections.unmodifiableCollection(_tokens);
    }
}
