/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceExcelDTO.
 */
package top.gabin.oa.web.dto;

import top.gabin.oa.web.utils.excel.annotation.ExcelField;

/**
 * @author linjiabin  on  15/12/15
 */
public class AttendanceExcelDTO {
    @ExcelField(value = "name", title = "姓名")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
