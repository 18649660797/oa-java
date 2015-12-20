package top.gabin.oa.web.entity;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/20
 */
public interface BusinessConfig {
    Long getId();

    void setId(Long id);

    String getLabel();

    void setLabel(String label);

    String getKey();

    void setKey(String key);

    String getValue();

    void setValue(String value);

    boolean isRequired();

    void setRequired(boolean required);

    String getDesc();

    void setDesc(String desc);

    BusinessType getBusinessType();

    void setBusinessType(BusinessType businessType);
}
