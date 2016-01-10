/**
 * Copyright (c) 2016 云智盛世
 * Created with MenusDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Menus;
import top.gabin.oa.web.entity.MenusImpl;

/**
 * @author linjiabin  on  16/1/10
 */
@Repository("menusDao")
public class MenusDaoImpl extends CommonBaseDaoImpl<Menus, MenusImpl> implements MenusDao {

}
