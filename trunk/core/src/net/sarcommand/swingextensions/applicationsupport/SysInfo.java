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
        return System.getProperty("os.version","unknown");
    }

    public static String getHeapSizeAsString() {
        final long mem = Runtime.getRuntime().maxMemory();
        final double exponent = Math.log(mem) / Math.log(2);
        if (exponent < 10)
            return mem+"B";
        if (exponent < 20)
            return (mem / 1024)+"KB";
        if (exponent < 30)
            return (mem / 1024 / 1024)+"MB";

        return (mem / 1024 / 1024 / 1024)+"GB";
    }

    public static int getCPUCount() {
        return Runtime.getRuntime().availableProcessors(); 
    }

    private SysInfo() {
    }
}
