package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.menus.MenusDTO;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  16/1/10
 */
public interface MenusService {
    List<MenusDTO> findTopMenusByAdminName(String adminName);
}
