package com.cec.sbs.device;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class DeviceTypeHelper {

    // OPERATING SYSTEM USER AGENTS
    private static final String ANDROID_USER_AGENTS = "^.*Android.*Mobile.*$";
    private static final String IPHONE_USER_AGENTS = "^.*iPhone.*$|^.*iPod.*$";
    private static final String BLACKBERRY_USER_AGENTS = "^.*BlackBerry.*$";
    private static final String WINDOWS_USER_AGENTS = "^.*Windows Phone.*$";

    private DeviceTypeHelper(){

    }

    public static String getUserAgent(HttpServletRequest req) {

        if (req != null) {
            return req.getHeader("User-Agent");
        } else {
            return null;
        }
    }

    public static boolean isAndroid(HttpServletRequest req) {

        Pattern pattern = Pattern.compile(ANDROID_USER_AGENTS);

        String userAgent = getUserAgent(req);

        if (userAgent != null) {
            Matcher patternMatcher = pattern.matcher(getUserAgent(req));
            return patternMatcher.find();
        } else {
            return false;
        }
    }

    public static boolean isIphone(HttpServletRequest req) {

        Pattern pattern = Pattern.compile(IPHONE_USER_AGENTS);

        String userAgent = getUserAgent(req);

        if (userAgent != null) {
            Matcher patternMatcher = pattern.matcher(getUserAgent(req));
            return patternMatcher.find();
        } else {
            return false;
        }

    }

    public static boolean isBlackberry(HttpServletRequest req) {

        Pattern pattern = Pattern.compile(BLACKBERRY_USER_AGENTS);

        String userAgent = getUserAgent(req);

        if (userAgent != null) {
            Matcher patternMatcher = pattern.matcher(getUserAgent(req));
            return patternMatcher.find();
        } else {
            return false;
        }

    }

    public static boolean isWindowsPhone(HttpServletRequest req) {

        Pattern pattern = Pattern.compile(WINDOWS_USER_AGENTS);

        String userAgent = getUserAgent(req);

        if (userAgent != null) {
            Matcher patternMatcher = pattern.matcher(getUserAgent(req));
            return patternMatcher.find();
        } else {
            return false;
        }
    }

}
