/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceBasicRuleConfigImpl.
 */
package top.gabin.oa.web.dto.business;

import org.apache.commons.lang3.StringUtils;
import top.gabin.oa.web.constant.BusinessConfigs;
import top.gabin.oa.web.constant.ConfigStatus;
import top.gabin.oa.web.entity.BusinessConfig;
import top.gabin.oa.web.entity.BusinessType;

import java.util.List;

/**
 * @author linjiabin  on  15/12/20
 */
public class AttendanceBasicRuleConfigImpl implements AttendanceBasicRuleConfig {
    private BusinessType businessType;

    public BusinessType getBusinessType() {
        return businessType;
    }

    public AttendanceBasicRuleConfigImpl(BusinessType businessType) {
        this.businessType = businessType;
    }

    @Override
    public Long getId() {
        return businessType.getId();
    }

    @Override
    public void setId(Long id) {
        businessType.setId(id);
    }

    @Override
    public String getLabel() {
        return businessType.getLabel();
    }

    @Override
    public void setLabel(String label) {
        businessType.setLabel(label);
    }

    @Override
    public String getKey() {
        return businessType.getKey();
    }

    @Override
    public void setKey(String key) {
        businessType.setKey(key);
    }

    @Override
    public ConfigStatus getStatus() {
        return businessType.getStatus();
    }

    @Override
    public void setStatus(ConfigStatus status) {
        businessType.setStatus(status);
    }

    @Override
    public boolean isShow() {
        return businessType.isShow();
    }

    @Override
    public void setShow(boolean show) {
        businessType.setShow(show);
    }

    @Override
    public List<BusinessConfig> getBusinessConfigList() {
        return businessType.getBusinessConfigList();
    }

    @Override
    public void setBusinessConfigList(List<BusinessConfig> businessConfigList) {
        businessType.setBusinessConfigList(businessConfigList);
    }

    public String getWorkFit() {
        return getConfigValueByKey(BusinessConfigs.WORK_FIT);
    }

    public String getLeaveFit() {
        return getConfigValueByKey(BusinessConfigs.LEAVE_FIT);
    }
    public String getRestBegin() {
        return getConfigValueByKey(BusinessConfigs.REST_BEGIN);
    }

    public String getRestEnd() {
        return getConfigValueByKey(BusinessConfigs.REST_END);
    }

    @Override
    public void setWorkFit(String workFit) {
        setConfigValueByKey(BusinessConfigs.WORK_FIT, workFit);
    }

    @Override
    public void setLeaveFit(String leaveFit) {
        setConfigValueByKey(BusinessConfigs.LEAVE_FIT, leaveFit);
    }

    @Override
    public void setRestBegin(String restBegin) {
        setConfigValueByKey(BusinessConfigs.REST_BEGIN, restBegin);
    }

    @Override
    public void setRestEnd(String restEnd) {
        setConfigValueByKey(BusinessConfigs.REST_END, restEnd);
    }

    @Override
    public Integer getWorkFitOffset() {
        String configValueByKey = getConfigValueByKey(BusinessConfigs.WORK_FIT_OFFSET);
        return StringUtils.isBlank(configValueByKey) ? 0 : Integer.parseInt(configValueByKey);
    }

    @Override
    public void setWorkFitOffset(Integer workFitOffset) {
        setConfigValueByKey(BusinessConfigs.WORK_FIT_OFFSET, workFitOffset == null ? "" : workFitOffset.toString());
    }

    private void setConfigValueByKey(BusinessConfigs key, String value) {
        List<BusinessConfig> businessConfigList = getBusinessConfigList();
        for (BusinessConfig config : businessConfigList) {
            if (config.getKey().equals(key.getKey())) {
                config.setValue(value);
            }
        }
    }

    private String getConfigValueByKey(BusinessConfigs key) {
        List<BusinessConfig> businessConfigList = getBusinessConfigList();
        for (BusinessConfig config : businessConfigList) {
            if (config.getKey().equals(key.getKey())) {
                return config.getValue();
            }
        }
        return null;
    }

}
