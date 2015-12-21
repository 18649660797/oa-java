/**
 * Copyright (c) 2015 云智盛世
 * Created with RuleController.
 */
package top.gabin.oa.web.controller.attendance;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.AttendanceBasicRuleConfigForm;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.AttendanceRuleImpl;
import top.gabin.oa.web.service.BusinessService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/20
 */
@Controller
@RequestMapping("/attendance/rule")
public class RuleController {
    private String dir = "attendance/rule";
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource(name = "businessService")
    private BusinessService businessService;

    @RequestMapping("/basic")
    public String basic(Model model) {
        AttendanceBasicRuleConfig attendanceBasicRule = businessService.getAttendanceBasicRule();
        model.addAttribute("rule", attendanceBasicRule);
        return dir + "/basic";
    }

    @RequestMapping("/list")
    public String list() {
        return dir + "/list";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(AttendanceRuleImpl.class, request, "id,beginDate,endDate,name,status.label status,type.label type,attendanceRuleDetailMap");
    }

    @RequestMapping(value = "/basic/save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveBasic(AttendanceBasicRuleConfigForm form) {
        businessService.setAttendanceBasicRule(form);
        return RenderUtils.SUCCESS_RESULT;
    }

}
