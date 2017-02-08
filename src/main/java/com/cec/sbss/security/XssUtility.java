package com.cec.sbss.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

public class XssUtility {

    private static final Logger LOGGER = Logger.getLogger(XssUtility.class);

    public static final String XSS_ENCODE = "<|&lt;,>|&gt;";

    // # chars are: <Script, </script, <%, %>, <iframe, </iframe, alert(,javascript
    public static final String XSS_FORBIDDEN_CHARS = "&lt;A,&lt;IMG,&lt;script,&amp;lt;script,&lt;%,&amp;lt;%,javascript,alert(,&lt;/script,amp;lt;/script,&lt;iframe,&amp;iframe,&lt;/iframe,&amp;/iframe,&amp;#x6a;&amp;#x61;&amp;#x76;&amp;#x61;&amp;#x73;&amp;#x63;&amp;#x72;&amp;#x69;&amp;#x70;&amp;#x74;";
    public static final String XSS_FORBIDDEN_REGEX = "(.*onload.*)(?i)|(.*[;<](\\s)*script.*)(?i)|(.*3[BC](\\s)*script.*)(?i)|(.*3[BC].*20script.*)(?i)";

    private static final Map<String, String> XSS_CHARS_ENCODE = new HashMap<String, String>();
    private static final List<String> XSS_CHARS_FORBIDDEN = new ArrayList<String>();

    private static final Pattern XSS_REGEX_FORBIDDEN = Pattern.compile(XSS_FORBIDDEN_REGEX.trim());

    static {
        init();
    }

    private XssUtility() {
    }

    private static void init() {

        StringTokenizer stRegex = null;
        String encodeName = null;
        String[] nameArray = null;
        String forbiddenName = null;

        stRegex = new StringTokenizer(XSS_ENCODE, ",");

        while (stRegex.hasMoreTokens()) {

            encodeName = stRegex.nextToken();

            nameArray = encodeName.trim().split("\\|");

            XSS_CHARS_ENCODE.put(nameArray[0].trim(), nameArray[1].trim());
        }

        stRegex = new StringTokenizer(XSS_FORBIDDEN_CHARS, ",");

        while (stRegex.hasMoreTokens()) {

            forbiddenName = stRegex.nextToken().trim();

            XSS_CHARS_FORBIDDEN.add(forbiddenName);
        }

    }

    public static boolean checkRequestForXSS(final ServletRequest req) {

        boolean passedXSS = true;
        boolean isSafe = true;

        List<String> badParams = new ArrayList<String>();

        String[] values = null;
        String[] cleanValues = null;
        String cleanValue = null;
        String key = null;

        Map<String, String[]> params = req.getParameterMap();

        for (Map.Entry<String, String[]> entry : params.entrySet()) {

            key = entry.getKey();
            values = entry.getValue();

            cleanValues = new String[values.length];

            for (int i = 0; i < values.length; i++) {

                cleanValue = cleanXSS(values[i]);

                cleanValues[i] = cleanValue;

                isSafe = isSafe(cleanValue);

                if (!isSafe) {
                    badParams.add(key + ":" + cleanValue);
                }
            }

            if (isSafe) {

                if (cleanValues.length == 1) {
                    req.setAttribute(key, cleanValues[0]);
                } else {
                    req.setAttribute(key, cleanValues);
                }
            }

        }

        if (!badParams.isEmpty()) {

            passedXSS = false;

            LOGGER.info("XSS elements detected " + badParams);

        }

        return passedXSS;
    }

    public static String cleanXSS(final String value) {

        String cleanValue = value;

        for (String key : XSS_CHARS_ENCODE.keySet()) {
            cleanValue = cleanValue.replace(key, XSS_CHARS_ENCODE.get(key).toString());
        }

        return cleanValue;
    }

    public static boolean isSafe(final String value) {

        boolean isSafe = true;

        for (String name : XSS_CHARS_FORBIDDEN) {
            if (value.toLowerCase().indexOf(name.toLowerCase()) != -1) {
                isSafe = false;
                break;
            }
        }

        if (isSafe) {
            Matcher match = XSS_REGEX_FORBIDDEN.matcher(value);
            if (match.find()) {
                isSafe = false;
            }
        }

        return isSafe;
    }
}
