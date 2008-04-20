package net.sarcommand.swingextensions.binding;

/**
 * This exception is being thrown if a keypath element is considered to be malformed, meaning that no property with the
 * given name could be found on the object returned by the previous element.
 * <p/>
 * <hr/> Copyright 2006-2008 Torsten Heup
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
public class MalformedKeypathException extends RuntimeException {
    public MalformedKeypathException(final String message) {
        super(message);
    }

    public MalformedKeypathException(final Object entryPoint, final String fullKeypathString,
                                     final int malformedElementIndex, final String malformedElementName,
                                     final Object lastKeypathElement) {
        super("Malformed keypath: Could not find property #" + malformedElementIndex + " of keypath "
                + fullKeypathString + " (" + malformedElementName + "). Entry point was " + entryPoint +
                ", last element was " + lastKeypathElement);
    }
}
