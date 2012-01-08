package net.sarcommand.swingextensions.completion;

/**
 * Common interface for classes which extract a piece of text (a token) from a given text a specified position. A token
 * in this context might be a line, a word, a sentence, a semantical unit etc.
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
public interface TokenProvider {
    /**
     * Returns the token at 'position' in 'text'.
     *
     * @param position Offset in the text in which the token should be looked up.
     * @param text     text to search for a token.
     * @return Token at the given position.
     */
    public String getTokenAtPosition(final int position, final String text);
}
