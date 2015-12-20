/**
 * Copyright (c) 2015 云智盛世
 * Created with ConfigDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Config;
import top.gabin.oa.web.entity.ConfigImpl;

/**
 * @author linjiabin  on  15/12/20
 */
@Repository("configDao")
public class ConfigDaoImpl extends CommonBaseDaoImpl<Config, ConfigImpl> implements ConfigDao {

}
