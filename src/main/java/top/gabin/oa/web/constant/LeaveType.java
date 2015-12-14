package top.gabin.oa.web.constant;

/**
 * @author linjiabin  on  15/12/14
 */
public enum LeaveType {
    NORMAL_LEAVE(1, "事假"),
    SICK_LEAVE(2, "病假"),
    OFF_LEAVE(3, "调休"),
    OUT_LEAVE(4, "外出"),
    FUNERAL_LEAVE(5, "丧假"),
    YEAR_LEAVE(6, "年假"),
    MATERNITY_LEAVE(7, "产假"),
    MARRY_LEAVE(8, "婚假")
    ;

    LeaveType(Integer type, String label) {
        this.type = type;
        this.label = label;
    }

    private Integer type;
    private String label;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static LeaveType instance(Integer type) {
        for (LeaveType leaveType : LeaveType.values()) {
            if (type == leaveType.getType()) {
                return leaveType;
            }
        }
        return null;
    }

    public static LeaveType instance(String label) {
        for (LeaveType leaveType : LeaveType.values()) {
            if (label.equals(leaveType.getLabel())) {
                return leaveType;
            }
        }
        return null;
    }

}
