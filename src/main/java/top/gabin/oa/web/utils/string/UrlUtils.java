/**
 * Copyright (c) 2016 云智盛世
 * Created with UrlUtils.
 */
package top.gabin.oa.web.utils.string;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * 处理一些url字符串解析问题
 *
 * @author linjiabin  on  16/1/20
 */
public class UrlUtils {

    /**
     * 当前页面,用于回调
     * @param request
     * @return
     */
    public static String buildSuccessUrl(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        Enumeration requestParamEnum = request.getParameterNames();
        boolean isFirstParam = true;
        while (requestParamEnum.hasMoreElements()) {
            String paramKey = (String) requestParamEnum.nextElement();
            requestUri += ((isFirstParam ? "?" : "&") + paramKey + "=" + request.getParameter(paramKey));
            isFirstParam = false;
        }

        return requestUri;
    }

    public static boolean isIgnoreUri(HttpServletRequest request, List<String> ignoreURIs) {
        if (request == null || ignoreURIs == null || ignoreURIs.isEmpty()) {
            return false;
        }
        // 不需要过滤的URI
        for (String ignoreURI : ignoreURIs) {
            RequestMatcher matcher = new AntPathRequestMatcher(ignoreURI);
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

}
