/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.AdminDTO;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.service.AdminService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/10
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "adminService")
    private AdminService adminService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "admin/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add() {
        return "admin/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model) {
        model.addAttribute("entity", adminService.findById(id));
        return "admin/edit";
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

}
