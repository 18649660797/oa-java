/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.entity.EmployeeImpl;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/13
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private CriteriaQueryService queryService;
    private String dir = "employee";

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return dir + "/list";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit() {
        return dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(EmployeeImpl.class, request, "id,name,attendanceCN");
    }

}
