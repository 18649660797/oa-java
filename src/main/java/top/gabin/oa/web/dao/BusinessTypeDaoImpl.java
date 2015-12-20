/**
 * Copyright (c) 2015 云智盛世
 * Created with BusinessTypeDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.BusinessType;
import top.gabin.oa.web.entity.BusinessTypeImpl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author linjiabin  on  15/12/20
 */
@Repository("businessTypeDao")
public class BusinessTypeDaoImpl extends CommonBaseDaoImpl<BusinessType, BusinessTypeImpl> implements BusinessTypeDao {
    @Cacheable(value = "configCache", key = "#name")
    @Override
    public BusinessType findByKey(String name) {
        if (StringUtils.isNotBlank(name)) {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<BusinessTypeImpl> query = criteriaBuilder.createQuery(BusinessTypeImpl.class);
            Root<BusinessTypeImpl> from = query.from(BusinessTypeImpl.class);
            query.select(from);
            query.where(criteriaBuilder.equal(from.get("key"), name));
            TypedQuery<BusinessTypeImpl> typeTypedQuery = em.createQuery(query);
            return typeTypedQuery.getSingleResult();
        }
        return null;
    }

    @CachePut(value = "configCache", key = "#t.getKey()")
    @Override
    public void persist(BusinessType t) {
        super.persist(t);
    }

}
