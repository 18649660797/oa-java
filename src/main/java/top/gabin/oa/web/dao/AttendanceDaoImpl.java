/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceImpl.
 */
package top.gabin.oa.web.dao;

import freemarker.template.utility.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.AttendanceImpl;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.persistence.Query;
import java.util.List;

/**
 * @author linjiabin  on  15/12/15
 */
@Repository("attendanceDao")
public class AttendanceDaoImpl extends CommonBaseDaoImpl<Attendance, top.gabin.oa.web.entity.AttendanceImpl> implements AttendanceDao {
    @Override
    public void clearMonth(String month) {
        Query query = em.createNativeQuery("delete from edy_attendance where work_date like '" + month + "%'");
        query.executeUpdate();
    }

    @Override
    public Attendance findYesterday(Attendance attendance) {
        Query query = em.createNativeQuery("select * from edy_attendance where employee_id=" + attendance.getEmployee().getId() + " and work_date='" + TimeUtils.format(DateUtils.addDays(attendance.getWorkDate(), -1)) + "'", AttendanceImpl.class);
        List resultList = query.getResultList();
        return resultList == null || resultList.isEmpty() ? null : (Attendance)resultList.get(0);
    }

    @Override
    public void batchSetLeaveDays(String days) {
        if (StringUtils.isBlank(days)) {
            return;
        }
        Query query = em.createNativeQuery("update edy_attendance set status=0 where work_date in (" + days + ")");
        query.executeUpdate();
    }
}
