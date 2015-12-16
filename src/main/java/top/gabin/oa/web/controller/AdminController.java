/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminController.
 */
package top.gabin.oa.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.AdminDTO;
import top.gabin.oa.web.entity.Admin;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.entity.Permission;
import top.gabin.oa.web.service.AdminService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * －－－－－－－－－－－
 * 管理员模块Controller类
 * －－－－－－－－－－－
 * @author linjiabin  on  15/12/10
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource(name = "adminService")
    private AdminService adminService;
    private String dir = "admin";

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return dir + "/list";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model) {
        if (id != null) {
            Admin admin = adminService.findById(id);
            List<String> ids = new ArrayList<String>();
            for (Permission permission : admin.getPermissionList()) {
                ids.add(permission.getId()  + "");
            }
            model.addAttribute("entity", admin);
            model.addAttribute("permissions", StringUtils.join(ids, ","));
        }
        return dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(AdminImpl.class, request, "id,name");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(AdminDTO adminDTO) {
        adminService.merge(adminDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> delete(String ids) {
        adminService.batchDelete(ids);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "uniqueCheck", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> uniqueCheck(String name, Long id) {
        Admin admin = adminService.findByName(name);
        if (admin != null && admin.getId() != id) {
            return RenderUtils.getFailMap("管理员名称已经存在!");
        }
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> reset(String password, Long id) {
        adminService.reset(id, password);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "viewPwdSet", method = RequestMethod.GET)
    public String viewSetPwd(Long id, Model model) {
        if (id != null) {
            Admin admin = adminService.findById(id);
            model.addAttribute("entity", admin);
        }
       return dir + "/passwordSet";
    }

}
