/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceController.
 */
package top.gabin.oa.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.gabin.oa.web.constant.AttendanceStatus;
import top.gabin.oa.web.dto.*;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.AttendanceImpl;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.service.AttendanceService;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.LeaveService;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.date.TimeUtils;
import top.gabin.oa.web.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author linjiabin  on  15/12/14
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    @Resource(name = "attendanceService")
    private AttendanceService attendanceService;
    @Resource(name = "leaveService")
    private LeaveService leaveService;
    private static int maxSize = 10 * 1024 * 1024;
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
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        Map<String, Object> conditions = criteriaCondition.getConditions();
        String workDate = request.getParameter("workDate");
        if (StringUtils.isNotBlank(workDate)) {
            conditions.put("ge_workDate", workDate + "-01 00:00:00");
            conditions.put("le_workDate", workDate + "-31 00:00:00");
        }
        PageDTO<AttendanceImpl> attendancePageDTO = queryService.queryPage(AttendanceImpl.class, criteriaCondition);
        return RenderUtils.filterPageDataResult(attendancePageDTO, "id,workDate,amTime,pmTime,employee.name employee,employee.department.name department,yesterdayPm yesterday,status.label status");
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
            attendanceService.importAttendance(dataList);
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
        attendanceService.clearMonth(month);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "getDays", method = RequestMethod.POST)
    public @ResponseBody List getDays(String month) {
        Date start = TimeUtils.parseDate(month + "-01", "yyyy-MM-dd");
        Date end = DateUtils.addMonths(start, 1);
        List<Map<String, Object>> dateArr = new ArrayList<Map<String, Object>>();
        while(start.before(end)) {
            String date = TimeUtils.format(start, "yyyy-MM-dd");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", date);
            map.put("value", date);
            map.put("text", date);
            dateArr.add(map);
            start = DateUtils.addDays(start, 1);
        }
        return dateArr;
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
    public void analysis(HttpServletResponse response, String month) {
        String fileName = month + "_analysis";
        // 1、获取考勤数据
        Map<Long, Map<Long, List<Attendance>>> departmentGroup =  attendanceService.getAttendanceGroup(month);
        // 2、获取请假数据
        // 如果此人当月有异常情况
        Map<Long, List<Leave>> leaveMap = leaveService.getLeaveGroup(month);
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(fileName);
        //设置表格默认列宽度为18个字节
        sheet.setDefaultColumnWidth(10);
        // 合并第一行
        CellRangeAddress cra = new CellRangeAddress(0, 1, 0, 12);
        //在sheet里增加合并单元格
        sheet.addMergedRegion(cra);
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cellTitle = row0.createCell(0);
        cellTitle.setCellStyle(getHeadFontStyle(workbook));
        setValue(row0, 0, "说明：蓝色填充为3次9:15前迟到机会，绿色填充为外出、加班晚到等未计考勤情况，黄色填充为违反制度情况。");
        String[] headers = new String[]{"部门", "姓名", "周期", "日期", "上班", "下班", "事假", "病假", "调休", "备注", "迟到", "早退", "旷工"};
        //产生表格标题行
        HSSFRow row = sheet.createRow(2);
        for (int i = 0; i < headers.length; i++) {
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            setValue(row, i, text);
        }
        int i = 3;
        // 3、处理数据
        Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> attendanceGroup = leaveService.workFlow(departmentGroup, leaveMap);
        attendanceGroup = attendanceService.yesterdayWorkDelayWorkFlow(attendanceGroup);
        Map<Long, Map<Long, EmployeeAttendanceDTO>> data = attendanceService.dealAttendanceRule(attendanceGroup);
        for (Long key : data.keySet()) {
            Map<Long, EmployeeAttendanceDTO> employeeAttendanceDTOMap = data.get(key);
            for (Long key0 : employeeAttendanceDTOMap.keySet()) {
                EmployeeAttendanceDTO employeeAttendanceDTO = employeeAttendanceDTOMap.get(key0);
                List<AttendanceWorkFlowDTO> attendances = employeeAttendanceDTO.getAttendanceWorkFlowDTOList();
                for (AttendanceWorkFlowDTO attendanceWorkFlowDTO : attendances) {
                    Attendance attendance = attendanceWorkFlowDTO.getAttendance();
                    HSSFRow row1 = sheet.createRow(i++);
                    // 填充部门
                    String departmentName = attendance.getEmployee().getDepartment().getName();
                    setValue(row1, 0, departmentName);
                    // 填充员工
                    String employeeName = attendance.getEmployee().getName();
                    setValue(row1, 1, employeeName);
                    Date workDate = attendance.getWorkDate();
                    // 填充周几
                    setValue(row1, 2, TimeUtils.getDay(workDate));
                    // 填充日期
                    String workDateFormat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    setValue(row1, 3, workDateFormat);
                    // 填充上午打卡时间
                    setValue(row1, 4, attendance.getAmTime());
                    if (attendanceWorkFlowDTO.isAmLimit()) {
                        row1.getCell(4).setCellStyle(getBlueFillStyle(workbook));
                    }
                    // 填充下午打开时间
                    setValue(row1, 5, attendance.getPmTime());
                    if (attendanceWorkFlowDTO.isPmLimit()) {
                        row1.getCell(5).setCellStyle(getBlueFillStyle(workbook));
                    }
                    if (AttendanceStatus.LEAVE.equals(attendance.getStatus())) {
                        row1.getCell(2).setCellStyle(getBlueFontStyle(workbook));
                    } else {
                        // 旷工
                        if (attendanceWorkFlowDTO.isHasNowWork()) {
                            setValue(row1, 12, 7.5);
                            row1.getCell(4).setCellStyle(getYellowFillStyle(workbook));
                            row1.getCell(5).setCellStyle(getYellowFillStyle(workbook));
                        }
                        if (attendanceWorkFlowDTO.isYesterdayWorkDelay()) {
                            row1.getCell(4).setCellStyle(getGreenFillStyle(workbook));
                        }
                        // 迟到
                        int amMinutes = attendanceWorkFlowDTO.getAmMinutes();
                        if (amMinutes > 0) {
                            setValue(row1, 10, amMinutes);
                            row1.getCell(4).setCellStyle(getYellowFillStyle(workbook));
                        }
                        // 早退
                        int pmMinutes = attendanceWorkFlowDTO.getPmMinutes();
                        if (pmMinutes > 0) {
                            setValue(row1, 11, pmMinutes);
                            row1.getCell(5).setCellStyle(getYellowFillStyle(workbook));
                        }
                        List<Leave> leaveList = attendanceWorkFlowDTO.getLeaveList();
                        String remark = attendanceWorkFlowDTO.getRemark();
                        if (leaveList != null && leaveList.size() > 0) {
                            if (leaveList.size() == 1) {
                                Double hours = attendanceWorkFlowDTO.getLeaveTimes() / 60D;
                                Leave leave = leaveList.get(0);
                                switch (leave.getType()) {
                                    case NORMAL_LEAVE:
                                        setValue(row1, 6, hours);
                                        break;
                                    case SICK_LEAVE:
                                        setValue(row1, 7, hours);
                                        break;
                                    case OFF_LEAVE:
                                        setValue(row1, 8, hours);
                                        break;
                                    case OUT_LEAVE:
                                    case FUNERAL_LEAVE:
                                    case YEAR_LEAVE:
                                    case MATERNITY_LEAVE:
                                    case MARRY_LEAVE:
                                        remark += leave.getType().getLabel() + hours;
                                        break;
                                }
                            } else {
                                row1.createCell(6);
                                row1.createCell(7);
                                row1.createCell(8);
                                for (Leave leave : leaveList) {
                                    Double hours = TimeUtils.getMinutes(leave.getEndDate(), leave.getBeginDate()) / 60D;
                                    switch (leave.getType()) {
                                        case NORMAL_LEAVE:
                                            setValue(row1, 6, hours);
                                            break;
                                        case SICK_LEAVE:
                                            setValue(row1, 7, hours);
                                            break;
                                        case OFF_LEAVE:
                                            setValue(row1, 8, hours);
                                            break;
                                        case OUT_LEAVE:
                                        case FUNERAL_LEAVE:
                                        case YEAR_LEAVE:
                                        case MATERNITY_LEAVE:
                                        case MARRY_LEAVE:
                                            remark += leave.getType().getLabel() + hours;
                                            break;
                                    }
                                }
                            }
                            setValue(row1, 9, remark);
                        }
                    }
                }
            }
            i++;
        }
        response.setContentType("application/vnd.ms-excel;");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName +".xls");
        try {
            OutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("导出Excel文件出错", e);
        }
    }

    private void setValue(HSSFRow row1, int idx, Object o) {
        HSSFCell cell = row1.getCell(idx);
        if (cell == null) {
            cell = row1.createCell(idx);
        }
        String content = o == null ? "" : o.toString();
        cell.setCellValue(content);
    }


    private static HSSFCellStyle getHeadFontStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //生成标题字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        //字体应用
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static HSSFCellStyle getBlueFillStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }

    private static HSSFCellStyle getGreenFillStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    private static HSSFCellStyle getYellowFillStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    private static HSSFCellStyle getBlueFontStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);
        //生成标题字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLUE.index);
        //字体应用
        cellStyle.setFont(font);
        return cellStyle;
    }

}
