/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentAnalysisResult.
 */
package top.gabin.oa.web.dto.attendance;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于部门的考勤分析结果
 * @author linjiabin  on  15/12/17
 */
public class DepartmentAnalysisResult {
    // 部门ID
    private Long id;
    // 部门的员工考勤状况
    private List<EmployeeAnalysisResult> employeeAnalysisResultList = new ArrayList<EmployeeAnalysisResult>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EmployeeAnalysisResult> getEmployeeAnalysisResultList() {
        return employeeAnalysisResultList;
    }

    public void setEmployeeAnalysisResultList(List<EmployeeAnalysisResult> employeeAnalysisResultList) {
        this.employeeAnalysisResultList = employeeAnalysisResultList;
    }

    public void add(EmployeeAnalysisResult employeeAnalysisResult) {
        if (employeeAnalysisResult != null) {
            employeeAnalysisResultList.add(employeeAnalysisResult);
        }
    }
}
