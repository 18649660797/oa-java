package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.AttendanceRuleType;
import top.gabin.oa.web.constant.ConfigStatus;

import java.util.Date;
import java.util.Map;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/21
 */
public interface AttendanceRule {
    Map<String, AttendanceRuleDetail> getAttendanceRuleDetailMap();

    void setAttendanceRuleDetailMap(Map<String, AttendanceRuleDetail> attendanceRuleDetailMap);

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getExtraData();

    void setExtraData(String extraData);

    AttendanceRuleType getType();

    void setType(AttendanceRuleType type);

    ConfigStatus getStatus();

    void setStatus(ConfigStatus status);

    Date getBeginDate();

    void setBeginDate(Date beginDate);

    Date getEndDate();

    void setEndDate(Date endDate);
}
