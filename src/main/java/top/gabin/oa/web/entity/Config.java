package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.ConfigStatus;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/20
 */
public interface Config {
    Long getId();

    void setId(Long id);

    String getLabel();

    void setLabel(String label);

    String getKey();

    void setKey(String key);

    String getValue();

    void setValue(String value);

    String getDefaultValue();

    void setDefaultValue(String defaultValue);

    ConfigStatus getStatus();

    void setStatus(ConfigStatus status);
}
