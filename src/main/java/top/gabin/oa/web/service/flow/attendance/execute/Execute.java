/**
 * Copyright (c) 2015 云智盛世
 * Created with Execute.
 */
package top.gabin.oa.web.service.flow.attendance.execute;

import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;

import java.util.List;

/**
 * @author linjiabin  on  15/12/17
 */
public interface Execute {
    List<DepartmentAnalysisResult> execute(String month);
}
