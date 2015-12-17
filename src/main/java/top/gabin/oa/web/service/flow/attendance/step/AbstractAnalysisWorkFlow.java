/**
 * Copyright (c) 2015 云智盛世
 * Created with AbstractAnalysisWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;

import java.util.List;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/12/17
 */
public abstract class AbstractAnalysisWorkFlow implements AnalysisStep {
    protected int order;
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> analysisResults) {
        return analysisResults;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

}
