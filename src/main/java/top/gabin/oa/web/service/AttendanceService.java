package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.entity.Attendance;

import java.util.List;

/**
 * @author linjiabin  on  15/12/15
 */
public interface AttendanceService {
    boolean importAttendance(List<AttendanceImportDTO> attendanceImportDTOList);
    void clearMonth(String month);
    void batchDelete(String ids);
    void merge(AttendanceDTO attendanceDTO);
    Attendance findById(Long id);
    void batchSetLeaveDays(String days);
    void batchSetWorkDays(String days);
}
