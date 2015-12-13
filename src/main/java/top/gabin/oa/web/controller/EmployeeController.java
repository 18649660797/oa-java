/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeController.
 */
package top.gabin.oa.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.dao.EmployeeDao;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.DepartmentDTO;
import top.gabin.oa.web.dto.EmployeeDTO;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.EmployeeService;
import top.gabin.oa.web.service.EmployeeServiceImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;
import top.gabin.oa.web.utils.json.JsonUtils;

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
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "employeeService")
    private EmployeeService employeeService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    private String dir = "employee";
    private static int maxSize = 10 * 1024 * 1024;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        return dir + "/list";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Model model, Long id) {
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        if (id != null) {
            model.addAttribute("entity", employeeService.findById(id));
        }
        return dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(EmployeeImpl.class, request, "id,name,attendanceCN,department.name department");
    }

    @RequestMapping(value = "importView", method = RequestMethod.GET)
    public String importView() {
        return dir + "/import";
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public @ResponseBody Map productImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
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
            employeeService.importEmployee(dataList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "导入数据有异常!");
        }
        return resultMap;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> delete(String ids) {
        employeeService.batchDelete(ids);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(EmployeeDTO employeeDTO) {
        employeeService.merge(employeeDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "suggest/{key}", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    public @ResponseBody String suggest(HttpServletRequest request, String callback, @PathVariable(value = "key") String key, String name) {
        List<EmployeeImpl> query = queryService.query(EmployeeImpl.class, CriteriaQueryUtils.parseCondition(request));
        List<Map> list = RenderUtils.filterPageData(query, key);
        List<String> names = new ArrayList<String>();
        for (Map map : list) {
            names.add(map.get(key) == null ? "" : map.get(key).toString());
        }
        return callback + "(" + JsonUtils.bean2Json(names) + ")";
    }

    @RequestMapping(value = "uniqueCheck", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> uniqueCheck(String name, Long id) {
        Employee employee = employeeService.findByAttendanceCN(name);
        if (employee != null && employee.getId() != id) {
            return RenderUtils.getFailMap("考勤号已经存在!");
        }
        return RenderUtils.SUCCESS_RESULT;
    }

}
