/**
 * Copyright (c) 2016 云智盛世
 * Created with EnableFilterAspect.
 */
package top.gabin.oa.web.tenant.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.gabin.oa.web.tenant.anotation.Tenant;
import top.gabin.oa.web.tenant.utils.TenantUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 切面定义:创建实体管理对象的时候,开启租户过滤
 * 手动开启或关闭请调用:TenancyUtils工具类的方法
 * @author linjiabin on  16/4/16
 */
@Aspect
@Component
public class EnableTenantFilterAspect {
    @PersistenceContext(name = "entityManagerFactory")
    protected EntityManager em;

    @AfterReturning(pointcut = "bean(entityManagerFactory) && execution(* createEntityManager(..))", returning = "retVal")
    public void getSessionAfter(JoinPoint joinPoint, Object retVal) {
        if (retVal != null && EntityManager.class.isInstance(retVal)) {
            TenantUtils.enableTenantFilter((EntityManager) retVal);
        }
    }

    @AfterReturning(pointcut = "execution(* getEntityManager(..))", returning = "retVal")
    public void test(JoinPoint joinPoint, Object retVal) throws Throwable {
        if (retVal != null && EntityManager.class.isInstance(retVal)) {
            TenantUtils.enableTenantFilter((EntityManager) retVal);
        }
    }

    // 过滤所有bean注解成service的方法
    @Around(value = "@annotation(tenant) || @within(tenant)")
    public Object enableFilter(ProceedingJoinPoint joinPoint, Tenant tenant) throws Throwable {
        // 有特殊注解的方法,在执行之前关闭过滤
        Long tenantId = TenantUtils.getTenantId();
        TenantUtils.setTenantId(null);
        Object proceed = joinPoint.proceed();
        TenantUtils.setTenantId(tenantId);
        return proceed;
    }

//    @After(value = "@target(org.springframework.stereotype.*) && @annotation(top.gabin.oa.web.tenant.anotation.Tenant)")
//    public void disableFilter(JoinPoint joinPoint) {
//        // 有特殊注解的方法,在执行之后重新开启过滤
//        Session session = (Session) em.getDelegate();
//        TenantUtils.enableTenantFilter(session);
//    }

}
