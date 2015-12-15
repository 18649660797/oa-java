package top.gabin.oa.web.constant;

/**
 * @author linjiabin  on  15/12/15
 */
public enum AttendanceStatus {
    WORK(1, "工作日"),
    LEAVE(0, "休息日");

    private Integer type;
    private String label;

    AttendanceStatus(Integer type, String label) {
        this.type = type;
        this.label = label;
    }

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

    public static AttendanceStatus instance(Integer status) {
        for (AttendanceStatus attendanceStatus : AttendanceStatus.values()) {
            if (attendanceStatus.getType() == status) {
                return attendanceStatus;
            }
        }
        return null;
    }
}
