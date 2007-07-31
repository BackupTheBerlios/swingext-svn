package net.sarcommand.swingextensions.applicationsupport;

/**
 * A small utility class used to retrieve information on the operating system without having to deal with
 * System.getProperty. This class really doesn't do a lot, but situationally I've found it to be quite handy nontheless.
 */
public class SysInfo {
    public static enum Platform {
        WINDOWS, LINUX, MACOS, SOLARIS, UKNOWN
    }

    private static Platform __platform;

    /**
     * Returns the underlying bit architecture (e.g. i386).
     * @return the underlying bit architecture (e.g. i386).
     */
    public static String getArchitecture() {
        return System.getProperty("os.arch","unknown");
    }

    /**
     * Returns the operating system's name.
     * @return the operating system's name.
     */
    public static String getDisplayName() {
        return System.getProperty("os.name","unknown");
    }

    /**
     * Returns a Platform enum token representing the underlying operating system.
     * @return a Platform enum token representing the underlying operating system.
     */
    public static Platform getPlatform() {
        if (__platform == null) {
            final String sysName = System.getProperty("os.name").toLowerCase();
            if (sysName.contains("windows"))
                __platform = Platform.WINDOWS;
            if (sysName.contains("mac"))
                __platform = Platform.MACOS;
            if (sysName.contains("linux"))
                __platform = Platform.LINUX;
            if (sysName.contains("solaris"))
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
        return System.getProperty("os.version","unknown");
    }

    private SysInfo() {
    }
}
