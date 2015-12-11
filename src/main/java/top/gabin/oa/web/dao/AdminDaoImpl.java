/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Admin;
import top.gabin.oa.web.entity.AdminImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Class description
 * @author linjiabin  on  15/12/11
 */
@Repository("adminDao")
public class AdminDaoImpl extends CommonBaseDaoImpl<Admin, AdminImpl> implements AdminDao {
    @PersistenceContext(name = "entityManagerFactory")
    protected EntityManager em;

    @Override
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String deleteSql = "delete from AdminImpl where id in (:ids)";
            Query query = em.createQuery(deleteSql);
            query.setParameter("ids", ids);
            query.executeUpdate();
        }
    }

}
