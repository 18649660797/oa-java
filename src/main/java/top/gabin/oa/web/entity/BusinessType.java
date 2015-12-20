package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.ConfigStatus;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/20
 */
public interface BusinessType {
    Long getId();

    void setId(Long id);

    String getLabel();

    void setLabel(String label);

    String getKey();

    void setKey(String key);

    ConfigStatus getStatus();

    void setStatus(ConfigStatus status);

    boolean isShow();

    void setShow(boolean show);

    List<BusinessConfig> getBusinessConfigList();

    void setBusinessConfigList(List<BusinessConfig> businessConfigList);
}
