/**
 * Copyright (c) 2016 云智盛世
 * Created with EnableFilterAspect.
 */
package top.gabin.oa.web.tenant.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.gabin.oa.web.tenant.utils.TenantUtils;

import javax.persistence.EntityManager;

/**
 * 切面定义:创建实体管理对象的时候,开启租户过滤
 * 手动开启或关闭请调用:TenancyUtils工具类的方法
 * @author linjiabin on  16/4/16
 */
@Aspect
@Component
public class EnableTenantFilterAspect {

    @AfterReturning(pointcut = "bean(entityManagerFactory) && execution(* createEntityManager(..))", returning = "retVal")
    public void getSessionAfter(JoinPoint joinPoint, Object retVal) {
        if (retVal != null && EntityManager.class.isInstance(retVal)) {
            TenantUtils.enableTenantFilter((EntityManager) retVal);
        }
    }

}
