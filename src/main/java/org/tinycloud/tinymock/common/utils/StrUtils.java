package org.tinycloud.tinymock.common.utils;


import java.util.concurrent.ThreadLocalRandom;

/**
 * 字符串工具类
 *
 * @author liuxingyu01
 * @since 2023-11-28
 **/
public class StrUtils {

    public static final char[] numberSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static final char[] charSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 下划线字符
     */
    public static final char UNDERLINE = '_';

    /**
     * 判断字符串中是否全是空白字符
     *
     * @param cs 需要判断的字符串
     * @return 如果字符串序列是 null 或者全是空白，返回 true
     */
    public static boolean isBlank(CharSequence cs) {
        if (isEmpty(cs)) {
            return true;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 驼峰转下划线 humpToLine("helloWorld") = "hello_world"
     *
     * @param param 字符串
     * @return 字符串
     */
    public static String humpToLine(String param) {
        if (isBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰 lineToHump("hello_world") = "helloWorld"
     *
     * @param param 字符串
     * @return 字符串
     */
    public static String lineToHump(String param) {
        if (isBlank(param)) {
            return "";
        }
        String temp = param.toLowerCase();
        int len = temp.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = temp.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(temp.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 首字母转换小写
     * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays as "URL".
     *
     * @param name 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * 这个字符串是否是全是数字
     *
     * @param str 输入字符串
     * @return true: 是，false: 不是
     */
    public static boolean isNumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除字符串中的字符
     *
     * @param string     输入字符串
     * @param deleteChar 需要删除的字符
     * @return 输出字符串
     */
    public static String deleteChar(String string, char deleteChar) {
        if (isBlank(string)) {
            return "";
        }
        char[] chars = string.toCharArray();
        StringBuilder sb = new StringBuilder(string.length());
        for (char ar : chars) {
            if (ar != deleteChar) {
                sb.append(ar);
            }
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否以prefixes其中某一个字符串为开头的
     *
     * @param str      输入字符串
     * @param prefixes 待匹配字符串数组
     * @return true是，false不是
     */
    public static boolean startsWithAny(String str, String... prefixes) {
        if (isBlank(str) || prefixes == null || prefixes.length == 0) {
            return false;
        }
        for (String prefix : prefixes) {
            if (str.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否以suffixes其中某一个字符串为结尾的
     *
     * @param str      输入字符串
     * @param suffixes 待匹配字符串数组
     * @return true是，false不是
     */
    public static boolean endsWithAny(String str, String... suffixes) {
        if (isBlank(str) || suffixes == null || suffixes.length == 0) {
            return false;
        }
        for (String suffix : suffixes) {
            if (str.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除字符串前后空格，如果字符串为null，则直接返回null
     *
     * @param string 输入字符串
     * @return 输出字符串
     */
    public static String trimOrNull(String string) {
        return string != null ? string.trim() : null;
    }

    /**
     * 分割separator之前的字符串
     *
     * @param str       待操作字符串
     * @param separator 分隔字符串
     * @return 输出字符串
     */
    public static String substringBefore(String str, String separator) {
        if (!isEmpty(str) && separator != null) {
            if (separator.isEmpty()) {
                return "";
            } else {
                int pos = str.indexOf(separator);
                return pos == -1 ? str : str.substring(0, pos);
            }
        } else {
            return str;
        }
    }

    /**
     * 获取随机字符串
     *
     * @param count 随机字符串长度
     * @return 随机字符串
     */
    public static String randomStr(int count) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String r = String.valueOf(charSeq[ThreadLocalRandom.current().nextInt(charSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }

    /**
     * 获取随机数字
     *
     * @param count 随机数字长度
     * @return 随机数字
     */
    public static String randomNumber(int count) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String r = String.valueOf(numberSeq[ThreadLocalRandom.current().nextInt(numberSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }
}
