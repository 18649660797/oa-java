/**
 * Copyright (c) 2015 云智盛世
 * Created with BusinessConfigDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.BusinessConfig;
import top.gabin.oa.web.entity.BusinessConfigImpl;

/**
 * @author linjiabin  on  15/12/20
 */
@Repository("businessConfigDao")
public class BusinessConfigDaoImpl extends CommonBaseDaoImpl<BusinessConfig, BusinessConfigImpl> implements BusinessConfigDao {
}
