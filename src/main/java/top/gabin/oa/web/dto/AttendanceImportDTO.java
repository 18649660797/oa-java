/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceImportDTO.
 */
package top.gabin.oa.web.dto;

import top.gabin.oa.web.utils.excel.annotation.ExcelField;

/**
 * @author linjiabin  on  15/12/14
 */
public class AttendanceImportDTO {
    @ExcelField(value = "attendance", title = "工号")
    private String attendance;
    @ExcelField(value = "realName", title = "姓名")
    private String realName;

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
