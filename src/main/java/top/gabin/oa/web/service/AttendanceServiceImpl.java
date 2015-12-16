/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.AttendanceStatus;
import top.gabin.oa.web.dao.AttendanceDao;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/15
 */
@Service("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {
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
            String department = attendance.getEmployee().getDepartment().getName();
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


}
