/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeAnalysisResult.
 */
package top.gabin.oa.web.dto.attendance;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于员工的考勤分析结果
 * @author linjiabin  on  15/12/17
 */
public class EmployeeAnalysisResult {
    // 员工id
    private Long id;
    // 每一天的考勤
    private List<AnalysisResult> analysisResultList = new ArrayList<AnalysisResult>();
    // 迟到次数
    private int delaySeconds;
    // 已经上班乐捐上限
    private int delayMoney;
    // 早退次数
    private int leaveEarlySeconds;
    // 已经下班乐捐上限
    private int leaveEarlyMoney;

    public int getDelayMoney() {
        return delayMoney;
    }

    public void setDelayMoney(int delayMoney) {
        this.delayMoney = delayMoney;
    }

    public int getLeaveEarlyMoney() {
        return leaveEarlyMoney;
    }

    public void setLeaveEarlyMoney(int leaveEarlyMoney) {
        this.leaveEarlyMoney = leaveEarlyMoney;
    }

    public List<AnalysisResult> getAnalysisResultList() {
        return analysisResultList;
    }

    public void setAnalysisResultList(List<AnalysisResult> analysisResultList) {
        this.analysisResultList = analysisResultList;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public int getLeaveEarlySeconds() {
        return leaveEarlySeconds;
    }

    public void setLeaveEarlySeconds(int leaveEarlySeconds) {
        this.leaveEarlySeconds = leaveEarlySeconds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void add(AnalysisResult analysisResult) {
        if (analysisResult != null) {
            analysisResultList.add(analysisResult);
        }
    }

}
