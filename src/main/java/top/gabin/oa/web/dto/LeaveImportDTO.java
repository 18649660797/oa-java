/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveImportDTO.
 */
package top.gabin.oa.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import top.gabin.oa.web.constant.LeaveType;
import top.gabin.oa.web.utils.excel.annotation.ExcelField;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
public class LeaveImportDTO {
    public LeaveImportDTO() {
    }

    public static Map<String, Long> ID_CACHE = new HashMap<String, Long>();
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
    private Integer leaveType;

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

    public String getLeaveType() {
        return LeaveType.instance(leaveName).getType().toString();
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
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

    @JsonIgnore
    public String getException() {
        String exception = "";
        if (beginDate.getTime() > endDate.getTime()) {
            exception += "开始时间不能早于结束时间 ";
        }
        if (!ID_CACHE.containsKey(StringUtils.trim(name))) {
            exception += "用户名不存在 ";
        }
        return exception;
    }

    @JsonIgnore
    public Long getId() {
        return ID_CACHE.get(StringUtils.trim(name));
    }

}
