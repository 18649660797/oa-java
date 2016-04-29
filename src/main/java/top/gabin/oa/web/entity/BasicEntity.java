/**
 * Copyright (c) 2016 云智盛世
 * Created with BasicEntity.
 */
package top.gabin.oa.web.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import top.gabin.oa.web.tenant.utils.TenantUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 *  租户基类,目前除了数据库版本表和序列值自增表外,其他都在数据库创建了TENANT_ID字段
 *
 * @author linjiabin on  16/4/16
 */
// hibernate过滤器,在适当的地方会加上过滤条件
@FilterDef(name = TenantUtils.FILTER_TENANT, parameters = {@ParamDef(name = TenantUtils.FILTER_TENANT_ID, type = "long")})
@Filters({ @Filter(name = TenantUtils.FILTER_TENANT, condition = ":" + TenantUtils.FILTER_TENANT_ID + " = TENANT_ID") })
// 将所有注解映射到子类去,父类不会产生新表
@MappedSuperclass
public class BasicEntity {
    @Column(name = "TENANT_ID")
    protected Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @PrePersist
    private void persist() {
        setTenantId(TenantUtils.getTenantId());
    }

}
