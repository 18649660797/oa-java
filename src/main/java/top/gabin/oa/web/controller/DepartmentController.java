/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentController.
 */
package top.gabin.oa.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.dao.DepartmentDao;
import top.gabin.oa.web.dto.AdminDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.DepartmentDTO;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/13
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    private String dir = "department";
    private static int maxSize = 10 * 1024 * 1024;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return dir + "/list";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("entity", departmentService.findById(id));
        }
        return dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(DepartmentImpl.class, request, "id,name");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(DepartmentDTO departmentDTO) {
        departmentService.merge(departmentDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "uniqueCheck", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> uniqueCheck(String name, Long id) {
        Department department = departmentService.findByName(name);
        if (department != null && department.getId() != id) {
            return RenderUtils.getFailMap("部门名称已经存在!");
        }
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> delete(String ids) {
        departmentService.batchDelete(ids);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "importView", method = RequestMethod.GET)
    public String importView() {
       return dir + "/import";
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public @ResponseBody Map productImport(@RequestParam("csvFile") MultipartFile file, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (file.isEmpty()) {
            resultMap.put("message", "请选择文件!");
            return resultMap;
        }
        if (file.getSize() > maxSize) {
            resultMap.put("message", "上传文件大小超过限制。");
            return resultMap;
        }
        try {
            ImportExcel importExcel = new ImportExcel(file, 3, 0);
            List<AttendanceImportDTO> dataList = importExcel.getDataList(AttendanceImportDTO.class);
            Map<String, Integer> cache = new HashMap<String, Integer>();
            List<Department> departmentList = new ArrayList<Department>();
            for (AttendanceImportDTO dto : dataList) {
                String departmentName = dto.getDepartment();
                if (StringUtils.isBlank(departmentName)) {
                    continue;
                }
                if (!cache.containsKey(departmentName)) {
                    DepartmentImpl department = new DepartmentImpl();
                    department.setName(departmentName);
                    departmentList.add(department);
                    cache.put(dto.getDepartment(), 1);
                }
            }
            departmentService.batchSave(departmentList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "导入数据有异常!");
        }
        return resultMap;
    }

}
