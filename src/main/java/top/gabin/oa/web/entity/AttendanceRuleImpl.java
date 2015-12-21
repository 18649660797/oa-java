/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceRuleImpl.
 */
package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.AttendanceRuleType;
import top.gabin.oa.web.constant.ConfigStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/21
 */
@Entity
@Table(name = "edy_attendance_rule")
public class AttendanceRuleImpl implements AttendanceRule {
    @Id
    @TableGenerator(name = "attendance_rule_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "attendance_rule_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "extra_data")
    private String extraData;
    @Column(name = "type")
    private Integer type;
    @Column(name = "status")
    private Integer status;
    @Column(name = "begin_date")
    private Date beginDate;
    @Column(name = "end_date")
    private Date endDate;
    @ManyToMany(targetEntity = AttendanceRuleDetailImpl.class)
    @JoinTable(name = "edy_attendance_rule_detail_xref", joinColumns = @JoinColumn(name = "detail_id"), inverseJoinColumns = @JoinColumn(name = "rule_id"))
    @MapKeyColumn(name = "map_key")
    private Map<String, AttendanceRuleDetail> attendanceRuleDetailMap = new HashMap<String, AttendanceRuleDetail>();

    @Override
    public Map<String, AttendanceRuleDetail> getAttendanceRuleDetailMap() {
        return attendanceRuleDetailMap;
    }

    @Override
    public void setAttendanceRuleDetailMap(Map<String, AttendanceRuleDetail> attendanceRuleDetailMap) {
        this.attendanceRuleDetailMap = attendanceRuleDetailMap;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getExtraData() {
        return extraData;
    }

    @Override
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    @Override
    public AttendanceRuleType getType() {
        return AttendanceRuleType.instance(this.type);
    }

    @Override
    public void setType(AttendanceRuleType type) {
        if (type != null) {
            this.type = type.getType();
        }
    }

    @Override
    public ConfigStatus getStatus() {
        return ConfigStatus.instance(this.status);
    }

    @Override
    public void setStatus(ConfigStatus status) {
        if (status != null) {
            this.status = status.getType();
        }
    }

    @Override
    public Date getBeginDate() {
        return beginDate;
    }

    @Override
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
