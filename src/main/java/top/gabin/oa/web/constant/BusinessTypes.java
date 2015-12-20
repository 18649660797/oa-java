package top.gabin.oa.web.constant;

/**
 * @author linjiabin  on  15/12/20
 */
public enum BusinessTypes {
    ATTENDANCE("考勤配置", "attendance_config");
    private String label;
    private String key;

    BusinessTypes(String label, String key) {
        this.label = label;
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
