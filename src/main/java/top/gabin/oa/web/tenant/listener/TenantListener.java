/**
 * Copyright (c) 2016 云智盛世
 * Created with TenantListener.
 */
package top.gabin.oa.web.tenant.listener;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.InitializeCollectionEvent;
import org.hibernate.event.spi.InitializeCollectionEventListener;

/**
 * Class description
 *
 *
 *
 * @author linjiabin on  16/4/15
 */
public class TenantListener implements InitializeCollectionEventListener {

    @Override
    public void onInitializeCollection(InitializeCollectionEvent initializeCollectionEvent) throws HibernateException {

    }
}
