/**
 * Copyright (c) 2016 云智盛世
 * Created with TenantListener.
 */
package top.gabin.oa.web.tenant.listener;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.InitializeCollectionEvent;
import org.hibernate.event.spi.InitializeCollectionEventListener;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;
import top.gabin.oa.web.entity.Leave;

/**
 * Class description
 *
 *
 *
 * @author linjiabin on  16/4/15
 */
public class TenantListener implements LoadEventListener {

    @Override
    public void onLoad(LoadEvent loadEvent, LoadType loadType) throws HibernateException {

    }
}
