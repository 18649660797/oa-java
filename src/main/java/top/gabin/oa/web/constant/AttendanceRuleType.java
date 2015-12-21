package top.gabin.oa.web.constant;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/21
 */
public enum AttendanceRuleType {
    WORK_FIT("上班打卡时间规则", 0),
    LEAVE_FIT("下班打卡时间规则", 1)
    ;
    private String label;
    private Integer type;

    AttendanceRuleType(String label, Integer type) {
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static AttendanceRuleType instance(Integer type) {
        for (AttendanceRuleType attendanceRuleType : AttendanceRuleType.values()) {
            if (attendanceRuleType.getType() == type) {
                return attendanceRuleType;
            }
        }
        return null;
    }

}
