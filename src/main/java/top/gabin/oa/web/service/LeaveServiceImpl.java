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
import top.gabin.oa.web.dao.LeaveImportDTO;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.date.DateUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {
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
                leave.setBeginDate(DateUtils.parseDate(leaveDTO.getBeginDate()));
            }
            if (leaveDTO.getEndDate() != null) {
                leave.setEndDate(DateUtils.parseDate(leaveDTO.getEndDate()));
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
}
