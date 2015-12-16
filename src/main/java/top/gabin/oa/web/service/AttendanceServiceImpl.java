/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.AttendanceStatus;
import top.gabin.oa.web.dao.AttendanceDao;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.AttendanceWorkFlowDTO;
import top.gabin.oa.web.dto.EmployeeAttendanceDTO;
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

    @Override
    public Map<Long, Map<Long, List<Attendance>>> getAttendanceGroup(String month) {
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
    public Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> yesterdayWorkDelayWorkFlow(Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> workFlowDTOGroup) {
        for (Long key : workFlowDTOGroup.keySet()) {
            Map<Long, List<AttendanceWorkFlowDTO>> employeeGroup = workFlowDTOGroup.get(key);
            for (Long key0 : employeeGroup.keySet()) {
                Attendance yesterday = null;
                List<AttendanceWorkFlowDTO> attendances = employeeGroup.get(key0);
                for (AttendanceWorkFlowDTO attendanceWorkFlowDTO : attendances) {
                    Attendance attendance = attendanceWorkFlowDTO.getAttendance();
                    if (yesterday != null && StringUtils.isNotBlank(yesterday.getPmTime())) {
                        String pmTime = yesterday.getPmTime();
                        Date pmDate = TimeUtils.parseDate("2015-01-01 " + pmTime + ":00");
                        if (TimeUtils.afterOrEqual(pmDate, TimeUtils.parseDate("2015-01-01 21:30:00"))) {
                            Date amNeedFitTime = attendanceWorkFlowDTO.getAmNeedFitTime();
                            Date workDate = attendance.getWorkDate();
                            String format = TimeUtils.format(workDate, "yyyy-MM-dd 10:00:00");
                            Date date = TimeUtils.parseDate(format);
                            if (amNeedFitTime == null || amNeedFitTime.before(date)) {
                                attendanceWorkFlowDTO.setAmNeedFitTime(date);
                                attendanceWorkFlowDTO.setYesterdayWorkDelay(true);
                            }
                        }
                    }
                    yesterday = attendance;
                }
            }
        }
        return workFlowDTOGroup;
    }

    @Override
    public Map<Long, Map<Long, EmployeeAttendanceDTO>> dealAttendanceRule(Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> workFlowDTOGroup) {
        Map<Long, Map<Long, EmployeeAttendanceDTO>> resultDTOMap = new HashMap<Long, Map<Long, EmployeeAttendanceDTO>>();
        for (Long key : workFlowDTOGroup.keySet()) {
            Map<Long, List<AttendanceWorkFlowDTO>> employeeGroup = workFlowDTOGroup.get(key);
            Map<Long, EmployeeAttendanceDTO> employeeAttendanceDTOMap = new HashMap<Long, EmployeeAttendanceDTO>();
            for (Long key0 : employeeGroup.keySet()) {
                List<AttendanceWorkFlowDTO> attendances = employeeGroup.get(key0);
                EmployeeAttendanceDTO employeeAttendanceDTO = new EmployeeAttendanceDTO();
                employeeAttendanceDTO.setAttendanceWorkFlowDTOList(attendances);
                for (int i = 0; i < attendances.size(); i++) {
                    AttendanceWorkFlowDTO attendanceWorkFlowDTO = attendances.get(i);
                    Attendance attendance = attendanceWorkFlowDTO.getAttendance();
                    Date workDate = attendance.getWorkDate();
                    String amTime = attendance.getAmTime();
                    Date amDate = StringUtils.isBlank(amTime) ? null : TimeUtils.parseDate(TimeUtils.format(workDate, "yyyy-MM-dd " + amTime + ":00"));
                    String pmTime = attendance.getPmTime();
                    Date pmDate = StringUtils.isBlank(pmTime) ? null : TimeUtils.parseDate(TimeUtils.format(workDate, "yyyy-MM-dd " + pmTime + ":00"));
                    if (amDate == null && pmDate == null) {
                        attendanceWorkFlowDTO.setHasNowWork(true);
                    }
                    if (!attendanceWorkFlowDTO.isNotNeedFit()) {
                        Date amNeedFitTime = attendanceWorkFlowDTO.getAmNeedFitTime();
                        Date pmNeedFitTime = attendanceWorkFlowDTO.getPmNeedFitTime();
                        String s = "";
                        // 如果迟到
                        if (amDate != null && amNeedFitTime != null && amDate.after(amNeedFitTime)) {
                            int delaySeconds = employeeAttendanceDTO.getDelaySeconds();
                            long minutes = TimeUtils.getMinutes(amDate, amNeedFitTime);
                            attendanceWorkFlowDTO.setAmMinutes((int) minutes);
                            // 十五分钟内
                            if (minutes <= 15 && delaySeconds < EmployeeAttendanceDTO.delayLimit) {
                                attendanceWorkFlowDTO.setAmLimit(true);

                            } else {
                                // 如果超过15分钟
                                int amMoney = employeeAttendanceDTO.getDelayMoneys();
                                if (minutes <= 30) {
                                    amMoney += attendanceWorkFlowDTO.ADD_MONEY_ONE;
                                    s += "迟到扣除" + amMoney + "元工资;";

                                } else if (minutes > 30 && minutes <= 60) {
                                    s +=  "迟到扣除1h工资;";
                                } else if (minutes > 60 && minutes <= 180) {
                                    s += "迟到扣除3h工资;";
                                } else if (minutes > 180) {
                                    s += "迟到扣除1天工资;";
                                }
                                attendanceWorkFlowDTO.setAmMoney(amMoney);
                                employeeAttendanceDTO.setDelayMoneys(amMoney);
                            }
                            employeeAttendanceDTO.setDelaySeconds(delaySeconds + 1);
                        }
                        // 如果早退
                        if (pmDate != null && pmNeedFitTime != null && pmDate.before(pmNeedFitTime)) {
                            int goQuickSeconds = employeeAttendanceDTO.getGoQuickSeconds();
                            if (goQuickSeconds < EmployeeAttendanceDTO.goQuickLimit) {
                                attendanceWorkFlowDTO.setPmLimit(true);
                                s += "下班补卡;";
                            } else {
                                int pmMoney = employeeAttendanceDTO.getGoQuickMoneys();
                                pmMoney += attendanceWorkFlowDTO.ADD_MONEY_ONE;
                                attendanceWorkFlowDTO.setPmMoney(pmMoney);
                                employeeAttendanceDTO.setDelayMoneys(pmMoney);
                                s += "早退扣除" + pmMoney + "元工资;";
                            }
                            long minutes = TimeUtils.getMinutes(pmNeedFitTime, pmDate);
                            attendanceWorkFlowDTO.setPmMinutes((int) minutes);
                            employeeAttendanceDTO.setGoQuickSeconds(goQuickSeconds + 1);
                        }
                        attendanceWorkFlowDTO.setRemark(s);
                    }
                }
                employeeAttendanceDTOMap.put(key0, employeeAttendanceDTO);
            }
            resultDTOMap.put(key, employeeAttendanceDTOMap);
        }
        return resultDTOMap;
    }

}
