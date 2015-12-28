/**
 * Copyright (c) 2015 云智盛世
 * Created with BusinessServiceImpl.
 */
package top.gabin.oa.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.BusinessConfigs;
import top.gabin.oa.web.constant.BusinessTypes;
import top.gabin.oa.web.dao.BusinessTypeDao;
import top.gabin.oa.web.dto.AttendanceBasicRuleConfigForm;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfigImpl;
import top.gabin.oa.web.entity.BusinessConfig;
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
        businessTypeDao.saveOrUpdate(businessType);
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
        return new AttendanceBasicRuleConfigImpl(business);
    }

    @Transactional("transactionManager")
    @Override
    public void setAttendanceBasicRule(AttendanceBasicRuleConfigForm form) {
        BusinessType business = businessTypeDao.findByKeyNotCache(BusinessTypes.ATTENDANCE.getKey());
        for (BusinessConfig businessConfig : business.getBusinessConfigList()) {
            if (businessConfig.getKey().equals(BusinessConfigs.WORK_FIT.getKey())) {
                businessConfig.setValue(form.getWorkFit());
            }
            if (businessConfig.getKey().equals(BusinessConfigs.LEAVE_FIT.getKey())) {
                businessConfig.setValue(form.getLeaveFit());
            }
            if (businessConfig.getKey().equals(BusinessConfigs.REST_BEGIN.getKey())) {
                businessConfig.setValue(form.getResetBegin());
            }
            if (businessConfig.getKey().equals(BusinessConfigs.REST_END.getKey())) {
                businessConfig.setValue(form.getResetEnd());
            }
            if (businessConfig.getKey().equals(BusinessConfigs.WORK_FIT_OFFSET.getKey())) {
                businessConfig.setValue(form.getWorkFitOffset().toString());
            }

        }
        businessTypeDao.merge(business);
    }

}
