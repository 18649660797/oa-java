/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.LeaveType;
import top.gabin.oa.web.dao.LeaveDao;
import top.gabin.oa.web.dto.LeaveImportDTO;
import top.gabin.oa.web.dto.LeaveDTO;
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
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;
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
        List<LeaveImpl> leaveList = criteriaQueryService.query(LeaveImpl.class, criteriaCondition);
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
            // 清除秒为00
            Date beginDate = dto.getBeginDate();
            leave.setBeginDate(TimeUtils.parseDate(TimeUtils.format(beginDate, "yyyy-MM-dd HH:mm:00")));
            Date endDate = dto.getEndDate();
            leave.setEndDate(TimeUtils.parseDate(TimeUtils.format(endDate, "yyyy-MM-dd HH:mm:00")));
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
        batchInsert(leaveList);
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
            conditions.put("le_beginDate", month + "-31 23:59:59");
        }
        List<LeaveImpl> attendanceList = criteriaQueryService.query(LeaveImpl.class, criteriaCondition);
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

    @Transactional("transactionManager")
    @Override
    public void batchDeleteByEmployeeIds(String ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("in_employee.id", ids);
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        List<LeaveImpl> leaveList = criteriaQueryService.query(LeaveImpl.class, criteriaCondition);
        for (Leave leave : leaveList) {
            leaveDao.delete(leave);
        }
    }
}
