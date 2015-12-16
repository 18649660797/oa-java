package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.LeaveImportDTO;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.dto.AttendanceWorkFlowDTO;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.Leave;

import java.util.List;
import java.util.Map;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/14
 */
public interface LeaveService {
    void batchDelete(String ids);
    void batchInsert(List<Leave> leaveList);
    Leave findById(Long id);
    Leave merge(Leave leave);
    Leave merge(LeaveDTO leaveDTO);
    void importLeave(List<LeaveImportDTO> leaveImportDTOList);

    /**
     * 清除月份的请假数据
     * @param month
     */
    void clearMonth(String month);

    Map<Long,List<Leave>> getLeaveGroup(String month);
    Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> workFlow(Map<Long, Map<Long, List<Attendance>>> attendanceGroup, Map<Long, List<Leave>> leaveGroup);
}
