package net.sarcommand.swingextensions.misc;

import java.io.File;
import java.util.Properties;

/**
 * This class works as a wrapper to the standard java.util.Properties implementation. It adds methods for storing and
 * retrieving standard value types other than Strings (currently the standard number types and file references).
 * <p/>
 * The API for accessing those value types is consistent with accessing strings in normal Properties instances. All
 * values will be converted to String internally, so you will have to take care of eventual NumberFormatExceptions
 * yourself.
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
public class ExtendedProperties extends Properties {
    public ExtendedProperties() {
    }

    public ExtendedProperties(final Properties defaults) {
        super(defaults);
    }

    public Object setInteger(final String key, final int value) {
        return setProperty(key, Integer.toString(value));
    }

    public Integer getInteger(final String key) {
        final String property = getProperty(key);
        return property == null ? null : Integer.valueOf(property);
    }

    public Integer getInteger(final String key, final int defaultValue) {
        final String property = getProperty(key);
        return property == null ? defaultValue : Integer.valueOf(property);
    }

    public Object setFloat(final String key, final float value) {
        return setProperty(key, Float.toString(value));
    }

    public Float getFloat(final String key) {
        final String property = getProperty(key);
        return property == null ? null : Float.valueOf(property);
    }

    public float getFloat(final String key, final float defaultValue) {
        final String property = getProperty(key);
        return property == null ? defaultValue : Float.valueOf(property);
    }

    public Object setDouble(final String key, final double value) {
        return setProperty(key, Double.toString(value));
    }

    public Double getDouble(final String key) {
        final String property = getProperty(key);
        return property == null ? null : Double.valueOf(property);
    }

    public Double getDouble(final String key, final double defaultValue) {
        final String property = getProperty(key);
        return property == null ? defaultValue : Double.valueOf(property);
    }

    public Object setLong(final String key, final long value) {
        return setProperty(key, Long.toString(value));
    }

    public Long getLong(final String key) {
        final String property = getProperty(key);
        return property == null ? null : Long.valueOf(property);
    }

    public Long getLong(final String key, final long defaultValue) {
        final String property = getProperty(key);
        return property == null ? defaultValue : Long.valueOf(property);
    }

    public Object setFile(final String key, final File value) {
        return setProperty(key, value == null ? null : value.getAbsolutePath());
    }

    public File getFile(final String key) {
        final String property = getProperty(key);
        return property == null ? null : new File(property);
    }

    public File getFile(final String key, final File defaultValue) {
        final String property = getProperty(key);
        return property == null ? defaultValue : new File(property);
    }
}
