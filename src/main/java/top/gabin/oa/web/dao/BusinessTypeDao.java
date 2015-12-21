/**
 * Copyright (c) 2015 云智盛世
 * Created with BusinessTypeDao.
 */
package top.gabin.oa.web.dao;

import top.gabin.oa.web.entity.BusinessType;
import top.gabin.oa.web.entity.BusinessTypeImpl;

/**
 * @author linjiabin  on  15/12/20
 */
public interface BusinessTypeDao extends CommonBaseDao<BusinessType, BusinessTypeImpl> {
    BusinessType findByKey(String name);
    BusinessType findByKeyNotCache(String name);
    BusinessType merge(BusinessType businessType);
}
