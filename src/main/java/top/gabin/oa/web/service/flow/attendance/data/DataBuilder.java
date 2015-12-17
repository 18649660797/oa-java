package top.gabin.oa.web.service.flow.attendance.data;

import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/17
 */
public interface DataBuilder {
    /**
     * 获取指定月份的考勤数据
     * @param month
     * @return
     */
    List<DepartmentAnalysisResult> buildAnalysisData(String month);
}
