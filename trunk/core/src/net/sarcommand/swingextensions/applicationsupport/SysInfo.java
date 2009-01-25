package net.sarcommand.swingextensions.applicationsupport;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A small utility class used to retrieve information on the operating system without having to deal with
 * System.getProperty. This class really doesn't do a lot, but situationally I've found it to be quite handy
 * nontheless.
 * <p/>
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
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
public class SysInfo {
    public static enum Platform {
        WINDOWS, LINUX, MACOS, SOLARIS, UKNOWN
    }

    private static Platform __platform;

    /**
     * Returns the underlying bit architecture (e.g. i386).
     *
     * @return the underlying bit architecture (e.g. i386).
     */
    public static String getArchitecture() {
        return System.getProperty("os.arch", "unknown");
    }

    /**
     * Returns the operating system's name.
     *
     * @return the operating system's name.
     */
    public static String getDisplayName() {
        return System.getProperty("os.name", "unknown");
    }

    /**
     * Returns a Platform enum token representing the underlying operating system.
     *
     * @return a Platform enum token representing the underlying operating system.
     */
    public static Platform getPlatform() {
        if (__platform == null) {
            final String sysName = System.getProperty("os.name").toLowerCase();
            if (sysName.contains("windows"))
                __platform = Platform.WINDOWS;
            else if (sysName.contains("mac"))
                __platform = Platform.MACOS;
            else if (sysName.contains("linux"))
                __platform = Platform.LINUX;
            else if (sysName.contains("solaris"))
                __platform = Platform.SOLARIS;
            else
                __platform = Platform.UKNOWN;
        }
        return __platform;
    }

    /**
     * Returns the operating system's version.
     *
     * @return the operating system's version.
     */
    public static String getVersion() {
        return System.getProperty("os.version", "unknown");
    }

    public static String getHeapSizeAsString() {
        final long mem = Runtime.getRuntime().maxMemory();
        final double exponent = Math.log(mem) / Math.log(2);
        if (exponent < 10)
            return mem + "B";
        if (exponent < 20)
            return Math.round(mem / 1024. * 100) / 100. + "KB";
        if (exponent < 30)
            return Math.round(mem / 1024. / 1024. * 100) / 100. + "MB";

        return Math.round(mem / 1024. / 1024. / 1024. * 100) / 100. + "GB";
    }

    /**
     * Returns the number of available cpus.
     *
     * @return the number of available cpus.
     */
    public static int getCPUCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Returns whether or not the platform provides accelerated drawing for images. This is merely a conveniance method
     * which uses the GraphicsEnvironment to check whether acceleration is available. The result of this method will be
     * correct for the primary screen, if multiple screens are attached you will have to check them separately.
     *
     * @return whether or not the platform provides accelerated drawing for images.
     */
    public static boolean isImageAccelerationAvailable() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().getImageCapabilities().isAccelerated();
    }

    /**
     * Returns the version string of the java runtime being used.
     *
     * @return the version string of the java runtime being used.
     */
    public static String getRuntimeVersion() {
        return System.getProperty("java.runtime.version");
    }

    /**
     * Creates a string containing all available system information. This is merely a conveniance method which can be
     * used for diagnotic purposes.
     *
     * @return a string containing all available system information.
     */
    public static String getSystemInformation() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Operating system:   ").append(getDisplayName()).append(' ').append(getVersion()).append(' ')
                .append(getArchitecture()).append('\n');
        builder.append("Java version:       ").append(getRuntimeVersion()).append('\n');
        builder.append("Date:               ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .append('\n');
        builder.append("Locale:             ").append(Locale.getDefault()).append('\n');
        builder.append("Number of CPUs:     ").append(getCPUCount()).append('\n');
        builder.append("Max. heap size:     ").append(getHeapSizeAsString()).append('\n');
        builder.append("Video acceleration: ").append(isImageAccelerationAvailable() ? "available" : "not available");
        return builder.toString();
    }

    private SysInfo() {
    }
}
