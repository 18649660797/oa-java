/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.LeaveService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    private String dir = "attendance";

    @RequestMapping("/list")
    public String list(Model model) {
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        return  dir + "/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        return  dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(LeaveImpl.class, request, "id,workDate,amDate,pmDate,employee.name employee");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(LeaveDTO leaveDTO) {
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> delete(String ids) {
        return RenderUtils.SUCCESS_RESULT;
    }
}
