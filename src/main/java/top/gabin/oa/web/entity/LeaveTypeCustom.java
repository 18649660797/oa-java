package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.attendance.LeaveTypeEnum;

/**
 * @author linjiabin  on  16/3/31
 */
public interface LeaveTypeCustom {
    Long getId();

    void setId(Long id);

    String getLabel();

    void setLabel(String label);

    LeaveTypeEnum getType();

    void setType(LeaveTypeEnum leaveTypeEnum);
}
