/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveServiceImpl.
 */
package top.gabin.oa.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.LeaveType;
import top.gabin.oa.web.dao.LeaveDao;
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
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
    public Leave merge(LeaveDTO leaveDTO) {
        Leave leave = new LeaveImpl();
        if (leaveDTO != null) {
            if (leaveDTO.getId() != null) {
                leave = leaveDao.findById(leaveDTO.getId());
            }
            if (leaveDTO.getBeginDate() != null) {
                leave.setBeginDate(leaveDTO.getBeginDate());
            }
            if (leaveDTO.getEndDate() != null) {
                leave.setEndDate(leaveDTO.getEndDate());
            }
            Long employeeId = leaveDTO.getEmployeeId();
            if (employeeId != null) {
                Employee employee = employeeService.findById(employeeId);
                if (employee != null) {
                    leave.setEmployee(employee);
                }
            }
            if (leaveDTO.getType() != null) {
                leave.setType(LeaveType.instance(leaveDTO.getType()));
            }
            leaveDao.saveOrUpdate(leave);
        }
        return leave;
    }

}
