/**
 * Copyright (c) 2016 云智盛世
 * Created with TenantInterceptor.
 */
package top.gabin.oa.web.tenant.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * hibernate租户拦截器
 * @author linjiabin on  16/4/14
 */
public class TenantInterceptor extends EmptyInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TenantInterceptor.class);

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//        if (entity instanceof Leave) {
//            Leave leave = (Leave) entity;
//            leave.setRemark("你妹啊");
//            entity = leave;
//        }
        boolean onSave = super.onSave(entity, id, state, propertyNames, types);
        return onSave;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
//        if (entity instanceof Leave) {
//            for (int i = 0; i < propertyNames.length; i++) {
//                if ("remark".equals(propertyNames[i])) currentState[i] = "尼玛";
//            }
//        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public Object getEntity(String entityName, Serializable id) {
        return super.getEntity(entityName, id);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public String onPrepareStatement(String sql) {
        return super.onPrepareStatement(sql);
    }



}
