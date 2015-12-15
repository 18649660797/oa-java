/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveImportDTO.
 */
package top.gabin.oa.web.dto;

import top.gabin.oa.web.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * @author linjiabin  on  15/12/14
 */
public class LeaveImportDTO {
    @ExcelField(value = "name", title = "员工名")
    private String name;
    @ExcelField(value = "leaveName", title = "假别")
    private String leaveName;
    @ExcelField(value = "beginDate", title = "开始日期", fieldType = Date.class)
    private Date beginDate;
    @ExcelField(value = "endDate", title = "结束日期", fieldType = Date.class)
    private Date endDate;
    @ExcelField(value = "remark", title = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
