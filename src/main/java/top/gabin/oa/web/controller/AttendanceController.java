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
        sheet.setDefaultColumnWidth(18);
        // 合并第一行
        CellRangeAddress cra = new CellRangeAddress(0, 1, 0, 12);
        //在sheet里增加合并单元格
        sheet.addMergedRegion(cra);
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cellTitle = row0.createCell(0);
        cellTitle.setCellStyle(getHeadFontStyle(workbook));
        cellTitle.setCellValue("说明：蓝色填充为3次9:15前迟到机会，绿色填充为外出、加班晚到等未计考勤情况，黄色填充为违反制度情况。 ");
        String[] headers = new String[]{"部门", "姓名", "周期", "日期", "上班", "下班", "事假", "病假", "调休", "备注", "迟到", "早退", "旷工"};
        //产生表格标题行
        HSSFRow row = sheet.createRow(2);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);//把数据放到单元格中
        }
        int i = 3;
        // 3、处理数据
        Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> attendanceGroup = leaveService.workFlow(departmentGroup, leaveMap);
        attendanceGroup = attendanceService.yesterdayWorkDelayWorkFlow(attendanceGroup);
        for (Long key : attendanceGroup.keySet()) {
            Map<Long, List<AttendanceWorkFlowDTO>> employeeGroup = attendanceGroup.get(key);
            Attendance a = null;
            for (Long key0 : employeeGroup.keySet()) {
                List<AttendanceWorkFlowDTO> attendances = employeeGroup.get(key0);
                // 迟到次数
                int $delayTimes = 0;
                // 上午乐捐
                int $applyMoneyAm = 0;
                // 下午早退
                int $overlayTimes = 0;
                // 下午乐捐
                int $applyMoneyPm = 0;
                for (AttendanceWorkFlowDTO attendanceWorkFlowDTO : attendances) {
                    Attendance attendance = attendanceWorkFlowDTO.getAttendance();
                    HSSFRow row1 = sheet.createRow(i++);
                    // 填充部门
                    String departmentName = attendance.getEmployee().getDepartment().getName();
                    row1.createCell(0).setCellValue(departmentName);
                    // 填充员工
                    String employeeName = attendance.getEmployee().getName();
                    row1.createCell(1).setCellValue(employeeName);
                    Date workDate = attendance.getWorkDate();
                    // 填充周几
                    row1.createCell(2).setCellValue(TimeUtils.getDay(workDate));
                    // 填充日期
                    String workDateFromat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    row1.createCell(3).setCellValue(workDateFromat);
                    // 填充上午打卡时间
                    row1.createCell(4).setCellValue(attendance.getAmTime());
                    // 填充下午打开时间
                    row1.createCell(5).setCellValue(attendance.getPmTime());
                    if (AttendanceStatus.LEAVE.equals(attendance.getStatus())) {
                        row1.getCell(2).setCellStyle(getBlueFontStyle(workbook));
                    } else {
                        List<Leave> leaveList = attendanceWorkFlowDTO.getLeaveList();
                        String remark = "";
                        if (leaveList != null && leaveList.size() > 0) {
                            if (leaveList.size() == 1) {
                                Double hours = attendanceWorkFlowDTO.getLeaveTimes() / 60D;
                                Leave leave = leaveList.get(0);
                                switch (leave.getType()) {
                                    case NORMAL_LEAVE:
                                        row1.createCell(6).setCellValue(hours);
                                        break;
                                    case SICK_LEAVE:
                                        row1.createCell(7).setCellValue(hours);
                                        break;
                                    case OFF_LEAVE:
                                        row1.createCell(8).setCellValue(hours);
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
                                            row1.getCell(6).setCellValue(hours);
                                            break;
                                        case SICK_LEAVE:
                                            row1.getCell(7).setCellValue(hours);
                                            break;
                                        case OFF_LEAVE:
                                            row1.getCell(8).setCellValue(hours);
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
                            row1.createCell(9).setCellValue(remark);
                        }
                        a = attendance;
                    }
                }
            }
            i++;
            a = null;
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

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public @ResponseBody Map analysisTest(HttpServletResponse response, String month) {
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
        sheet.setDefaultColumnWidth(18);
        // 合并第一行
        CellRangeAddress cra = new CellRangeAddress(0, 1, 0, 12);
        //在sheet里增加合并单元格
        sheet.addMergedRegion(cra);
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cellTitle = row0.createCell(0);
        cellTitle.setCellStyle(getHeadFontStyle(workbook));
        cellTitle.setCellValue("说明：蓝色填充为3次9:15前迟到机会，绿色填充为外出、加班晚到等未计考勤情况，黄色填充为违反制度情况。 ");
        String[] headers = new String[]{"部门", "姓名", "周期", "日期", "上班", "下班", "事假", "病假", "调休", "备注", "迟到", "早退", "旷工"};
        //产生表格标题行
        HSSFRow row = sheet.createRow(2);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);//把数据放到单元格中
        }
        int i = 3;
        // 3、处理数据
        Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> attendanceGroup = leaveService.workFlow(departmentGroup, leaveMap);
        attendanceGroup = attendanceService.yesterdayWorkDelayWorkFlow(attendanceGroup);
        for (Long key : attendanceGroup.keySet()) {
            Map<Long, List<AttendanceWorkFlowDTO>> employeeGroup = attendanceGroup.get(key);
            Attendance a = null;
            for (Long key0 : employeeGroup.keySet()) {
                List<AttendanceWorkFlowDTO> attendances = employeeGroup.get(key0);
                // 迟到次数
                int $delayTimes = 0;
                // 上午乐捐
                int $applyMoneyAm = 0;
                // 下午早退
                int $overlayTimes = 0;
                // 下午乐捐
                int $applyMoneyPm = 0;
                for (AttendanceWorkFlowDTO attendanceWorkFlowDTO : attendances) {
                    Attendance attendance = attendanceWorkFlowDTO.getAttendance();
                    HSSFRow row1 = sheet.createRow(i++);
                    // 填充部门
                    String departmentName = attendance.getEmployee().getDepartment().getName();
                    row1.createCell(0).setCellValue(departmentName);
                    // 填充员工
                    String employeeName = attendance.getEmployee().getName();
                    row1.createCell(1).setCellValue(employeeName);
                    Date workDate = attendance.getWorkDate();
                    // 填充周几
                    row1.createCell(2).setCellValue(TimeUtils.getDay(workDate));
                    // 填充日期
                    String workDateFromat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    row1.createCell(3).setCellValue(workDateFromat);
                    // 填充上午打卡时间
                    String amTime = attendance.getAmTime();
                    row1.createCell(4).setCellValue(amTime);
                    // 填充下午打开时间
                    String pmTime = attendance.getPmTime();
                    row1.createCell(5).setCellValue(pmTime);
                    if (AttendanceStatus.LEAVE.equals(attendance.getStatus())) {
                        row1.getCell(2).setCellStyle(getBlueFontStyle(workbook));
                    } else {
                        if (StringUtils.isBlank(pmTime) && StringUtils.isBlank(amTime)) {
                            row1.createCell(12).setCellValue(7.5);
                        }
                        List<Leave> leaveList = attendanceWorkFlowDTO.getLeaveList();
                        String remark = "";
                        if (leaveList != null && leaveList.size() > 0) {
                            if (leaveList.size() == 1) {
                                Double hours = attendanceWorkFlowDTO.getLeaveTimes() / 60D;
                                Leave leave = leaveList.get(0);
                                switch (leave.getType()) {
                                    case NORMAL_LEAVE:
                                        row1.createCell(6).setCellValue(hours);
                                        break;
                                    case SICK_LEAVE:
                                        row1.createCell(7).setCellValue(hours);
                                        break;
                                    case OFF_LEAVE:
                                        row1.createCell(8).setCellValue(hours);
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
                                            row1.getCell(6).setCellValue(hours);
                                            break;
                                        case SICK_LEAVE:
                                            row1.getCell(7).setCellValue(hours);
                                            break;
                                        case OFF_LEAVE:
                                            row1.getCell(8).setCellValue(hours);
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
                            row1.createCell(9).setCellValue(remark);
                        }
                        a = attendance;
                    }
                }
            }
            i++;
            a = null;
        }
        return RenderUtils.SUCCESS_RESULT;
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
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //字体应用
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static HSSFCellStyle getBlueFillStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        //生成标题字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //字体应用
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static HSSFCellStyle getBlueFontStyle(HSSFWorkbook workbook) {
//        if (blueFontStyle != null) {
//            return blueFontStyle;
//        }
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //生成标题字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLUE.index);
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //字体应用
        cellStyle.setFont(font);
        return cellStyle;
    }

}
