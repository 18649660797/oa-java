/**
 * Copyright (c) 2015 云智盛世
 * Created with PermissionController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.dto.SimpleTreeDTO;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.entity.Permission;
import top.gabin.oa.web.entity.PermissionImpl;
import top.gabin.oa.web.service.PermissionService;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin  on  15/12/10
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource(name = "permissionService")
    private PermissionService permissionService;
    @RequestMapping("/grid")
    public @ResponseBody List<Map> grid(HttpServletRequest request) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<PermissionImpl> permissionPageDTO = queryService.queryPage(PermissionImpl.class, criteriaCondition);
        return RenderUtils.filterPageData(permissionPageDTO.getContent(), "id value,name,label text");
    }

    @RequestMapping(value = "/simpleTree", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> simpleTree(HttpServletRequest request) {
        return RenderUtils.getSuccessResult(permissionService.getPermissionTreeData(request));
    }

}
