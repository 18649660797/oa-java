/**
 * Copyright (c) 2016 云智盛世
 * Created with AppUtils.
 */
package top.gabin.oa.web.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Class description
 *
 * @author linjiabin on  16/6/6
 */
public class AppUtils {
    // 手机浏览器 型号
    private static final String[] MOBILE_SPECIFIC_SUBSTRING = {
            "iPhone","Android","MIDP","Opera Mobi",
            "Opera Mini","BlackBerry","HP iPAQ","IEMobile",
            "MSIEMobile","Windows Phone","HTC","LG",
            "MOT","Nokia","Symbian","Fennec",
            "Maemo","Tear","Midori","armv",
            "Windows CE","WindowsCE","Smartphone","240x320",
            "176x220","320x320","160x160","webOS",
            "Palm","Sagem","Samsung","SGH",
            "SonyEricsson","MMP","UCWEB"};

    /**
     *  是否 来自手机浏览器访问
     *
     * @param request
     * @return
     */
    public static boolean isMobileBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if(StringUtils.isBlank(userAgent)){
            return false;
        }
        for (String mobile: MOBILE_SPECIFIC_SUBSTRING){
            if (userAgent.contains(mobile)
                    || userAgent.contains(mobile.toUpperCase())
                    || userAgent.contains(mobile.toLowerCase())){
                return true;
            }
        }

        return false;
    }

}
