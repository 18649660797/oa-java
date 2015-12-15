/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.dto.LeaveImportDTO;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.LeaveService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "leaveService")
    private LeaveService leaveService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    private String dir = "leave";
    private static int maxSize = 10 * 1024 * 1024;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        return  dir + "/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("entity", leaveService.findById(id));
        }
        return  dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return queryService.queryPage(LeaveImpl.class, request, "id,beginDate,endDate,type.label type,employee.name realName,employee.department.name department,remark");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(LeaveDTO leaveDTO) {
        leaveService.merge(leaveDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> delete(String ids) {
        leaveService.batchDelete(ids);
        return RenderUtils.SUCCESS_RESULT;
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
            List<LeaveImportDTO> dataList = importExcel.getDataList(LeaveImportDTO.class);
            leaveService.importLeave(dataList);
            resultMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "导入数据有异常!");
        }
        return resultMap;
    }

    @RequestMapping(value = "dropView", method = RequestMethod.GET)
    public String dropView() {
        return dir + "/drop";
    }

    @RequestMapping(value = "dropMonth", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> dropMonth(String month) {
        leaveService.clearMonth(month);
        return RenderUtils.SUCCESS_RESULT;
    }

}
