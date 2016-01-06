/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.AttendanceStatus;
import top.gabin.oa.web.dao.AttendanceDao;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.dto.attendance.LeaveResult;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.date.TimeUtils;
import top.gabin.oa.web.utils.mvel.MvelUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author linjiabin  on  15/12/15
 */
@Service("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    @Resource(name = "leaveService")
    private LeaveService leaveService;
    @Resource(name = "employeeService")
    private EmployeeService employeeService;
    @Resource(name = "attendanceDao")
    private AttendanceDao attendanceDao;
    @Resource(name = "businessService")
    private BusinessService businessService;
    @Resource(name = "attendanceRuleService")
    private AttendanceRuleService attendanceRuleService;
    @Resource
    private CriteriaQueryService queryService;

    @Override
    @Transactional("transactionManager")
    public boolean importAttendance(List<AttendanceImportDTO> attendanceImportDTOList) {
        Map<String, Department> cacheDepartment = new HashMap<String, Department>();
        Map<String, Employee> cacheEmployee = new HashMap<String, Employee>();
        List<Attendance> attendanceList = new ArrayList<Attendance>();
        for (AttendanceImportDTO dto : attendanceImportDTOList) {
            String realName = dto.getRealName();
            if (StringUtils.isBlank(realName)) {
                continue;
            }
            Attendance attendance = new AttendanceImpl();
            attendance.setPmTime(dto.getPmTime3());
            if (StringUtils.isBlank(attendance.getPmTime())) {
                attendance.setPmTime(dto.getPmTime2());
                if (StringUtils.isBlank(attendance.getPmTime())) {
                    attendance.setPmTime(dto.getPmTime1());
                }
                if (StringUtils.isBlank(attendance.getPmTime())) {
                    attendance.setPmTime(dto.getAmTime3());
                    if (StringUtils.isBlank(attendance.getPmTime())) {
                        attendance.setPmTime(dto.getAmTime2());
                    }
                }
            }
            attendance.setAmTime(dto.getAmTime1());
            if (StringUtils.isBlank(attendance.getAmTime())) {
                attendance.setAmTime(dto.getAmTime2());
                if (StringUtils.isBlank(attendance.getAmTime())) {
                    attendance.setAmTime(dto.getAmTime3());
                }
                if (StringUtils.isBlank(attendance.getAmTime())) {
                    attendance.setAmTime(dto.getPmTime1());
                    if (StringUtils.isBlank(attendance.getAmTime())) {
                        attendance.setAmTime(dto.getPmTime2());
                        if (StringUtils.isBlank(attendance.getAmTime())) {
                            attendance.setAmTime(dto.getPmTime3());
                        }
                    }
                }
            }
            String workDate = dto.getWorkDate();
            attendance.setWorkDateFormat(workDate);
            attendance.setWorkDate(TimeUtils.parseDate(workDate, "yyyy-MM-dd"));
            String departmentName = dto.getDepartment();
            Employee employee;
            if (cacheEmployee.containsKey(realName)) {
                employee = cacheEmployee.get(realName);
            } else {
                employee = employeeService.findByName(realName);
                if (employee == null) {
                    employee = new EmployeeImpl();
                    employee.setName(realName);
                    employee.setAttendanceCN(dto.getAttendance());
                    employeeService.persist(employee);
                }
                Department department = null;
                if (cacheDepartment.containsKey(departmentName)) {
                    department = cacheDepartment.get(departmentName);
                    employee.setDepartment(department);
                } else {
                    department = departmentService.findByName(StringUtils.trim(departmentName));
                    if (department == null) {
                        department = new DepartmentImpl();
                        department.setName(departmentName);
                        departmentService.persist(department);
                    }
                    employee.setDepartment(department);
                    cacheDepartment.put(departmentName, department);
                }
                cacheEmployee.put(realName, employee);
            }
            attendance.setEmployee(employee);
            attendanceList.add(attendance);
        }
        for (Attendance attendance : attendanceList) {
            attendanceDao.saveOrUpdate(attendance);
        }
        return true;
    }

    @Override
    @Transactional("transactionManager")
    public void clearMonth(String month) {
        attendanceDao.clearMonth(month);
    }

    @Override
    @Transactional("transactionManager")
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<AttendanceImpl> attendanceList = queryService.query(AttendanceImpl.class, condition);
            if (attendanceList == null) {
                return;
            }
            for (Attendance attendance : attendanceList) {
                attendanceDao.delete(attendance);
            }
        }
    }

    @Override
    @Transactional("transactionManager")
    public void merge(AttendanceDTO attendanceDTO) {
        if (attendanceDTO != null) {
            Attendance attendance;
            if (attendanceDTO.getId() != null) {
                attendance = attendanceDao.findById(attendanceDTO.getId());
            } else {
                attendance = new AttendanceImpl();
            }
            if (attendanceDTO.getPmTime() != null) {
                attendance.setPmTime(attendanceDTO.getPmTime());
            }
            if (attendanceDTO.getStatus() != null) {
                attendance.setStatus(AttendanceStatus.instance(attendanceDTO.getStatus()));
            }
            if (attendanceDTO.getAmTime() != null) {
                attendance.setAmTime(attendanceDTO.getAmTime());
            }
            String workDate = attendanceDTO.getWorkDate();
            if (workDate != null) {
                attendance.setWorkDateFormat(workDate);
                attendance.setWorkDate(TimeUtils.parseDate(workDate));
            }
            attendanceDao.saveOrUpdate(attendance);
        }
    }

    @Override
    public Attendance findById(Long id) {
        return attendanceDao.findById(id);
    }

    @Override
    @Transactional("transactionManager")
    public void batchSetLeaveDays(String days) {
        attendanceDao.batchSetLeaveDays(days);
    }

    @Override
    @Transactional("transactionManager")
    public void batchSetWorkDays(String days) {
        attendanceDao.batchSetWorkDays(days);
    }

    private Map<Long, Map<Long, List<Attendance>>> getAttendanceGroup(String month) {
        // 1、获取指定月份所有的考勤
        Map<String, Object> conditions = new HashMap<String, Object>();
        CriteriaCondition criteriaCondition = new CriteriaCondition(conditions);
        conditions.put("bw_workDateFormat", month);
        criteriaCondition.setSort("workDate asc");
        List<AttendanceImpl> attendanceList = queryService.query(AttendanceImpl.class, criteriaCondition);
        // 2、根据员工分组考勤数据
        Map<Long, Map<Long, List<Attendance>>> departmentGroup = new HashMap<Long, Map<Long, List<Attendance>>>();
        for (Attendance attendance : attendanceList) {
            Map<Long, List<Attendance>> employeeGroup;
            Long departmentId = attendance.getEmployee().getDepartment().getId();
            if (departmentGroup.containsKey(departmentId)) {
                employeeGroup = departmentGroup.get(departmentId);
            } else {
                employeeGroup = new HashMap<Long, List<Attendance>>();
                departmentGroup.put(departmentId, employeeGroup);
            }
            Long id = attendance.getEmployee().getId();
            List<Attendance> attendances = employeeGroup.get(id);
            if (attendances == null) {
                attendances = new ArrayList<Attendance>();
                employeeGroup.put(id, attendances);
            }
            attendances.add(attendance);
        }
        return departmentGroup;
    }

    @Override
    public int getFineMoneyBasicOfDelay() {
        return 10;
    }

    @Override
    public int getFineMoneyBasicOfLeaveEarly() {
        return 10;
    }

    /**
     * 根据月份获取考勤
     * @param month
     * @return
     */
    private List<AttendanceImpl> findAttendanceByMonth(String month) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        CriteriaCondition criteriaCondition = new CriteriaCondition(conditions);
        if (StringUtils.isNotBlank(month)) {
            conditions.put("bw_workDateFormat", month);
        }
        criteriaCondition.setSort("workDate asc");
        List<AttendanceImpl> attendanceList = queryService.query(AttendanceImpl.class, criteriaCondition);
        return attendanceList;
    }

    @Override
    public List<DepartmentAnalysisResult> buildAnalysisData(String month) {
        List<AttendanceRuleImpl> rules = attendanceRuleService.findRulesByMonth(month);
        List<DepartmentAnalysisResult> departmentAnalysisResultList = new ArrayList<DepartmentAnalysisResult>();
        Map<Long, DepartmentAnalysisResult> departmentAnalysisResultMap = new HashMap<Long, DepartmentAnalysisResult>();
        Map<Long, EmployeeAnalysisResult> employeeAnalysisResultMap = new HashMap<Long, EmployeeAnalysisResult>();
        // 1、获取指定月份所有的考勤
        List<AttendanceImpl> attendanceList = findAttendanceByMonth(month);
        AttendanceBasicRuleConfig attendanceBasicRule = businessService.getAttendanceBasicRule();
        String workFit = attendanceBasicRule.getWorkFit();
        String leaveFit = attendanceBasicRule.getLeaveFit();
        // 2、根据员工分组考勤数据
        for (Attendance attendance : attendanceList) {
            Long departmentId, employeeId;
            Employee employee = attendance.getEmployee();
            employeeId = employee.getId();
            departmentId = employee.getDepartment().getId();
            DepartmentAnalysisResult departmentAnalysisResult = departmentAnalysisResultMap.get(departmentId);
            if (departmentAnalysisResult == null) {
                departmentAnalysisResult = new DepartmentAnalysisResult();
                departmentAnalysisResultMap.put(departmentId, departmentAnalysisResult);
                departmentAnalysisResultList.add(departmentAnalysisResult);
            }
            EmployeeAnalysisResult employeeAnalysisResult = employeeAnalysisResultMap.get(employeeId);
            if (employeeAnalysisResult == null) {
                employeeAnalysisResult = new EmployeeAnalysisResult();
                employeeAnalysisResultMap.put(employeeId, employeeAnalysisResult);
                departmentAnalysisResult.add(employeeAnalysisResult);
            }
            AnalysisResult analysisResult = new AnalysisResult(attendance);
            if (rules != null && !rules.isEmpty()) {
                for (AttendanceRule rule : rules) {
                    Map<String, AttendanceRuleDetail> attendanceRuleDetailMap = rule.getAttendanceRuleDetailMap();
                    for (String key : attendanceRuleDetailMap.keySet()) {
                        AttendanceRuleDetail attendanceRuleDetail = attendanceRuleDetailMap.get(key);
                        Map<String, Object> vars = new HashMap<String, Object>();
                        vars.put("attendance", attendance);
                        if (MvelUtils.eval(attendanceRuleDetail.getRule(), vars)) {
                            analysisResult.getAttendanceRuleList().add(rule);
                        }
                    }
                }
            }
            String workDateFormat = attendance.getWorkDateFormat();
            analysisResult.setWorkFit(TimeUtils.parseDate(workDateFormat + " " + workFit));
            analysisResult.setLeaveFit(TimeUtils.parseDate(workDateFormat + " " + leaveFit));
            employeeAnalysisResult.add(analysisResult);
        }
        Map<Long, List<Leave>> leaveEmployeeMap = leaveService.getLeaveGroup(month);
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    Long id = analysisResult.getAttendance().getEmployee().getId();
                    if (leaveEmployeeMap.containsKey(id)) {
                        fillLeave(analysisResult, leaveEmployeeMap.get(id));
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }

    private void fillLeave(AnalysisResult analysisResult, List<Leave> leaveList) {
        if (leaveList != null) {
            Attendance attendance = analysisResult.getAttendance();
            String workDateFormat = attendance.getWorkDateFormat();
            AttendanceBasicRuleConfig attendanceBasicRule = businessService.getAttendanceBasicRule();
            Date amNeedFit = TimeUtils.parseDate(workDateFormat + " " + attendanceBasicRule.getWorkFit());
            Date pmNeedFit = TimeUtils.parseDate(workDateFormat + " " + attendanceBasicRule.getLeaveFit());
            // 获取请假时长
            Date tmpBeginDate = amNeedFit;
            Date tmpEndDate = pmNeedFit;
            for (Leave leave : leaveList) {
                Date beginDate = leave.getBeginDate();
                Date endDate = leave.getEndDate();
                // 请假日匹配
                if (TimeUtils.isBetween(tmpBeginDate, beginDate, endDate) || TimeUtils.isBetween(tmpEndDate, beginDate, endDate) || (beginDate.getTime() > tmpBeginDate.getTime() && endDate.getTime() < tmpEndDate.getTime())) {
                    LeaveResult leaveResult = new LeaveResult(leave);
                    analysisResult.add(leaveResult);
                }
            }

        }
    }

    /**
     * 早退限制，默认4
     * @return
     */
    @Override
    public int getLeaveEarlyLimit() {
        return 4;
    }

    /**
     * 获取迟到限制，目前默认3
     * @return
     */
    @Override
    public int getDelayLimit() {
        return 3;
    }

    @Override
    public HSSFWorkbook buildAnalysisExcel(List<DepartmentAnalysisResult> data) {
        String fileName = "analysis";
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
        for (DepartmentAnalysisResult departmentAnalysisResult : data) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    Attendance attendance = analysisResult.getAttendance();
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
                    String workDateFormat = attendance.getWorkDateFormat();
                    setValue(row1, 3, workDateFormat);
                    // 填充上午打卡时间
                    setValue(row1, 4, attendance.getAmTime());
                    // 填充下午打开时间
                    setValue(row1, 5, attendance.getPmTime());
                    if (AttendanceStatus.LEAVE.equals(attendance.getStatus())) {
                        row1.getCell(2).setCellStyle(getBlueFontStyle(workbook));
                    } else {
                        HSSFCellStyle workFitStyle = null;
                        HSSFCellStyle leaveFitStyle = null;
                        if (analysisResult.isYesterdayWorkDelay()) {
                            workFitStyle = getGreenFillStyle(workbook);
                        }
                        if (analysisResult.isWorkBad()) {
                            setValue(row1, 12, 7.5);
                            workFitStyle = getYellowFillStyle(workbook);
                            leaveFitStyle = getYellowFillStyle(workbook);
                        }
                        if (analysisResult.isImpunityWorkDelay()) {
                            workFitStyle = getBlueFillStyle(workbook);
                        } else {
                            // 迟到
                            int amMinutes = analysisResult.getWorkDelayMinutes();
                            if (amMinutes > 0) {
                                setValue(row1, 10, amMinutes);
                                workFitStyle = getYellowFillStyle(workbook);
                            }
                        }
                        if (workFitStyle != null) {
                            row1.getCell(4).setCellStyle(workFitStyle);
                        }
                        if (analysisResult.isImpunityLeaveEarly()) {
                            leaveFitStyle = getBlueFillStyle(workbook);
                        } else {
                            // 早退
                            int pmMinutes = analysisResult.getLeaveEarlyMinutes();
                            if (pmMinutes > 0) {
                                setValue(row1, 11, pmMinutes);
                                leaveFitStyle = getYellowFillStyle(workbook);
                            }
                        }
                        if (leaveFitStyle != null) {
                            row1.getCell(5).setCellStyle(leaveFitStyle);
                        }
                        List<LeaveResult> leaveList = analysisResult.getLeaveList();
                        String remark = StringUtils.isBlank(analysisResult.getRemark()) ? "" : analysisResult.getRemark();
                        row1.createCell(6);
                        row1.createCell(7);
                        row1.createCell(8);
                        for (LeaveResult leaveResult : leaveList) {
                            long minutes = leaveResult.getLeaveMinutes();
                            double hours =  minutes / 60D;
                            String times = hours + "h";
                            Leave leave = leaveResult.getLeave();
                            switch (leave.getType()) {
                                case NORMAL_LEAVE:
                                    setValue(row1, 6, times);
                                    break;
                                case SICK_LEAVE:
                                    setValue(row1, 7, times);
                                    break;
                                case OFF_LEAVE:
                                    setValue(row1, 8, times);
                                    break;
                                case OUT_LEAVE:
                                case FUNERAL_LEAVE:
                                case YEAR_LEAVE:
                                case MATERNITY_LEAVE:
                                case MARRY_LEAVE:
                                    remark += leave.getType().getLabel() + times;
                                    break;
                            }
                        }
                        setValue(row1, 9, remark);
                    }
                }
                i++;
            }
        }
        _font = null;
        return workbook;
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
        cellStyle.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    private static HSSFCellStyle getYellowFillStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    private static HSSFFont _font;

    private static HSSFCellStyle getBlueFontStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.BLACK.index);
        //生成标题字体
        if (_font == null) {
            _font = workbook.createFont();
            _font.setColor(HSSFColor.BLUE.index);
        }
        HSSFFont font = _font;
        //字体应用
        cellStyle.setFont(font);
        return cellStyle;
    }

    @Transactional("transactionManager")
    @Override
    public void batchDeleteByEmployeeIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_employee.id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<AttendanceImpl> attendanceList = queryService.query(AttendanceImpl.class, condition);
            if (attendanceList == null) {
                return;
            }
            for (Attendance attendance : attendanceList) {
                attendanceDao.delete(attendance);
            }
        }
    }
}
