/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.LeaveType;
import top.gabin.oa.web.dao.LeaveDao;
import top.gabin.oa.web.dto.LeaveImportDTO;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.dto.AttendanceWorkFlowDTO;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author linjiabin  on  15/12/14
 */
@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {
    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);
    @Resource
    private CriteriaQueryService queryService;
    @Resource(name = "leaveDao")
    private LeaveDao leaveDao;
    @Resource(name = "employeeService")
    private EmployeeService employeeService;

    @Override
    @Transactional("transactionManager")
    public void batchDelete(String ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("in_id", ids);
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        List<LeaveImpl> leaveList = queryService.query(LeaveImpl.class, criteriaCondition);
        for (Leave leave : leaveList) {
            leaveDao.delete(leave);
        }
    }

    @Override
    @Transactional("transactionManager")
    public void batchInsert(List<Leave> leaveList) {
        for (Leave leave : leaveList) {
            leaveDao.saveOrUpdate(leave);
        }
    }

    @Override
    public Leave findById(Long id) {
        return leaveDao.findById(id);
    }

    @Override
    @Transactional("transactionManager")
    public Leave merge(Leave leave) {
        return leaveDao.saveOrUpdate(leave);
    }

    @Override
    @Transactional("transactionManager")
    public Leave merge(LeaveDTO leaveDTO) {
        Leave leave = new LeaveImpl();
        if (leaveDTO != null) {
            if (leaveDTO.getId() != null) {
                leave = leaveDao.findById(leaveDTO.getId());
            }
            if (leaveDTO.getBeginDate() != null) {
                leave.setBeginDate(TimeUtils.parseDate(leaveDTO.getBeginDate()));
            }
            if (leaveDTO.getEndDate() != null) {
                leave.setEndDate(TimeUtils.parseDate(leaveDTO.getEndDate()));
            }
            String name = leaveDTO.getName();
            if (StringUtils.isNotBlank(name)) {
                Employee employee = employeeService.findByName(name);
                if (employee != null) {
                    leave.setEmployee(employee);
                }
            }
            if (leaveDTO.getType() != null) {
                leave.setType(LeaveType.instance(leaveDTO.getType()));
            }
            if (StringUtils.isNotBlank(leaveDTO.getRemark())) {
                leave.setRemark(leaveDTO.getRemark());
            }
            leaveDao.saveOrUpdate(leave);
        }
        return leave;
    }

    @Override
    @Transactional("transactionManager")
    public void importLeave(List<LeaveImportDTO> leaveImportDTOList) {
        Map<String, Employee> cacheEmployee = new HashMap<String, Employee>();
        List<Leave> leaveList = new ArrayList<Leave>();
        for (LeaveImportDTO dto : leaveImportDTOList) {
            String realName = dto.getName();
            if (StringUtils.isBlank(realName)) {
                continue;
            }
            Leave leave = new LeaveImpl();
            leave.setBeginDate(dto.getBeginDate());
            leave.setEndDate(dto.getEndDate());
            leave.setType(LeaveType.instance(dto.getLeaveName()));
            Employee employee;
            if (cacheEmployee.containsKey(realName)) {
                employee = cacheEmployee.get(realName);
            } else {
                employee = employeeService.findByName(realName);
            }
            leave.setEmployee(employee);
            leaveList.add(leave);
        }
        for (Leave leave : leaveList) {
            leaveDao.saveOrUpdate(leave);
        }
    }

    @Transactional("transactionManager")
    @Override
    public void clearMonth(String month) {
        leaveDao.clearMonth(month);
    }

    @Override
    public Map<Long, List<Leave>> getLeaveGroup(String month) {
        Map<Long, List<Leave>> leaveGroup = new HashMap<Long, List<Leave>>();
        Map<String, Object> conditions = new HashMap<String, Object>();
        CriteriaCondition criteriaCondition = new CriteriaCondition(conditions);
        if (StringUtils.isNotBlank(month)) {
            conditions.put("ge_beginDate", month + "-01 00:00:00");
            conditions.put("le_beginDate", month + "-31 00:00:00");
        }
        List<LeaveImpl> attendanceList = queryService.query(LeaveImpl.class, criteriaCondition);
        if (attendanceList == null) {
            return leaveGroup;
        }
        for (Leave leave : attendanceList) {
            Long id = leave.getEmployee().getId();
            List<Leave> leaveList;
            if (leaveGroup.containsKey(id)) {
                leaveList = leaveGroup.get(id);
            } else {
                leaveList = new ArrayList<Leave>();
                leaveGroup.put(id, leaveList);
            }
            leaveList.add(leave);
        }
        return leaveGroup;
    }

    @Override
    public Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> workFlow(Map<Long, Map<Long, List<Attendance>>> attendanceGroup, Map<Long, List<Leave>> leaveGroup) {
        Map<Long, Map<Long, List<AttendanceWorkFlowDTO>>> leaveWorkFlowGroup = new HashMap<Long, Map<Long, List<AttendanceWorkFlowDTO>>>();
        for (Long key : attendanceGroup.keySet()) {
            Map<Long, List<AttendanceWorkFlowDTO>>  LeaveWorkFlowGroup = leaveWorkFlowGroup.get(key);
            if (LeaveWorkFlowGroup == null || LeaveWorkFlowGroup.isEmpty()) {
                LeaveWorkFlowGroup = new HashMap<Long, List<AttendanceWorkFlowDTO>>();
            }
            Map<Long, List<Attendance>> employeeGroup = attendanceGroup.get(key);
            for (Long key0 : employeeGroup.keySet()) {
                List<AttendanceWorkFlowDTO> attendanceWorkFlowDTOList = LeaveWorkFlowGroup.get(key0);
                if (attendanceWorkFlowDTOList == null || attendanceWorkFlowDTOList.isEmpty()) {
                    attendanceWorkFlowDTOList = new ArrayList<AttendanceWorkFlowDTO>();
                }
                List<Attendance> attendances = employeeGroup.get(key0);
                for (Attendance attendance : attendances) {
                    AttendanceWorkFlowDTO attendanceWorkFlowDTO = new AttendanceWorkFlowDTO();
                    attendanceWorkFlowDTO.setAttendance(attendance);
                    Long id = attendance.getEmployee().getId();
                    String workDateFormat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    // 上午应打卡时间
                    Date amNeedFit = TimeUtils.parseDate(workDateFormat + " 09:00:00");
                    Date pmNeedFit = TimeUtils.parseDate(workDateFormat + " 18:00:00");
                    // 获取当天的请假记录
                    if (leaveGroup.containsKey(id)) {
                        List<Leave> leaveList = leaveGroup.get(id);
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
                                        attendanceWorkFlowDTO.setLeaveTimes(75 * 6L);
                                        attendanceWorkFlowDTO.setNotNeedFit(true);
                                    } else if (TimeUtils.beforeOrEqual(beginDate, tmpBeginDate)) { // 请假开始时间在上午需要打卡的时刻或之前
                                        // 请假结束时间在上午需要打卡之后
                                        if (endDate.after(tmpBeginDate)) {
                                            // 如果请假结束时间在下午需要打卡之前
                                            if (endDate.before(tmpEndDate)) {
                                                tmpBeginDate = endDate;
                                            }
                                        }
                                        delayTimes = (endDate.getTime() - TimeUtils.parseDate(workDateFormat + " 09:00:00").getTime()) / 1000 / 60;
                                    } else if (TimeUtils.afterOrEqual(endDate, tmpEndDate)) { // 请假结束时间在下午需要打卡的时刻或之后
                                        // 请假开始时间在上午需要打卡之前
                                        if (beginDate.before(tmpEndDate)) {
                                            if (beginDate.after(tmpBeginDate)) {
                                                tmpEndDate = beginDate;
                                            }
                                        }
                                        delayTimes = (TimeUtils.parseDate(workDateFormat + " 18:00:00").getTime() - beginDate.getTime()) / 1000 / 60;
                                    } else {
                                        delayTimes = (endDate.getTime() - beginDate.getTime()) / 1000 / 60;
                                    }
                                    if (tmpBeginDate.getTime() == TimeUtils.parseDate(workDateFormat + " 12:00:00").getTime()) {
                                        tmpBeginDate =  TimeUtils.parseDate(workDateFormat + " 13:30:00");
                                    }
                                    attendanceWorkFlowDTO.setAmNeedFitTime(tmpBeginDate);
                                    if (tmpEndDate.getTime() == TimeUtils.parseDate(workDateFormat + " 13:30:00").getTime()) {
                                        tmpEndDate =  TimeUtils.parseDate(workDateFormat + " 12:00:00");
                                    }
                                    attendanceWorkFlowDTO.setPmNeedFitTime(tmpEndDate);
                                    if (!attendanceWorkFlowDTO.isNotNeedFit()) {
                                        if (TimeUtils.beforeOrEqual(beginDate, TimeUtils.parseDate(workDateFormat + " 12:00:00")) &&  TimeUtils.afterOrEqual(endDate, TimeUtils.parseDate(workDateFormat + " 13:30:00"))) {
                                            delayTimes = delayTimes - 90;
                                        }
                                        attendanceWorkFlowDTO.setLeaveTimes(delayTimes);
                                    }
                                }
                            }
                        }
                    }
                    attendanceWorkFlowDTOList.add(attendanceWorkFlowDTO);
                }
                LeaveWorkFlowGroup.put(key0, attendanceWorkFlowDTOList);
            }
            leaveWorkFlowGroup.put(key, LeaveWorkFlowGroup);
        }
        return leaveWorkFlowGroup;
    }
}
