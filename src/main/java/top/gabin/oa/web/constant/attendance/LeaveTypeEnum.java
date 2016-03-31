package top.gabin.oa.web.constant.attendance;

/**
 * @author linjiabin  on  16/3/31
 */
public enum LeaveTypeEnum {
    PAID_LEAVE("PAID_LEAVE", "带薪假"),
    UN_PAID_LEAVE("UN_PAID_LEAVE", "无薪假");
    private String type;
    private String label;

    LeaveTypeEnum(String type, String label) {
        this.type = type;
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean equals(LeaveTypeEnum leaveTypeEnum) {
        return leaveTypeEnum != null && leaveTypeEnum.getType().equals(this.getType());
    }

    public static LeaveTypeEnum instance(String type) {
        for (LeaveTypeEnum leaveTypeEnum : LeaveTypeEnum.values()) {
            if (leaveTypeEnum.getType().equals(type)) {
                return leaveTypeEnum;
            }
        }
        return UN_PAID_LEAVE;
    }

}
