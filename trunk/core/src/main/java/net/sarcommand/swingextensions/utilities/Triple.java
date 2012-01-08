package net.sarcommand.swingextensions.utilities;

/**
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
public class Triple<F, S, T> {
    private F _first;
    private S _second;
    private T _third;

    public Triple(final F first, final S second, final T third) {
        _first = first;
        _second = second;
        _third = third;
    }

    public F getFirst() {
        return _first;
    }

    public S getSecond() {
        return _second;
    }

    public T getThird() {
        return _third;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Triple triple = (Triple) o;

        if (_first != null ? !_first.equals(triple._first) : triple._first != null) return false;
        if (_second != null ? !_second.equals(triple._second) : triple._second != null) return false;
        if (_third != null ? !_third.equals(triple._third) : triple._third != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _first != null ? _first.hashCode() : 0;
        result = 31 * result + (_second != null ? _second.hashCode() : 0);
        result = 31 * result + (_third != null ? _third.hashCode() : 0);
        return result;
    }
}
