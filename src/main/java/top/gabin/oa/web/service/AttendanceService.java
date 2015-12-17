package top.gabin.oa.web.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.service.flow.attendance.data.DataBuilder;

import java.util.List;

/**
 * @author linjiabin  on  15/12/15
 */
public interface AttendanceService extends DataBuilder {
    boolean importAttendance(List<AttendanceImportDTO> attendanceImportDTOList);
    void clearMonth(String month);
    void batchDelete(String ids);
    void merge(AttendanceDTO attendanceDTO);
    Attendance findById(Long id);
    void batchSetLeaveDays(String days);
    void batchSetWorkDays(String days);

    /**
     * 获取迟到罚款基数，现在默认都是10
     * @return
     */
    int getFineMoneyBasicOfDelay();

    /**
     * 获取早退罚款基数，现在默认都是10
     * @return
     */
    int getFineMoneyBasicOfLeaveEarly();

    int getLeaveEarlyLimit();

    int getDelayLimit();

    /**
     * 导出分析数据的excel
     * @param data
     * @return
     */
    HSSFWorkbook buildAnalysisExcel(List<DepartmentAnalysisResult> data);

}
