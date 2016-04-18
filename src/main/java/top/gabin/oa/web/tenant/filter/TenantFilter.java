package top.gabin.oa.web.tenant.filter;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gabin.oa.web.utils.AuthUtils;
import top.gabin.oa.web.tenant.utils.TenantUtils;
import top.gabin.oa.web.utils.string.UrlUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class description
 *
 * @author linjiabin on  16/4/14
 */
public class TenantFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TenantFilter.class);
    // 不需要过滤的
    private List<String> ignoreURIs = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 某些URL前缀不予处理（例如 /static/**）
        String ignores = filterConfig.getInitParameter("ignoreURIs");
        if(ignores != null)
            for(String ig : StringUtils.split(ignores, ','))
                ignoreURIs.add(ig.trim());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 不需要过滤的URI
        if (UrlUtils.isIgnoreUri(request, ignoreURIs)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Long sessionTenancy = TenantUtils.getSessionTenancy(session);
        String requestURI = request.getRequestURI();
        String tenancy = getTenancy(requestURI);
        // 将新的租户信息存储线程单例中
        Long tenancyId = getTenancyIdByKey(tenancy);
        if (tenancyId != null) {
            TenantUtils.set(tenancyId);
            TenantUtils.setSessionTenancy(session, tenancyId);
            if (AuthUtils.getCurrentLoginUserName() != null) {
                servletRequest.getRequestDispatcher(requestURI.replace("/" + tenancy, "")).forward(request,response);
                return;
            }
        } else if (sessionTenancy != null) {
            TenantUtils.set(sessionTenancy);
        }
        filterChain.doFilter(request, response);
    }

    private Long getTenancyIdByKey (String key) {
        Map<String, Long> tenancyMap = new HashedMap();
        tenancyMap.put("fujian", 1L);
        tenancyMap.put("jiangxi", 2L);
        return tenancyMap.get(key);
    }

    private String getTenancy(String uri) {
        String[] split = uri.split("/");
        if (split.length > 1) {
            return split[1];
        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
