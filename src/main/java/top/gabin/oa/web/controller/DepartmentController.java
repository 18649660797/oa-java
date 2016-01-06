/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.DepartmentDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * －－－－－－－－－－－－－－－－－－
 * －－－－ 部门controller类－－－－
 * －－－－－－－－－－－－－－－－－－
 * @author linjiabin  on  15/12/13
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    private String dir = "department";

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
    public @ResponseBody Map productImport(@RequestParam("csvFile") MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            ImportExcel importExcel = new ImportExcel(file, 3, 0);
            List<AttendanceImportDTO> dataList = importExcel.getDataList(AttendanceImportDTO.class);
            departmentService.batchSave(dataList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "导入数据有异常!");
        }
        return resultMap;
    }

}
