/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Admin;
import top.gabin.oa.web.entity.AdminImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Class description
 * @author linjiabin  on  15/12/11
 */
@Repository("adminDao")
public class AdminDaoImpl extends CommonBaseDaoImpl<Admin, AdminImpl> implements AdminDao {
    @PersistenceContext(name = "entityManagerFactory")
    protected EntityManager em;


}
