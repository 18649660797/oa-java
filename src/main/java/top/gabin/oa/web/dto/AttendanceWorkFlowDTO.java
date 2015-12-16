/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveWorlFlow.
 */
package top.gabin.oa.web.dto;

import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.Leave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author linjiabin  on  15/12/16
 */
public class AttendanceWorkFlowDTO {
    private Attendance attendance;
    private Date amNeedFitTime;
    private Date pmNeedFitTime;
    private Long leaveTimes; //  请假分钟数
    private boolean notNeedFit;
    private List<Leave> leaveList = new ArrayList<Leave>();

    public List<Leave> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<Leave> leaveList) {
        this.leaveList = leaveList;
    }

    public boolean isNotNeedFit() {
        return notNeedFit;
    }

    public void setNotNeedFit(boolean notNeedFit) {
        this.notNeedFit = notNeedFit;
    }

    public Long getLeaveTimes() {
        return leaveTimes;
    }

    public void setLeaveTimes(Long leaveTimes) {
        this.leaveTimes = leaveTimes;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public Date getAmNeedFitTime() {
        return amNeedFitTime;
    }

    public void setAmNeedFitTime(Date amNeedFitTime) {
        this.amNeedFitTime = amNeedFitTime;
    }

    public Date getPmNeedFitTime() {
        return pmNeedFitTime;
    }

    public void setPmNeedFitTime(Date pmNeedFitTime) {
        this.pmNeedFitTime = pmNeedFitTime;
    }
}
