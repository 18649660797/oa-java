/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceBasicRuleConfigForm.
 */
package top.gabin.oa.web.dto;

/**
 * @author linjiabin  on  15/12/20
 */
public class AttendanceBasicRuleConfigForm {
    private String workFit;
    private String leaveFit;
    private Integer workFitOffset;
    private String resetBegin;
    private String resetEnd;

    public String getWorkFit() {
        return workFit;
    }

    public void setWorkFit(String workFit) {
        this.workFit = workFit;
    }

    public String getLeaveFit() {
        return leaveFit;
    }

    public void setLeaveFit(String leaveFit) {
        this.leaveFit = leaveFit;
    }

    public Integer getWorkFitOffset() {
        return workFitOffset;
    }

    public void setWorkFitOffset(Integer workFitOffset) {
        this.workFitOffset = workFitOffset;
    }

    public String getResetBegin() {
        return resetBegin;
    }

    public void setResetBegin(String resetBegin) {
        this.resetBegin = resetBegin;
    }

    public String getResetEnd() {
        return resetEnd;
    }

    public void setResetEnd(String resetEnd) {
        this.resetEnd = resetEnd;
    }
}
