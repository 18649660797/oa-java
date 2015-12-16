package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.AttendanceWorkFlowDTO;
import top.gabin.oa.web.dto.EmployeeAttendanceDTO;
import top.gabin.oa.web.entity.Attendance;

import java.util.List;
import java.util.Map;

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
    Map<Long, Map<Long,List<Attendance>>> getAttendanceGroup(String month);
    Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> yesterdayWorkDelayWorkFlow(Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> workFlowDTOGroup);
    Map<Long, Map<Long, EmployeeAttendanceDTO>> dealAttendanceRule(Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> workFlowDTOGroup);
}
