/**
 * Created with BusinessConfigs.
 */
package top.gabin.oa.web.constant;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/12/20
 */
public enum  BusinessConfigs {
    WORK_FIT("上班打卡时间", "WORK_FIT"),
    LEAVE_FIT("下班打卡时间", "LEAVE_FIT"),
    REST_BEGIN("中午休息开始时间", "REST_BEGIN"),
    REST_END("中午休息结束时间", "REST_END"),
    WORK_FIT_OFFSET("上班打卡偏移时间", "WORK_FIT_OFFSET")
    ;
    private String label;
    private String key;

    BusinessConfigs(String label, String key) {
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
