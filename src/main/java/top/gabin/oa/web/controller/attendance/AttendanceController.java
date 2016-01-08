/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceController.
 */
package top.gabin.oa.web.controller.attendance;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.AttendanceImpl;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.service.AttendanceService;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.flow.attendance.execute.Execute;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.date.TimeUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * -------------------------------
 * --------- 考勤 Controller------
 * -------------------------------
 * @author linjiabin  on  15/12/14
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    @Resource(name = "attendanceService")
    private AttendanceService attendanceService;
    @Resource(name = "attendanceWorkFlow")
    private Execute execute;
    private String dir = "attendance";

    @RequestMapping("/list")
    public String list(Model model) {
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        return  dir + "/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            Attendance attendance = attendanceService.findById(id);
            model.addAttribute("entity", attendance);
        }
        return  dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> grid(HttpServletRequest request) {
        return criteriaQueryService.queryPage(AttendanceImpl.class, request, "id,workDate,amTime,pmTime,employee.name employee,employee.department.name department,yesterdayPm yesterday,status.label status");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(AttendanceDTO attendanceDTO) {
        attendanceService.merge(attendanceDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> delete(String ids) {
        attendanceService.batchDelete(ids);
        return RenderUtils.SUCCESS_RESULT;
    }


    @RequestMapping(value = "importView", method = RequestMethod.GET)
    public String importView() {
        return dir + "/import";
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public @ResponseBody Map productImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            ImportExcel importExcel = new ImportExcel(file, 3, 0);
            List<AttendanceImportDTO> dataList = importExcel.getDataList(AttendanceImportDTO.class);
            attendanceService.importAttendance(dataList);
            return RenderUtils.SUCCESS_RESULT;
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("导入数据有异常!");
        }
    }


    @RequestMapping(value = "dropView", method = RequestMethod.GET)
    public String dropView() {
        return dir + "/drop";
    }

    @RequestMapping(value = "dropMonth", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> dropMonth(String month) {
        attendanceService.clearMonth(month);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "getDays", method = RequestMethod.POST)
    public @ResponseBody List getDays(String month) {
        Date start = TimeUtils.parseDate(month + "-01", "yyyy-MM-dd");
        Date end = DateUtils.addMonths(start, 1);
        return TimeUtils.getDays(start, end);
    }

    @RequestMapping(value = "unsetDays", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> unsetDays(String days) {
        attendanceService.batchSetLeaveDays(days);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "setDays", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> setDays(String days) {
        attendanceService.batchSetWorkDays(days);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "analysis", method = RequestMethod.GET)
    public void analysis(String month, HttpServletResponse response) {
        try {
            List<DepartmentAnalysisResult> data = execute.execute(month);
            HSSFWorkbook hssfWorkbook = attendanceService.buildAnalysisExcel(data);
            RenderUtils.renderExcel(response, hssfWorkbook, "analysis_" + month);
        } catch (Exception e) {
            throw new RuntimeException("导出Excel文件出错", e);
        }
    }

}
