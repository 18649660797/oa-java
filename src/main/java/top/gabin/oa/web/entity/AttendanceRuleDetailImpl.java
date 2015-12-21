/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceRuleDetailImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;

/**
 *
 * @author linjiabin  on  15/12/21
 */
@Entity
@Table(name = "edy_attendance_rule_detail")
public class AttendanceRuleDetailImpl implements AttendanceRuleDetail {
    @Id
    @TableGenerator(name = "attendance_rule_detail_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "attendance_rule_detail_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "rule")
    private String rule;
    @Column(name = "limits")
    private Integer limit;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getRule() {
        return rule;
    }

    @Override
    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
