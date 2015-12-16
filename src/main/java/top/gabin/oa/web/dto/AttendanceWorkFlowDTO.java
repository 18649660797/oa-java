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
    public static int ADD_MONEY_ONE = 10;
    private Attendance attendance;
    private Date amNeedFitTime;
    private Date pmNeedFitTime;
    private Long leaveTimes; //  请假分钟数
    private boolean notNeedFit = false;
    // 早上十五分钟内一个月迟到n次中的一次
    private boolean amLimit = false;
    private boolean pmLimit = false;
    private int amMoney;
    private int amMinutes; // 迟到分钟
    private int pmMoney;
    private int pmMinutes; // 早退分钟
    private String remark;
    private boolean yesterdayWorkDelay = false;

    public boolean isYesterdayWorkDelay() {
        return yesterdayWorkDelay;
    }

    public void setYesterdayWorkDelay(boolean yesterdayWorkDelay) {
        this.yesterdayWorkDelay = yesterdayWorkDelay;
    }

    // 旷工
    private boolean hasNowWork = false;

    public boolean isHasNowWork() {
        return hasNowWork;
    }

    public void setHasNowWork(boolean hasNowWork) {
        this.hasNowWork = hasNowWork;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getAmMinutes() {
        return amMinutes;
    }

    public void setAmMinutes(int amMinutes) {
        this.amMinutes = amMinutes;
    }

    public int getPmMinutes() {
        return pmMinutes;
    }

    public void setPmMinutes(int pmMinutes) {
        this.pmMinutes = pmMinutes;
    }

    public int getAmMoney() {
        return amMoney;
    }

    public void setAmMoney(int amMoney) {
        this.amMoney = amMoney;
    }

    public int getPmMoney() {
        return pmMoney;
    }

    public void setPmMoney(int pmMoney) {
        this.pmMoney = pmMoney;
    }

    public boolean isAmLimit() {
        return amLimit;
    }

    public void setAmLimit(boolean amLimit) {
        this.amLimit = amLimit;
    }

    public boolean isPmLimit() {
        return pmLimit;
    }

    public void setPmLimit(boolean pmLimit) {
        this.pmLimit = pmLimit;
    }

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
