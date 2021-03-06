package top.gabin.oa.web.tenant.utils;

import org.hibernate.Filter;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

/**
 * Class description
 *
 * @author linjiabin on  16/4/14
 */
public class TenantUtils {
    // hibernate租户过滤的key名称
    public final static String FILTER_TENANT = "tenantFilter";
    // hibernate租户过滤对应的动态参数名,用于绑定实际变量(实际租户ID)
    public final static String FILTER_TENANT_ID = "tenantId";
    // 线程变量,用于保存当前租户
    private static final ThreadLocal<Long> localThread = new ThreadLocal<>();
    private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
    // 租户的session缓存
    private static final String SESSION_TENANCY = "SESSION_TENANCY";

    public static void clear() {
        localThread.remove();
    }

    public static Long getTenantId() {
        // 需要有默认值
        return localThread.get();
    }

    public static void setTenantId(Long tenancy) {
        localThread.set(tenancy);
    }

    public static Session getSession() {
        return sessionThreadLocal.get();
    }

     public static void setSession(Session session) {
        sessionThreadLocal.set(session);
    }



    /**
     * 设置租户session缓存
     * @param session
     * @param tenancyId
     */
    public static void setSessionTenancy(HttpSession session, Long tenancyId) {
        if (session != null) {
            session.setAttribute(SESSION_TENANCY, tenancyId);
        }
    }

    /**
     * 获取租户session缓存
     * @param session
     * @return
     */
    public static Long getSessionTenancy(HttpSession session) {
        if (session != null) {
            Object tenancy = session.getAttribute(SESSION_TENANCY);
            return tenancy == null ? null : (Long) tenancy;
        }
        return null;
    }

    /**
     * 手动开启过滤查询
     * @param entityManager
     */
    public static void enableTenantFilter(EntityManager entityManager) {
        Session session = entityManager.unwrap(Session.class);
        enableTenantFilter(session);
    }

    /**
     * 手动开启过滤查询
     * @param session
     */
    public static void enableTenantFilter(Session session) {
        Long tenantId = getTenantId();
        if (tenantId == null) {
            return;
        }
        Filter enabledFilter = session.getEnabledFilter(FILTER_TENANT);
        if (enabledFilter ==  null) {
            session.enableFilter(FILTER_TENANT).setParameter(FILTER_TENANT_ID, tenantId);
        }
    }

    /**
     * 手动关闭过滤查询
     * @param entityManager
     */
    public static void disableTenantFilter(EntityManager entityManager) {
        Session session = entityManager.unwrap(Session.class);
        disableTenantFilter(session);
    }

    /**
     * 手动关闭过滤查询
     * @param session
     */
    public static void disableTenantFilter(Session session) {
        Filter enabledFilter = session.getEnabledFilter(FILTER_TENANT);
        if (enabledFilter != null) {
            session.disableFilter(FILTER_TENANT);
        }
    }

}
