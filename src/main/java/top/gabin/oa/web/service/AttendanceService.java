package top.gabin.oa.web.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
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

    /**
     * 根据月份组装需要处理的考勤数据
     * @param month 格式：12-01
     * @return
     */
    List<DepartmentAnalysisResult> buildAnalysisDataByMonth(String month);

    /**
     * 处理昨日晚卡的数据
     * @param departmentAnalysisResultList
     * @return
     */
    List<DepartmentAnalysisResult> yesterdayWorkDelayWorkFlow(List<DepartmentAnalysisResult> departmentAnalysisResultList);

    /**
     * 处理考勤规则数据
     * @param departmentAnalysisResultList
     * @return
     */
    List<DepartmentAnalysisResult> dealRuleWorkFlow(List<DepartmentAnalysisResult> departmentAnalysisResultList);

    /**
     * 分析月份的考勤数据
     * @param month
     * @return
     */
    List<DepartmentAnalysisResult> analysisDataByMonth(String month);

    /**
     * 导出指定月份的分析数据的excel
     * @param month
     * @return
     */
    HSSFWorkbook buildAnalysisExcel(String month);

}
