/**
 * Copyright (c) 2016 云智盛世
 * Created with AuthException.
 */
package top.gabin.oa.web.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author linjiabin  on  16/1/10
 */
public class AuthException extends AuthenticationException {
    private Integer errorCode;

    public Integer getErrorCode() {
        return errorCode;
    }

    public AuthException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

}
