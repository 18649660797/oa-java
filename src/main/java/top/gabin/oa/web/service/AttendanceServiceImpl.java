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
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.date.TimeUtils;

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
            attendance.setWorkDate(TimeUtils.parseDate(dto.getWorkDate(), "yyyy-MM-dd"));
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
            if (attendanceDTO.getWorkDate() != null) {
                attendance.setWorkDate(TimeUtils.parseDate(attendanceDTO.getWorkDate()));
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
        if (StringUtils.isNotBlank(month)) {
            conditions.put("ge_workDate", month + "-01 00:00:00");
            conditions.put("le_workDate", month + "-31 00:00:00");
        }
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

    @Override
    public List<DepartmentAnalysisResult> buildAnalysisDataByMonth(String month) {
        // 1、获取考勤数据
        Map<Long, Map<Long, List<Attendance>>> attendanceDepartmentMap =  getAttendanceGroup(month);
        // 2、获取请假数据
        Map<Long, List<Leave>> leaveEmployeeMap = leaveService.getLeaveGroup(month);
        Map<Long, Map<Long, List<AnalysisResult>>> analysisResultMap = new HashMap<Long, Map<Long, List<AnalysisResult>>>();
        for (Long key : attendanceDepartmentMap.keySet()) {
            Map<Long, List<AnalysisResult>>  LeaveWorkFlowGroup = analysisResultMap.get(key);
            if (LeaveWorkFlowGroup == null || LeaveWorkFlowGroup.isEmpty()) {
                LeaveWorkFlowGroup = new HashMap<Long, List<AnalysisResult>>();
            }
            Map<Long, List<Attendance>> employeeGroup = attendanceDepartmentMap.get(key);
            for (Long key0 : employeeGroup.keySet()) {
                List<AnalysisResult> attendanceWorkFlowDTOList = LeaveWorkFlowGroup.get(key0);
                if (attendanceWorkFlowDTOList == null || attendanceWorkFlowDTOList.isEmpty()) {
                    attendanceWorkFlowDTOList = new ArrayList<AnalysisResult>();
                }
                List<Attendance> attendances = employeeGroup.get(key0);
                for (Attendance attendance : attendances) {
                    AnalysisResult attendanceWorkFlowDTO = new AnalysisResult();
                    attendanceWorkFlowDTO.setAttendance(attendance);
                    Long id = attendance.getEmployee().getId();
                    String workDateFormat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    // 上午应打卡时间
                    Date amNeedFit = TimeUtils.parseDate(workDateFormat + " 09:00:00");
                    Date pmNeedFit = TimeUtils.parseDate(workDateFormat + " 18:00:00");
                    // 获取当天的请假记录
                    if (leaveEmployeeMap.containsKey(id)) {
                        List<Leave> leaveList = leaveEmployeeMap.get(id);
                        if (leaveList != null) {
                            // 获取请假时长
                            Date tmpBeginDate = amNeedFit;
                            Date tmpEndDate = pmNeedFit;
                            List<Leave> tmpLeaveList = new ArrayList<Leave>();
                            for (Leave leave : leaveList) {
                                Date beginDate = leave.getBeginDate();
                                Date endDate = leave.getEndDate();
                                if (TimeUtils.isBetween(tmpBeginDate, beginDate, endDate) || TimeUtils.isBetween(tmpEndDate, beginDate, endDate)) {
                                    tmpLeaveList.add(leave);
                                    attendanceWorkFlowDTO.getLeaveList().add(leave);
                                }
                            }
                            if (!tmpLeaveList.isEmpty()) {
                                Collections.sort(tmpLeaveList, new Comparator<Leave>() {
                                    @Override
                                    public int compare(Leave o1, Leave o2) {
                                        return (int) (o1.getBeginDate().getTime() - o2.getBeginDate().getTime());
                                    }
                                });
                                for (Leave leave : tmpLeaveList) {
                                    Date beginDate = leave.getBeginDate();
                                    Date endDate = leave.getEndDate();
                                    long delayTimes = 0;
                                    if (TimeUtils.beforeOrEqual(beginDate, tmpBeginDate) && TimeUtils.afterOrEqual(endDate, tmpEndDate)) {
                                        attendanceWorkFlowDTO.setLeaveMinutes(450);
                                        attendanceWorkFlowDTO.setIsLeaveDay(true);
                                    } else if (TimeUtils.beforeOrEqual(beginDate, tmpBeginDate)) { // 请假开始时间在上午需要打卡的时刻或之前
                                        // 请假结束时间在上午需要打卡之后
                                        if (endDate.after(tmpBeginDate)) {
                                            // 如果请假结束时间在下午需要打卡之前
                                            if (endDate.before(tmpEndDate)) {
                                                tmpBeginDate = endDate;
                                            }
                                        }
                                        delayTimes = TimeUtils.getMinutes(endDate, TimeUtils.parseDate(workDateFormat + " 09:00:00"));
                                    } else if (TimeUtils.afterOrEqual(endDate, tmpEndDate)) { // 请假结束时间在下午需要打卡的时刻或之后
                                        // 请假开始时间在上午需要打卡之前
                                        if (beginDate.before(tmpEndDate)) {
                                            if (beginDate.after(tmpBeginDate)) {
                                                tmpEndDate = beginDate;
                                            }
                                        }
                                        delayTimes = TimeUtils.getMinutes(TimeUtils.parseDate(workDateFormat + " 18:00:00"), beginDate);
                                    } else {
                                        delayTimes = TimeUtils.getMinutes(endDate, beginDate);
                                    }
                                    if (tmpBeginDate.getTime() == TimeUtils.parseDate(workDateFormat + " 12:00:00").getTime()) {
                                        tmpBeginDate =  TimeUtils.parseDate(workDateFormat + " 13:30:00");
                                    }
                                    attendanceWorkFlowDTO.setWorkFit(tmpBeginDate);
                                    if (tmpEndDate.getTime() == TimeUtils.parseDate(workDateFormat + " 13:30:00").getTime()) {
                                        tmpEndDate =  TimeUtils.parseDate(workDateFormat + " 12:00:00");
                                    }
                                    attendanceWorkFlowDTO.setLeaveFit(tmpEndDate);
                                    if (!attendanceWorkFlowDTO.isLeaveDay()) {
                                        if (TimeUtils.beforeOrEqual(beginDate, TimeUtils.parseDate(workDateFormat + " 12:00:00")) &&  TimeUtils.afterOrEqual(endDate, TimeUtils.parseDate(workDateFormat + " 13:30:00"))) {
                                            delayTimes = delayTimes - 90;
                                        }
                                        attendanceWorkFlowDTO.setLeaveMinutes((int) delayTimes);
                                    }
                                }
                            }
                        }
                    }
                    attendanceWorkFlowDTOList.add(attendanceWorkFlowDTO);
                }
                LeaveWorkFlowGroup.put(key0, attendanceWorkFlowDTOList);
            }
            analysisResultMap.put(key, LeaveWorkFlowGroup);
        }
        List<DepartmentAnalysisResult> departmentAnalysisResultList = new ArrayList<DepartmentAnalysisResult>();
        for (Long departmentId : analysisResultMap.keySet()) {
            DepartmentAnalysisResult departmentAnalysisResult = new DepartmentAnalysisResult();
            departmentAnalysisResult.setId(departmentId);
            departmentAnalysisResultList.add(departmentAnalysisResult);
            Map<Long, List<AnalysisResult>> employeeAnalysisMap = analysisResultMap.get(departmentId);
            for (Long employeeId : employeeAnalysisMap.keySet()) {
                List<AnalysisResult> analysisResults = employeeAnalysisMap.get(employeeId);
                EmployeeAnalysisResult employeeAnalysisResult = new EmployeeAnalysisResult();
                employeeAnalysisResult.setAnalysisResultList(analysisResults);
                employeeAnalysisResult.setId(employeeId);
                departmentAnalysisResult.add(employeeAnalysisResult);
            }
        }
        return departmentAnalysisResultList;
    }

    @Override
    public List<DepartmentAnalysisResult> yesterdayWorkDelayWorkFlow(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                Attendance yesterday = null;
                List<AnalysisResult> analysisResults = employeeAnalysisResult.getAnalysisResultList();
                for (AnalysisResult analysisResult : analysisResults) {
                    Attendance attendance = analysisResult.getAttendance();
                    if (yesterday != null && StringUtils.isNotBlank(yesterday.getPmTime())) {
                        String pmTime = yesterday.getPmTime();
                        Date pmDate = TimeUtils.parseDate("2015-01-01 " + pmTime + ":00");
                        if (TimeUtils.afterOrEqual(pmDate, TimeUtils.parseDate("2015-01-01 21:30:00"))) {
                            Date amNeedFitTime = analysisResult.getWorkFit();
                            Date workDate = attendance.getWorkDate();
                            String format = TimeUtils.format(workDate, "yyyy-MM-dd 10:00:00");
                            Date date = TimeUtils.parseDate(format);
                            if (amNeedFitTime == null || amNeedFitTime.before(date)) {
                                analysisResult.setWorkFit(date);
                                analysisResult.setYesterdayWorkDelay(true);
                            }
                        }
                    }
                    yesterday = attendance;
                }
            }
        }
        return departmentAnalysisResultList;
    }

    @Override
    public List<DepartmentAnalysisResult> dealRuleWorkFlow(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        int fineMoneyBasicOfDelay = getFineMoneyBasicOfDelay();
        int fineMoneyBasicOfLeaveEarly = getFineMoneyBasicOfLeaveEarly();
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                List<AnalysisResult> analysisResults = employeeAnalysisResult.getAnalysisResultList();
                for (AnalysisResult analysisResult : analysisResults) {
                    Attendance attendance = analysisResult.getAttendance();
                    Date workDate = attendance.getWorkDate();
                    String amTime = attendance.getAmTime();
                    Date amDate = StringUtils.isBlank(amTime) ? null : TimeUtils.parseDate(TimeUtils.format(workDate, "yyyy-MM-dd " + amTime + ":00"));
                    String pmTime = attendance.getPmTime();
                    Date pmDate = StringUtils.isBlank(pmTime) ? null : TimeUtils.parseDate(TimeUtils.format(workDate, "yyyy-MM-dd " + pmTime + ":00"));
                    if (amDate == null && pmDate == null && !analysisResult.isLeaveDay()) {
                        analysisResult.setWorkBad(true);
                    }
                    if (!analysisResult.isLeaveDay()) {
                        Date amNeedFitTime = analysisResult.getWorkFit();
                        Date pmNeedFitTime = analysisResult.getLeaveFit();
                        String remark = "";
                        // 如果迟到
                        if (amDate != null && amNeedFitTime != null && amDate.after(amNeedFitTime)) {
                            int delaySeconds = employeeAnalysisResult.getDelaySeconds();
                            long minutes = TimeUtils.getMinutes(amDate, amNeedFitTime);
                            analysisResult.setWorkDelayMinutes((int) minutes);
                            // 十五分钟内
                            if (minutes <= 15 && delaySeconds < getDelayLimit()) {
                                analysisResult.setImpunityWorkDelay(true);
                            } else {
                                // 如果超过15分钟
                                int amMoney = employeeAnalysisResult.getDelayMoney();
                                if (minutes <= 30) {
                                    amMoney += fineMoneyBasicOfDelay;
                                    remark += "迟到扣除" + amMoney + "元工资;";
                                } else if (minutes > 30 && minutes <= 60) {
                                    remark +=  "迟到扣除1h工资;";
                                } else if (minutes > 60 && minutes <= 180) {
                                    remark += "迟到扣除3h工资;";
                                } else if (minutes > 180) {
                                    remark += "迟到扣除1天工资;";
                                }
                                analysisResult.setLightFineWorkDelay(amMoney);
                                employeeAnalysisResult.setDelayMoney(amMoney);
                            }
                            employeeAnalysisResult.setDelaySeconds(delaySeconds + 1);
                        }
                        // 如果早退
                        if (pmDate != null && pmNeedFitTime != null && pmDate.before(pmNeedFitTime)) {
                            int goQuickSeconds = employeeAnalysisResult.getLeaveEarlySeconds();
                            if (goQuickSeconds < getLeaveEarlyLimit()) {
                                analysisResult.setImpunityLeaveEarly(true);
                                remark += "下班补卡;";
                            } else {
                                int pmMoney = employeeAnalysisResult.getLeaveEarlyMoney();
                                pmMoney += fineMoneyBasicOfLeaveEarly;
                                analysisResult.setFineLeaveEarly(pmMoney);
                                employeeAnalysisResult.setLeaveEarlyMoney(pmMoney);
                                remark += "早退扣除" + pmMoney + "元工资;";
                            }
                            long minutes = TimeUtils.getMinutes(pmNeedFitTime, pmDate);
                            analysisResult.setLeaveEarlyMinutes((int) minutes);
                            employeeAnalysisResult.setLeaveEarlySeconds(goQuickSeconds + 1);
                        }
                        analysisResult.setRemark(remark);
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }

    /**
     * 早退限制，默认4
     * @return
     */
    private int getLeaveEarlyLimit() {
        return 4;
    }

    /**
     * 获取迟到限制，目前默认3
     * @return
     */
    private int getDelayLimit() {
        return 3;
    }

    @Override
    public List<DepartmentAnalysisResult> analysisDataByMonth(String month) {
        List<DepartmentAnalysisResult> departmentAnalysisResultList = buildAnalysisDataByMonth(month);
        departmentAnalysisResultList = yesterdayWorkDelayWorkFlow(departmentAnalysisResultList);
        departmentAnalysisResultList = dealRuleWorkFlow(departmentAnalysisResultList);
        return departmentAnalysisResultList;
    }

    @Override
    public HSSFWorkbook buildAnalysisExcel(String month) {
        String fileName = month + "_analysis";
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
        List<DepartmentAnalysisResult> data = analysisDataByMonth(month);
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
                    String workDateFormat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    setValue(row1, 3, workDateFormat);
                    // 填充上午打卡时间
                    setValue(row1, 4, attendance.getAmTime());
                    if (analysisResult.isImpunityWorkDelay()) {
                        row1.getCell(4).setCellStyle(getBlueFillStyle(workbook));
                    }
                    // 填充下午打开时间
                    setValue(row1, 5, attendance.getPmTime());
                    if (analysisResult.isImpunityLeaveEarly()) {
                        row1.getCell(5).setCellStyle(getBlueFillStyle(workbook));
                    }
                    if (AttendanceStatus.LEAVE.equals(attendance.getStatus())) {
                        row1.getCell(2).setCellStyle(getBlueFontStyle(workbook));
                    } else {
                        if (analysisResult.isWorkBad()) {
                            setValue(row1, 12, 7.5);
                            row1.getCell(4).setCellStyle(getYellowFillStyle(workbook));
                            row1.getCell(5).setCellStyle(getYellowFillStyle(workbook));
                        }
                        if (analysisResult.isYesterdayWorkDelay()) {
                            row1.getCell(4).setCellStyle(getGreenFillStyle(workbook));
                        }
                        // 迟到
                        int amMinutes = analysisResult.getWorkDelayMinutes();
                        if (amMinutes > 0) {
                            setValue(row1, 10, amMinutes);
                            row1.getCell(4).setCellStyle(getYellowFillStyle(workbook));
                        }
                        // 早退
                        int pmMinutes = analysisResult.getLeaveEarlyMinutes();
                        if (pmMinutes > 0) {
                            setValue(row1, 11, pmMinutes);
                            row1.getCell(5).setCellStyle(getYellowFillStyle(workbook));
                        }
                        List<Leave> leaveList = analysisResult.getLeaveList();
                        String remark = StringUtils.isBlank(analysisResult.getRemark()) ? "" : analysisResult.getRemark();
                        if (leaveList != null && leaveList.size() > 0) {
                            if (leaveList.size() == 1) {
                                Leave leave = leaveList.get(0);
                                long minutes = analysisResult.getLeaveMinutes();
                                int hours =  (int) minutes / 60;
                                int minute =  (int) minutes % 60;
                                String times = hours + "小时" + minute + "分";
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
                            } else {
                                row1.createCell(6);
                                row1.createCell(7);
                                row1.createCell(8);
                                for (Leave leave : leaveList) {
                                    long minutes = TimeUtils.getMinutes(leave.getEndDate(), leave.getBeginDate());
                                    int hours =  (int) minutes / 60;
                                    int minute =  (int) minutes % 60;
                                    String times = hours + "小时" + minute + "分";
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
                            }
                            setValue(row1, 9, remark);
                        }
                    }
                }
            }
            i++;
        }
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
