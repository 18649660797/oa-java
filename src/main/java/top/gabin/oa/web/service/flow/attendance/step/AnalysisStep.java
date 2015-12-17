package top.gabin.oa.web.service.flow.attendance.step;

import org.springframework.core.Ordered;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;

import java.util.List;

/**
 * @author linjiabin  on  15/12/17
 */
public interface AnalysisStep extends Ordered {
    List<DepartmentAnalysisResult> analysis (List<DepartmentAnalysisResult> analysisResults);
}
