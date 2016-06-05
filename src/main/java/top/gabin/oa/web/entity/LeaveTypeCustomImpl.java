/**
 * Copyright (c) 2016 云智盛世
 * Created with LeaveType.
 */
package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.attendance.LeaveTypeEnum;

import javax.persistence.*;

/**
 *
 * @author linjiabin  on  16/3/31
 */
@Entity
@Table(name = "edy_leave_type")
public class LeaveTypeCustomImpl implements LeaveTypeCustom {
    @Id
    @TableGenerator(name = "leave_type_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 1000, allocationSize = 50)
    @GeneratedValue(generator = "leave_type_sequences", strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private Long id;
    @Column(name = "LABEL")
    private String label;
    @Column(name = "TYPE")
    private String type;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public LeaveTypeEnum getType() {
        return LeaveTypeEnum.instance(this.type);
    }

    @Override
    public void setType(LeaveTypeEnum leaveTypeEnum) {
        if (leaveTypeEnum != null) {
            this.type = leaveTypeEnum.getType();
        }
    }

}
