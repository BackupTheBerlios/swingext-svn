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
public class Pair<V, T> {
    private V _key;
    private T _value;

    public Pair(final V key, final T value) {
        _key = key;
        _value = value;
    }

    public V getKey() {
        return _key;
    }

    public void setKey(final V key) {
        _key = key;
    }

    public T getValue() {
        return _value;
    }

    public void setValue(final T value) {
        _value = value;
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Pair pair = (Pair) o;

        if (_key != null ? !_key.equals(pair._key) : pair._key != null) return false;
        if (_value != null ? !_value.equals(pair._value) : pair._value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (_key != null ? _key.hashCode() : 0);
        result = 31 * result + (_value != null ? _value.hashCode() : 0);
        return result;
    }
}
