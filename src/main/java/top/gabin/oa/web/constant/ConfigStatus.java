package top.gabin.oa.web.constant;

/**
 * @author linjiabin  on  15/12/20
 */
public enum ConfigStatus {
    ENABLE(1, "启用"),
    UNABLE(0, "关闭");

    private Integer type;
    private String label;

    ConfigStatus(Integer type, String label) {
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

    public static ConfigStatus instance(Integer status) {
        for (ConfigStatus status0 : ConfigStatus.values()) {
            if (status0.getType() == status) {
                return status0;
            }
        }
        return null;
    }

}
