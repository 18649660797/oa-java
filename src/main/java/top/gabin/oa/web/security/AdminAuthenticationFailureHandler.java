package top.gabin.oa.web.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import top.gabin.oa.web.exception.AuthException;
import top.gabin.oa.web.utils.string.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String defaultFailureUrl;

    public AdminAuthenticationFailureHandler() {
        super();
    }

    public AdminAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
        this.defaultFailureUrl = defaultFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String failureUrlParam = StringUtil.cleanseUrlString(request.getParameter("failureUrl"));
        String successUrlParam = StringUtil.cleanseUrlString(request.getParameter("successUrl"));
        String failureUrl = StringUtils.trimToNull(failureUrlParam);

        // Verify that the url passed in is a servlet path and not a link redirecting away from the webapp.
        failureUrl = validateUrlParam(failureUrl);
        successUrlParam = validateUrlParam(successUrlParam);

        if (failureUrl == null) {
            failureUrl = StringUtils.trimToNull(defaultFailureUrl);
        }
        if (failureUrl != null) {
            int errorCode = -1;
            if (exception instanceof AuthException) {
                errorCode = ((AuthException) exception).getErrorCode();
            } else if ("Bad credentials".equalsIgnoreCase(exception.getMessage())) {
                errorCode = -2;
            }

            if (!failureUrl.contains("?")) {
                failureUrl += "?error=" + errorCode;
            } else {
                failureUrl += "&error=" + errorCode;
            }

            if (StringUtils.isNotEmpty(successUrlParam)) {
                if (!failureUrl.contains("?")) {
                    failureUrl += "?successUrl=" + successUrlParam;
                } else {
                    failureUrl += "&successUrl=" + successUrlParam;
                }
            }
            saveException(request, exception);
            getRedirectStrategy().sendRedirect(request, response, failureUrl);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    public String validateUrlParam(String url) {
        if (url != null) {
            if (url.contains("http") || url.contains("www") || url.contains(".")) {
                return null;
            }
        }
        return url;
    }

}
