package com.onlineexam.util;

import java.util.regex.Pattern;

/** Small helpers for reading, validating and safely rendering request input. */
public final class WebUtil {

    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private WebUtil() {
    }

    /** Never-null, trimmed version of a request parameter. */
    public static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String value) {
        return value != null && EMAIL.matcher(value).matches();
    }

    /**
     * HTML-escape a string so user-supplied content (names, question text,
     * options, ...) cannot inject markup or scripts when rendered in a JSP.
     * Used everywhere untrusted data is echoed back to the page.
     */
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '&':  sb.append("&amp;");  break;
                case '<':  sb.append("&lt;");   break;
                case '>':  sb.append("&gt;");   break;
                case '"':  sb.append("&quot;"); break;
                case '\'': sb.append("&#39;");  break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }
}
