package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.AttendanceStatus;

import java.util.Date;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/15
 */
public interface Attendance {
    String getWorkDateFormat();

    void setWorkDateFormat(String workDateFormat);

    Long getId();

    void setId(Long id);

    Date getWorkDate();

    void setWorkDate(Date workDate);

    String getAmTime();

    void setAmTime(String amTime);

    String getPmTime();

    void setPmTime(String pmTime);

    AttendanceStatus getStatus();

    void setStatus(AttendanceStatus status);

    Employee getEmployee();

    void setEmployee(Employee employee);

    String getYesterdayPm();

}
