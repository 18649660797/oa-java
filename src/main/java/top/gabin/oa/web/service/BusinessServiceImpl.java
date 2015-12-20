/**
 * Copyright (c) 2015 云智盛世
 * Created with BusinessServiceImpl.
 */
package top.gabin.oa.web.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.BusinessTypes;
import top.gabin.oa.web.dao.BusinessTypeDao;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.BusinessType;

import javax.annotation.Resource;

/**
 * @author linjiabin  on  15/12/20
 */
@Service("businessService")
public class BusinessServiceImpl implements BusinessService {
    @Resource(name = "businessTypeDao")
    private BusinessTypeDao businessTypeDao;
    @Transactional("transactionManager")

    @Override
    public void saveBusiness(BusinessType businessType) {
        businessTypeDao.persist(businessType);
    }

    @Override
    public BusinessType getBusiness(BusinessTypes businessType) {
        if (businessType != null) {
            return businessTypeDao.findByKey(businessType.getKey());
        }
        return null;
    }

    @Override
    public AttendanceBasicRuleConfig getAttendanceBasicRule() {
        BusinessType business = getBusiness(BusinessTypes.ATTENDANCE);
        return new AttendanceBasicRuleConfig(business);
    }

}
