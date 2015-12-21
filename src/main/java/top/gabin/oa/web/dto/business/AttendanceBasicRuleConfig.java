package top.gabin.oa.web.dto.business;

import top.gabin.oa.web.entity.BusinessType;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/20
 */
public interface AttendanceBasicRuleConfig extends BusinessType {
    String getWorkFit();
    void setWorkFit(String workFit);
    String getLeaveFit();
    void setLeaveFit(String leaveFit);
    String getRestBegin();
    void setRestBegin(String restBegin);
    String getRestEnd();
    void setRestEnd(String restEnd);
    Integer getWorkFitOffset();
    void setWorkFitOffset(Integer workFitOffset);
    BusinessType getBusinessType();
}
