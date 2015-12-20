/**
 * Copyright (c) 2015 云智盛世
 * Created with ConfigImpl.
 */
package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.ConfigStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author linjiabin  on  15/12/20
 */
@Entity
@Table(name = "edy_config")
public class ConfigImpl implements Config {
    @Id
    private Long id;
    @Column(name = "label")
    private String label;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
    @Column(name = "defaultValue")
    private String defaultValue;
    @Column(name = "status")
    private Integer status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public ConfigStatus getStatus() {
        return ConfigStatus.instance(this.status);
    }

    @Override
    public void setStatus(ConfigStatus status) {
        if (status != null) {
            this.status = status.getType();
        }
    }

}
