package top.gabin.oa.web.service;

import top.gabin.oa.web.constant.BusinessTypes;
import top.gabin.oa.web.dto.AttendanceBasicRuleConfigForm;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.BusinessType;

/**
 * @author linjiabin  on  15/12/20
 */
public interface BusinessService {
    void saveBusiness(BusinessType businessType);
    BusinessType getBusiness(BusinessTypes businessType);
    AttendanceBasicRuleConfig getAttendanceBasicRule();
    void setAttendanceBasicRule(AttendanceBasicRuleConfigForm form);
}
