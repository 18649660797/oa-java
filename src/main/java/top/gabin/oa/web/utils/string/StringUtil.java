/**
 * Copyright (c) 2016 云智盛世
 * Created with StringUtil.
 */
package top.gabin.oa.web.utils.string;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author linjiabin  on  16/1/10
 */
public class StringUtil {

    public static String cleanseUrlString(String input) {
        return removeSpecialCharacters(decodeUrl(input));
    }
    public static String removeSpecialCharacters(String input) {
        if(input != null) {
            input = input.replaceAll("[ \\r\\n]", "");
        }
        return input;
    }

    public static String decodeUrl(String encodedUrl) {
        try {
            return encodedUrl == null?null: URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return encodedUrl;
        }
    }

}
