package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.LeaveType;

import java.util.Date;

/**
 * @author linjiabin  on  15/12/14
 */
public interface Leave {
    Long getId();

    void setId(Long id);

    Date getEndDate();

    void setEndDate(Date endDate);

    Date getBeginDate();

    void setBeginDate(Date beginDate);

    Employee getEmployee();

    void setEmployee(Employee employee);

    void setType(LeaveType leaveType);

    LeaveType getType();

}
