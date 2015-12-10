/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
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

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "admin/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add() {
        return "admin/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<AdminImpl> adminPageDTO = queryService.queryPage(AdminImpl.class, criteriaCondition);
        return RenderUtils.filterPageDataResult(adminPageDTO, "id,name");
    }
}
