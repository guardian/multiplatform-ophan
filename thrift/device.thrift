namespace * ophan.thrift.device

/**
 * This class is designed to match the DeviceClass returned from our user agent parsing library yauaa
 * (see https://github.com/nielsbasjes/yauaa/blob/0fb59d61f5de6d9e77d7e78e1c5b153bdc6ed2aa/analyzer/src/main/java/nl/basjes/parse/useragent/classify/DeviceClass.java#L20).
 * Values added to this class should reflect what is in the yauaa class, we should not pollute it with values which do not
 * represent devices like the GUARDIAN_ANDROID_NATIVE_APP found in ua.thrift
 */
enum DeviceClass {
    /**
     * The device is assessed as a Desktop/Laptop class device.
     */
    DESKTOP = 1
    /**
     * In some cases the useragent has been altered by anonimization software.
     */
    ANONYMIZED = 2
    /**
     * A device that is mobile yet we do not know if it is a eReader/Tablet/Phone or Watch.
     */
    MOBILE = 3
    /**
     * A mobile device with a rather large screen (common &gt; 7").
     */
    TABLET = 4
    /**
     * A mobile device with a small screen (common &lt; 7").
     */
    PHONE = 5
}
