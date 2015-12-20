/**
 * Copyright (c) 2015 云智盛世
 * Created with BusinessTypeImpl.
 */
package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.ConfigStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author linjiabin  on  15/12/20
 */
@Entity
@Table(name = "edy_business_type")
public class BusinessTypeImpl implements BusinessType, Serializable {
    @Id
    private Long id;
    @Column(name = "label")
    private String label;
    @Column(name = "key")
    private String key;
    @Column(name = "status")
    private Integer status;
    @Column(name = "show")
    private boolean show = false;
    @OneToMany(targetEntity = BusinessConfigImpl.class, mappedBy = "businessType")
    private List<BusinessConfig> businessConfigList;

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
    public ConfigStatus getStatus() {
        return ConfigStatus.instance(this.status);
    }

    @Override
    public void setStatus(ConfigStatus status) {
        if (status != null) {
            this.status = status.getType();
        }
    }

    @Override
    public boolean isShow() {
        return show;
    }

    @Override
    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public List<BusinessConfig> getBusinessConfigList() {
        return businessConfigList;
    }

    @Override
    public void setBusinessConfigList(List<BusinessConfig> businessConfigList) {
        this.businessConfigList = businessConfigList;
    }
}
