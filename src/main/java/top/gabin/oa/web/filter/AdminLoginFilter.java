package top.gabin.oa.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.gabin.oa.web.constant.CommonConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String FROM_USERNAME_KEY = "j_username";

    private static final String FROM_PASSWORD_KEY = "j_password";

    private static final String FROM_VALID_CODE_KEY = "randomCode";

    private String usernameParameter = FROM_USERNAME_KEY;
    private String passwordParameter = FROM_PASSWORD_KEY;
    private String validCodeParameter = FROM_VALID_CODE_KEY;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String validCode = obtainValidCode(request);
        String password = obtainPassword(request);

        String sessionKey = CommonConstants.SESSION_CODE;
        String systemFromParam = request.getParameter("system");
        if(StringUtils.isNotEmpty(systemFromParam)){
            sessionKey = systemFromParam + "_" + sessionKey;
        }
        String randomCodeServer = (String)request.getSession().getAttribute(sessionKey);
//        if (StringUtils.isEmpty(validCode) || !validCode.equalsIgnoreCase(randomCodeServer)) {
//            logger.error("AdminLoginFilter: validCodeFromClient[" + validCode + "] validCodeFromServer[" + randomCodeServer + "] validCodeKey[" + sessionKey);
//            throw new AuthException(validCodeParameter + " error!", -1);
//        }

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);


    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    protected String obtainValidCode(HttpServletRequest request) {
        return request.getParameter(validCodeParameter);
    }

}
