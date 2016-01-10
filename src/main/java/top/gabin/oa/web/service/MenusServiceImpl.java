/**
 * Copyright (c) 2016 云智盛世
 * Created with MenusServiceImpl.
 */
package top.gabin.oa.web.service;

import org.springframework.stereotype.Service;
import top.gabin.oa.web.dto.menus.MenusDTO;
import top.gabin.oa.web.entity.Admin;
import top.gabin.oa.web.entity.Menus;
import top.gabin.oa.web.entity.MenusImpl;
import top.gabin.oa.web.entity.Permission;
import top.gabin.oa.web.service.criteria.simple.QueryCondition;
import top.gabin.oa.web.service.criteria.simple.QueryService;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linjiabin  on  16/1/10
 */
@Service("menusService")
public class MenusServiceImpl implements MenusService {
    @Resource(name = "adminService")
    private AdminService adminService;
    @Resource(name = "permissionService")
    private PermissionService permissionService;
    @Resource(name = "queryService")
    private QueryService queryService;

    @Override
    public List<MenusDTO> findTopMenusByAdminName(String adminName) {
        List<MenusDTO> menusDTOList = new ArrayList<MenusDTO>();
        Admin admin = adminService.findByName(adminName);
        List<Permission> permissionList = admin.getPermissionList();
        if (admin.getId() == -1) {
            permissionList = permissionService.findAll();
        }
        final List<Long> permissionIds = new ArrayList<Long>();
        for (Permission permission : permissionList) {
            permissionIds.add(permission.getId());
        }
        List<Menus> menusList = permissionIds.isEmpty() ? new ArrayList<Menus>() : queryService.query(Menus.class, MenusImpl.class, new QueryCondition() {
            @Override
            public List<Predicate> buildWhere(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, From from) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                predicateList.add(criteriaBuilder.equal(from.get("level"), 1));
                predicateList.add(from.get("permission").get("id").in(permissionIds));
                return predicateList;
            }
        });
        if (!menusList.isEmpty()) {
            for (Menus menus : menusList) {
                MenusDTO dto = new MenusDTO();
                dto.setName(menus.getName());
                dto.setUrl(menus.getUrl());
                dto.setId(menus.getId());
                List<MenusDTO> childs0 = new ArrayList<MenusDTO>();
                if (!menus.getChildMenus().isEmpty()) {
                    for (Menus menus1 : menus.getChildMenus()) {
                        MenusDTO dto1 = new MenusDTO();
                        dto1.setName(menus1.getName());
                        dto1.setId(menus1.getId());
                        dto1.setUrl(menus1.getUrl());
                        List<MenusDTO> childs1 = new ArrayList<MenusDTO>();
                        if (!menus1.getChildMenus().isEmpty()) {
                            for (Menus menus2 : menus1.getChildMenus()) {
                                MenusDTO dto2 = new MenusDTO();
                                dto2.setName(menus2.getName());
                                dto2.setUrl(menus2.getUrl());
                                dto2.setId(menus2.getId());
                                childs1.add(dto2);
                            }
                        }
                        dto1.setChildMenus(childs1);
                        childs0.add(dto1);
                    }
                }
                dto.setChildMenus(childs0);
                menusDTOList.add(dto);
            }
        }
        return menusDTOList;
    }



}
