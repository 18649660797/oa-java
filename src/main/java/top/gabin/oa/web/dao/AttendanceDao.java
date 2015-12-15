package top.gabin.oa.web.dao;

import top.gabin.oa.web.dao.CommonBaseDao;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.AttendanceImpl;

/**
 * @author linjiabin  on  15/12/15
 */
public interface AttendanceDao extends CommonBaseDao<Attendance, AttendanceImpl> {
    void clearMonth(String month);
    Attendance findYesterday(Attendance attendance);
    void batchSetLeaveDays(String days);
    void batchSetWorkDays(String days);
}
