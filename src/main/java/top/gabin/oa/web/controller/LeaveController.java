/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveController.
 */
package top.gabin.oa.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.dto.LeaveImportDTO;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.entity.LeaveTypeCustom;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.EmployeeService;
import top.gabin.oa.web.service.LeaveService;
import top.gabin.oa.web.service.attendance.LeaveTypeService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.excel.ExcelUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;
import top.gabin.oa.web.utils.json.JsonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * －－－－－－－－－－－－－－－－－－
 * －－－－－请假Controller类－－－－
 * －－－－－－－－－－－－－－－－－－
 * @author linjiabin  on  15/12/14
 */
@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;
    @Resource(name = "leaveService")
    private LeaveService leaveService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    @Resource(name = "employeeService")
    private EmployeeService employeeService;
    @Resource(name = "leaveTypeService")
    private LeaveTypeService leaveTypeService;
    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);
    private final static String IMPORT_DATA = "SESSION_LEAVE_IMPORT";

    private String dir = "leave";

    @RequestMapping("/list")
    public String list(Model model) {
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        List<LeaveTypeCustom> typeCustomList = leaveTypeService.findAll();
        model.addAttribute("typeCustomList", typeCustomList);
        return  dir + "/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("entity", leaveService.findById(id));
        }
        List<LeaveTypeCustom> typeCustomList = leaveTypeService.findAll();
        model.addAttribute("typeCustomList", typeCustomList);
        return  dir + "/edit";
    }

    @RequestMapping(value = "/grid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> grid(HttpServletRequest request) {
        return criteriaQueryService.queryPage(LeaveImpl.class, request, "id,beginDate,endDate,leaveTypeCustom.label type,employee.name realName,employee.department.name department,remark");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(LeaveDTO leaveDTO) {
        leaveService.merge(leaveDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        leaveService.batchDelete(ids);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "/importView", method = RequestMethod.GET)
    public String importView() {
        return dir + "/import";
    }

    /**
     * 1 上传文件
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Map productImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            List<LeaveImportDTO> dataList = importExcel.getDataList(LeaveImportDTO.class);
            request.getSession().setAttribute(IMPORT_DATA, dataList);
            return RenderUtils.SUCCESS_RESULT;
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("导入数据有异常");
        }
    }

    @RequestMapping(value = "/previewCheck")
    @ResponseBody
    public Map<String, Object> previewCheck(HttpServletRequest request) {
        try {
            List<Object> dataList = (List) request.getSession().getAttribute(IMPORT_DATA);
            PageDTO<Object> objectPageDTO = new PageDTO<Object>(1, 1000, dataList.size(), dataList);
            LeaveImportDTO.ID_CACHE = employeeService.findAllNameMapId();
            return RenderUtils.filterPageDataResult(objectPageDTO, "name,leaveName,beginDate,endDate,remark,exception,id,leaveTypeCustom leaveType");
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("获取数据有异常");
        }
    }

    // 2 预览数据
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public String preview(Model model) {
        List<LeaveTypeCustom> typeCustomList = leaveTypeService.findAll();
        model.addAttribute("typeCustomList", typeCustomList);
        Map<String, Long> allNameMapId = employeeService.findAllNameMapId();
        model.addAttribute("allNameMapId", allNameMapId);
        return dir + "/preview";
    }

    // 3 确认导入
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> check(HttpServletRequest request, String jsonData) {
        JsonData data = JsonUtils.json2Bean(JsonData.class, jsonData);
        leaveService.importLeave(data.getData());
        request.getSession().removeAttribute(IMPORT_DATA);
        return RenderUtils.SUCCESS_RESULT;
    }

    public static class JsonData {
        private List<LeaveImportDTO> data;

        public List<LeaveImportDTO> getData() {
            return data;
        }

        public void setData(List<LeaveImportDTO> data) {
            this.data = data;
        }
    }

    @RequestMapping(value = "/dropView", method = RequestMethod.GET)
    public String dropView() {
        return dir + "/drop";
    }

    @RequestMapping(value = "/dropMonth", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> dropMonth(String month) {
        leaveService.clearMonth(month);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public void demo(HttpSession session, HttpServletResponse response) {
        String realPath = session.getServletContext().getRealPath("/static/download/leave.xlsx");
        RenderUtils.downloadFile(response, realPath, "请假外出导入模板.xlsx");
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String prosStr = "employee.name realName,employee.department.name department,leaveTypeCustom.label type,beginDate,endDate,remark";
        String[] pros = {"realName", "department", "type", "beginDate",  "endDate", "remark"};
        List<LeaveImpl> leaveList = criteriaQueryService.query(LeaveImpl.class, CriteriaQueryUtils.parseCondition(request));
        List<Map<String, Object>> data = RenderUtils.transListProp(leaveList, prosStr);
        ExcelUtils.excel(response, data, pros);
    }

}
