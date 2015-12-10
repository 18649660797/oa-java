/**
 * Copyright (c) 2015 云智盛世
 * Created with PermissionController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.entity.Permission;
import top.gabin.oa.web.entity.PermissionImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin  on  15/12/10
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private CriteriaQueryService queryService;
    @RequestMapping("/grid")
    public @ResponseBody List<Map> grid(HttpServletRequest request) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<PermissionImpl> adminPageDTO = queryService.queryPage(PermissionImpl.class, criteriaCondition);
        return RenderUtils.filterPageData(adminPageDTO.getContent(), "id value,name,label text");
    }

}
