/**
 * Copyright (c) 2016 云智盛世
 * Created with LeaveResult.
 */
package top.gabin.oa.web.dto.attendance;

import top.gabin.oa.web.entity.Leave;

/**
 * Class description
 *
 * @author linjiabin  on  16/1/4
 */
public class LeaveResult {
    private Leave leave;
    private int leaveMinutes;

    public LeaveResult(Leave leave) {
        this.leave = leave;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public int getLeaveMinutes() {
        return leaveMinutes;
    }

    public void setLeaveMinutes(int leaveMinutes) {
        this.leaveMinutes = leaveMinutes;
    }
}
