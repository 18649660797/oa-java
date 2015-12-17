/**
 * Copyright (c) 2015 云智盛世
 * Created with WorkFlowExecute.
 */
package top.gabin.oa.web.service.flow.attendance.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.service.flow.attendance.data.DataBuilder;
import top.gabin.oa.web.service.flow.attendance.step.AnalysisStep;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author linjiabin  on  15/12/17
 */
public class WorkFlowExecute implements Execute {
    private List<AnalysisStep> analysisStepList;
    private DataBuilder dataBuilder;
    private static final Logger logger = LoggerFactory.getLogger(Execute.class);

    public List<AnalysisStep> getAnalysisStepList() {
        return analysisStepList;
    }

    public void setAnalysisStepList(List<AnalysisStep> analysisStepList) {
        this.analysisStepList = analysisStepList;
    }

    public DataBuilder getDataBuilder() {
        return dataBuilder;
    }

    public void setDataBuilder(DataBuilder dataBuilder) {
        this.dataBuilder = dataBuilder;
    }

    @Override
    public List<DepartmentAnalysisResult> execute(String month) {
        List<DepartmentAnalysisResult> departmentAnalysisResultList = dataBuilder.buildAnalysisData(month);
        if (analysisStepList != null && !analysisStepList.isEmpty()) {
            Collections.sort(analysisStepList, new Comparator<AnalysisStep>() {
                @Override
                public int compare(AnalysisStep o1, AnalysisStep o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
            Iterator<AnalysisStep> iterator = analysisStepList.iterator();
            while (iterator.hasNext()) {
                AnalysisStep analysisStep = iterator.next();
                logger.info(analysisStep.getOrder() + "");
                departmentAnalysisResultList = analysisStep.analysis(departmentAnalysisResultList);
            }
        }
        return departmentAnalysisResultList;
    }

}
