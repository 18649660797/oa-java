/**
 * Copyright (c) 2015 云智盛世
 * Created with AuthUtils.
 */
package top.gabin.oa.web.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author linjiabin  on  15/12/10
 */
public class AuthUtils {

    // 匿名用户
    private static final String ANONYMOUS_USER_NAME = "anonymousUser";
    /**
     *  获取 当前登录的 管理员帐号
     *
     * @return
     */
    public static String getCurrentLoginUserName(){
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth != null && !auth.getName().equals(ANONYMOUS_USER_NAME)) {
                UserDetails temp = (UserDetails) auth.getPrincipal();
                return temp.getUsername();
            }
        }
        return null;
    }

}
